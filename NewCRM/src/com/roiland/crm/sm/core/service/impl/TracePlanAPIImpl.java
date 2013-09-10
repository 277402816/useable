package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.model.TracePlan.AdvancedSearch;
import com.roiland.crm.sm.core.service.TracePlanAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 销售过程管理/跟踪计划 实现类
 * @extends AbstractBaseAPI
 * @implements TracePlanAPI
 * @author shuang.gao
 * @version $Id: TracePlanAPIImpl.java, v 0.1 2013-5-9 下午3:41:49 shuang.gao Exp $
 */
public class TracePlanAPIImpl extends AbstractBaseAPI implements TracePlanAPI {
	private static final String tag = Log.getTag(TracePlanAPIImpl.class);
	
	/**
	 * @see com.roiland.crm.core.service.TracePlanAPI#getTracePlanList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	    @SuppressWarnings("unchecked")
	    @Override
	    public List<TracePlan> getTracePlanList(String userID, String dealerOrgID,
	            String projectID, String customerID, String searchWord,
	            String searchColumns, int startNum, int rowCount)
	            throws ResponseException {

	        // 获取跟踪计划列表
	        ReleasableList<TracePlan> tracePlanList = null;
	        try {
	            if (userID == null || dealerOrgID == null) {
	                throw new ResponseException("userID or dealerOrgID is null.");
	            }
	            JSONObject params = new JSONObject();
	            if (userID != null) {
	                params.put("userID", userID);
	            }
	            if (dealerOrgID != null) {
	                params.put("dealerOrgID", dealerOrgID);
	            }
	            if (projectID != null) {
	                params.put("projectID", projectID);
	            }
	            if (customerID != null) {
	                params.put("customerID", customerID);
	            }

	            if (searchWord != null) {
	                params.put("searchWord", searchWord);
	            }
	            if (searchColumns != null) {
	                params.put("searchColumns", searchColumns);
	            }
	            if (startNum != 0) {
	                params.put("startNum", startNum);
	            }
	            if (rowCount != 0) {
	                params.put("rowCount", rowCount);
	            }
                    params.put("searchType", "0");
	            // 获取缓存Key
	            String key = getKey(URLContact.METHOD_GET_TRACEPLAN_LIST, params);

	            // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
	            tracePlanList = lruCache.get(key);
	            if (tracePlanList != null && !tracePlanList.isExpired()) {
	                return tracePlanList;
	            }

	            RLHttpResponse response = getHttpClient().executePostJSON(
	                    getURLAddress(URLContact.METHOD_GET_TRACEPLAN_LIST), params, null);
	            if (response.isSuccess()) {
	                tracePlanList = new ArrayReleasableList<TracePlan>();
	                JSONObject jsonBean = new JSONObject(getSimpleString(response));
	                JSONArray tracePlan = jsonBean.getJSONArray("result");
	                for (int i = 0; i < tracePlan.length(); i++) {
	                    JSONObject json = tracePlan.getJSONObject(i);
	                    TracePlan resultTracePlan = new TracePlan();
	                    Customer customer = new Customer();
	                    customer.setCustName(json.getString("custName"));
	                    customer.setCustMobile(json.getString("custMobile"));
	                    customer.setCustOtherPhone(json.getString("custOtherPhone"));
	                    customer.setCustomerID(json.getString("customerID"));
	                    customer.setProjectID(json.getString("projectID"));
	                    resultTracePlan.setActivityID(json.getString("activityID"));
	                    resultTracePlan.setActivityTypeCode(json
	                            .getString("activityTypeCode"));
	                    resultTracePlan.setActivityType(json
	                            .getString("activityType"));
	                    resultTracePlan.setExecuteTime(parsingLong(json
	                            .getString("executeTime")));
	                    resultTracePlan.setExecuteStatusCode(json
	                            .getString("executeStatusCode"));
	                    resultTracePlan.setExecuteStatus(json
	                            .getString("executeStatus"));
	                    resultTracePlan.setActivityContent(json
	                            .getString("activityContent"));
	                    resultTracePlan.setContactResult(json
	                            .getString("contactResult")=="null"?" ":json
	                                    .getString("contactResult"));
	                    resultTracePlan.setCustFeedback(json
	                            .getString("custFeedback"));
	                    resultTracePlan.setLeaderComment(json
	                            .getString("leaderComment"));
	                    resultTracePlan.setCustomer(customer);
	                    resultTracePlan.setOwerName(json
                                .getString("owerName"));
	                    resultTracePlan.setCreateDate(parsingLong(json
                            .getString("createDate")));
	                    tracePlanList.add(resultTracePlan);
	                }
	                // 缓存数据
	                if (tracePlanList != null) {
	                    lruCache.put(key, tracePlanList);
	                }
	                return tracePlanList;
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
	        }
	    }
/**
 * @see com.roiland.crm.core.service.TracePlanAPI#createTracePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.roiland.crm.core.model.TracePlan)
 */
	@Override
	public String createTracePlan(String userID, String dealerOrgID,
			String projectID, String customerID, TracePlan tracePlan)
			throws ResponseException {
		// 新建跟踪计划
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			JSONObject params = new JSONObject();
			if (userID != null) {
				params.put("userID", userID);
			}
			if (dealerOrgID != null) {
				params.put("dealerOrgID", dealerOrgID);
			}
			if (projectID != null) {
				params.put("projectID", projectID);
			}
			if (customerID != null) {
				params.put("customerID", customerID);
			}
			params.put("activityTypeCode", tracePlan.getActivityTypeCode());
			params.put("executeTime", tracePlan.getExecuteTime());
			params.put("executeStatusCode", tracePlan.getExecuteStatusCode());
			params.put("activityContent", tracePlan.getActivityContent());
			params.put("contactResult", tracePlan.getContactResult());
			params.put("custFeedback", tracePlan.getCustFeedback());
			params.put("leaderComment", tracePlan.getLeaderComment());

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_CREATE_TRACEPLAN), params, null);
			String activityID = null;
			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					activityID = result.getString("activityID");
				}
				return activityID;
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
		}
	}
/**
 * @see com.roiland.crm.core.service.TracePlanAPI#updateTracePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.roiland.crm.core.model.TracePlan, com.roiland.crm.core.model.TracePlan)
 */
	public Boolean updateTracePlan(String userID, String dealerOrgID, String projectID, String customerID,
			TracePlan editTracePlan, TracePlan newTracePlan) throws ResponseException {

		Boolean success = false;
		// 更新跟踪计划
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			JSONArray paramArray = new JSONArray();
			JSONObject params1 = new JSONObject();
			params1.put("userID", userID);
			params1.put("dealerOrgID", dealerOrgID);
			params1.put("projectID", projectID);
			params1.put("customerID", customerID);
			params1.put("activityID", editTracePlan.getActivityID());
			params1.put("activityTypeCode", editTracePlan.getActivityTypeCode());
			params1.put("executeTime", editTracePlan.getExecuteTime());
			params1.put("executeStatusCode", editTracePlan.getExecuteStatusCode());
			params1.put("activityContent", editTracePlan.getActivityContent());
			params1.put("contactResult", editTracePlan.getContactResult());
			params1.put("custFeedback", editTracePlan.getCustFeedback());
			params1.put("leaderComment", editTracePlan.getLeaderComment());
			paramArray.put(0, params1);
			
			//如果有新建跟踪计划，就添加新建跟踪计划参数
			if (newTracePlan != null) { 
				JSONObject params2 = new JSONObject();
				params2.put("userID", userID);
				params2.put("dealerOrgID", dealerOrgID);
				params2.put("projectID", projectID);
				params2.put("customerID", customerID);
				params2.put("activityID", newTracePlan.getActivityID());
				params2.put("activityTypeCode", newTracePlan.getActivityTypeCode());
				params2.put("executeTime", newTracePlan.getExecuteTime());
				params2.put("executeStatusCode", newTracePlan.getExecuteStatusCode());
				params2.put("activityContent", newTracePlan.getActivityContent());
				params2.put("contactResult", newTracePlan.getContactResult());
				params2.put("custFeedback", newTracePlan.getCustFeedback());
				params2.put("leaderComment", newTracePlan.getLeaderComment());
				
				paramArray.put(1, params2);
			}
			JSONObject params = new JSONObject();
			params.put("traceplan", paramArray);
			
			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_UPDATE_TRACEPLAN), params, null);

			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					success = result.getBoolean("success");
				}
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
		}
	}
	/**
	 * @see com.roiland.crm.core.service.TracePlanAPI#updateTracePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.roiland.crm.core.model.TracePlan)
	 */
	@Override
	public Boolean updateTracePlan(String userID, String dealerOrgID,
			String projectID, String customerID, TracePlan tracePlan)
			throws ResponseException {
		Boolean success = false;
		// 更新跟踪计划
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("projectID", projectID);
			params.put("customerID", customerID);
			params.put("activityID", tracePlan.getActivityID());
			params.put("activityTypeCode", tracePlan.getActivityTypeCode());
			params.put("executeTime", tracePlan.getExecuteTime());
			params.put("executeStatusCode", tracePlan.getExecuteStatusCode());
			params.put("activityContent", tracePlan.getActivityContent());
			params.put("contactResult", StringUtils.trimNull(tracePlan.getContactResult()));
			params.put("custFeedback", StringUtils.trimNull(tracePlan.getCustFeedback()));
			params.put("leaderComment", tracePlan.getLeaderComment());

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_UPDATE_TRACEPLAN), params, null);

			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					success = result.getBoolean("success");
				}
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
		}
	}
	/**
	 * @see com.roiland.crm.sm.core.service.TracePlanAPI#advSearchTracePlan(com.roiland.crm.sm.core.model.TracePlan.AdvancedSearch)
	 */
    @Override
    public List<TracePlan> advSearchTracePlan(AdvancedSearch adaAdvancedSearch,int startNum, int rowCount) throws ResponseException {

        // 获取列表
        ReleasableList<TracePlan> tracePlanList = null;
        try {
            JSONObject params = new JSONObject();
            params.put("executeStatus", adaAdvancedSearch.getExecuteStatus());
            params.put("leaderComment", adaAdvancedSearch.getLeaderComment());
            params.put("sort", adaAdvancedSearch.getSort());
            params.put("startDate", adaAdvancedSearch.getStartDate());
            params.put("endDate", adaAdvancedSearch.getEndDate());
            params.put("actionType", adaAdvancedSearch.getActionType());
            params.put("ownerId", StringUtils.trimNull(adaAdvancedSearch.getOwnerId()));
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("searchType", "0");
            // 获取缓存Key
            String key = getKey(URLContact.METHOD_ADV_SEARCH_TRACEPLAN, params);
            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_ADV_SEARCH_TRACEPLAN), params, null);

            if (response.isSuccess()) {
                tracePlanList = new ArrayReleasableList<TracePlan>();
            JSONObject jsonBean = new JSONObject(getSimpleString(response));
            JSONArray tracePlan = jsonBean.getJSONArray("result");
            for (int i = 0; i < tracePlan.length(); i++) {
                    JSONObject json = tracePlan.getJSONObject(i);
                    TracePlan resultTracePlan = new TracePlan();
                    Customer customer = new Customer();
                    customer.setCustName(json.getString("custName"));
                    customer.setCustMobile(json.getString("custMobile"));
                    customer.setCustOtherPhone(json.getString("custOtherPhone"));
                    customer.setCustomerID(json.getString("customerID"));
                    customer.setProjectID(json.getString("projectID"));
                    resultTracePlan.setActivityID(json.getString("activityID"));
                    resultTracePlan.setActivityTypeCode(json.getString("activityTypeCode"));
                    resultTracePlan.setActivityType(json.getString("activityType"));
                    resultTracePlan.setExecuteTime(parsingLong(json.getString("executeTime")));
                    resultTracePlan.setCreateDate(parsingLong(json.getString("createDate")));
                    resultTracePlan.setExecuteStatusCode(json.getString("executeStatusCode"));
                    resultTracePlan.setExecuteStatus(json.getString("executeStatus"));
                    resultTracePlan.setActivityContent(json.getString("activityContent"));
                    resultTracePlan.setContactResult(json.getString("contactResult") == "null" ? " " : 
                        json.getString("contactResult"));
                    resultTracePlan.setCustFeedback(json.getString("custFeedback"));
                    resultTracePlan.setLeaderComment(json.getString("leaderComment"));
                    resultTracePlan.setOwerName(json
                        .getString("owerName"));
                    resultTracePlan.setCustomer(customer);
                    tracePlanList.add(resultTracePlan);
                }
                // 缓存数据
                if (tracePlanList != null) {
                    lruCache.put(key, tracePlanList);
                }
                return tracePlanList;
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
        }
    }
    
    /**
     * @see com.roiland.crm.sm.core.service.TracePlanAPI#getExpiredActionPlanList(java.lang.String, int, int)
     */
    @Override
    public List<TracePlan> getExpiredActionPlanList(String userID, int startNum, int rowCount)
                                                                                              throws ResponseException {
        ReleasableList<TracePlan> tracePlanList = null;
        try{

            if (userID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("searchType", "0");
            // 获取缓存Key
            String key = getKey(URLContact.METHOD_GET_EXPIRED_ACTION_PLAN_LIST, params);
            // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
            tracePlanList = lruCache.get(key);
            if (tracePlanList != null && !tracePlanList.isExpired()) {
                return tracePlanList;
            }

            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_GET_EXPIRED_ACTION_PLAN_LIST), params, null);
            if (response.isSuccess()) {
                tracePlanList = new ArrayReleasableList<TracePlan>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray tracePlan = jsonBean.getJSONArray("result");
                for (int i = 0; i < tracePlan.length(); i++) {
                    JSONObject json = tracePlan.getJSONObject(i);
                    TracePlan resultTracePlan = new TracePlan();
                    Customer customer = new Customer();
                    customer.setCustName(json.getString("custName"));
                    customer.setCustMobile(json.getString("custMobile"));
                    customer.setCustOtherPhone(json.getString("custOtherPhone"));
                    customer.setCustomerID(json.getString("customerID"));
                    customer.setProjectID(json.getString("projectID"));
                    resultTracePlan.setActivityID(json.getString("activityID"));
                    resultTracePlan.setActivityTypeCode(json
                            .getString("activityTypeCode"));
                    resultTracePlan.setActivityType(json
                            .getString("activityType"));
                    resultTracePlan.setExecuteTime(parsingLong(json
                            .getString("executeTime")));
                    resultTracePlan.setExecuteStatusCode(json
                            .getString("executeStatusCode"));
                    resultTracePlan.setExecuteStatus(json
                            .getString("executeStatus"));
                    resultTracePlan.setActivityContent(json
                            .getString("activityContent"));
                    resultTracePlan.setContactResult(json
                            .getString("contactResult")=="null"?" ":json
                                    .getString("contactResult"));
                    resultTracePlan.setCustFeedback(json
                            .getString("custFeedback"));
                    resultTracePlan.setLeaderComment(json
                            .getString("leaderComment"));
                    resultTracePlan.setCustomer(customer);
                    resultTracePlan.setOwerName(json
                            .getString("owerName"));
                    tracePlanList.add(resultTracePlan);
              }
                // 缓存数据
                if (tracePlanList != null) {
                    lruCache.put(key, tracePlanList);
                }
                return tracePlanList;
                }
            } catch (IOException e) {
                Log.e(tag, "Connection network error.", e);
                throw new ResponseException(e);
            } catch (JSONException e) {
                Log.e(tag, "Parsing data error.", e);
                throw new ResponseException(e);
            } catch (Exception e) {
                throw new ResponseException(e);
            }
        return null;
    }
    @Override
    public List<TracePlan> getTodayActionPlanList(String userID, int startNum, int rowCount)
                                                                                            throws ResponseException {
        ReleasableList<TracePlan> tracePlanList = null;
        // 新建跟踪计划
        try {
            if (userID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("startNum", startNum);
            params.put("rowCount",rowCount);
            params.put("searchType", "0");

            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_GET_TODAY_ACTION_PLAN_LIST), params, null);
         // 获取缓存Key
            String key = getKey(URLContact.METHOD_GET_TODAY_ACTION_PLAN_LIST, params);
            tracePlanList = lruCache.get(key);
            if(tracePlanList != null && !tracePlanList.isExpired()){
                return tracePlanList;
            }
            if (response.isSuccess()) {
                tracePlanList = new ArrayReleasableList<TracePlan>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray tracePlan = jsonBean.getJSONArray("result");
                for (int i = 0; i < tracePlan.length(); i++) {
                    JSONObject json = tracePlan.getJSONObject(i);
                    TracePlan resultTracePlan = new TracePlan();
                    Customer customer = new Customer();
                    customer.setCustName(json.getString("custName"));
                    customer.setCustMobile(json.getString("custMobile"));
                    customer.setCustOtherPhone(json.getString("custOtherPhone"));
                    customer.setCustomerID(json.getString("customerID"));
                    customer.setProjectID(json.getString("projectID"));
                    resultTracePlan.setActivityID(json.getString("activityID"));
                    resultTracePlan.setActivityTypeCode(json
                            .getString("activityTypeCode"));
                    resultTracePlan.setActivityType(json
                            .getString("activityType"));
                    resultTracePlan.setExecuteTime(parsingLong(json
                            .getString("executeTime")));
                    resultTracePlan.setExecuteStatusCode(json
                            .getString("executeStatusCode"));
                    resultTracePlan.setExecuteStatus(json
                            .getString("executeStatus"));
                    resultTracePlan.setActivityContent(json
                            .getString("activityContent"));
                    resultTracePlan.setContactResult(json
                            .getString("contactResult")=="null"?" ":json
                                    .getString("contactResult"));
                    resultTracePlan.setCustFeedback(json
                            .getString("custFeedback"));
                    resultTracePlan.setLeaderComment(json
                            .getString("leaderComment"));
                    resultTracePlan.setCustomer(customer);
                    resultTracePlan.setOwerName(json
                            .getString("owerName"));
                    tracePlanList.add(resultTracePlan);
                }
                // 缓存数据
                if (tracePlanList != null) {
                    lruCache.put(key, tracePlanList);
                }
                return tracePlanList;
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
        }
    
    }
	
}
