package com.roiland.crm.sm.core.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;

import com.roiland.crm.sm.core.http.security.EasySSLSocketFactory;
import com.roiland.crm.sm.utils.Log;

/**
 * This class help user to make all http operations. All well known http operations are supported. (GET, PUT, POST, DELETE, HEAD, TRACE, OPTIONS)
 * RLHttpClient also supports multithreaded concurrent http operations.
 * 
 * Sample code.
 * 
 *      String url = "http://www.yahoo.com";
 *      RLHttpClient rlHttpClient = new RLHttpClient(3000, 3000);
 *
 *   	RLHttpResponse response = rlHttpClient.executeMethod(RLHttpClient.METHOD_GET, url);    		
 * 		rlHttpClient.close();
 * 
 * Multi thread sample:
 * 
 *      final String urls[] = new String[]{"http://www.yahoo.com", "http://www.google.com", "http://www.cnn.com", "http://www.aol.com", "http://www.ebay.com"};
 *      final RLHttpClient rlHttpClient = new RLHttpClient(true, 3000, 3000);
 *
 *		for (int i=0;i<5;i++){
 *			final int index = i;
 *			new Thread(new Runnable() {
 *				public void run() {
 *					try {
 *			    		RLHttpResponse response = rlHttpClient.executeMethod(RLHttpClient.METHOD_GET, urls[index]);    		
 *			    		if(response.getStatusCode() == 200){
 *			    			response.getResponse().getEntity().consumeContent();
 *						}
 *					} catch (Exception e) {
 *					}
 *				}
 *			}).start();    			
 *		}
 *
 */
public class RLHttpClient extends AbstractRLHttpClient {

	private static final String tag = Log.getTag(RLHttpClient.class);

	private DefaultHttpClient mHttpClient;

    public static final int LAN_CONNECT_TIMEOUT = 30 * 1000;
    public static final int LAN_SOCKET_TIMEOUT = 30 * 1000;

    public static final int CONNECT_TIMEOUT = 90 * 1000;
    public static final int SOCKET_TIMEOUT = 90 * 1000;

    private boolean isMultiThreaded;

    /**
     * Create WD specific Http Client. User can create RLHttpClient class to make any Http operations. User can use the same instance of Http Client for all http operations.
     * This method will use default timesouts (30 seconds) 
     * @param connectionTimeout 
     * @param socketTimeout
     */
    public RLHttpClient() {
    	this(false, LAN_CONNECT_TIMEOUT, LAN_SOCKET_TIMEOUT);
    }
    
    
    /**
     * Create WD specific Http Client. User can create RLHttpClient class to make any Http operations. User can use the same instance of Http Client for all http operations. 
     * @param connectionTimeout 
     * @param socketTimeout
     */
    public RLHttpClient(int connectionTimeout, int socketTimeout) {
    	this(false, connectionTimeout, socketTimeout);
    }

    /**
     * Create WD specific Http Client. User can create RLHttpClient class to make any Http operations. User can use the same instance of Http Client for all http operations. 
     * This constructor allow user to specify whether Http client needs to support concurent http operations.
     * @param multiThreaded boolean indicating whether multithreaded http operation is required.
     * @param connectionTimeout 
     * @param socketTimeout
     */
    public RLHttpClient(boolean multiThreaded, int connectionTimeout, int socketTimeout) {
        super(connectionTimeout, socketTimeout);
        isMultiThreaded = multiThreaded;
    }

    @Override
    public synchronized HttpClient getHttpClient() {
        if (mHttpClient == null) {
            SchemeRegistry schreg = new SchemeRegistry();
            schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schreg.register(new Scheme("https", new EasySSLSocketFactory(), 443));
            if (isMultiThreaded){
            	ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schreg); // connection manager will be shutdown at close() method
                mHttpClient = new DefaultHttpClient(cm, params);
            }
            else{
            	mHttpClient = new DefaultHttpClient(params);
            }
    		try{	
    			SchemeRegistry schemeRegistry = mHttpClient.getConnectionManager().getSchemeRegistry(); 
    			schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443)); 	
    		} catch(Exception ex){
    			Log.i(tag, "trustStore", ex);
    		}
        }

        return mHttpClient;
    }

    /**
     * Create clone of existing RLHttpClient object
     */
    @Override
    public RLHttpClient clone() {
        RLHttpClient brother = new RLHttpClient(mConnectionTimeout, mSocketTimeout);

        if (mHttpClient != null) {
            CookieStore cookieStore = mHttpClient.getCookieStore();
            DefaultHttpClient cloneHttpClient = (DefaultHttpClient) brother.getHttpClient();
            cloneHttpClient.setCookieStore(cookieStore);
        }

        return brother;
    }

    /** 
     * Save the current cookie for future use
     */
	public void saveCurrentCookies() {
	    CookieStore cookieStore = mHttpClient.getCookieStore();
	    // Create local HTTP context
        localContext = new BasicHttpContext();     
        // Bind custom cookie store to the local context     
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}
	
	/**
	 * Debug method to print cookies
	 */
	public void printCookies() {
		if (localContext != null) {
			CookieStore cookieStore = (CookieStore) localContext.getAttribute(ClientContext.COOKIE_STORE);
	    	if ( cookieStore != null ) {
	        	for (Cookie cookie : cookieStore.getCookies()) {
	        		Log.i(tag, "printCookie() >> current cookie is ============= " + cookie.getValue());
	        	}
	    	} 
		}
		Log.i(tag, "printCookie() >> current cookie is empty============= ");
	}
	
	public boolean hasSession() {
		if (localContext != null) {
			CookieStore cookieStore = (CookieStore) localContext.getAttribute(ClientContext.COOKIE_STORE);
	    	if ( cookieStore != null &&  cookieStore.getCookies() != null) {
	        	return true;
	    	} 
		}
		return false;
	}

	@Override
	protected RLHttpResponse createRLHttpResponse(HttpClient httpClient,
			HttpRequest request, HttpResponse response) {
		return new RLHttpResponse( request, response);
	}

}
