package com.roiland.crm.sm.core.http;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import com.roiland.crm.sm.utils.Log;

/**
 * @author Chunji Li
 *
 */
public class PooledHttpClient extends AbstractRLHttpClient {
    private static final String tag = Log.getTag(PooledHttpClient.class);

    private int mPoolSize;
    private final Queue<AbstractRLHttpClient> mFreeQueue;
    private final Queue<AbstractRLHttpClient> mUsedQueue;

    public PooledHttpClient(int connectionTimeout, int socketTimeout, int poolSize) {
        super(connectionTimeout, socketTimeout);
        mPoolSize = poolSize;

        mFreeQueue = new LinkedList<AbstractRLHttpClient>();
        mUsedQueue = new LinkedList<AbstractRLHttpClient>();

        for (int i = 0; i < mPoolSize; i++) {
            AbstractRLHttpClient httpClient = new RLHttpClient(connectionTimeout, socketTimeout);
            mFreeQueue.add(httpClient);
        }
    }


    @Override
    public HttpClient getHttpClient() {
        Log.e(tag, "can not invoke getHttpClient() method!");
        AbstractRLHttpClient wdHttpClient = getRLHttpClient();
        return wdHttpClient == null? null: wdHttpClient.getHttpClient();
    }


    public synchronized AbstractRLHttpClient getRLHttpClient() {
        if (mFreeQueue.size() > 0) {
            AbstractRLHttpClient wdHttpClient = mFreeQueue.remove();
            mUsedQueue.add(wdHttpClient);
            return wdHttpClient;
        }

        return null;
    }

    public RLHttpResponse execute(HttpRequestBase request, Map<String, String> headers) throws IOException {
        URI uri = request.getURI();
        AbstractRLHttpClient wdHttpClient = getRLHttpClient();
        if (wdHttpClient == null) {
            throw new IOException("httpClient is NULL!");
        } else {
            RLHttpResponse wdHttpResponse = wdHttpClient.execute(request, headers);
            String path = uri.getPath();
            if (path.contains("login.do")) {
                org.apache.http.impl.client.AbstractHttpClient httpClient = (org.apache.http.impl.client.AbstractHttpClient) wdHttpClient.getHttpClient();
                CookieStore cookieStore = httpClient.getCookieStore();
                synchronized (this) {
                    for (AbstractRLHttpClient usedHttpClient : mUsedQueue) {
                        if (usedHttpClient == wdHttpClient) {
                            continue;
                        }

                        httpClient = (org.apache.http.impl.client.AbstractHttpClient) usedHttpClient.getHttpClient();
                        httpClient.setCookieStore(cookieStore);
                    }

                    for (AbstractRLHttpClient usedHttpClient : mFreeQueue) {
                        httpClient = (org.apache.http.impl.client.AbstractHttpClient) usedHttpClient.getHttpClient();
                        httpClient.setCookieStore(cookieStore);
                    }
                }
            }

            return new PooledClientHttpResponse(wdHttpClient, request, wdHttpResponse);
        }
    }


    /** 
     * @param httpClient
     * @param request
     * @param response
     * @return
     * @see com.roiland.crm.core.http.AbstractRLHttpClient#createRLHttpResponse(org.apache.http.client.HttpClient, org.apache.http.HttpRequest, org.apache.http.HttpResponse)
     */
    @Override
    protected RLHttpResponse createRLHttpResponse(HttpClient httpClient, HttpRequest request,
                                                  HttpResponse response) {
        return null;
    }


    public class PooledClientHttpResponse extends RLHttpResponse {
        private RLHttpResponse mWdHttpResponse;
        private AbstractRLHttpClient httpClient;
        private PooledClientHttpResponse(AbstractRLHttpClient httpClient, HttpRequest request, RLHttpResponse wdHttpResponse) {
            super(request, wdHttpResponse.mResponse);
            this.httpClient = httpClient;
            mWdHttpResponse = wdHttpResponse;
        }

        @Override
        public void release() {
            mWdHttpResponse.release();
            synchronized (PooledHttpClient.this) {
                mUsedQueue.remove(httpClient);
                mFreeQueue.add(httpClient);

                if (Log.DEBUG.get()) {
                    Log.d(tag, "----------> PooledClientHttpResponse release(" + mFreeQueue.size() + ") <----------");
                }
            }

            super.release();
        }
    }

    public PooledHttpClient clone() {
        return this;
    }


    /** 
     * 
     * @see com.roiland.crm.core.http.HttpClientInterface#saveCurrentCookies()
     */
    @Override
    public void saveCurrentCookies() {
    }


    /** 
     * 
     * @see com.roiland.crm.core.http.HttpClientInterface#printCookies()
     */
    @Override
    public void printCookies() {
    }


    /** 
     * @return
     * @see com.roiland.crm.core.http.HttpClientInterface#hasSession()
     */
    @Override
    public boolean hasSession() {
        return false;
    }
}
