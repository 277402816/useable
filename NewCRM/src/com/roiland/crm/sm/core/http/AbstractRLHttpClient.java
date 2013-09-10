package com.roiland.crm.sm.core.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.text.TextUtils;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.utils.EventReceiver;
import com.roiland.crm.sm.utils.Log;




/**
 * @author Chunji Li
 */
public abstract class AbstractRLHttpClient implements HttpClientInterface {
    private final static String tag = Log.getTag(AbstractRLHttpClient.class);

    
    private final static String DEFAULT_ENCODE = HTTP.UTF_8;
    protected final static AtomicInteger mInConnectionCounter = new AtomicInteger(0);

    protected HttpParams params;

    protected HttpContext localContext;

    protected int mConnectionTimeout;
    protected int mSocketTimeout;

    protected abstract RLHttpResponse createRLHttpResponse(HttpClient httpClient, HttpRequest request, HttpResponse response);
    public abstract AbstractRLHttpClient clone();

    private String lastDomain;
    
    AbstractRLHttpClient(int connectionTimeout, int socketTimeout) {
        mConnectionTimeout = connectionTimeout;
        mSocketTimeout = socketTimeout;

        params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);
        HttpClientParams.setRedirecting(params, true);
        HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);
    }
    
    /**
     * User can call this method to know whether any http connection is going on with HttpClient
     */
    public boolean inConnection() {
    	
        return mInConnectionCounter.get() > 0;
    }

    /**
     * Closes the Http connection. User has to call this method after all http connection jobs are over. This call will make all necessary clean up process.
     */
    @Override
    public void close() {
        Log.d(tag, " shutdown ConnectionManager >>> ");
        getHttpClient().getConnectionManager().shutdown();
    }
    
    protected RLHttpResponse execute(HttpRequestBase request, Map<String, String> headers) throws IOException {
        HttpResponse response = null;
        if (request == null){
        	throw new NullPointerException("HttpRequestBase is null");
        }
        if (EventReceiver.isNetworkUnavailable()) {
            throw new IOException("Network is unavailable");
        }
        URI uri = request.getURI();
        String host = uri.getHost();
        int port = uri.getPort();
        String domain = uri.getScheme() + "://" + host;
        if (port > 0) {
            domain = domain + ":" + port;
        }

        if (Log.DEBUG.get()) {
            Log.d(tag, request.getMethod() + " >>> " + uri);
        }

        RLHttpResponse rlHttpResponse = null;
        HttpClient httpClient = null;
        try {
            if (headers != null) {
                StringBuilder builder = new StringBuilder();
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (Log.DEBUG.get()) {
                        Log.d(tag, "HTTP Header "+ key + ":"+ value);
                    }
                    request.addHeader(key, value);

                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(key).append(":").append(value);
                }

                if (Log.DEBUG.get()) {
                    Log.d(tag, "headers >>> " + builder);
                }
            }

            httpClient = getHttpClient();
            if (httpClient == null) {
                throw new IOException("httpClient is NULL! " + uri);
            }

            if (!domain.equals(lastDomain)) {
                httpClient.getConnectionManager().closeExpiredConnections();
            }
            mInConnectionCounter.incrementAndGet();//Increment connection count

            if (localContext == null) {
            	Log.i(tag, "HttpClient execute with no cookie =======no====== ");
                response = httpClient.execute(request);
            } else {
            	CookieStore cookieStore = (CookieStore) localContext.getAttribute(ClientContext.COOKIE_STORE);
            	if ( cookieStore != null ) {
                	for (Cookie cookie : cookieStore.getCookies()) {
                		Log.i(tag, "HttpClient execute with cookie ============= " + cookie.getValue());
                	}
            	}
                response = httpClient.execute(request, localContext);	//Get cookie info to request of header.
            }

            if (response != null) {
                rlHttpResponse = createRLHttpResponse(httpClient, request, response);
            }
            lastDomain = domain;
        } catch (IOException e) {
        	//Check if it contains authorization credentials we want to send them to Log.d so that they dont get printed for release
        	int index = uri.toString().indexOf("device_user_auth_code");
        	if(index >0){
        		Log.d(tag, "Connect failed: " + uri.toString().substring(0,  index));
        	}
        	else {
        		Log.e(tag, "Connect failed: " +uri);
        	}
            throw e;
        } finally {
        	mInConnectionCounter.decrementAndGet(); //Decrement connection count
            if (rlHttpResponse == null) {
                if (response != null) {
                    request.abort();
                }

            }
        }

        return rlHttpResponse;
    }

    /**
     * Request through POST method with JSON data
     */
    public RLHttpResponse executePostJSON(String url, JSONObject params, Map<String, String> headers) throws IOException {
    	String newUrl = composeUrl(url, null);
    	HttpEntityEnclosingRequestBase request = new HttpPost(newUrl);
        if (params != null) {
        	Log.i(url, "Request entity : " + params.toString().replace("\"null\"", "null"));
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        	nvps.add(new BasicNameValuePair(URLContact.JSON_PREFIX, params.toString().replace("\"null\"", "null")));
        	request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        }
        return execute(request, headers);
    }
    
    private RLHttpResponse executePostMethod(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        String newUrl = composeUrl(url, null);
        HttpEntityEnclosingRequestBase request = null;
        request = new HttpPost(newUrl);
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                qparams.add(new BasicNameValuePair(key, value));
                if (Log.DEBUG.get()) {
                    Log.i(tag, key + " = " + value);
                }
            }
            request.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
        }

        return execute(request, headers);
    }

    private RLHttpResponse executeSimpleMethod(String url, Map<String, String> params, Map<String, String> headers, int methodType) throws IOException {
        String newUrl = composeUrl(url, params);
        HttpRequestBase request = null;
        switch (methodType) {
            case METHOD_OPTIONS:
                request = new HttpOptions(newUrl);
                break;
            case METHOD_HEAD:
                request = new HttpHead(newUrl);
                break;
            case METHOD_DELETE:
                request = new HttpDelete(newUrl);
                break;
            case METHOD_TRACE:
                request = new HttpTrace(newUrl);
                break;
            case METHOD_PUT:
                request = new HttpPut(newUrl);
                break;
            default:
                request = new HttpGet(newUrl);
                break;
        }

        return execute(request, headers);
    }
    
    private String composeUrl(String url, Map<String, String> params) {
        StringBuilder result = new StringBuilder(url);

        if (params != null && !params.isEmpty()) {
            if (url.contains("?")) {
                result.append("&");
            }
            else {
                result.append("?");
            }

            boolean isNotFirst = false;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (!TextUtils.isEmpty(value))
                    	value = URLEncoder.encode(value, DEFAULT_ENCODE);
                    else
                    	value = "";
                    
                    if (isNotFirst) {
                        result.append("&");
                    } else {
                        isNotFirst = true;
                    }

                    result.append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                    Log.e(tag, e.getMessage(), e);
                }
            }
        }

        return result.toString();
    }

    /**
     * This method will allow the caller to inject cookies to the Http Client
     * @param cookies cookies need to be injected.
     */
    @Override
    public void setCookies(List<Cookie> cookies) {
        CookieStore cookieStore = new BasicCookieStore();
        for (Cookie cookie : cookies) {
            cookieStore.addCookie(cookie);
        }

        if (localContext == null) {
            localContext = new BasicHttpContext();
        }

        // Bind custom cookie store to the local context     
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);    
    }

    /**
     * This method allow user to make any HTTP operations with Http client. Supported http operations are defined as constants.
     * 
     * @param type Type of http operation. GET, PUT, POST etc
     * @param params Http parameters if any
     * @param headers Http headers if any
     * @return response RLHttpResponse
     * Sample code
     *          
     * RLHttpResponse response = rlHttpClient.executeMethod(RLHttpClient.METHOD_GET, "http://www.yahoo.com");    		
     * if(response.getStatusCode() == 200){}
     *
     */
    @Override
    public RLHttpResponse executeMethod(int type, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
    	if (type == METHOD_POST){
    		return executePostMethod(url, params, headers);
    	}
    	else{
    		return executeSimpleMethod(url, params, headers, type);	
    	}
        
    }


    /**
     * This method allow user to make any HTTP operations with Http client. Supported http operations are defined as constants.
     * 
     * @param type Type of http operation. GET, PUT, POST etc
     * @param params Http parameters if any
     * @return response RLHttpResponse
     * Sample code
     *          
     * RLHttpResponse response = rlHttpClient.executeMethod(RLHttpClient.METHOD_GET, "http://www.yahoo.com");    		
     * if(response.getStatusCode() == 200){}
     *
     */
    @Override
    public RLHttpResponse executeMethod(int type, String url, Map<String, String> params) throws IOException {
    	if (type == METHOD_POST){
    		return executePostMethod(url, params, null);
    	}
    	else{
    		return executeSimpleMethod(url, params, null, type);
    	}
    }


    /**
     * This method allow user to make any HTTP operations with Http client. Supported http operations are defined as constants.
     * 
     * @param type Type of http operation. GET, PUT, POST etc
     * @return response RLHttpResponse
     * Sample code
     *          
     * RLHttpResponse response = rlHttpClient.executeMethod(RLHttpClient.METHOD_GET, "http://www.yahoo.com");    		
     * if(response.getStatusCode() == 200){}
     *
     */
    @Override
    public RLHttpResponse executeMethod(int type, String url) throws IOException {
    	if (type == METHOD_POST){
    		return executePostMethod(url, null, null);
    	}
    	else{
    		return executeSimpleMethod(url, null, null, type);
    	}
    }


    /**
     * Set the connection time out parameter
     * @param timeout time out in integer
     */
    @Override
    public void setConnectionTimeout(int timeout) {
        HttpConnectionParams.setConnectionTimeout(getHttpClient().getParams(), timeout);
    }
    

    /**
     * Set the socket time out parameter
     * @param timeout time out in integer
     */
    @Override
    public void setSoTimeout(int timeout) {
        HttpConnectionParams.setSoTimeout(getHttpClient().getParams(), timeout);
    }
}
