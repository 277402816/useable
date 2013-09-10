package com.roiland.crm.sm.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;

/**
 * This class is utility class on top of apach http response object. This class will help user to retrieve specifi error code.
 * 
 * @author Chunji Li
 */
public class RLHttpResponse {
    private final static String tag = Log.getTag(RLHttpResponse.class);
    private static final String HEADER_ERROR_CODE = "Error code";

//	protected final AbstractRLHttpClient mHttpClient;
    protected final HttpResponse mResponse;
    protected final HttpRequest mRequest;
    protected final int statusCode;
    protected final String errorCode;

    
    public RLHttpResponse( HttpRequest request, HttpResponse response) {
        mRequest = request;
        mResponse = response;
        statusCode = mResponse.getStatusLine().getStatusCode();

        Header header = mResponse.getLastHeader(HEADER_ERROR_CODE);
        errorCode = header == null? "": header.getValue();
        
        header = mResponse.getLastHeader("Set-Cookie");
        if (header != null) {
        	Log.i(tag, "Get response of cookie , name ==  " + header.getName() + "  value == " + header.getValue());
        	if (header.getValue() != null && header.getValue().indexOf("PHPSESSID") == -1) {
        		mResponse.removeHeaders("Set-Cookie");
        		Log.i(tag, "This cookie is incorrect, removed it, " + header.getName() + "  value == " + header.getValue());
        		header = null;
        	}
        }
    }

    /**
     * Release the Response object and clean the Http request lists.
     */
    public void release() {
        abort();
    }

    void abort() {
        HttpRequestBase request = (HttpRequestBase) mRequest;
        if (request != null && !request.isAborted()) {
            request.abort();
        }
        URI uri = ((HttpRequestBase) mRequest).getURI();
        String host = uri.getHost();

        Log.d(tag, "RELEASE >>> " + uri);
    }

    /**
     * Http Status code
     * @return
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get error code
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    
    /**
     * this method return the apache HttpResponse 
     * @return
     */
    public HttpResponse getResponse() {
        return mResponse;
    }
    
    /**
     * this method return the java InputStream to read response data. 
     * @return
     */
    public InputStream getInputStream() throws IOException {
        return mResponse.getEntity().getContent();
    }
    
    public String toString() {
        return statusCode + " " + errorCode + ". length:" + mResponse.getEntity().getContentLength();
    }
    
    /**
     * Utility method to know the http operation status.
     * @return
     */
    public boolean isSuccess() throws ResponseException {
        int statusCode = getStatusCode();
        if (statusCode >= 200 && statusCode <= 207)
        	return true;
        else
        	throw new ResponseException(statusCode);
    }

    /**
     * Utility method to retrieve http request.
     * @return
     */
    public HttpRequest getRequest() {
        return mRequest;
    }
}
