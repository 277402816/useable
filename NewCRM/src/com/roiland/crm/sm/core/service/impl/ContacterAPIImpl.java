package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.service.ContacterAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 联系人接口实现类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ContacterAPIImpl.java, v 0.1 2013-8-2 上午10:59:58 shuang.gao Exp $
 */
public class ContacterAPIImpl extends AbstractBaseAPI implements ContacterAPI {
    private static final String tag = Log.getTag(ContacterAPIImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public List<Contacter> getContacterList(String userID, String dealerOrgID,
            String projectID, String customerID) throws ResponseException {
        ReleasableList<Contacter> contacterList = null;
        try {

            // 获取联系人列表.
            if (userID == null || dealerOrgID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }

            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", projectID);
            params.put("customerID", customerID);

            // 获取缓存Key
            String key = getKey(URLContact.METHOD_GET_CONTACTER_LIST, params);

            // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
            contacterList = lruCache.get(key);
            if (contacterList != null && !contacterList.isExpired()) {
                return contacterList;
            }

            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_GET_CONTACTER_LIST), params, null);
            if (response.isSuccess()) {
                contacterList = new ArrayReleasableList<Contacter>();
                String data = getSimpleString(response);
                Log.i("getContacterList", data);
                JSONObject jsonBean = new JSONObject(data);
                JSONArray contacter = jsonBean.getJSONArray("result");
                int n=contacter.length();
                for (int i = 0; i < n; i++) {
                    JSONObject json = contacter.getJSONObject(i);
                    Contacter resultContacter = new Contacter();
                    resultContacter.setContacterID(json
                            .getString("contacterID"));
                    resultContacter.setProjectID(String.valueOf(json.getString("projectID")));
                    resultContacter.setCustomerID(String.valueOf(json.getString("customerID")));
                    resultContacter.setContName(parsingString(json.get("contName")));
                    resultContacter.setContMobile(parsingString(json.get("contMobile")));
                    resultContacter.setContOtherPhone(parsingString(json
                            .get("contOtherPhone")));
                    resultContacter.setIsPrimContanter(String.valueOf(json
                            .getBoolean("isPrimContanter")));
                    resultContacter.setContGenderCode(parsingString(json
                            .get("contGenderCode")));
                    resultContacter.setContGender(parsingString(json.get("contGender")));
                    resultContacter.setContBirthday(String.valueOf(DataVerify.isZero(json
                            .getString("contBirthday"))));
                    resultContacter.setIdNumber(parsingString(json.get("idNumber")));
                    resultContacter.setAgeScopeCode(parsingString(json
                            .get("ageScopeCode")));
                    resultContacter.setAgeScope(parsingString(json.get("ageScope")));
                    resultContacter.setContType(parsingString(json.get("contType")));
                    resultContacter.setContTypeCode(String.valueOf(json
                            .getString("contTypeCode")));
                    resultContacter.setContRelationCode(String.valueOf(json
                            .getString("contRelationCode")));
                    resultContacter.setContRelation(parsingString(json
                            .get("contRelation")));
                    resultContacter.setLicenseValid(StringUtils.toLong(DataVerify.isZero(json.getString("licenseValid"))));
                    contacterList.add(resultContacter);

                }
                // 缓存数据
                if (contacterList != null) {
                    lruCache.put(key, contacterList);
                }
                return contacterList;
            }
            throw new ResponseException();
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        }

    }

    @Override
    public Contacter createContacter(String userID, String dealerOrgID,
            Contacter contacter) throws ResponseException {

        // 创建联系人
        Contacter returnContacter = null;
        try {
            if (userID == null || dealerOrgID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", contacter.getProjectID());
            params.put("customerID", contacter.getCustomerID());
            params.put("contName", contacter.getContName());
            params.put("contMobile", contacter.getContMobile());
            params.put("contOtherPhone", contacter.getContOtherPhone());
            params.put("isPrimContanter", contacter.getIsPrimContanter());
            params.put("contGenderCode", contacter.getContGenderCode());
            params.put("contBirthday", contacter.getContBirthday());
            params.put("idNumber", contacter.getIdNumber());
            params.put("ageScopeCode", contacter.getAgeScopeCode());
            params.put("contTypeCode", contacter.getContTypeCode());
            params.put("contRelationCode", contacter.getContRelationCode());
            if( contacter.getLicenseValid() == 0){
                params.put("licenseValid", null);
            }else
            params.put("licenseValid", contacter.getLicenseValid());

            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_CREATE_CONTACTER), params, null);

            if (response.isSuccess()) {
                
                returnContacter = contacter;
                JSONObject result = new JSONObject(getSimpleString(response));
                String node = null;
                String error = null;
                JSONArray nodeArray = result.names();
                if (nodeArray != null) {
                    for (int i=0; i<nodeArray.length(); i++) {
                        node = nodeArray.get(i).toString();
                        if (node.equalsIgnoreCase("success")) {
                            
                            Boolean success = Boolean.parseBoolean(result.getString("success"));
                            if (success) {
                             // 联系人ID
                                returnContacter.setContacterID(result.getString("contacterID"));
                            }
                        } else if (node.equalsIgnoreCase("validate_error")) {
                            error = parsingValidation(result.getJSONObject(node));
                            throw new ResponseException(error);
                        }
                    }
                }

                return returnContacter;
            }
            throw new ResponseException();
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        }
    }

    @Override
    public Contacter updateContacter(String userID, String dealerOrgID,
            Contacter contacter) throws ResponseException {
        // 修改联系人
        Contacter returnContacter = null;
        try {
            if (userID == null || dealerOrgID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", contacter.getProjectID());
            params.put("customerID", contacter.getCustomerID());
            params.put("contacterID", contacter.getContacterID());
            params.put("contName", contacter.getContName());
            params.put("contMobile", contacter.getContMobile());
            params.put("contOtherPhone", contacter.getContOtherPhone());
            params.put("isPrimContanter", Boolean.parseBoolean(contacter.getIsPrimContanter()));
            params.put("contGenderCode", contacter.getContGenderCode());
            params.put("contBirthday", contacter.getContBirthday());
            params.put("idNumber", contacter.getIdNumber());
            params.put("ageScopeCode", contacter.getAgeScopeCode());
            params.put("contTypeCode", contacter.getContTypeCode());
            params.put("contRelationCode", contacter.getContRelationCode());
            if( contacter.getLicenseValid() == 0)
                params.put("licenseValid", null);
            else
            params.put("licenseValid", contacter.getLicenseValid());

            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_UPDATE_CONTACTER), params, null);

            if (response.isSuccess()) {
                returnContacter = contacter;
                JSONObject result = new JSONObject(getSimpleString(response));
                // 是否创建成功
                String node = null;
                String error = null;
                JSONArray nodeArray = result.names();
                if (nodeArray != null) {
                    for (int i=0; i<nodeArray.length(); i++) {
                        node = nodeArray.get(i).toString();
                        if (node.equalsIgnoreCase("success")) {
                            
                            Boolean success = Boolean.parseBoolean(result.getString("success"));
//                            if (success) {
//                             // 联系人ID
//                                returnContacter.setContacterID(result.getString("contacterID"));
//                            }
                        } else if (node.equalsIgnoreCase("validate_error")) {
                            error = parsingValidation(result.getJSONObject(node));
                            throw new ResponseException(error);
                        }
                    }
                }
                return returnContacter;
            }
            throw new ResponseException();
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e.getMessage());
        }
    }

    @Override
    public List<Dictionary> getEmployeeList() throws ResponseException {
        List<Dictionary> employeeList = null;
        try {
            JSONObject params = new JSONObject();
            params.put("status", "3");
            RLHttpResponse response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_EMPLOYEE_LIST), params, null);
            if(response.isSuccess()){
                employeeList = new ArrayList<Dictionary>();
                JSONObject result = new JSONObject(getSimpleString(response));
                JSONArray array = result.getJSONArray("list");
                for(int i=0;i<array.length();i++){
                    Dictionary dic = new Dictionary();
                    JSONObject object = array.getJSONObject(i);
                    dic.setDicKey(object.getString("id"));
                    dic.setDicValue(object.getString("name"));
                    employeeList.add(dic);
                }
            }
        } catch (IOException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        }

        return employeeList;
    }
}