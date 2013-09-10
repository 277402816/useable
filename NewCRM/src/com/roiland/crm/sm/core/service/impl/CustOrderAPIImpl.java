package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.service.CustOrderAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;

/**
 * 
 * <pre>
 * 客户订单接口实现类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: CustOrderAPIImpl.java, v 0.1 2013-8-2 上午11:02:23 shuang.gao Exp $
 */
public class CustOrderAPIImpl extends AbstractBaseAPI implements CustOrderAPI {
	private static final String tag = Log.getTag(CustOrderAPIImpl.class);

	@Override
	public String getOrderNo(String userID, String dealerOrgID,
			String projectID, String customerID) throws ResponseException {
		String orderNo = null;
		if (userID == null || dealerOrgID == null) {
			throw new ResponseException("userID or dealerOrgID is null.");
		}

		try {
			// 获取联系人列表.
			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("projectID", projectID);
			params.put("customerID", customerID);

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_ORDER_NO), params, null);

			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					orderNo = result.getString("orderNo");
				}
				return orderNo;
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

	@Override
	public Boolean isProjectActive(String projectID, String dealerOrgID)
			throws ResponseException {
		try {
			// 是否存在有效订单
			if (user == null) {
				throw new ResponseException("User is null.");
			}
			Boolean rtn = null;
			JSONObject params = new JSONObject();
			params.put("dealerOrgID", user.getDealerOrgID());
			params.put("projectID", projectID);

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_IS_PROJECT_ACTIVE), params, null);

			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					rtn = result.getBoolean("rtn");
				}
				return rtn;
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

	@Override
	public Boolean isRoleForMatch(String userID, String dealerOrgID)
			throws ResponseException {
		Boolean rtn = null;
		if (userID == null || dealerOrgID == null) {
			throw new ResponseException("userID or dealerOrgID is null.");
		}

		try {
			JSONObject params = new JSONObject();
			params.put("userId", userID);
			params.put("dealerOrgID", dealerOrgID);

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_IS_ROLE_FOR_MATCH), params, null);

			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("rtn")) {
					rtn = result.getBoolean("rtn");
				}
				return rtn;
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

	@Override
	public Boolean createOrder(String userID, String dealerOrgID,
			String customerID, String projectID, CustOrder custOrder)
			throws ResponseException {
		Boolean rtn = null;
		if (userID == null || dealerOrgID == null) {
			throw new ResponseException("userID or dealerOrgID is null.");
		}

		try {
			// 创建新购车订单
			if (user == null) {
				throw new ResponseException("User is null.");
			}
			JSONObject params = new JSONObject();
			params.put("userID", user.getUserID());
			params.put("dealerOrgID", user.getDealerOrgID());
			params.put("customerID", customerID);
			params.put("projectID", projectID);
			params.put("invoiceTitle", custOrder.getInvoiceTitle());
			params.put("orderTypeCode", custOrder.getOrderTypeCode());
			params.put("orderVehiTypeCode", custOrder.getOrderVehiTypeCode());

			params.put("orderStatus", custOrder.getOrderStatus());
			params.put("decorate", custOrder.getDecorate());
			params.put("orderPriorityCode", custOrder.getOrderPriorityCode());
			params.put("preDeliveryDate", custOrder.getPreDeliveryDateCode());
			params.put("custExpeDeliDate", custOrder.getCustExpeDeliDate());
			params.put("deliveryPlaceCode", custOrder.getDeliveryPlaceCode());
			params.put("deposit", custOrder.getDeposit());
			params.put("offerPrice", custOrder.getOfferPrice());
			params.put("paymentCode", custOrder.getPaymentCode());
			params.put("matchingVehicleID", custOrder.getMatchingVehicleID());
			params.put("matchingChassisNo", custOrder.getMatchingChassisNo());
			params.put("buyForSelfFlag", custOrder.getBuyForSelfFlag());
			params.put("orderFailureFlag", custOrder.getOrderFailureFlag());
			params.put("orderFailureReasonCode",
					custOrder.getOrderFailureReasonCode());

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_CREATE_ORDER), params, null);

			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					rtn = result.getBoolean("success");
				}
				return rtn;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<CustOrder> getOrderList(String userID, String dealerOrgID,String projectID,String customerID,
			String searchWord, String searchColumns, Integer startNum,
			Integer rowCount) throws ResponseException {
		ReleasableList<CustOrder> custOrderList = null;

		try {
			// 获取订单列表
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("projectID", projectID);
			params.put("customerID", customerID);
			params.put("searchWord", searchWord);
			params.put("searchColumns", searchColumns);
			params.put("startNum", startNum);
			params.put("rowCount", rowCount);

			//获取缓存Key
			String key = getKey(URLContact.METHOD_GET_ORDER_LIST, params);
			
			//从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
			custOrderList = lruCache.get(key);
			if (custOrderList != null && !custOrderList.isExpired()) {
				return custOrderList;
			}
			
			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_ORDER_LIST), params, null);

			if (response.isSuccess()) {
				custOrderList = new ArrayReleasableList<CustOrder>();
				JSONObject jsonBean = new JSONObject(getSimpleString(response));
				JSONArray custOrder = jsonBean.getJSONArray("result");

				for (int i = 0; i < custOrder.length(); i++) {
					JSONObject json = custOrder.getJSONObject(i);
					CustOrder order = new CustOrder();
					PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
					Customer customer = new Customer();
					// 客戶信息
					customer.setCustName(json.getString("custName"));

					customer.setCustMobile(json.getString("custPhone"));
					customer.setCustOtherPhone(json.getString("custOtherPhone"));
					order.setCustomer(customer);
					// 购车意向
					purchaseCarIntention.setBrandCode(json
							.getString("brandCode"));
					purchaseCarIntention.setBrand(json.getString("brand"));
					purchaseCarIntention.setModelCode(json
							.getString("modelCode"));
					purchaseCarIntention.setModel(json.getString("model"));

					purchaseCarIntention.setCarConfigurationCode(String
							.valueOf(json.getString("configCode")));
					purchaseCarIntention.setCarConfiguration(String
							.valueOf(json.getString("config")));
					order.setPurchaseCarIntention(purchaseCarIntention);
					// 客户订单
					order.setOrderID(json.getString("orderNo"));
					order.setOrderTypeCode(json.getString("orderTypeCode"));
					order.setOrderType(json.getString("orderType"));
					order.setPreDeliveryDate(json.getString("preDeliveryDate"));
					order.setDeposit(json.getString("deposit"));
					order.setOrderStatusCode(json.getString("orderStatusCode"));
					order.setOrderStatus(json.getString("orderStatus"));
					order.setMatchingChassisNo(json
							.getString("matchingChassisNo"));
					order.setMatchingVehicleID(json
							.getString("matchingOrderID"));
					order.setCreateTime(Long.parseLong(json.getString("createTime")));// (json.getString("createTime"));
					custOrderList.add(order);
					System.out.println(custOrderList);
				}
				return custOrderList;
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

	@Override
	public CustOrder getOrderDetailInfo(String userID, String dealerOrgID,
			String orderNo) throws ResponseException {
		CustOrder order = null;
		try {
			// 获取订单详细信息
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}

			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("orderNo", orderNo);

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_ORDER_DETAIL_INFO), params, null);

			if (response.isSuccess()) {
				order = new CustOrder();
				JSONObject jsonBean = new JSONObject(getSimpleString(response));
				JSONObject json = jsonBean.getJSONObject("result");

				PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
				Customer customer = new Customer();
				// 客戶信息
				customer.setCustName(json.getString("custName"));

				customer.setCustMobile(json.getString("custPhone"));
				customer.setCustOtherPhone(json.getString("custOtherPhone"));
				customer.setIdNumber(json.getString("idNumber"));
				customer.setProjectID(json.getString("projectID"));
				order.setCustomer(customer);
				// 购车意向
				purchaseCarIntention.setBrandCode(json
						.getString("brandCode"));
				purchaseCarIntention.setBrand(json.getString("brand"));
				purchaseCarIntention.setModelCode(json
						.getString("modelCode"));
				purchaseCarIntention.setModel(json.getString("model"));
				purchaseCarIntention.setOutsideColor(json.getString("outsideColor"));
				purchaseCarIntention.setOutsideColorCode(json.getString("outsideColorCode"));
				purchaseCarIntention.setInsideColor(json.getString("insideColor"));
				purchaseCarIntention.setInsideColorCode(json.getString("insideColorCode"));
				purchaseCarIntention.setInsideColorCheck(json.getBoolean("insideColorCheck"));
				purchaseCarIntention.setCarConfigurationCode(json
						.getString("configCode"));
				purchaseCarIntention.setCarConfiguration(json
						.getString("config"));
				order.setPurchaseCarIntention(purchaseCarIntention);
				// 客户订单
				order.setInvoiceTitle(json.getString("invoiceTitle"));
				order.setOrderID(json.getString("orderNo"));
				order.setOrderTypeCode(json.getString("orderTypeCode"));
				order.setOrderType(json.getString("orderType"));
				order.setOrderVehiTypeCode(json
						.getString("orderVehiTypeCode"));
				order.setOrderVehiType(json.getString("orderVehiType"));
				order.setOrderStatus(json.getString("orderStatus"));
				order.setDecorate(json.getString("decorate"));
				order.setOrderPriorityCode(json
						.getString("orderPriorityCode"));
				order.setOrderPriority(json.getString("orderPriority"));
				order.setPreDeliveryDate(json.getString("preDeliveryDate"));
				order.setCustExpeDeliDate(json
						.getString("custExpeDeliDate"));
				order.setDeliveryPlaceCode(json
						.getString("deliveryPlaceCode"));
				order.setDeliveryPlace(json.getString("deliveryPlace"));
				order.setDeposit(json.getString("deposit"));
				order.setOfferPrice(json.getString("offerPrice"));
				order.setPaymentCode(json.getString("paymentCode"));
				order.setPayment(json.getString("payment"));
				order.setMatchingVehicleID(json
						.getString("matchingOrderID"));
				order.setMatchingChassisNo(json
						.getString("matchingChassisNo"));
				order.setBuyForSelfFlag(json.getString("buyForSelfFlag"));
				order.setOrderFailureFlag(json
						.getString("orderFailureFlag"));
				order.setOrderFailureReasonCode(json
						.getString("orderFailureReasonCode"));
				order.setOrderFailure(json.getString("orderFailure"));
				order.setOrderSignTime(json.getString("orderSignTime"));
				
				return order;
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

	@Override
	public Boolean updateOrder(String userID, String dealerOrgID,
			String projectID, CustOrder custOrder) throws ResponseException {
		Boolean rtn = null;
		try {
			// 更新订单
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}

			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("orderNo", custOrder.getOrderID());
			params.put("customerID", custOrder.getCustomer().getCustomerID());
			params.put("projectID", projectID);
			params.put("invoiceTitle", custOrder.getInvoiceTitle());
			params.put("orderTypeCode", custOrder.getOrderTypeCode());
			params.put("orderVehiTypeCode", custOrder.getOrderVehiTypeCode());
			params.put("orderStatus", custOrder.getOrderStatus());
			params.put("decorate", custOrder.getDecorate());
			params.put("orderPriorityCode", custOrder.getOrderPriorityCode());
			params.put("preDeliveryDate", custOrder.getPreDeliveryDate());
			params.put("custExpeDeliDate", custOrder.getCustExpeDeliDate());
			params.put("deliveryPlaceCode", custOrder.getDeliveryPlaceCode());
			params.put("deposit", custOrder.getDeposit());
			params.put("offerPrice", custOrder.getOfferPrice());
			params.put("paymentCode", custOrder.getPaymentCode());
			params.put("matchingVehicleID", custOrder.getMatchingVehicleID());
			params.put("matchingChassisNo", custOrder.getMatchingChassisNo());
			params.put("buyForSelfFlag", custOrder.getBuyForSelfFlag());
			params.put("orderFailureFlag", custOrder.getOrderFailureFlag());
			params.put("orderFailureReasonCode",
					custOrder.getOrderFailureReasonCode());
			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_UPDATE_ORDER), params, null);
			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					rtn = result.getBoolean("success");
				}
				return rtn;
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

	@Override
	public Boolean submitResRequirement(String userID, String dealerOrgID,
			String orderNo) throws ResponseException {
		Boolean rtn = null;
		try {
			// 资源提报
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}

			JSONObject params = new JSONObject();
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("orderNo", orderNo);

			RLHttpResponse response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_SUBMIT_RES_REQUIREMENT), params, null);
			if (response.isSuccess()) {
				JSONObject result = new JSONObject(getSimpleString(response));
				if (result.getBoolean("success")) {
					rtn = result.getBoolean("success");
				}
				return rtn;
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
 * @see com.roiland.crm.sm.core.service.CustOrderAPI#getThreeDaysDeliveryOrderList(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
 */
	@Override
    public List<CustOrder> getThreeDaysDeliveryOrderList(
            String searchWord, String searchColumns, Integer startNum,
            Integer rowCount) throws ResponseException {
        ReleasableList<CustOrder> custOrderList = null;

        try {
            // 获取订单列表
            JSONObject params = new JSONObject();
            params.put("searchWord", searchWord);
            params.put("searchColumns", searchColumns);
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);

            //获取缓存Key
            String key = getKey(URLContact.METHOD_GET_THREEDAYS_DELIVERY_ORDER_LIST, params);
            
            //从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
            custOrderList = lruCache.get(key);
            if (custOrderList != null && !custOrderList.isExpired()) {
                return custOrderList;
            }
            
            RLHttpResponse response = getHttpClient().executePostJSON(
                    getURLAddress(URLContact.METHOD_GET_THREEDAYS_DELIVERY_ORDER_LIST), params, null);

            if (response.isSuccess()) {
                custOrderList = new ArrayReleasableList<CustOrder>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray custOrder = jsonBean.getJSONArray("result");

                for (int i = 0; i < custOrder.length(); i++) {
                    JSONObject json = custOrder.getJSONObject(i);
                    CustOrder order = new CustOrder();
                    PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                    Customer customer = new Customer();
                    // 客戶信息
                    customer.setCustName(json.getString("custName"));

                    customer.setCustMobile(json.getString("custPhone"));
                    customer.setCustOtherPhone(json.getString("custOtherPhone"));
                    order.setCustomer(customer);
                    // 购车意向
                    purchaseCarIntention.setBrandCode(json
                            .getString("brandCode"));
                    purchaseCarIntention.setBrand(json.getString("brand"));
                    purchaseCarIntention.setModelCode(json
                            .getString("modelCode"));
                    purchaseCarIntention.setModel(json.getString("model"));

                    purchaseCarIntention.setCarConfigurationCode(String
                            .valueOf(json.getString("configCode")));
                    purchaseCarIntention.setCarConfiguration(String
                            .valueOf(json.getString("config")));
                    order.setPurchaseCarIntention(purchaseCarIntention);
                    // 客户订单
                    order.setOrderID(json.getString("orderNo"));
                    order.setOrderTypeCode(json.getString("orderTypeCode"));
                    order.setOrderType(json.getString("orderType"));
                    order.setPreDeliveryDate(json.getString("preDeliveryDate"));
                    order.setDeposit(json.getString("deposit"));
                    order.setOrderStatusCode(json.getString("orderStatusCode"));
                    order.setOrderStatus(json.getString("orderStatus"));
                    order.setMatchingChassisNo(json
                            .getString("matchingChassisNo"));
                    order.setMatchingVehicleID(json
                            .getString("matchingOrderID"));
                    order.setCreateTime(Long.parseLong(json.getString("createTime")));// (json.getString("createTime"));
                    custOrderList.add(order);
                    System.out.println(custOrderList);
                }
                return custOrderList;
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
