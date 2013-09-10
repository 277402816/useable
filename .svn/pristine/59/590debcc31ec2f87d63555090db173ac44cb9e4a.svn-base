package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.service.CustomerManagmentAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DataVerify;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;

/**
 * 
 * <pre>
 * 客户信息接口实现类
 * </pre>
 *
 * @author zhao.jiaqi
 * @version $Id: CustomerManagmentAPIImpl.java, v 0.1 2013-8-2 上午11:01:19 zhao.jiaqi Exp $
 */
public class CustomerManagmentAPIImpl extends AbstractBaseAPI implements
		CustomerManagmentAPI {
	private static final String tag = Log
			.getTag(CustomerManagmentAPIImpl.class);
	
/**
 * @throws ResponseException
 * @see com.roiland.crm.sm.core.service.CustomerManagmentAPI#getCustomerList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
	@Override
	public List<Customer> getCustomerList(String userID, String dealerOrgID, String searchWord,
	                                      String searchColumns, Integer startNum, Integer rowCount,
	                                      String sortRule, String custOwer,
        	                              String custStatus, String owerID) throws ResponseException{
              if (userID == null || dealerOrgID == null) {
              throw new ResponseException("userID or dealerOrgID is null.");
          }
        
          // 定义变量接受返回值
          ReleasableList<Customer> customerList = null;
          RLHttpResponse response = null;
          try {
              JSONObject params = new JSONObject();
              // 添加参数
              params.put("searchWord", searchWord);
              params.put("searchColumns", searchColumns);
              params.put("startNum", startNum);
              params.put("rowCount", rowCount);
              params.put("sortRule", sortRule);
              if(custOwer != null){
                  params.put("custOwer", custOwer);
              }
              if(custStatus != null){
                  params.put("custStatus", custStatus);
              }
              if(owerID != null){
                  params.put("owerID", owerID);
              }
              params = appendBaseParams(params);
        
              // 获取缓存Key
              String key = getKey(URLContact.METHOD_CUSTOMER_LIST, params);
        
              // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
              customerList = lruCache.get(key);
              if (customerList != null && !customerList.isExpired()) {
                  return customerList;
              }
        
              // 连接服务器
              response = getHttpClient().executePostJSON(
                      getURLAddress(URLContact.METHOD_CUSTOMER_LIST), params, null);
        
              // 如果与服务器连接成功，执行下列操作
              if (response.isSuccess()) {
                  customerList = new ArrayReleasableList<Customer>();
                  // 返回json
                  JSONObject result = new JSONObject(getSimpleString(response));
        
                  // 解析json,把数据添加到Customer中
                  JSONArray arrayResult = result.getJSONArray("result");
                  int n = arrayResult.length();
                  for (int i = 0; i < n; i++) {
                      JSONObject objectResult = arrayResult.getJSONObject(i);
                      Customer customer = new Customer();
                      customer.setCustomerID(String.valueOf(objectResult
                              .get("customerID")));
                      customer.setCustName(String.valueOf(objectResult
                              .get("custName")));
                      customer.setCustFromCode(String.valueOf(objectResult
                              .get("custFromCode")));
                      customer.setCustFrom(String.valueOf(objectResult
                              .get("custFrom")));
                      customer.setCustTypeCode(String.valueOf(objectResult
                              .get("custTypeCode")));
                      customer.setCustType(String.valueOf(objectResult
                              .get("custType")));
                      customer.setInfoFromCode(String.valueOf(objectResult
                              .get("infoFromCode")));
                      customer.setInfoFrom(String.valueOf(objectResult
                              .get("infoFrom")));
                      customer.setCollectFromCode(String.valueOf(objectResult
                              .get("collectFromCode")));
                      customer.setCollectFrom(String.valueOf(objectResult
                              .get("collectFrom")));
                      customer.setCustMobile(String.valueOf(objectResult
                              .get("custMobile")));
                      customer.setCustOtherPhone(String.valueOf(objectResult
                              .get("custOtherPhone")));
                      customer.setGenderCode(String.valueOf(objectResult
                              .get("genderCode")));
                      customer.setBirthday(String.valueOf(objectResult
                              .get("birthday")));
                      customer.setIdTypeCode(String.valueOf(objectResult
                              .get("idTypeCode")));
                      customer.setIdType(String.valueOf(objectResult
                              .get("idType")));
                      customer.setIdNumber(String.valueOf(objectResult
                              .get("idNumber")));
                      customer.setProvinceCode(String.valueOf(objectResult
                              .get("provinceCode")));
                      customer.setProvince(String.valueOf(objectResult
                              .get("province")));
                      customer.setCityCode(String.valueOf(objectResult
                              .get("cityCode")));
                      customer.setCity(String.valueOf(objectResult.get("city")));
                      customer.setDistrictCode(String.valueOf(objectResult
                              .get("districtCode")));
                      customer.setDistrict(String.valueOf(objectResult
                              .get("district")));
                      customer.setCustComment(String.valueOf(objectResult
                              .get("custComment")));
                      customer.setOwnerName(String.valueOf(objectResult.get("owerName")));
                      customer.setCustStatus(objectResult.getString("custStatus"));
                      customer.setHasUnexePlan(String.valueOf(objectResult.getBoolean("hasUnexePlan")));
                      customer.setCreateDate(objectResult.getLong("createDate"));
                      customerList.add(customer);
                  }
        
                  // 缓存数据
                  if (customerList != null) {
                      lruCache.put(key, customerList);
                  }
                  return customerList;
              }
              throw new ResponseException();
        
          } catch (JSONException e) {
              Log.e(tag, "Parsing json data error.", e);
              throw new ResponseException(e);
          } catch (IOException e) {
              Log.e(tag, "Connect to server error.", e);
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
 * 
 * @see com.roiland.crm.core.service.CustomerManagmentAPI#getCustomerDetail(java.lang.String, java.lang.String, java.lang.String)
 */
	@Override
	public Customer getCustomerDetail(String userID, String dealerOrgID,
			String customerID) throws ResponseException {

		Customer customer = null;
        RLHttpResponse response = null;
		try {
			if (userID == null || dealerOrgID == null) {
				throw new ResponseException("userID or dealerOrgID is null.");
			}
			JSONObject params = new JSONObject();
			// 添加参数
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("customerID", customerID);

			// 连接服务器
			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_CUSTOMER_DETAIL), params, null);

			if (response.isSuccess()) {
				customer = new Customer();
				// 接受返回结果
				JSONObject result = new JSONObject(getSimpleString(response));

				JSONObject objectResult = result.getJSONObject("customer");
				// //解析返回数据
				customer.setCustomerID(objectResult.getString("customerID"));
				customer.setCustName(objectResult.getString("custName"));
				customer.setCustFromCode(objectResult.getString("custFromCode"));
				customer.setCustFrom(objectResult.getString("custFrom"));
				customer.setCustTypeCode(objectResult.getString("custTypeCode"));
				customer.setCustType(objectResult.getString("custType"));
				customer.setInfoFromCode(objectResult.getString("infoFromCode"));
				customer.setInfoFrom(objectResult.getString("infoFrom"));
				customer.setCollectFromCode(objectResult
						.getString("collectFromCode"));
				customer.setCollectFrom(objectResult.getString("collectFrom"));
				customer.setCustMobile(objectResult.getString("custMobile"));
				customer.setCustOtherPhone(objectResult
						.getString("custOtherPhone"));
				customer.setGenderCode(objectResult.getString("genderCode"));
				customer.setGender(objectResult.getString("gender"));
				customer.setBirthday(DataVerify.isZero(objectResult.getString("birthday")));
				customer.setIdTypeCode(objectResult.getString("idTypeCode"));
				customer.setIdType(objectResult.getString("idType"));
				customer.setIdNumber(objectResult.getString("idNumber"));
				customer.setProvinceCode(objectResult.getString("provinceCode"));
				customer.setProvince(objectResult.getString("province"));
				customer.setCityCode(objectResult.getString("cityCode"));
				customer.setCity(objectResult.getString("city"));
				customer.setDistrictCode(objectResult.getString("districtCode"));
				customer.setDistrict(objectResult.getString("district"));
				customer.setQq(objectResult.getString("qq"));
				customer.setAddress(objectResult.getString("address"));
				customer.setPostcode(objectResult.getString("postcode"));
				customer.setEmail(objectResult.getString("email"));
				customer.setConvContactTime(objectResult
						.getString("convContactTime"));
                customer.setConvContactTimeCode(objectResult
                    .getString("convContactTimeCode"));
				customer.setExpectContactWayCode(objectResult
						.getString("expectContactWayCode"));
				customer.setExpectContactWay(objectResult
						.getString("expectContactWay"));
				customer.setFax(objectResult.getString("fax"));
				customer.setExistingCarCode(objectResult
						.getString("existingCarCode"));
				customer.setExistingCar(objectResult
						.getString("existingCar"));
				customer.setExistingCarBrand(objectResult
						.getString("existingCarBrand"));
				customer.setIndustryCode(objectResult.getString("industryCode"));
				customer.setIndustry(objectResult.getString("industry"));
				customer.setPositionCode(objectResult.getString("positionCode"));
				customer.setPosition(objectResult.getString("position"));
				customer.setEducationCode(objectResult
						.getString("educationCode"));
				customer.setEducation(objectResult
						.getString("education"));
				customer.setCustInterestCode1(objectResult
						.getString("custInterestCode1"));
				customer.setCustInterest1(objectResult
						.getString("custInterest1"));
				customer.setCustInterestCode2(objectResult
						.getString("custInterestCode2"));
				customer.setCustInterest2(objectResult
						.getString("custInterest2"));
				customer.setCustInterestCode3(objectResult
						.getString("custInterestCode3"));
				customer.setCustInterest3(objectResult
						.getString("custInterest3"));
				customer.setExistLisenPlate(objectResult
						.getString("existLisenPlate"));
				customer.setEnterpTypeCode(objectResult
						.getString("enterpTypeCode"));
				customer.setEnterpType(objectResult.getString("enterpType"));
				customer.setEnterpPeopleCountCode(objectResult
						.getString("enterpPeopleCountCode"));
				customer.setEnterpPeopleCount(objectResult
						.getString("enterpPeopleCount"));
				customer.setRegisteredCapitalCode(objectResult
						.getString("registeredCapitalCode"));
				customer.setRegisteredCapital(objectResult
						.getString("registeredCapital"));
				customer.setCompeCarModelCode(objectResult
						.getString("compeCarModelCode"));
				customer.setCompeCarModel(objectResult
						.getString("compeCarModel"));
				customer.setRebuyStoreCustTag(objectResult
						.getBoolean("rebuyStoreCustTag"));
				customer.setRebuyOnlineCustTag(objectResult
						.getBoolean("rebuyOnlineCustTag"));
				customer.setChangeCustTag(objectResult
						.getBoolean("changeCustTag"));
				customer.setLoanCustTag(objectResult.getBoolean("loanCustTag"));
				customer.setHeaderQuartCustTag(objectResult
						.getBoolean("headerQuartCustTag"));
				customer.setRegularCustTag(objectResult
						.getBoolean("regularCustTag"));
				customer.setRegularCustCode(objectResult
						.getString("regularCustCode"));
				customer.setRegularCust(objectResult.getString("regularCust"));
				customer.setBigCustTag(objectResult.getBoolean("bigCustTag"));
				customer.setBigCusts(objectResult.getString("bigCusts"));
				customer.setBigCustsCode(objectResult.getString("bigCustsCode"));
				customer.setCustComment(objectResult.getString("custComment"));
				customer.setDormancy(objectResult.getString("dormancy").equals("1"));
				customer.setCustStatus(objectResult.getString("custStatus"));
				
				return customer;
			}
			throw new ResponseException();
		} catch (JSONException e) {
			Log.e(tag, "Parsing json data error.", e);
			throw new ResponseException(e);
		} catch (IOException e) {
			Log.e(tag, "Connect to server error.", e);
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
 * 
 * @see com.roiland.crm.core.service.CustomerManagmentAPI#updateCustomer(java.lang.String, java.lang.String, com.roiland.crm.core.model.Customer)
 */
	@Override
	public Boolean updateCustomer(String userID, String dealerOrgID,
			Customer customer) throws ResponseException {

		JSONObject params = new JSONObject();
        RLHttpResponse response = null;
		if (userID == null || dealerOrgID == null) {
			throw new ResponseException("userID or dealerOrgID is null.");
		}
		// 添加参数
		try {

			params.put("userID", userID);
			
			params.put("customerID", customer.getCustomerID());
			params.put("custName", customer.getCustName());
			params.put("custFromCode", customer.getCustFromCode());
			params.put("custFrom", customer.getCustFrom());
			params.put("custTypeCode", customer.getCustTypeCode());
			params.put("custType", customer.getCustType());
			params.put("infoFromCode", customer.getInfoFromCode());
			params.put("infoFrom", customer.getInfoFrom());
			params.put("collectFromCode", customer.getCollectFromCode());
			params.put("collectFrom", customer.getCollectFrom());
			params.put("custMobile", customer.getCustMobile());
			params.put("custOtherPhone", customer.getCustOtherPhone());
			params.put("genderCode", customer.getGenderCode());
			params.put("gender", customer.getGender());
			params.put("birthday", customer.getBirthday());
			params.put("idTypeCode", customer.getIdTypeCode());
			params.put("idType", customer.getIdType());
			params.put("idNumber", customer.getIdNumber());
			params.put("provinceCode", customer.getProvinceCode());
			params.put("province", customer.getProvince());
			params.put("cityCode", customer.getCityCode());
			params.put("city", customer.getCity());
			params.put("districtCode", customer.getDistrictCode());
			params.put("district", customer.getDistrict());
			params.put("qq", customer.getQq());
			params.put("address", customer.getAddress());
			params.put("postcode", customer.getPostcode());
			params.put("email", customer.getEmail());
			params.put("convContactTime", customer.getConvContactTime());
			params.put("convContactTimeCode", customer.getConvContactTimeCode());
			params.put("expectContactWayCode", customer.getExpectContactWayCode());
			params.put("expectContactWay", customer.getExpectContactWay());
			params.put("fax", customer.getFax());
			params.put("existingCarCode", customer.getExistingCarCode());
			params.put("existingCar", customer.getExistingCarCode());
			params.put("existingCarBrand", customer.getExistingCarBrand());
			params.put("industryCode", customer.getIndustryCode());
			params.put("industry", customer.getIndustryCode());
			params.put("positionCode", customer.getPositionCode());
			params.put("position", customer.getPositionCode());
			params.put("educationCode", customer.getEducationCode());
			params.put("education", customer.getEducationCode());
			params.put("custInterestCode1", customer.getCustInterestCode1());
			params.put("custInterest1", customer.getCustInterest1());
			params.put("custInterestCode2", customer.getCustInterestCode2());
			params.put("custInterest2", customer.getCustInterest2());
			params.put("custInterestCode3", customer.getCustInterestCode3());
			params.put("custInterest3", customer.getCustInterest3());
			params.put("existLisenPlate", customer.getExistLisenPlate());
			params.put("enterpTypeCode", customer.getEnterpTypeCode());
			params.put("enterpType", customer.getEnterpType());
			params.put("enterpPeopleCountCode", customer.getEnterpPeopleCountCode());
			params.put("enterpPeopleCount", customer.getEnterpPeopleCount());
			params.put("registeredCapitalCode", customer.getRegisteredCapitalCode());
			params.put("registeredCapital", customer.getRegisteredCapital());
			params.put("compeCarModelCode", customer.getCompeCarModelCode());
			params.put("compeCarModel", customer.getCompeCarModel());
			params.put("rebuyStoreCustTag", customer.getRebuyStoreCustTag());
			params.put("rebuyOnlineCustTag", customer.getRebuyOnlineCustTag());
			params.put("changeCustTag", customer.getChangeCustTag());
			params.put("loanCustTag", customer.getLoanCustTag());
			params.put("headerQuartCustTag", customer.getHeaderQuartCustTag());
			params.put("regularCustTag", customer.getRegularCustTag());
			params.put("regularCustCode", customer.getRegularCustCode());
			params.put("regularCust", customer.getRegularCust());
			params.put("bigCustTag", customer.getBigCustTag());
			params.put("bigCustsCode", customer.getBigCustsCode());
			params.put("bigCusts", customer.getBigCusts());
			params.put("custComment", customer.getCustComment());
			params.put("dormancy", customer.getDormancy() ? true : false);
			params.put("custStatus", customer.getCustStatus());
			params.put("abandonProject", customer.isAbandonProject());
			
			params.put("dealerOrgID", dealerOrgID);

			// 连接服务器
			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_UPDATE_CUSTOMER), params, null);
			// 判断是否连接成功，如果连接成功执行下列操作
			if (response.isSuccess()) {
                String data = getSimpleString(response);
                JSONObject result = new JSONObject(data);
                String node = null;
                String error = null;
                JSONArray nodeArray = result.names();
                if (nodeArray != null) {
                    for (int i = 0; i < nodeArray.length(); i++) {
                        node = nodeArray.get(i).toString();
                        if (node.equalsIgnoreCase("success")) {

                            Boolean success = Boolean.parseBoolean(result.getString("success"));
                            if (success) {
                                return true;
                            }
                        } else if (node.equalsIgnoreCase("validate_error")) {
                            error = parsingValidation(result.getJSONObject(node));
                            throw new ResponseException(error);
                        }
                    }
                }
            }
		} catch (JSONException e) {
			Log.e(tag, "Parsing json data error.", e);
			throw new ResponseException(e);
		} catch (IOException e) {
			Log.e(tag, "Connect to server error.", e);
			throw new ResponseException(e);
		} finally {
            if (response != null) {
                response.release();
            }
        }
		return false;
	}
/**
 * @see com.roiland.crm.core.service.CustomerManagmentAPI#getCustomerCarIntention(java.lang.String, java.lang.String, java.lang.String)
 */
	@Override
	public String getCustomerCarIntention(String userID, String dealerOrgID,
			String customerID) throws ResponseException {
		if (userID == null || dealerOrgID == null) {
			throw new ResponseException("userID or dealerOrgID is null.");
		}
		@SuppressWarnings("unused")
		JSONObject objectResult = null;
		JSONObject params = new JSONObject();
        RLHttpResponse response = null;

		try {
			params.put("userID", userID);
			params.put("dealerOrgID", dealerOrgID);
			params.put("dealerOrgID", dealerOrgID);
			// 连接服务器
			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_CUSTOMER_CAR_INTENTION), params, null);
			if (response.isSuccess()) {
				// 解析返回結果
				JSONObject result = new JSONObject(getSimpleString(response));
				JSONArray arrayResult = result
						.getJSONArray("purchaseCarIntention");
				int n = arrayResult.length();
				for (int i = 0; i < n; i++) {
					objectResult = arrayResult.getJSONObject(i);
				}
				// 將結果返回
				return "";
			}
			throw new ResponseException();
		} catch (JSONException e) {
			Log.e(tag, "Parsing json data error.", e);
			throw new ResponseException(e);
		} catch (IOException e) {
			Log.e(tag, "Connect to server error.", e);
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
 * 
 * @see com.roiland.crm.core.service.CustomerManagmentAPI#getOldCustomer()
 */
	@Override
	public List<Customer> getOldCustomer() throws ResponseException {

		JSONObject params = new JSONObject();
		List<Customer> list = null;
        RLHttpResponse response = null;
		try {
			// 连接服务器
			response = getHttpClient().executePostJSON(
					getURLAddress(URLContact.METHOD_GET_OLD_CUSTOMER), params, null);
			if (response.isSuccess()) {
				list = new ArrayList<Customer>();
				// 解析返回結果
				JSONObject result = new JSONObject(getSimpleString(response));
				JSONArray arrayResult = result.getJSONArray("result");
				int n = arrayResult.length();
				for (int i = 0; i < n; i++) {
					Customer customer = new Customer();
					JSONObject objectResult = arrayResult.getJSONObject(i);
					customer.setCustomerID(objectResult.getString("ckhdm"));
					customer.setCustName(objectResult.getString("ckhmc"));
					customer.setAddress(objectResult.getString("ckhdz"));
					customer.setCustMobile(objectResult.getString("cyddh"));
					customer.setCustOtherPhone(objectResult.getString("cdh"));
					customer.setPostcode(objectResult.getString("cyb"));
					customer.setRegularCustContacter(objectResult
							.getString("clxr"));
					list.add(customer);
				}
				return list;
			}
			throw new ResponseException();
		} catch (JSONException e) {
			Log.e(tag, "Parsing json data error.", e);
			throw new ResponseException(e);
		} catch (IOException e) {
			Log.e(tag, "Connect to server error.", e);
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
	 * @see com.roiland.crm.sm.core.service.CustomerManagmentAPI#noPlanCustomerList(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	   @Override
	    public List<Customer> noPlanCustomerList(String searchWord,
	                                          String searchColumns, Integer startNum, Integer rowCount) throws ResponseException{
	        
	          // 定义变量接受返回值
	          ReleasableList<Customer> customerList = null;
	          RLHttpResponse response = null;
	          try {
	              JSONObject params = new JSONObject();
	              // 添加参数
	              params.put("searchWord", searchWord);
	              params.put("searchColumns", searchColumns);
	              params.put("startNum", startNum);
	              params.put("rowCount", rowCount);
	        
	              // 获取缓存Key
	              String key = getKey(URLContact.METHOD_NOACTION_PLAN_PROJECT_COUNT, params);
	        
	              // 从缓存中取数据，如果有数据并且没有过期，直接返回缓存中数据。
	              customerList = lruCache.get(key);
	              if (customerList != null && !customerList.isExpired()) {
	                  return customerList;
	              }
	        
	              // 连接服务器
	              response = getHttpClient().executePostJSON(
	                      getURLAddress(URLContact.METHOD_NOACTION_PLAN_PROJECT_COUNT), params, null);
	        
	              // 如果与服务器连接成功，执行下列操作
	              if (response.isSuccess()) {
	                  customerList = new ArrayReleasableList<Customer>();
	                  // 返回json
	                  JSONObject result = new JSONObject(getSimpleString(response));
	        
	                  // 解析json,把数据添加到Customer中
	                  JSONArray arrayResult = result.getJSONArray("result");
	                  int n = arrayResult.length();
	                  for (int i = 0; i < n; i++) {
	                      JSONObject objectResult = arrayResult.getJSONObject(i);
	                      Customer customer = new Customer();
	                      customer.setCustomerID(String.valueOf(objectResult
	                              .get("customerID")));
	                      customer.setCustName(String.valueOf(objectResult
	                              .get("custName")));
	                      customer.setCustFromCode(String.valueOf(objectResult
	                              .get("custFromCode")));
	                      customer.setCustFrom(String.valueOf(objectResult
	                              .get("custFrom")));
	                      customer.setCustTypeCode(String.valueOf(objectResult
	                              .get("custTypeCode")));
	                      customer.setCustType(String.valueOf(objectResult
	                              .get("custType")));
	                      customer.setInfoFromCode(String.valueOf(objectResult
	                              .get("infoFromCode")));
	                      customer.setInfoFrom(String.valueOf(objectResult
	                              .get("infoFrom")));
	                      customer.setCollectFromCode(String.valueOf(objectResult
	                              .get("collectFromCode")));
	                      customer.setCollectFrom(String.valueOf(objectResult
	                              .get("collectFrom")));
	                      customer.setCustMobile(String.valueOf(objectResult
	                              .get("custMobile")));
	                      customer.setCustOtherPhone(String.valueOf(objectResult
	                              .get("custOtherPhone")));
	                      customer.setGenderCode(String.valueOf(objectResult
	                              .get("genderCode")));
	                      customer.setBirthday(String.valueOf(objectResult
	                              .get("birthday")));
	                      customer.setIdTypeCode(String.valueOf(objectResult
	                              .get("idTypeCode")));
	                      customer.setIdType(String.valueOf(objectResult
	                              .get("idType")));
	                      customer.setIdNumber(String.valueOf(objectResult
	                              .get("idNumber")));
	                      customer.setProvinceCode(String.valueOf(objectResult
	                              .get("provinceCode")));
	                      customer.setProvince(String.valueOf(objectResult
	                              .get("province")));
	                      customer.setCityCode(String.valueOf(objectResult
	                              .get("cityCode")));
	                      customer.setCity(String.valueOf(objectResult.get("city")));
	                      customer.setDistrictCode(String.valueOf(objectResult
	                              .get("districtCode")));
	                      customer.setDistrict(String.valueOf(objectResult
	                              .get("district")));
	                      customer.setCustComment(String.valueOf(objectResult
	                              .get("custComment")));
	                      customer.setOwnerName(String.valueOf(objectResult.get("owerName")));
	                      customer.setCustStatus(objectResult.getString("custStatus"));
	                      customer.setCreateDate(objectResult.getLong("createDate"));
	                      customerList.add(customer);
	                  }
	        
	                  // 缓存数据
	                  if (customerList != null) {
	                      lruCache.put(key, customerList);
	                  }
	                  return customerList;
	              }
	              throw new ResponseException();
	        
	          } catch (JSONException e) {
	              Log.e(tag, "Parsing json data error.", e);
	              throw new ResponseException(e);
	          } catch (IOException e) {
	              Log.e(tag, "Connect to server error.", e);
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
	    * @see com.roiland.crm.sm.core.service.CustomerManagmentAPI#assignCustomer(java.lang.String, java.lang.String)
	    */
        @Override
        public boolean assignCustomer(String customerID, String ownerID) throws ResponseException{

            JSONObject params = new JSONObject();
            RLHttpResponse response = null;
            // 添加参数
            try {
                params.put("customerID", customerID);
                params.put("ownerID", ownerID);

                // 连接服务器
                response = getHttpClient().executePostJSON(
                        getURLAddress(URLContact.METHOD_ASSIGN_CUSTOMER), params, null);
                // 判断是否连接成功，如果连接成功执行下列操作
                if (response.isSuccess()) {
                    JSONObject result = new JSONObject(getSimpleString(response));
                    boolean flag = result.getBoolean("success");
                    // 解析返回结果
                    return flag;
                }
            } catch (JSONException e) {
                Log.e(tag, "Parsing json data error.", e);
                throw new ResponseException(e);
            } catch (IOException e) {
                Log.e(tag, "Connect to server error.", e);
                throw new ResponseException(e);
            } catch (Exception e) {
                throw new ResponseException(e);
            } finally {
                if (response != null) {
                    response.release();
                }
            }
            return false;
        }
	   
}
