package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.core.service.AttachAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 文档图片接口实现类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: AttachAPIImpl.java, v 0.1 2013-8-2 上午10:57:43 shuang.gao Exp $
 */
public class AttachAPIImpl extends AbstractBaseAPI implements AttachAPI {
    private static final String tag = Log.getTag(AttachAPIImpl.class);

    /**
     * @see com.roiland.crm.sm.core.service.AttachAPI#getAttachmentList(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Attach> getAttachmentList(String userID, String dealerOrgID, String projectID,
                                          String customerID) throws ResponseException {

        if (userID == null || dealerOrgID == null) {
            throw new ResponseException("userID or dealerOrgID is null.");
        }

        JSONObject params = new JSONObject();

        ReleasableList<Attach> attachList = null;

        try {
            // 添加参数
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", projectID);
            params.put("customerID", customerID);

            // 获取缓存Key
            String key = getKey(URLContact.METHOD_GET_ATTACHMENT_LIST, params);

            // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
            attachList = lruCache.get(key);
            if (attachList != null && !attachList.isExpired()) {
                return attachList;
            }
            // 创建连接
            RLHttpResponse response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_ATTACHMENT_LIST), params, null);
            // 判断连接是否成功
            if (response.isSuccess()) {
                attachList = new ArrayReleasableList<Attach>();

                // 接受返回结果
                JSONObject result = new JSONObject(getSimpleString(response));
                // 解析返回结果
                JSONArray arrayResult = new JSONArray();
                arrayResult = result.getJSONArray("result");
                int n = arrayResult.length();
                for (int i = 0; i < n; i++) {
                    Attach attachTemp = new Attach();
                    JSONObject objectResult = arrayResult.getJSONObject(i);
                    attachTemp.setAttachmentID(objectResult.getString("attachmentID"));
                    attachTemp.setAttachName(objectResult.getString("attachName"));
                    attachTemp.setUploadDate(objectResult.getString("uploadDate"));
                    attachTemp.setPictureSuffix(objectResult.getString("pictureSuffix"));
                    if (StringUtils.isEmpty(attachTemp.getPictureSuffix())) {
                        attachTemp.setPictureSuffix(StringUtils.extractSuffix(attachTemp
                            .getAttachName()));
                    }
                    attachTemp.setPictureURL(objectResult.getString("pictureURL"));
                    attachTemp.setComment(objectResult.getString("comment"));
                    attachList.add(attachTemp);
                }
                // 缓存数据
                if (attachList != null) {
                    lruCache.put(key, attachList);
                }
                return attachList;
            }
            throw new ResponseException();
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (Exception e) {
            throw new ResponseException(e);
        }
    }

    /**
     * 上传文档
     * @see com.roiland.crm.sm.core.service.AttachAPI#uploadDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Attach uploadDocument(String userID, String dealerOrgID, String projectID,
                                 String customerID, String documentName, String pictureSuffix)
                                                                                              throws ResponseException {

        JSONObject params = new JSONObject();
        Attach attach = null;
        if (userID == null || dealerOrgID == null) {
            throw new ResponseException("userID or dealerOrgID is null.");
        }

        try {
            // 添加参数
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", projectID);
            params.put("customerID", customerID);
            params.put("documentName", documentName);
            params.put("pictureSuffix", pictureSuffix);

            // 创建连接
            RLHttpResponse response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_UPLOAD_DOCUMENT), params, null);
            // 判断连接是否成功
            if (response.isSuccess()) {
                attach = new Attach();
                JSONObject result = new JSONObject(getSimpleString(response));
                System.out.println(result);
                JSONArray arrayResult = new JSONArray();
                arrayResult = result.getJSONArray("result");
                int n = arrayResult.length();
                for (int i = 0; i < n; i++) {
                    JSONObject objecResult = arrayResult.getJSONObject(i);
                    attach.setAttachmentID(objecResult.getString("attachmentID"));
                    System.out.println(result.getString("attachmentID"));
                }
                return attach;
            }
            throw new ResponseException();
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (Exception e) {
            throw new ResponseException(e);
        }
    }

    @Override
    public InputStream downloadFile(String attachmentID) throws ResponseException {

        JSONObject params = new JSONObject();
        try {
            // 添加参数
            params.put("attachmentID", attachmentID);

            // 创建连接
            RLHttpResponse response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_DOWNLOAD_FILE), params, null);
            // 判断是否连接成功
            if (response.isSuccess()) {
                return response.getInputStream();
            }
            throw new ResponseException();
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (Exception e) {
            throw new ResponseException(e);
        }
    }
}
