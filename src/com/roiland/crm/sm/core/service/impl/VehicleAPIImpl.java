package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.core.service.VehicleAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;

/**
 * 
 * <pre>
 * 车辆资源实现类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: VehicleAPIImpl.java, v 0.1 2013-8-2 上午11:14:42 shuang.gao Exp $
 */
public class VehicleAPIImpl extends AbstractBaseAPI implements VehicleAPI {
    private static final String tag = Log.getTag(VehicleAPIImpl.class);

    /**
     * @throws ResponseException 
     * @see com.roiland.crm.core.service.VehicleAPI#getCarResList(com.roiland.crm.core.model.User, java.lang.String, java.lang.String, java.lang.String, com.roiland.crm.core.model.Vehicle, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> getCarResList(User user, String searchType, String searchWord,
                                       String searchColumns, Vehicle vehicle, Integer startNum,
                                       Integer rowCount, String sortRule) throws ResponseException {

        ReleasableList<Vehicle> vehicleList = null;
        if (user == null || searchType == null) {
            throw new ResponseException("User is null.");
        }
        RLHttpResponse response = null;
        try {
            // 添加输入的参数
            JSONObject params = new JSONObject();
            params.put("userID", user.getUserID());
            params.put("dealerOrgID", user.getDealerOrgID());
            params.put("searchType", searchType);
            if (searchType.equals("0")) {
                params.put("searchWord", searchWord);
                params.put("searchColumns", searchColumns);
            } else {
                if (vehicle != null) {
                    params.put("brandCode", vehicle.getBrandCode());
                    params.put("modelCode", vehicle.getModelCode());
                    params.put("outsideColorCode", vehicle.getOutsideColorCode());
                    params.put("insideColorCode", vehicle.getInsideColorCode());
                    params.put("vehiConfigCode", vehicle.getVehiConfigCode());
                }
            }
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("sortRule", sortRule);

            // 获取缓存Key
            String key = getKey(URLContact.METHOD_GET_CAR_RES_LIST, params);

            // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
            vehicleList = lruCache.get(key);
            if (vehicleList != null && !vehicleList.isExpired()) {
                return vehicleList;
            }

            // 创建连接
            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_CAR_RES_LIST), params, null);
            // 判断连接是否成功
            if (response.isSuccess()) {
                vehicleList = new ArrayReleasableList<Vehicle>();

                JSONObject result = new JSONObject(getSimpleString(response));
                JSONArray arrayResult = (JSONArray) result.getJSONArray("result");

                int n = arrayResult.length();

                for (int i = 0; i < n; i++) {

                    JSONObject objectResult = arrayResult.getJSONObject(i);
                    Vehicle vehicleTemp = new Vehicle();
                    // 取得返回值
                    vehicleTemp.setVehicleID(String.valueOf(objectResult.getString("vehicleID")));
                    vehicleTemp.setChassisNo(String.valueOf(objectResult.getString("chassisNo")));
                    vehicleTemp.setBrandCode(String.valueOf(objectResult.getString("brandCode")));
                    vehicleTemp.setBrand(String.valueOf(objectResult.getString("brand")));
                    vehicleTemp.setModelCode(String.valueOf(objectResult.getString("modelCode")));
                    vehicleTemp.setModel(String.valueOf(objectResult.getString("model")));
                    vehicleTemp.setOutsideColorCode(String.valueOf(objectResult
                        .getString("outsideColorCode")));
                    vehicleTemp.setOutsideColor(String.valueOf(objectResult
                        .getString("outsideColor")));
                    vehicleTemp.setInsideColorCode(String.valueOf(objectResult
                        .getString("insideColorCode")));
                    vehicleTemp
                        .setInsideColor(String.valueOf(objectResult.getString("insideColor")));
                    vehicleTemp.setVehiConfigCode(String.valueOf(objectResult
                        .getString("vehiConfigCode")));
                    vehicleTemp.setVehiConfig(String.valueOf(objectResult.getString("vehiConfig")));
                    vehicleTemp.setVehiStatus(String.valueOf(objectResult.getString("vehiStatus")));
                    vehicleTemp
                        .setStoreStatus(String.valueOf(objectResult.getString("storeStatus")));
                    vehicleTemp.setTagStatus(String.valueOf(objectResult.getString("tagStatus")));
                    vehicleTemp.setUserID(String.valueOf(objectResult.getString("userID")));
                    vehicleTemp.setOrderID(String.valueOf(objectResult.getString("orderNo")));

                    vehicleList.add(vehicleTemp);
                }
                // 缓存数据
                if (vehicleList != null) {
                    lruCache.put(key, vehicleList);
                }
                // 返回服务器端的返回值
                return vehicleList;
            }
            throw new ResponseException();
        } catch (JSONException e) {
            Log.e(tag, "Paring json exception.", e);
            throw new ResponseException(e);
        } catch (IOException e) {
            Log.e(tag, "Http connection exception.", e);
            throw new ResponseException(e);
        } catch (Exception e) {
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
    }

    /**
     * @see com.roiland.crm.core.service.VehicleAPI#getCarResList(com.roiland.crm.core.model.User, java.lang.String, java.lang.String, java.lang.String[], com.roiland.crm.core.model.Vehicle, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    @Override
    public List<Vehicle> getCarResList(User user, String searchType, String searchWord,
                                       String[] searchColumns, Vehicle vehicle, Integer startNum,
                                       Integer rowCount, String sortRule) throws ResponseException {

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        if (user == null) {
            throw new ResponseException("User is null.");
        }
        RLHttpResponse response = null;
        try {
            // 添加输入的参数
            JSONObject params = new JSONObject();
            params.put("userID", user.getUserID());
            params.put("dealerOrgID", user.getDealerOrgID());
            params.put("searchType", searchType);
            params.put("searchWord", searchWord);
            JSONArray array = new JSONArray();
            for (String item : searchColumns) {
                array.put(item);
            }

            params.put("searchColumns", array);
            if (vehicle.getBrandCode() != null)
                params.put("brandCode", vehicle.getBrandCode());
            if (vehicle.getModelCode() != null)
                params.put("modelCode", vehicle.getModelCode());
            if (vehicle.getOutsideColorCode() != null)
                params.put("outsideColorCode", vehicle.getOutsideColorCode());
            if (vehicle.getInsideColorCode() != null)
                params.put("insideColorCode", vehicle.getInsideColorCode());
            if (vehicle.getVehiConfigCode() != null)
                params.put("vehiConfigCode", vehicle.getVehiConfigCode());
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("sortRule", sortRule);

            // 创建连接
            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_CAR_RES_LIST), params, null);
            // 判断连接是否成功
            if (response.isSuccess()) {

                JSONObject result = new JSONObject(getSimpleString(response));
                JSONArray arrayResult = (JSONArray) result.getJSONArray("result");
                System.out.println(arrayResult);
                int n = arrayResult.length();
                System.out.println(n);
                for (int i = 0; i < n; i++) {

                    JSONObject objectResult = arrayResult.getJSONObject(i);
                    Vehicle vehicleTemp = new Vehicle();
                    // 取得返回值
                    vehicleTemp.setVehicleID(String.valueOf(objectResult.getString("vehicleID")));
                    vehicleTemp.setChassisNo(String.valueOf(objectResult.getString("chassisNo")));
                    vehicleTemp.setBrandCode(String.valueOf(objectResult.getString("brandCode")));
                    vehicleTemp.setBrand(String.valueOf(objectResult.getString("brand")));
                    vehicleTemp.setModelCode(String.valueOf(objectResult.getString("modelCode")));
                    vehicleTemp.setModel(String.valueOf(objectResult.getString("model")));
                    vehicleTemp.setOutsideColorCode(String.valueOf(objectResult
                        .getString("outsideColorCode")));
                    vehicleTemp.setOutsideColor(String.valueOf(objectResult
                        .getString("outsideColor")));
                    vehicleTemp.setInsideColorCode(String.valueOf(objectResult
                        .getString("insideColorCode")));
                    vehicleTemp
                        .setInsideColor(String.valueOf(objectResult.getString("insideColor")));
                    vehicleTemp.setVehiConfigCode(String.valueOf(objectResult
                        .getString("vehiConfigCode")));
                    vehicleTemp.setVehiConfig(String.valueOf(objectResult.getString("vehiConfig")));
                    vehicleTemp.setVehiStatus(String.valueOf(objectResult.getString("vehiStatus")));
                    vehicleTemp
                        .setStoreStatus(String.valueOf(objectResult.getString("storeStatus")));
                    vehicleTemp.setTagStatus(String.valueOf(objectResult.getString("tagStatus")));
                    vehicleTemp.setUserID(String.valueOf(objectResult.getString("userID")));
                    vehicleTemp.setOrderID(String.valueOf(objectResult.getString("orderNo")));
                    System.out.println(vehicleTemp);
                    vehicleList.add(vehicleTemp);
                    System.out.println(vehicleList);
                }
                // 返回服务器端的返回值

            }
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (Exception e) {
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
        // 返回服务器端的返回值
        return vehicleList;
    }
}
