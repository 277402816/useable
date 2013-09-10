package com.roiland.crm.sm.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.roiland.crm.sm.GlobalConstant;
import com.roiland.crm.sm.core.http.HttpClientInterface;
import com.roiland.crm.sm.core.http.RLHttpClient;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.utils.LRUCache;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * API基础抽象类
 * </pre>
 *
 * @author Chunji Li
 * @version $Id: AbstractBaseAPI.java, v 0.1 2013-8-2 上午10:56:37 Chunji Li Exp $
 */
public abstract class AbstractBaseAPI {
    private static final String                       tag                  = Log
                                                                               .getTag(AbstractBaseAPI.class);

    protected static HttpClientInterface              httpClient;
    protected User                                    user;
    // 缓存过期时间为3分钟
    public static final long                          RELEASE_EXPIRED_TIME = 3 * 60 * 1000L;
    // 存储内存中的临时缓存数据
    @SuppressWarnings("rawtypes")
    protected static LRUCache<String, ReleasableList> lruCache             = new LRUCache<String, ReleasableList>(
                                                                               100);

    public synchronized HttpClientInterface getHttpClient() {
        if (httpClient == null) {
            httpClient = new RLHttpClient();
        }
        return httpClient;
    }

    public void setHttpClient(HttpClientInterface httpClient) {
        this.httpClient = httpClient;
    }

    public String getURLAddress(String formatRUL) {
        String url = String.format(formatRUL, GlobalConstant.BASE_ADDRESS);
        return url;
    }

    /**
     * 
     * <pre>
     * 将响应数据转变成String型
     * </pre>
     *
     * @param response
     * @return
     */
    protected static String getSimpleString(RLHttpResponse response) {
        String result = null;
        BufferedReader reader = null;
        try {
            HttpEntity entity = response.getResponse().getEntity();
            String encoding = HTTP.UTF_8;
            Header encodingHeader = entity.getContentEncoding();
            if (encodingHeader != null && !TextUtils.isEmpty(encodingHeader.getValue())) {
                encoding = encodingHeader.getValue();
            }

            InputStream input = entity.getContent();
            reader = new BufferedReader(new InputStreamReader(input, encoding));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            result = builder.toString();
            Log.i(tag, "result :== " + result);
        } catch (IllegalStateException e) {
            Log.e(tag, "getSimpleString", e);
        } catch (IOException e) {
            Log.e(tag, "getSimpleString", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(tag, e.getMessage(), e);
                }
            }
        }

        return result;
    }

    /**
     * 
     * <pre>
     * 添加dealerOrgID和userID
     * </pre>
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public JSONObject appendBaseParams(JSONObject result) throws JSONException {
        if (user != null) {
            result.put("dealerOrgID", user.getDealerOrgID());
            result.put("userID", user.getUserID());
        }
        return result;
    }

    /**
     * 
     * <pre>
     * 返回成功标志
     * </pre>
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public boolean isResponseSuccess(JSONObject result) throws JSONException {
        if (result == null) {
            return false;
        }
        if (result.has("status")) {
            int status = result.getInt("status");
            return status == 200;
        }
        return false;
    }

    /**
     * 
     * <pre>
     * 生成缓存Key
     * </pre>
     *
     * @param url
     * @param params
     * @return
     */
    public String getKey(String url, JSONObject params) {
        return url + params.toString();
    }

    public String parsingString(Object node) {
        if (node.equals(null)) {

            return null;
        }
        return node.toString();
    }

    /**
     * 
     * <pre>
     * 字符串类型转长整数类型方法， 如果为空则返回 0
     * </pre>
     *
     * @param node
     * @return
     */
    public long parsingLong(String node) {
        if (StringUtils.isEmpty(node)) {
            return 0;
        }
        return Long.parseLong(node);
    }

    /**
     * 
     * <pre>
     * 字符串类型转整数类型方法， 如果为空则返回 0
     * </pre>
     *
     * @param node
     * @return
     */
    public int parsingInt(String node) {
        if (StringUtils.isEmpty(node)) {
            return 0;
        }
        return Integer.parseInt(node);
    }

    /**
     * 
     * <pre>
     * 字符串类型转double类型方法， 如果为空则返回 0.0f
     * </pre>
     *
     * @param value
     * @return
     */
    public static float parsingIntFloat(String value) {
        if (!StringUtils.isEmpty(value))
            return Float.parseFloat(value);
        return 0.0f;
    }

    /**
     * 
     * <pre>
     * 解析服务器端返回的验证错误信息
     * </pre>
     *
     * @param jsonBean
     * @return
     * @throws JSONException 
     */
    public String parsingValidation(JSONObject jsonBean) throws JSONException {
        JSONArray nameArray = jsonBean.names();
        if (nameArray != null) {
            for (int i = 0; i < nameArray.length();) {
                return jsonBean.getString(nameArray.get(i).toString());
            }
        }
        return null;
    }

    /**
     * 是设定时间限制的ArrayList, 使用时检查列表中数据是否已超时，如果超时将列表中数据释放，重新从API接口获取
     * 
     * @author cjyy
     * 
     * @param <E>
     */
    public class ArrayReleasableList<E> extends ArrayList<E> implements ReleasableList<E> {
        private static final long   serialVersionUID = 1L;

        private final AtomicInteger count            = new AtomicInteger(0);

        private long                mCreatedTime     = System.currentTimeMillis();

        private AtomicBoolean       mDirty           = new AtomicBoolean(false);

        public ArrayReleasableList() {
            super();
        }

        public ArrayReleasableList(int capacity) {
            super(capacity);
        }

        public ArrayReleasableList(Collection<? extends E> collection) {
            super(collection);
        }

        @Override
        public void acquire() {
            count.incrementAndGet();
        }

        @Override
        public void release() {
            count.decrementAndGet();
        }

        @Override
        public boolean isExpired() {
            return System.currentTimeMillis() - mCreatedTime > RELEASE_EXPIRED_TIME || isDirty();
        }

        @Override
        public int getSemaphore() {
            return count.get();
        }

        @Override
        public boolean isDirty() {
            return mDirty.get();
        }

        @Override
        public void setDirty(boolean newValue) {
            mDirty.set(newValue);
        }

    }
}
