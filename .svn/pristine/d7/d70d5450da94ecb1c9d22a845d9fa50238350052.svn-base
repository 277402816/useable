package com.roiland.crm.sm.core.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.json.JSONObject;


/**
 * @author Chunji Li
 *
 */
public interface HttpClientInterface {

	static final int NETWORK_STATUS_NONE = -1;
	static final int NETWORK_STATUS_NO_NETWORK = 0;
	static final int NETWORK_STATUS_LAN = 1;
	static final int NETWORK_STATUS_WAN = 2;
	
    public final static int METHOD_OPTIONS 	= 0;
    public final static int METHOD_GET 		= 1;
    public final static int METHOD_HEAD 	= 2;
    public final static int METHOD_DELETE 	= 3;
    public final static int METHOD_TRACE 	= 4;
    public final static int METHOD_PUT 		= 5;
    public final static int METHOD_POST 	= 6;
	
	HttpClient getHttpClient();
    boolean inConnection();    
    HttpClientInterface clone();
    void close();

    RLHttpResponse executePostJSON(String url, JSONObject params, Map<String, String> headers) throws IOException;
    
    RLHttpResponse executeMethod(int type, String url) throws IOException;
    RLHttpResponse executeMethod(int type, String url, Map<String, String> params) throws IOException;
    RLHttpResponse executeMethod(int type, String url, Map<String, String> params, Map<String, String> headers) throws IOException;

    void setConnectionTimeout(int timeout);
    void setSoTimeout(int timeout);
    void saveCurrentCookies();
    void printCookies();
    boolean hasSession();
    void setCookies(List<Cookie> cookies);
}
