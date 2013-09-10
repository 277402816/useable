package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.service.VersionAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 获取版本号和下载地址
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: VersionAPIImpl.java, v 0.1 2013-6-20 下午12:57:29 shuang.gao Exp $
 */
public class VersionAPIImpl extends AbstractBaseAPI implements VersionAPI {
    private static final String tag = Log.getTag(VersionAPIImpl.class);

    @Override
    public String[] getVersion() throws ResponseException {

        String[] version;
        JSONObject params = new JSONObject();
        try {
            // 连接服务器
            RLHttpResponse response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_VERSION), params, null);
            // 判断是否连接成功，如果连接成功执行下列操作
            if (response.isSuccess()) {
                version = new String[2];
                JSONObject result = new JSONObject(getSimpleString(response));
                version[0] = result.getString("url");
                version[1] = result.getString("version");
                // 解析返回结果
                return version;
            }
        } catch (JSONException e) {
            Log.e(tag, "Parsing json data error.", e);
            throw new ResponseException(e);
        } catch (IOException e) {
            Log.e(tag, "Connect to server error.", e);
            throw new ResponseException(e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, e.getMessage());
            throw new ResponseException(e);
        }
        return null;
    }

}
