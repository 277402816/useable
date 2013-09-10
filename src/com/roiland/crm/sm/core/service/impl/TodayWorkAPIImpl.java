package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.StatusCodeConstant;
import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.TodayWorkContent;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.service.TodayWorkAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;

/**
 * 
 * <pre>
 * 今日待办事项实现类
 * </pre>
 *
 * @author cjyy
 * @version $Id: TodayWorkAPIImpl.java, v 0.1 2013-5-14 下午05:08:52 cjyy Exp $
 */
public class TodayWorkAPIImpl extends AbstractBaseAPI implements TodayWorkAPI {
	private static final String tag = Log.getTag(TodayWorkAPIImpl.class);
	

	public boolean login(User user) throws ResponseException {
		if (user == null) 
			return true;
		RLHttpResponse response = null;
		try {
			JSONObject requestJSON = new JSONObject();
			requestJSON.put("username", user.getUserName());
			requestJSON.put("password", user.getPassword());
			if (user.getDealerOrgID() != null) {
				requestJSON.put("dealerOrgID", user.getDealerOrgID());
			}
			response = getHttpClient().executePostJSON(getURLAddress(URLContact.METHOD＿LOGIN), requestJSON, null);
			if (response.isSuccess()) {
				String data = getSimpleString(response);
				JSONObject result = new JSONObject(data);
					JSONObject userObject = result.getJSONObject("user");
					user.setUserID(userObject.getString("userID"));
					user.setDealerOrgID(userObject.getString("dealerOrgID"));
					user.setDepartName(userObject.getString("departmentName"));
					user.setUserName(userObject.getString("salesConsultantName"));
					user.setDealerOrgName(userObject.getString("dealerOrgName"));
					user.setPosiName(userObject.getString("roleName"));
					
					this.user = user; //Save user info
//					getHttpClient().saveCurrentCookies();
					return true;
				
			}
			throw new ResponseException(401);
		} catch (IOException e) {
			Log.e(tag, "Connection network error.", e);
			throw new ResponseException(e);
		} catch (JSONException e) {
			Log.e(tag, "Parsing data error.", e);
			 throw new ResponseException(StatusCodeConstant.UNKNOWUSER);
		} catch (Exception e) {
			throw new ResponseException(e);
		} finally {
            if (response != null) {
                response.release();
            }
        }
	}
	
	/**
	 * Get todo working content.
	 * @throws ResponseException 
	 */
	@Override
	public TodayWorkContent getTodayWorking(User user) throws ResponseException {
	    RLHttpResponse response = null;
		try {
			if (user == null) {
				throw new ResponseException("User has not got autherization.");
			}
			
			JSONObject params = new JSONObject();
			params.put("userID", user.getUserID());
			
			response = getHttpClient().executePostJSON(getURLAddress(URLContact.METHOD_TODAY_WORK_CONTENT), params, null);
			if (response.isSuccess()) {
				String data = getSimpleString(response);
				Log.i(tag, "getTodayWorkContent: \n" + data);
				JSONObject result = new JSONObject(data);
				
				TodayWorkContent todayWorkContent = new TodayWorkContent();
				todayWorkContent.setUser(user);
				todayWorkContent.setExpiredActionPlanCount(String.valueOf(result.getInt("expiredActionPlanCount")));
				todayWorkContent.setTodayActionPlanCount(String.valueOf(result.getInt("todayActionPlanCount")));
				todayWorkContent.setExpiredSalesProjectCount(String.valueOf(result.getInt("expiredSalesProjectCount")));
				todayWorkContent.setWaitingTransCollectCount(String.valueOf(result.getInt("waitingTransCollectCount")));
				todayWorkContent.setThreeDaysSalesProjectCount(String.valueOf(result.getInt("threeDaysSalesProjectCount")));
				todayWorkContent.setThreeDaysDeliveryOrderCount(String.valueOf(result.getInt("threeDaysDeliveryOrderCount")));
				todayWorkContent.setNoActionPlanProjectCount(String.valueOf(result.getInt("noActionPlanProjectCount")));
				todayWorkContent.setTodayCollectCustomers(String.valueOf(result.getInt("todayCollectCustomers")));
				todayWorkContent.setTodayCustomerOrder(String.valueOf(result.getInt("todayCustomerOrder")));
				todayWorkContent.setTodaySalesProject(String.valueOf(result.getInt("todaySalesProject")));
				todayWorkContent.setTodayCompleteSalesProject(String.valueOf(result.getInt("todaycompleteSalesProject")));
				todayWorkContent.setCompleteTracPlan(String.valueOf(result.getInt("completeTracPlan")));
				todayWorkContent.setTodayDestroySalesProject(String.valueOf(result.getInt("todayDestroySalesProject")));
				todayWorkContent.setTodayTestDrive(String.valueOf(result.getInt("todayTestDrive")));
				return todayWorkContent;
				
			}
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
		return null;
	}

}
