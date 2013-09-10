package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.core.service.DriveTestAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 试乘试驾接口实现类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: DriveTestAPIImpl.java, v 0.1 2013-8-2 上午11:03:40 shuang.gao Exp $
 */
public class DriveTestAPIImpl extends AbstractBaseAPI implements DriveTestAPI {
	private static final String tag = Log.getTag(DriveTestAPIImpl.class);

	@Override
	public String createDriveTest(String userID, String dealerOrgID,
			DriveTest driveTest) throws ResponseException {
        RLHttpResponse response = null;
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			// 传入参数
			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("projectID", driveTest.getProjectID());
			params.put("driverName", driveTest.getDriverName());
			params.put("driverMobile", driveTest.getDriverMobile());
			params.put("driverLicenseNo", driveTest.getDriverLicenseNo());
			params.put("driveChassisNo", driveTest.getDriveChassisNo());
			params.put("driveLicensePlate", driveTest.getDriveLicensePlate());
			params.put("driveStatus", driveTest.getDriveStatus());
			params.put("driveStartTime", driveTest.getDriveStartTime());
			params.put("driveEndTime", driveTest.getDriveEndTime());
			params.put("driveStartKM", driveTest.getDriveStartKM());
			params.put("driveEndKM", driveTest.getDriveEndKM());
			params.put("driveComment", driveTest.getDriveComment());

			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_CREATE_DRIVETEST), params, null);
			// 返回 试乘试驾ID
			String driveTestID = null;
			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response))
						.getJSONObject("result");
				driveTestID = result.getString("activityID");
				return driveTestID;
			}
			throw new ResponseException();
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			throw new ResponseException(e);
		} catch (Exception e) {
			throw new ResponseException(e);
		} finally {
            if (response != null) {
                response.release();
            }
        }
	}

	@Override
	public Boolean updateDriveTest(String userID, String dealerOrgID,
			DriveTest driveTest) throws ResponseException {

        RLHttpResponse response = null;
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			// 传入参数
			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("projectID", driveTest.getProjectID());
			params.put("driveTestID", driveTest.getDriveTestID());
			params.put("driverName", driveTest.getDriverName());
			params.put("driverMobile", driveTest.getDriverMobile());
			params.put("driverLicenseNo", driveTest.getDriverLicenseNo());
			params.put("driveChassisNo", driveTest.getDriveChassisNo());
			params.put("driveLicensePlate", driveTest.getDriveLicensePlate());
			params.put("driveStatus", driveTest.getDriveStatus());
			params.put("driveStartTime", driveTest.getDriveStartTime());
			params.put("driveEndTime", driveTest.getDriveEndTime());
			params.put("driveStartKM", driveTest.getDriveStartKM());
			params.put("driveEndKM", driveTest.getDriveEndKM());
			params.put("driveComment", driveTest.getDriveComment());

			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_UPDATE_DRIVETEST), params, null);
			boolean success;
			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response))
						.getJSONObject("result");

				// 是否创建成功
				success = (result.getBoolean("success"));

				return success;
			}
			throw new ResponseException();
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			throw new ResponseException(e);
		} catch (Exception e) {
			throw new ResponseException(e);
		} finally {
            if (response != null) {
                response.release();
            }
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DriveTest> getDriveTestList(String userID, String dealerOrgID,
			String searchWord, String searchColumns, int startNum,
			int rowCount, String sortRule) throws ResponseException {
		ReleasableList<DriveTest> list = null;
        RLHttpResponse response = null;
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			// 传入参数
			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("searchWord", searchWord);
			params.put("searchColumns", searchColumns);
			params.put("startNum", startNum);
			params.put("rowCount", rowCount);
			params.put("sortRule", sortRule);

			//获取缓存Key
			String key = getKey(URLContact.METHOD_GET_DRIVETEST_LIST, params);
			
			//从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
			list = lruCache.get(key);
			if (list != null && !list.isExpired()) {
				return list;
			}
			
			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_DRIVETEST_LIST), params, null);
			if (response.isSuccess()) {
				list = new ArrayReleasableList<DriveTest>();
				JSONObject jsonbean = new JSONObject(getSimpleString(response));
				JSONArray json = jsonbean.getJSONArray("result");
				int num = json.length();
				for (int i = 0; i < num; i++) {
					JSONObject result = json.getJSONObject(i);
					DriveTest driveTest = new DriveTest();
					// 返回值
					driveTest.setDriveTestID(result.getString("driveTestID"));
					driveTest.setProjectID(String.valueOf(result
							.getString("projectID")));
					driveTest.setDriverName(String.valueOf(result
							.getString("driverName")));
					driveTest.setDriverMobile(String.valueOf(result
							.getString("driverMobile")));
					driveTest.setDriverLicenseNo(String.valueOf(result
							.getString("driverLicenseNo")));
					driveTest.setDriveChassisNo(String.valueOf(result
							.getString("driveChassisNo")));
					driveTest.setDriveLicensePlate(String.valueOf(result
							.getString("driveLicensePlate")));
					driveTest.setDriveStatusCode(String.valueOf(result
							.getString("driveStatusCode")));
					driveTest.setDriveStatus(String.valueOf(result
							.getString("driveStatus")));
					driveTest.setDriveStartTime((result
							.getString("driveStartTime")));
					driveTest
							.setDriveEndTime((result.getString("driveEndTime")));
					driveTest.setDriveStartKM(String.valueOf(result
							.getString("driveStartKM")));
					driveTest.setDriveEndKM(String.valueOf(result
							.getString("driveEndKM")));
					driveTest.setDriveComment(String.valueOf(result
							.getString("driveComment")));
					list.add(driveTest);
				}
				// 缓存数据
				if (list != null) {
					lruCache.put(key, list);
				}
				return list;
			}
			throw new ResponseException();
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			throw new ResponseException(e);
		} catch (Exception e) {
			throw new ResponseException(e);
		} finally {
            if (response != null) {
                response.release();
            }
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DriveTest> getProjectDriveTest(String projectID) throws ResponseException {
		ReleasableList<DriveTest> projectDriveTestList = null;
		JSONObject params = new JSONObject();
        RLHttpResponse response = null;
		try {
			params.put("projectID", projectID);
			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_PROJECT_DRIVETES_LIST),
					params, null);
			// 获取缓存Key
			String key = getKey(URLContact.METHOD_GET_PROJECT_DRIVETES_LIST,
					params);
			// 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
			projectDriveTestList = lruCache.get(key);
			if (projectDriveTestList != null && !projectDriveTestList.isExpired()) {
				return projectDriveTestList;
			}
			if (response.isSuccess()) {
				projectDriveTestList = new ArrayReleasableList<DriveTest>();
				JSONObject jsonbean = new JSONObject(getSimpleString(response));
				JSONArray json = jsonbean.getJSONArray("result");
				int num = json.length();
				for (int i = 0; i < num; i++) {
					JSONObject result = json.getJSONObject(i);
					DriveTest driveTest = new DriveTest();
					driveTest.setDriveTestID(result.getString("driveTestID"));
					driveTest.setProjectID(String.valueOf(result
							.getString("projectID")));
					driveTest.setDriverName(String.valueOf(result
							.getString("driverName")));
					driveTest.setDriverMobile(String.valueOf(result
							.getString("driverMobile")));
					driveTest.setDriverLicenseNo(String.valueOf(result
							.getString("driverLicenseNo")));
					driveTest.setDriveChassisNo(String.valueOf(result
							.getString("driveChassisNo")));
					driveTest.setDriveLicensePlate(String.valueOf(result
							.getString("driveLicensePlate")));
					driveTest.setDriveStatusCode(String.valueOf(result
							.getString("driveStatusCode")));
					driveTest.setDriveStatus(String.valueOf(result
							.getString("driveStatus")));
					driveTest.setDriveStartTime((result
							.getString("driveStartTime")));
					driveTest
							.setDriveEndTime((result.getString("driveEndTime")));
					driveTest.setDriveStartKM(String.valueOf(result
							.getString("driveStartKM")));
					driveTest.setDriveEndKM(StringUtils.notKMNull(String.valueOf(result
							.getString("driveEndKM"))));
					driveTest.setDriveComment(String.valueOf(result
							.getString("driveComment")));
					projectDriveTestList.add(driveTest);
				}
				// 缓存数据
				if (projectDriveTestList != null) {
					lruCache.put(key, projectDriveTestList);
				}
				return projectDriveTestList;
			}
			throw new ResponseException();
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			throw new ResponseException(e);
		} catch (Exception e) {
			throw new ResponseException(e);
		} finally {
            if (response != null) {
                response.release();
            }
        }
	}
}
