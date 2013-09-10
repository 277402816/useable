package com.roiland.crm.sm.core.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roiland.crm.sm.GlobalConstant.URLContact;
import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.OppoFunnel;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.ProjectAPI;
import com.roiland.crm.sm.core.service.exception.ResponseException;
import com.roiland.crm.sm.utils.DateFormatUtils;
import com.roiland.crm.sm.utils.Log;
import com.roiland.crm.sm.utils.ReleasableList;
import com.roiland.crm.sm.utils.StringUtils;

/**
 * 
 * <pre>
 * 销售线索实现类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ProjectAPIImpl.java, v 0.1 2013-8-2 上午11:04:38 shuang.gao Exp $
 */
public class ProjectAPIImpl extends AbstractBaseAPI implements ProjectAPI {

    private static final String tag = Log.getTag(ProjectAPIImpl.class);

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#getProjectList()
     */
    @Override
    public List<Project> getProjectList(String searchWord, String searchColumns, String expired,
                                        Integer startNum, Integer rowCount,
                                        Project.AdvancedSearch advancedSearch)
                                                                              throws ResponseException {
        // 获取列表
        ReleasableList<Project> projectList = null;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("searchWord", searchWord);
            params.put("searchColumns", searchColumns);
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("expired", expired);
            params.put("searchType", "0");

            //设置高级搜索条件
            if (advancedSearch != null) {
                params.put("brand", advancedSearch.getBrand());
                params.put("intentAuto", advancedSearch.getModel());
                params.put("flowid", advancedSearch.getFollowStatus());
                params.put("owner", advancedSearch.getOwner());
                params.put("startDate", advancedSearch.getStartDate());
                params.put("closeDate", advancedSearch.getEndDate());
                params.put("orderBy", advancedSearch.getOrderBy());
            }

            // 获取缓存Key
            @SuppressWarnings("unused")
            String key = getKey(URLContact.METHOD_GET_PROJECT_LIST, params);
            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_PROJECT_LIST), params, null);

            if (response.isSuccess()) {
                projectList = new ArrayReleasableList<Project>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray project = jsonBean.getJSONArray("result");

                for (int i = 0; i < project.length(); i++) {
                    try {
                        JSONObject json = project.getJSONObject(i);
                        Project result = new Project();
                        Customer cust = new Customer();
                        result.setProjectID(json.getString("projectID")); //添加project ID在Project对象里。
                        cust.setProjectID(json.getString("projectID"));
                        cust.setCustName(json.getString("custName"));
                        cust.setCustMobile(json.getString("custMobile"));
                        cust.setCustOtherPhone(json.getString("custOtherPhone"));
                        cust.setHasUnexePlan(json.getString("hasUnexePlan"));
                        cust.setCustomerID(json.getString("customerID"));
                        cust.setCustFrom(json.getString("custFrom"));
                        cust.setCustFromCode(json.getString("custFromCode"));
                        cust.setCustType(json.getString("custType"));
                        cust.setCustTypeCode(json.getString("custTypeCode"));
                        cust.setCustComment(json.getString("custComment"));
                        cust.setIdNumber(json.getString("idNumber"));

                        PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                        purchaseCarIntention.setBrandCode(json.getString("brandCode"));
                        purchaseCarIntention.setBrand(json.getString("brand"));
                        purchaseCarIntention.setModelCode(json.getString("modelCode"));
                        purchaseCarIntention.setModel(json.getString("model"));
                        purchaseCarIntention.setFlowStatus(json.getString("flowStatus"));
                        purchaseCarIntention.setInsideColorCheck(Boolean.parseBoolean(String
                            .valueOf(json.getString("insideColorCheck"))));
                        purchaseCarIntention.setPreorderDate(parsingLong(json
                            .getString("preorderDate")));
                        purchaseCarIntention
                            .setCreateDate(parsingLong(json.getString("createDate")));
                        purchaseCarIntention.setProjectComment(json.getString("projectComment"));
                        purchaseCarIntention
                            .setOutsideColorCode(json.getString("outsideColorCode"));
                        purchaseCarIntention.setOutsideColor(json.getString("outsideColor"));
                        purchaseCarIntention.setInsideColor(json.getString("insideColor"));
                        purchaseCarIntention.setInsideColorCode(json.getString("insideColorCode"));
                        purchaseCarIntention
                            .setCarConfiguration(json.getString("carConfiguration"));
                        purchaseCarIntention.setCarConfigurationCode(json
                            .getString("carConfigurationCode"));
                        purchaseCarIntention.setEmployeeName(json.getString("employeeName"));
                        purchaseCarIntention.setAbandonFlag(json.getString("abandonFlag"));

                        result.setCustomer(cust);
                        result.setPurchaseCarIntention(purchaseCarIntention);
                        projectList.add(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return projectList;
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

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#getProjectInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Project getProjectInfo(String userID, String dealerOrgID, String projectID,
                                  String customerID) throws ResponseException {
        Project project = null;
        RLHttpResponse response = null;
        try {
            if (userID == null || dealerOrgID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", projectID);
            params.put("customerID", customerID);

            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_PROJECT_INFO), params, null);
            if (response.isSuccess()) {
                project = new Project();
                JSONObject result = new JSONObject(getSimpleString(response))
                    .getJSONObject("result");
                JSONObject customerEntityresult = result.getJSONObject("customerEntity");
                JSONObject purchaseCarIntentionresult = result
                    .getJSONObject("purchaseCarIntention");

                Customer customerEntity = new Customer();
                PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                if (StringUtils.isEmpty(projectID)) {
                    customerEntity.setProjectID(projectID);
                }
                customerEntity.setCustomerID(parsingString(customerEntityresult.get("customerID")));
                customerEntity.setCustName(parsingString(customerEntityresult.get("custName")));
                customerEntity.setCustFromCode(parsingString(customerEntityresult
                    .get("custFromCode")));
                customerEntity.setCustFrom(parsingString(customerEntityresult.get("custFrom")));
                customerEntity.setCustTypeCode(parsingString(customerEntityresult
                    .get("custTypeCode")));
                customerEntity.setCustType(parsingString(customerEntityresult.get("custType")));
                customerEntity.setInfoFromCode(parsingString(customerEntityresult
                    .get("infoFromCode")));
                customerEntity.setInfoFrom(parsingString(customerEntityresult.get("infoFrom")));
                customerEntity.setCollectFromCode(parsingString(customerEntityresult
                    .get("collectFromCode")));
                customerEntity
                    .setCollectFrom(parsingString(customerEntityresult.get("collectFrom")));
                customerEntity.setCustMobile(parsingString(customerEntityresult.get("custMobile")));
                customerEntity.setCustOtherPhone(parsingString(customerEntityresult
                    .get("custOtherPhone")));
                customerEntity.setGenderCode(parsingString(customerEntityresult.get("genderCode")));
                customerEntity.setGender(parsingString(customerEntityresult.get("gender")));
                customerEntity.setBirthday(parsingString(customerEntityresult.get("birthday")));
                customerEntity
                    .setIdTypeCode((parsingString(customerEntityresult.get("idTypeCode"))));
                customerEntity.setIdType(parsingString(customerEntityresult.get("idType")));
                customerEntity.setIdNumber(parsingString(customerEntityresult.get("idNumber")));
                customerEntity.setProvinceCode(parsingString(customerEntityresult
                    .get("provinceCode")));
                customerEntity.setProvince(parsingString(customerEntityresult.get("province")));
                customerEntity.setCityCode(parsingString(customerEntityresult.get("cityCode")));
                customerEntity.setCity(parsingString(customerEntityresult.get("city")));
                customerEntity.setDistrictCode(parsingString(customerEntityresult
                    .get("districtCode")));
                customerEntity.setDistrict(parsingString(customerEntityresult.get("district")));
                customerEntity.setQq(parsingString(customerEntityresult.get("qq")));
                customerEntity.setAddress(parsingString(customerEntityresult.get("address")));
                customerEntity.setPostcode(parsingString(customerEntityresult.get("postcode")));
                customerEntity.setEmail(parsingString(customerEntityresult.get("email")));
                customerEntity.setConvContactTime(parsingString(customerEntityresult
                    .get("convContactTime")));
                //              customerEntity.setConvContactTimeCode((customerEntityresult
                //                              .get("convContactTimeCode")));
                customerEntity.setExpectContactWayCode(parsingString(customerEntityresult
                    .get("expectContactWayCode")));
                customerEntity.setExpectContactWay((parsingString(customerEntityresult
                    .get("expectContactWay"))));
                customerEntity.setFax(parsingString(customerEntityresult.get("fax")));
                //              customerEntity.setExistingCarCode(String
                //                      .valueOf(parsingString(customerEntityresult
                //                              .get("existingCarCode"))));
                customerEntity.setExistingCarBrand(parsingString(customerEntityresult
                    .get("existingCarBrand")));
                customerEntity.setIndustryCode(parsingString(customerEntityresult
                    .get("industryCode")));
                customerEntity.setPositionCode(parsingString(customerEntityresult
                    .get("positionCode")));
                customerEntity.setEducationCode(parsingString(customerEntityresult
                    .get("educationCode")));
                customerEntity
                    .setExistingCar(parsingString(customerEntityresult.get("existingCar")));
                customerEntity.setIndustry(parsingString(customerEntityresult.get("industry")));
                customerEntity.setPosition(parsingString(customerEntityresult.get("position")));
                customerEntity.setEducation(parsingString(customerEntityresult.get("education")));
                customerEntity.setCustInterestCode1(parsingString(customerEntityresult
                    .get("custInterestCode1")));
                customerEntity.setCustInterest1((parsingString(customerEntityresult
                    .get("custInterest1"))));
                customerEntity.setCustInterestCode2((parsingString(customerEntityresult
                    .get("custInterestCode2"))));
                customerEntity.setCustInterest2(parsingString(customerEntityresult
                    .get("custInterest2")));
                customerEntity.setCustInterestCode3(parsingString(customerEntityresult
                    .get("custInterestCode3")));
                customerEntity.setCustInterest3(parsingString(customerEntityresult
                    .get("custInterest3")));
                customerEntity.setExistLisenPlate(parsingString(customerEntityresult
                    .get("existLisenPlate")));
                customerEntity.setEnterpTypeCode(parsingString(customerEntityresult
                    .get("enterpTypeCode")));
                customerEntity.setEnterpType(parsingString(customerEntityresult.get("enterpType")));
                customerEntity.setEnterpPeopleCountCode(parsingString(customerEntityresult
                    .get("enterpPeopleCountCode")));
                customerEntity.setEnterpPeopleCount(parsingString(customerEntityresult
                    .get("enterpPeopleCount")));
                customerEntity.setRegisteredCapitalCode(parsingString(customerEntityresult
                    .get("registeredCapitalCode")));
                customerEntity.setRegisteredCapital(parsingString(customerEntityresult
                    .get("registeredCapital")));
                //              customerEntity.setCompeCarModelCode(String
                //                      .valueOf(parsingString(customerEntityresult
                //                              .get("compeCarModelCode"))));
                customerEntity.setCompeCarModel(parsingString(customerEntityresult
                    .get("compeCarModel")));
                customerEntity.setRebuyStoreCustTag(Boolean
                    .parseBoolean((parsingString(customerEntityresult.get("rebuyStoreCustTag")))));
                customerEntity.setRebuyOnlineCustTag(Boolean
                    .parseBoolean(parsingString(customerEntityresult.get("rebuyOnlineCustTag"))));
                customerEntity.setChangeCustTag(Boolean
                    .parseBoolean(parsingString(customerEntityresult.get("changeCustTag"))));
                customerEntity.setLoanCustTag(Boolean
                    .parseBoolean((parsingString(customerEntityresult.get("loanCustTag")))));
                customerEntity.setHeaderQuartCustTag(Boolean
                    .parseBoolean(parsingString(customerEntityresult.get("headerQuartCustTag"))));
                customerEntity.setRegularCustTag(Boolean
                    .parseBoolean(parsingString(customerEntityresult.get("regularCustTag"))));
                customerEntity.setRegularCustCode(parsingString(customerEntityresult
                    .get("regularCustCode")));
                customerEntity
                    .setRegularCust(parsingString(customerEntityresult.get("regularCust")));
                customerEntity.setBigCustTag(Boolean
                    .parseBoolean(parsingString(customerEntityresult.get("bigCustTag"))));
                customerEntity.setBigCustsCode(parsingString(customerEntityresult
                    .get("bigCustsCode")));
                customerEntity.setBigCusts(parsingString(customerEntityresult.get("bigCusts")));
                customerEntity
                    .setCustComment(parsingString(customerEntityresult.get("custComment")));
                //customerEntity.setHasUnexePlan(parsingString(customerEntityresult.get("hasUnexePlan")));

                purchaseCarIntention.setBrandCode(parsingString(purchaseCarIntentionresult
                    .get("brandCode")));
                purchaseCarIntention
                    .setBrand(parsingString(purchaseCarIntentionresult.get("brand")));

                purchaseCarIntention.setModelCode(parsingString(purchaseCarIntentionresult
                    .get("modelCode")));
                purchaseCarIntention
                    .setModel(parsingString(purchaseCarIntentionresult.get("model")));
                purchaseCarIntention.setOutsideColorCode(parsingString(purchaseCarIntentionresult
                    .get("outsideColorCode")));
                purchaseCarIntention.setOutsideColor(parsingString(purchaseCarIntentionresult
                    .get("outsideColor")));
                purchaseCarIntention.setInsideColorCode(parsingString(purchaseCarIntentionresult
                    .get("insideColorCode")));
                purchaseCarIntention.setInsideColor(parsingString(purchaseCarIntentionresult
                    .get("insideColor")));
                purchaseCarIntention.setInsideColorCheck(purchaseCarIntentionresult
                    .getBoolean("insideColorCheck") ? true : false);
                purchaseCarIntention
                    .setCarConfigurationCode(parsingString(purchaseCarIntentionresult
                        .get("carConfigurationCode")));
                purchaseCarIntention.setCarConfiguration(parsingString(purchaseCarIntentionresult
                    .get("carConfiguration")));
                purchaseCarIntention.setSalesQuote(parsingString(purchaseCarIntentionresult
                    .get("salesQuote")));
                purchaseCarIntention
                    .setDealPriceIntervalCode(parsingString(purchaseCarIntentionresult
                        .get("dealPriceIntervalCode")));
                purchaseCarIntention.setDealPriceInterval(parsingString(purchaseCarIntentionresult
                    .get("dealPriceInterval")));
                purchaseCarIntention.setPaymentCode(parsingString(purchaseCarIntentionresult
                    .get("paymentCode")));
                purchaseCarIntention.setPayment(parsingString(purchaseCarIntentionresult
                    .get("payment")));
                purchaseCarIntention.setPreorderCount(parsingString(purchaseCarIntentionresult
                    .get("preorderCount")));
                purchaseCarIntention.setPreorderDate(purchaseCarIntentionresult
                    .isNull("preorderDate") ? 0L : purchaseCarIntentionresult
                    .getLong("preorderDate"));
                purchaseCarIntention.setFlowStatusCode(parsingString(purchaseCarIntentionresult
                    .get("flowStatusCode")));
                purchaseCarIntention.setFlowStatus(parsingString(purchaseCarIntentionresult
                    .get("flowStatus")));
                purchaseCarIntention.setDealPossibility(purchaseCarIntentionresult
                    .isNull("dealPossibility") ? "" : parsingString(purchaseCarIntentionresult
                    .get("dealPossibility")));
                purchaseCarIntention
                    .setPurchMotivationCode(parsingString(purchaseCarIntentionresult
                        .get("purchMotivationCode")));
                purchaseCarIntention.setPurchMotivation(parsingString(purchaseCarIntentionresult
                    .get("purchMotivation")));
                purchaseCarIntention.setChassisNo(parsingString(purchaseCarIntentionresult
                    .get("chassisNo")));
                purchaseCarIntention.setEngineNo(parsingString(purchaseCarIntentionresult
                    .get("engineNo")));
                purchaseCarIntention.setLicensePlate(parsingString(purchaseCarIntentionresult
                    .get("licensePlate")));
                purchaseCarIntention.setLicenseProp(parsingString(purchaseCarIntentionresult
                    .get("licenseProp")));
                purchaseCarIntention.setLicensePropCode(parsingString(purchaseCarIntentionresult
                    .get("licensePropCode")));
                purchaseCarIntention.setPickupDate(parsingString(purchaseCarIntentionresult
                    .get("pickupDate")));
                purchaseCarIntention.setPreorderTag(parsingString(purchaseCarIntentionresult
                    .get("preorderTag")));
                purchaseCarIntention.setGiveupTag(purchaseCarIntentionresult
                    .getBoolean("giveupTag"));
                purchaseCarIntention.setGiveupReason(parsingString(purchaseCarIntentionresult
                    .get("giveupReason")));
                //              purchaseCarIntention.setGiveupReasonCode(String
                //                      .valueOf(parsingString(purchaseCarIntentionresult
                //                              .get("giveupReasonCode")));
                purchaseCarIntention.setInvoiceTitle(parsingString(purchaseCarIntentionresult
                    .get("invoiceTitle")));
                purchaseCarIntention.setProjectComment(parsingString(purchaseCarIntentionresult
                    .get("projectComment")));
                // 是否有活动订单
                purchaseCarIntention.setHasActiveOrder(purchaseCarIntentionresult
                    .getBoolean("hasActiveOrder"));
                // 是否有试乘试驾
                purchaseCarIntention.setHasActiveDrive(purchaseCarIntentionresult
                    .getBoolean("hasActiveDrive"));
                //是否有未执行的跟踪计划
                purchaseCarIntention.setHasUnexePlan(purchaseCarIntentionresult
                    .getBoolean("hasUnexePlan"));
                //添加获取订单状态字段
                purchaseCarIntention.setOrderStatus(parsingString(purchaseCarIntentionresult
                    .get("orderStatus")));
                project.setCustomer(customerEntity);
                project.setPurchaseCarIntention(purchaseCarIntention);

                return project;
            }
            throw new ResponseException();
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
    }

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#createProject(java.lang.String, java.lang.String, com.roiland.crm.core.model.Project, com.roiland.crm.core.model.TracePlan)
     */
    @Override
    public Boolean createProject(String userID, String dealerOrgID, Project project,
                                 TracePlan tracePlan) throws ResponseException {
        RLHttpResponse response = null;
        try {
            if (userID == null || dealerOrgID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();

            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            //添加该字段是为了一个客户创建多个销售线索的情况而设的，以免相同的客户被多次创建。
            Log.d(tag, "createProject() >> customerID ================== "
                       + project.getCustomer().getCustomerID());
            params.put("customerID", project.getCustomer().getCustomerID());
            params.put("custName", project.getCustomer().getCustName());
            params.put("custFromCode",
                StringUtils.convertNull(project.getCustomer().getCustFromCode()));
            params.put("custTypeCode",
                StringUtils.convertNull(project.getCustomer().getCustTypeCode()));
            params.put("infoFromCode",
                StringUtils.convertNull(project.getCustomer().getInfoFromCode()));
            params.put("collectFromCode",
                StringUtils.convertNull(project.getCustomer().getCollectFromCode()));
            params
                .put("custMobile", StringUtils.convertNull(project.getCustomer().getCustMobile()));
            params.put("custOtherPhone",
                StringUtils.convertNull(project.getCustomer().getCustOtherPhone()));
            params
                .put("genderCode", StringUtils.convertNull(project.getCustomer().getGenderCode()));
            params.put("birthday",StringUtils.isEmpty(project.getCustomer().getBirthday()) ? ""
                : Long.parseLong(project.getCustomer().getBirthday()));
            params
                .put("idTypeCode", StringUtils.convertNull(project.getCustomer().getIdTypeCode()));
            params.put("idNumber", StringUtils.convertNull(project.getCustomer().getIdNumber()));
            params.put("provinceCode",
                StringUtils.convertNull(project.getCustomer().getProvinceCode()));
            params.put("cityCode", StringUtils.convertNull(project.getCustomer().getCityCode()));
            params.put("districtCode",
                StringUtils.convertNull(project.getCustomer().getDistrictCode()));
            params.put("qq", StringUtils.convertNull(project.getCustomer().getQq()));
            params.put("address", StringUtils.convertNull(project.getCustomer().getAddress()));
            params.put("postcode", StringUtils.convertNull(project.getCustomer().getPostcode()));
            params.put("email", StringUtils.convertNull(project.getCustomer().getEmail()));
            params.put("convContactTime",
                StringUtils.convertNull(project.getCustomer().getConvContactTime()));
            params.put("convContactTimeCode",
                StringUtils.convertNull(project.getCustomer().getConvContactTimeCode()));
            params.put("expectContactWayCode",
                StringUtils.convertNull(project.getCustomer().getExpectContactWayCode()));
            params.put("fax", StringUtils.convertNull(project.getCustomer().getFax()));
            params.put("existingCarCode",
                StringUtils.convertNull(project.getCustomer().getExistingCarCode()));
            params.put("existingCar",
                StringUtils.convertNull(project.getCustomer().getExistingCar()));
            params.put("existingCarBrand",
                StringUtils.convertNull(project.getCustomer().getExistingCarBrand()));
            params.put("industryCode",
                StringUtils.convertNull(project.getCustomer().getIndustryCode()));
            params.put("positionCode",
                StringUtils.convertNull(project.getCustomer().getPositionCode()));
            params.put("educationCode",
                StringUtils.convertNull(project.getCustomer().getEducationCode()));
            params.put("industry", StringUtils.convertNull(project.getCustomer().getIndustry()));
            params.put("position", StringUtils.convertNull(project.getCustomer().getPosition()));
            params.put("education", StringUtils.convertNull(project.getCustomer().getEducation()));
            params.put("custInterestCode1",
                StringUtils.convertNull(project.getCustomer().getCustInterestCode1()));
            params.put("custInterestCode2",
                StringUtils.convertNull(project.getCustomer().getCustInterestCode2()));
            params.put("custInterestCode3",
                StringUtils.convertNull(project.getCustomer().getCustInterestCode3()));
            params.put("existLisenPlate",
                StringUtils.convertNull(project.getCustomer().getExistLisenPlate()));
            params.put("enterpTypeCode",
                StringUtils.convertNull(project.getCustomer().getEnterpTypeCode()));
            params.put("enterpPeopleCountCode",
                StringUtils.convertNull(project.getCustomer().getEnterpPeopleCountCode()));
            params.put("registeredCapitalCode",
                StringUtils.convertNull(project.getCustomer().getRegisteredCapitalCode()));
            params.put("compeCarModelCode", project.getCustomer().getCompeCarModelCode());
            params.put("rebuyStoreCustTag", project.getCustomer().getRebuyStoreCustTag());
            params.put("rebuyOnlineCustTag", project.getCustomer().getRebuyOnlineCustTag());
            params.put("changeCustTag", project.getCustomer().getChangeCustTag());
            params.put("loanCustTag", project.getCustomer().getLoanCustTag());
            params.put("headerQuartCustTag", project.getCustomer().getHeaderQuartCustTag());
            params.put("regularCustTag", project.getCustomer().getRegularCustTag());
            params.put("regularCustCode",
                StringUtils.convertNull(project.getCustomer().getRegularCustCode()));
            params.put("regularCust",
                StringUtils.convertNull(project.getCustomer().getRegularCust()));
            params.put("bigCustTag", project.getCustomer().getBigCustTag());
            params.put("bigCusts", StringUtils.convertNull(project.getCustomer().getBigCusts()));
            params.put("bigCustsCode",
                StringUtils.convertNull(project.getCustomer().getBigCustsCode()));
            params.put("custComment",
                StringUtils.convertNull(project.getCustomer().getCustComment()));
            params.put("dormancy", (project.getCustomer().getDormancy() != null ? project
                .getCustomer().getDormancy() : false));
            params.put("updateCustInfo", project.getCustomer().isUpdateCustInfo());

            params.put("brandCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getBrandCode()));
            params.put("modelCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getModelCode()));
            params.put("outsideColorCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getOutsideColorCode()));
            params.put("insideColorCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getInsideColorCode()));
            params.put("insideColorCheck", project.getPurchaseCarIntention().isInsideColorCheck());
            params.put("carConfigurationCode", StringUtils.convertNull(project
                .getPurchaseCarIntention().getCarConfigurationCode()));
            params.put("salesQuote",
                "".equals(project.getPurchaseCarIntention().getSalesQuote()) ? null : project
                    .getPurchaseCarIntention().getSalesQuote());
            params.put("dealPriceInterval",
                StringUtils.convertNull(project.getPurchaseCarIntention().getDealPriceInterval()));
            params.put("dealPriceIntervalCode", StringUtils.convertNull(project
                .getPurchaseCarIntention().getDealPriceIntervalCode()));
            params.put("payment",
                StringUtils.convertNull(project.getPurchaseCarIntention().getPayment()));
            params.put("paymentCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getPaymentCode()));
            params.put("preorderCount",
                StringUtils.isEmpty(project.getPurchaseCarIntention().getPreorderCount()) ? 1
                    : Integer.parseInt(project.getPurchaseCarIntention().getPreorderCount()));
            params.put("preorderDate", project.getPurchaseCarIntention().getPreorderDate());
            params.put("flowStatusCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getFlowStatusCode()));
            params.put("flowStatus",
                StringUtils.convertNull(project.getPurchaseCarIntention().getFlowStatus()));
            params.put("dealPossibility",
                StringUtils.convertNull(project.getPurchaseCarIntention().getDealPossibility()));
            params.put("purchMotivationCode", "".equals(project.getPurchaseCarIntention()
                .getPurchMotivationCode()) ? null : project.getPurchaseCarIntention()
                .getPurchMotivationCode());
            params.put("chassisNo",
                StringUtils.convertNull(project.getPurchaseCarIntention().getChassisNo()));
            params.put("engineNo",
                StringUtils.convertNull(project.getPurchaseCarIntention().getEngineNo()));
            params.put("licensePlate",
                StringUtils.convertNull(project.getPurchaseCarIntention().getLicensePlate()));
            params.put("licensePropCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getLicensePropCode()));
            params.put("licenseProp",
                StringUtils.convertNull(project.getPurchaseCarIntention().getLicenseProp()));
            params
                .put("pickupDate", parsingLong(project.getPurchaseCarIntention().getPickupDate()));
            if (parsingLong(project.getPurchaseCarIntention().getPickupDate()) == 0)
                params.put("pickupDate", null);
            else
                params.put("pickupDate", parsingLong(project.getPurchaseCarIntention()
                    .getPickupDate()));
            params.put("preorderTag",
                Boolean.parseBoolean(project.getPurchaseCarIntention().getPreorderTag()));
            params.put("giveupTag",
                project.getPurchaseCarIntention().isGiveupTag() != null ? project
                    .getPurchaseCarIntention().isGiveupTag() : null);
            params.put("giveupReason",
                StringUtils.convertNull(project.getPurchaseCarIntention().getGiveupReason()));
            params.put("giveupReasonCode",
                StringUtils.convertNull(project.getPurchaseCarIntention().getGiveupReasonCode()));
            params.put("invoiceTitle",
                StringUtils.convertNull(project.getPurchaseCarIntention().getInvoiceTitle()));
            params.put("projectComment",
                StringUtils.convertNull(project.getPurchaseCarIntention().getProjectComment()));
            params.put("isInsideColorCheck",
                project.getPurchaseCarIntention().isInsideColorCheck() != null ? project
                    .getPurchaseCarIntention().isInsideColorCheck() ? "1" : "0" : "0");
            params.put("abandonFlag",
                project.getPurchaseCarIntention().getAbandonFlag() != null ? project
                    .getPurchaseCarIntention().getAbandonFlag() : "0");

            if (tracePlan != null) {
                params.put("activityTypeCode", tracePlan.getActivityTypeCode());
                params.put("executeTime", tracePlan.getExecuteTime());
                params.put("executeStatus", tracePlan.getExecuteStatus());
                params.put("executeStatusCode", tracePlan.getExecuteStatusCode());
                params.put("activityContent",
                    StringUtils.convertNull(tracePlan.getActivityContent()));
                params.put("contactResultCode",
                    StringUtils.convertNull(tracePlan.getContactResultCode()));
                params.put("custFeedback", StringUtils.convertNull(tracePlan.getCustFeedback()));
                params.put("collcustomerId", tracePlan.getCollcustomerId());
            }
            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_CREATE_PROJECT), params, null);

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
                                project.setProjectID(result.getString("projectID"));
                                project.getCustomer().setProjectID(result.getString("projectID"));
                                project.getCustomer().setCustomerID(result.getString("customerID"));
                                return true;
                            }
                        } else if (node.equalsIgnoreCase("validate_error")) {
                            error = parsingValidation(result.getJSONObject(node));
                            throw new ResponseException(error);
                        }
                    }
                }
            }
            throw new ResponseException(500);
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
    }

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#updateProjectInfo(java.lang.String, java.lang.String, com.roiland.crm.core.model.Project)
     */
    @Override
    public Boolean updateProjectInfo(String userID, String dealerOrgID, Project project)
                                                                                        throws ResponseException {
        RLHttpResponse response = null;
        try {
            if (userID == null || dealerOrgID == null) {
                throw new ResponseException("userID or dealerOrgID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", project.getCustomer().getProjectID());
            params.put("customerID", project.getCustomer().getCustomerID());

            params.put("custName", project.getCustomer().getCustName());
            params.put("custFromCode", project.getCustomer().getCustFromCode());
            params.put("custTypeCode", project.getCustomer().getCustTypeCode());
            params.put("infoFromCode", project.getCustomer().getInfoFromCode());
            params.put("collectFromCode", project.getCustomer().getCollectFromCode());
            params.put("custMobile", project.getCustomer().getCustMobile());
            params.put("custOtherPhone", project.getCustomer().getCustOtherPhone());
            params.put("genderCode", project.getCustomer().getGenderCode());
            Log.d(tag, "updateProjectInfo() >> birthday ================== "
                       + project.getCustomer().getBirthday());
            params.put("birthday", StringUtils.isEmpty(project.getCustomer().getBirthday()) ? ""
                : Long.parseLong(project.getCustomer().getBirthday()));
            params.put("idTypeCode", project.getCustomer().getIdTypeCode());
            params.put("idNumber", project.getCustomer().getIdNumber());
            params.put("provinceCode", project.getCustomer().getProvinceCode());
            params.put("cityCode", project.getCustomer().getCityCode());
            params.put("districtCode", project.getCustomer().getDistrictCode());
            params.put("qq", project.getCustomer().getQq());
            params.put("address", project.getCustomer().getAddress());
            params.put("postcode", project.getCustomer().getPostcode());
            params.put("email", project.getCustomer().getEmail());
            params.put("convContactTime", project.getCustomer().getConvContactTime());
            params.put("convContactTimeCode", project.getCustomer().getConvContactTimeCode());
            params.put("expectContactWayCode", project.getCustomer().getExpectContactWayCode());
            params.put("fax", project.getCustomer().getFax());
            params.put("existingCarCode", project.getCustomer().getExistingCarCode());
            params.put("existingCar", project.getCustomer().getExistingCar());
            params.put("existingCarBrand", project.getCustomer().getExistingCarBrand());
            params.put("industryCode", project.getCustomer().getIndustryCode());
            params.put("positionCode", project.getCustomer().getPositionCode());
            params.put("educationCode", project.getCustomer().getEducationCode());
            params.put("industry", project.getCustomer().getIndustry());
            params.put("position", project.getCustomer().getPosition());
            params.put("education", project.getCustomer().getEducation());
            params.put("custInterestCode1", project.getCustomer().getCustInterestCode1());
            params.put("custInterestCode2", project.getCustomer().getCustInterestCode2());
            params.put("custInterestCode3", project.getCustomer().getCustInterestCode3());
            params.put("existLisenPlate", project.getCustomer().getExistLisenPlate());
            params.put("enterpTypeCode", project.getCustomer().getEnterpTypeCode());
            params.put("enterpPeopleCountCode", project.getCustomer().getEnterpPeopleCountCode());
            params.put("registeredCapitalCode", project.getCustomer().getRegisteredCapitalCode());
            params.put("compeCarModelCode", project.getCustomer().getCompeCarModelCode());
            params.put("rebuyStoreCustTag", project.getCustomer().getRebuyStoreCustTag());
            params.put("rebuyOnlineCustTag", project.getCustomer().getRebuyOnlineCustTag());
            params.put("changeCustTag", project.getCustomer().getChangeCustTag());
            params.put("loanCustTag", project.getCustomer().getLoanCustTag());
            params.put("headerQuartCustTag", project.getCustomer().getHeaderQuartCustTag());
            params.put("regularCustTag", project.getCustomer().getRegularCustTag());
            params.put("regularCustCode",project.getCustomer().getRegularCustCode());
            params.put("regularCust",project.getCustomer().getRegularCust());
            params.put("bigCustTag", project.getCustomer().getBigCustTag());
            params.put("bigCusts", project.getCustomer().getBigCusts());
            params.put("bigCustsCode", project.getCustomer().getBigCustsCode());
            params.put("custComment", project.getCustomer().getCustComment());
            params.put("dormancy", (project.getCustomer().getDormancy() != null ? project
                .getCustomer().getDormancy() : false));
            params.put("brandCode", project.getPurchaseCarIntention().getBrandCode());
            params.put("modelCode", project.getPurchaseCarIntention().getModelCode());
            params.put("outsideColorCode", project.getPurchaseCarIntention().getOutsideColorCode());
            params.put("insideColorCode", project.getPurchaseCarIntention().getInsideColorCode());
            params.put("insideColorCheck",
                project.getPurchaseCarIntention().isInsideColorCheck() ? true : false);
            params.put("carConfigurationCode", project.getPurchaseCarIntention()
                .getCarConfigurationCode());
            params.put("salesQuote",
                "".equals(project.getPurchaseCarIntention().getSalesQuote()) ? null : project
                    .getPurchaseCarIntention().getSalesQuote());
            params.put("dealPriceInterval", project.getPurchaseCarIntention()
                .getDealPriceInterval());
            params.put("dealPriceIntervalCode", project.getPurchaseCarIntention()
                .getDealPriceIntervalCode());
            params.put("payment", project.getPurchaseCarIntention().getPayment());
            params.put("paymentCode", project.getPurchaseCarIntention().getPaymentCode());
            params.put("preorderCount",
                StringUtils.isEmpty(project.getPurchaseCarIntention().getPreorderCount()) ? 1
                    : Integer.parseInt(project.getPurchaseCarIntention().getPreorderCount()));
            params.put("preorderDate", project.getPurchaseCarIntention().getPreorderDate());
            params.put("flowStatusCode", project.getPurchaseCarIntention().getFlowStatusCode());
            params.put("flowStatus", project.getPurchaseCarIntention().getFlowStatus());
            params.put("dealPossibility", project.getPurchaseCarIntention().getDealPossibility());
            params.put("purchMotivationCode", "".equals(project.getPurchaseCarIntention()
                .getPurchMotivationCode()) ? null : project.getPurchaseCarIntention()
                .getPurchMotivationCode());
            params.put("chassisNo", project.getPurchaseCarIntention().getChassisNo());
            params.put("engineNo", project.getPurchaseCarIntention().getEngineNo());
            params.put("licensePlate", project.getPurchaseCarIntention().getLicensePlate());
            params.put("licensePropCode", project.getPurchaseCarIntention().getLicensePropCode());
            params.put("licenseProp", project.getPurchaseCarIntention().getLicenseProp());
            params
                .put("pickupDate", parsingLong(project.getPurchaseCarIntention().getPickupDate()));
            if (parsingLong(project.getPurchaseCarIntention().getPickupDate()) == 0)
                params.put("pickupDate", "null");
            else
                params.put("pickupDate", parsingLong(project.getPurchaseCarIntention()
                    .getPickupDate()));
            params.put("preorderTag",
                Boolean.parseBoolean(project.getPurchaseCarIntention().getPreorderTag()));
            params.put("giveupTag",
                project.getPurchaseCarIntention().isGiveupTag() != null ? project
                    .getPurchaseCarIntention().isGiveupTag() : false);
            params.put("giveupReason", project.getPurchaseCarIntention().getGiveupReason());
            params.put("giveupReasonCode", project.getPurchaseCarIntention().getGiveupReasonCode());
            params.put("invoiceTitle", project.getPurchaseCarIntention().getInvoiceTitle());
            params.put("projectComment", project.getPurchaseCarIntention().getProjectComment());
            params.put("isInsideColorCheck",
                project.getPurchaseCarIntention().isInsideColorCheck() != null ? project
                    .getPurchaseCarIntention().isInsideColorCheck() ? "1" : "0" : "0");

            response = getHttpClient().executePostJSON(
                (getURLAddress(URLContact.METHOD_UPDATE_PROJECT_INFO)), params, null);
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
            throw new ResponseException(500);
        } catch (IOException e) {
            Log.e(tag, "Connection network error.", e);
            throw new ResponseException(e);
        } catch (JSONException e) {
            Log.e(tag, "Parsing data error.", e);
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }

    }

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#getCustomerProjectList(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Project> getCustomerProjectList(String userID, String customerID)
                                                                                 throws ResponseException {
        // 获取列表
        ReleasableList<Project> projectList = null;
        RLHttpResponse response = null;
        try {
            if (userID == null) {
                throw new ResponseException("userID is null.");
            }
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("customerID", customerID);

            // 获取缓存Key
            String key = getKey(URLContact.METHOD_GET_CUSTOMER_PROJECT_LIST, params);

            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_CUSTOMER_PROJECT_LIST), params, null);

            if (response.isSuccess()) {
                projectList = new ArrayReleasableList<Project>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray project = jsonBean.getJSONArray("result");

                for (int i = 0; i < project.length(); i++) {
                    JSONObject json = project.getJSONObject(i);
                    Project result = new Project();
                    Customer cust = new Customer();
                    result.setProjectID("projectID");
                    cust.setProjectID(json.getString("projectID"));
                    cust.setCustName(json.getString("custName"));
                    cust.setCustMobile(json.getString("custMobile"));
                    cust.setCustOtherPhone(json.getString("custOtherPhone"));
                    cust.setHasUnexePlan(json.getString("hasUnexePlan"));
                    cust.setCustomerID(json.getString("customerID"));
                    cust.setCustFrom(json.getString("custFrom"));
                    cust.setCustFromCode(json.getString("custFromCode"));
                    cust.setCustType(json.getString("custType"));
                    cust.setCustTypeCode(json.getString("custTypeCode"));
                    cust.setCustComment(json.getString("custComment"));
                    cust.setIdNumber(json.getString("idNumber"));

                    PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                    purchaseCarIntention.setBrandCode(json.getString("brandCode"));
                    purchaseCarIntention.setBrand(parsingString(json.get("brand")));
                    purchaseCarIntention.setModelCode(json.getString("modelCode"));
                    purchaseCarIntention.setModel(parsingString(json.get("model")));
                    purchaseCarIntention.setFlowStatus(parsingString(json.get("flowStatus")));
                    purchaseCarIntention.setFlowStatusCode("flowStatusCode");
                    purchaseCarIntention.setInsideColorCheck(Boolean.parseBoolean(String
                        .valueOf(json.getString("insideColorCheck"))));

                    purchaseCarIntention.setPreorderDate(parsingLong(json.getString("preorderDate")));
                    purchaseCarIntention.setProjectComment(json.getString("projectComment"));
                    purchaseCarIntention.setOutsideColorCode(json.getString("outsideColorCode"));
                    purchaseCarIntention.setOutsideColor(json.getString("outsideColor"));
                    purchaseCarIntention.setInsideColor(json.getString("insideColor"));
                    purchaseCarIntention.setInsideColorCode(json.getString("insideColorCode"));
                    purchaseCarIntention.setCarConfiguration(json.getString("carConfiguration"));
                    purchaseCarIntention.setCarConfigurationCode(json
                        .getString("carConfigurationCode"));

                    result.setCustomer(cust);
                    result.setPurchaseCarIntention(purchaseCarIntention);
                    projectList.add(result);
                }

                return projectList;
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

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#isExistProject(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, String> isExistProject(String mobileNumber, String otherPhone,
                                              String dealerOrgID) throws ResponseException {
        // 获取列表
        Map<String, String> map = null;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("mobileNumber", mobileNumber);
            params.put("otherPhone", otherPhone);
            params.put("dealerOrgID", dealerOrgID);
            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_IS_EXIST_PROJECT), params, null);

            if (response.isSuccess()) {
                map = new HashMap<String, String>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                map.put("isExisting", jsonBean.getString("isExisting"));
                map.put("success", jsonBean.getString("success"));
                if (jsonBean.getString("isExisting").equals("true")) {
                    map.put("dealerOrgID", jsonBean.getString("dealerOrgID"));
                    map.put("userID", jsonBean.getString("userID"));
                    map.put("userName", jsonBean.getString("userName"));
                    map.put("custStatus", jsonBean.getString("custStatus"));
                }
                return map;
            }
            throw new ResponseException(response.getStatusCode());
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

    /**
     * @see com.roiland.crm.core.service.ProjectAPI#syncContacter(java.lang.String, java.lang.String, java.lang.String, com.roiland.crm.core.model.Customer)
     */
    @Override
    public boolean syncContacter(String userID, String dealerOrgID, String projectID,
                                 Customer customer) throws ResponseException {
        // 获取列表
        boolean success;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("dealerOrgID", dealerOrgID);
            params.put("projectID", projectID);
            //params.put("projectID", customer.getProjectID());
            params.put("customerID", customer.getCustomerID());

            params.put("custName", customer.getCustName());
            params.put("custFromCode", StringUtils.convertNull(customer.getCustFromCode()));
            params.put("custTypeCode", StringUtils.convertNull(customer.getCustTypeCode()));
            params.put("infoFromCode", StringUtils.convertNull(customer.getInfoFromCode()));
            params.put("collectFromCode", StringUtils.convertNull(customer.getCollectFromCode()));
            params.put("custMobile", StringUtils.convertNull(customer.getCustMobile()));
            params.put("custOtherPhone", StringUtils.convertNull(customer.getCustOtherPhone()));
            params.put("genderCode", StringUtils.convertNull(customer.getGenderCode()));
            Log.d(tag,
                "updateProjectInfo() >> birthday ================== " + customer.getBirthday());
            params.put("birthday", DateFormatUtils.parseDateToLong(customer.getBirthday()));
            params.put("idTypeCode", StringUtils.convertNull(customer.getIdTypeCode()));
            params.put("idNumber", StringUtils.convertNull(customer.getIdNumber()));
            params.put("provinceCode", StringUtils.convertNull(customer.getProvinceCode()));
            params.put("cityCode", StringUtils.convertNull(customer.getCityCode()));
            params.put("districtCode", StringUtils.convertNull(customer.getDistrictCode()));
            params.put("qq", StringUtils.convertNull(customer.getQq()));
            params.put("address", StringUtils.convertNull(customer.getAddress()));
            params.put("postcode", StringUtils.convertNull(customer.getPostcode()));
            params.put("email", StringUtils.convertNull(customer.getEmail()));
            params.put("convContactTime", StringUtils.convertNull(customer.getConvContactTime()));
            params.put("convContactTimeCode",
                StringUtils.convertNull(customer.getConvContactTimeCode()));
            params.put("expectContactWayCode",
                StringUtils.convertNull(customer.getExpectContactWayCode()));
            params.put("fax", StringUtils.convertNull(customer.getFax()));
            params.put("existingCarCode", StringUtils.convertNull(customer.getExistingCarCode()));
            params.put("existingCar", StringUtils.convertNull(customer.getExistingCar()));
            params.put("existingCarBrand", StringUtils.convertNull(customer.getExistingCarBrand()));
            params.put("industryCode", StringUtils.convertNull(customer.getIndustryCode()));
            params.put("positionCode", StringUtils.convertNull(customer.getPositionCode()));
            params.put("educationCode", StringUtils.convertNull(customer.getEducationCode()));
            params.put("industry", StringUtils.convertNull(customer.getIndustry()));
            params.put("position", StringUtils.convertNull(customer.getPosition()));
            params.put("education", StringUtils.convertNull(customer.getEducation()));
            params.put("custInterestCode1",
                StringUtils.convertNull(customer.getCustInterestCode1()));
            params.put("custInterestCode2",
                StringUtils.convertNull(customer.getCustInterestCode2()));
            params.put("custInterestCode3",
                StringUtils.convertNull(customer.getCustInterestCode3()));
            params.put("existLisenPlate", StringUtils.convertNull(customer.getExistLisenPlate()));
            params.put("enterpTypeCode", StringUtils.convertNull(customer.getEnterpTypeCode()));
            params.put("enterpPeopleCountCode",
                StringUtils.convertNull(customer.getEnterpPeopleCountCode()));
            params.put("registeredCapitalCode",
                StringUtils.convertNull(customer.getRegisteredCapitalCode()));
            params.put("compeCarModelCode", customer.getCompeCarModelCode());
            params.put("rebuyStoreCustTag", customer.getRebuyStoreCustTag());
            params.put("rebuyOnlineCustTag", customer.getRebuyOnlineCustTag());
            params.put("changeCustTag", customer.getChangeCustTag());
            params.put("loanCustTag", customer.getLoanCustTag());
            params.put("headerQuartCustTag", customer.getHeaderQuartCustTag());
            params.put("regularCustTag", customer.getRegularCustTag());
            params.put("regularCustCode", StringUtils.convertNull(customer.getRegularCustCode()));
            params.put("regularCust", StringUtils.convertNull(customer.getRegularCust()));
            params.put("bigCustTag", customer.getBigCustTag());
            params.put("bigCustsCode", StringUtils.convertNull(customer.getBigCustsCode()));
            params.put("custComment", StringUtils.convertNull(customer.getCustComment()));
            params.put("dormancy",
                (customer.getDormancy() != null ? customer.getDormancy() : false));

            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_SYNC_CONTACTER), params, null);

            if (response.isSuccess()) {
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                success = jsonBean.getBoolean("success");
                return success;
            }
            throw new ResponseException(response.getStatusCode());
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
    public List<Project> getTodayProjectList(String searchText, String searchColumns, int startNum,
                                             int rowCount, String status) throws ResponseException {
        ReleasableList<Project> projectList = null;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("searchWord", searchText);
            params.put("searchColumns", searchColumns);
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("status", status);
            params.put("searchType", "0");

            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_TODAY_PROJECT_LIST), params, null);
            if (response.isSuccess()) {
                projectList = new ArrayReleasableList<Project>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray array = jsonBean.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Project result = new Project();
                    Customer cust = new Customer();
                    result.setProjectID(json.getString("projectID")); //添加project ID在Project对象里。
                    cust.setProjectID(json.getString("projectID"));
                    cust.setCustName(json.getString("custName"));
                    cust.setCustMobile(json.getString("custMobile"));
                    cust.setCustOtherPhone(json.getString("custOtherPhone"));
                    cust.setHasUnexePlan(json.getString("hasUnexePlan"));
                    cust.setCustomerID(json.getString("customerID"));
                    cust.setCustFrom(json.getString("custFrom"));
                    cust.setCustFromCode(json.getString("custFromCode"));
                    cust.setCustType(json.getString("custType"));
                    cust.setCustTypeCode(json.getString("custTypeCode"));
                    cust.setCustComment(json.getString("custComment"));
                    cust.setIdNumber(json.getString("idNumber"));

                    PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                    purchaseCarIntention.setBrandCode(json.getString("brandCode"));
                    purchaseCarIntention.setBrand(json.getString("brand"));
                    purchaseCarIntention.setModelCode(json.getString("modelCode"));
                    purchaseCarIntention.setModel(json.getString("model"));
                    purchaseCarIntention.setFlowStatus(json.getString("flowStatus"));
                    purchaseCarIntention.setInsideColorCheck(Boolean.parseBoolean(String
                        .valueOf(json.getString("insideColorCheck"))));
                    purchaseCarIntention.setPreorderDate(json.getLong("preorderDate"));
                    purchaseCarIntention.setProjectComment(json.getString("projectComment"));
                    purchaseCarIntention.setOutsideColorCode(json.getString("outsideColorCode"));
                    purchaseCarIntention.setOutsideColor(json.getString("outsideColor"));
                    purchaseCarIntention.setInsideColor(json.getString("insideColor"));
                    purchaseCarIntention.setInsideColorCode(json.getString("insideColorCode"));
                    purchaseCarIntention.setCarConfiguration(json.getString("carConfiguration"));
                    purchaseCarIntention.setCarConfigurationCode(json
                        .getString("carConfigurationCode"));
                    purchaseCarIntention.setEmployeeName(json.getString("employeeName"));
                    purchaseCarIntention.setCreateDate(parsingLong(json.getString("createDate")));

                    result.setCustomer(cust);
                    result.setPurchaseCarIntention(purchaseCarIntention);
                    projectList.add(result);
                }
            }

        } catch (IOException e) {
            throw new ResponseException(e);
        } catch (JSONException e) {
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
        return projectList;
    }

    @Override
    public List<Project> getExpiredProjectList(String searchText, String searchColumns,
                                               int startNum, int rowCount) throws ResponseException {
        ReleasableList<Project> projectList = null;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("searchWord", searchText);
            params.put("searchColumns", searchColumns);
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);
            params.put("searchType", "0");

            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_EXPIRED_PROJECT_LIST), params, null);
            if (response.isSuccess()) {
                projectList = new ArrayReleasableList<Project>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONArray array = jsonBean.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    Project result = new Project();
                    Customer cust = new Customer();
                    result.setProjectID(json.getString("projectID")); //添加project ID在Project对象里。
                    cust.setProjectID(json.getString("projectID"));
                    cust.setCustName(json.getString("custName"));
                    cust.setCustMobile(json.getString("custMobile"));
                    cust.setCustOtherPhone(json.getString("custOtherPhone"));
                    cust.setHasUnexePlan(json.getString("hasUnexePlan"));
                    cust.setCustomerID(json.getString("customerID"));
                    cust.setCustFrom(json.getString("custFrom"));
                    cust.setCustFromCode(json.getString("custFromCode"));
                    cust.setCustType(json.getString("custType"));
                    cust.setCustTypeCode(json.getString("custTypeCode"));
                    cust.setCustComment(json.getString("custComment"));
                    cust.setIdNumber(json.getString("idNumber"));

                    PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                    purchaseCarIntention.setBrandCode(json.getString("brandCode"));
                    purchaseCarIntention.setBrand(json.getString("brand"));
                    purchaseCarIntention.setModelCode(json.getString("modelCode"));
                    purchaseCarIntention.setModel(json.getString("model"));
                    purchaseCarIntention.setFlowStatus(json.getString("flowStatus"));
                    purchaseCarIntention.setInsideColorCheck(Boolean.parseBoolean(String
                        .valueOf(json.getString("insideColorCheck"))));
                    purchaseCarIntention.setPreorderDate(json.getLong("preorderDate"));
                    purchaseCarIntention.setProjectComment(json.getString("projectComment"));
                    purchaseCarIntention.setOutsideColorCode(json.getString("outsideColorCode"));
                    purchaseCarIntention.setOutsideColor(json.getString("outsideColor"));
                    purchaseCarIntention.setInsideColor(json.getString("insideColor"));
                    purchaseCarIntention.setInsideColorCode(json.getString("insideColorCode"));
                    purchaseCarIntention.setCarConfiguration(json.getString("carConfiguration"));
                    purchaseCarIntention.setCarConfigurationCode(json
                        .getString("carConfigurationCode"));
                    purchaseCarIntention.setEmployeeName(json.getString("employeeName"));

                    result.setCustomer(cust);
                    result.setPurchaseCarIntention(purchaseCarIntention);
                    projectList.add(result);
                }
            }

        } catch (IOException e) {
            throw new ResponseException(e);
        } catch (JSONException e) {
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
        return projectList;
    }

    /**
     * 
     * @see com.roiland.crm.sm.core.service.ProjectAPI#getOppoFunnel(long, long)
     */
    public OppoFunnel getOppoFunnel(long startDate, long endDate) throws ResponseException {
        //解析统计图数据
        OppoFunnel oppoFunnel = null;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_SEARCH_PROJECT_FUNNEL), params, null);
            if (response.isSuccess()) {
                JSONObject jsonBean = new JSONObject(getSimpleString(response));
                JSONObject info = jsonBean.getJSONObject("info");
                //解析统计图数据
                oppoFunnel = new OppoFunnel();
                oppoFunnel.setRecordNum(parsingInt(info.getString("recordNum")));
                oppoFunnel.setRevenueNum(parsingInt(info.getString("revenueNum")));
                oppoFunnel.setWeightTotalCount(parsingIntFloat(info.getString("winNum")));
                oppoFunnel.setFirstNum(parsingInt(info.getString("firstNum")));
                oppoFunnel.setNeedNum(parsingInt(info.getString("needNum")));
                oppoFunnel.setDisplayNum(parsingInt(info.getString("displayNum")));
                oppoFunnel.setDriveNum(parsingInt(info.getString("driveNum")));
                oppoFunnel.setOrderNum(parsingInt(info.getString("orderNum")));
                oppoFunnel.setPriceNum(parsingInt(info.getString("priceNum")));
            }

        } catch (IOException e) {
            throw new ResponseException(e);
        } catch (JSONException e) {
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
        return oppoFunnel;
    }

    /** 
     * @see com.roiland.crm.sm.core.service.ProjectAPI#searchSalesOppoFunncelList(long, long)
     */
    @Override
    public List<Project> searchSalesOppoFunncelList(long startDate, long endDate, int startNum,
                                                    int rowCount) throws ResponseException {
        ReleasableList<Project> projectList = null;
        RLHttpResponse response = null;
        try {
            JSONObject params = new JSONObject();
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            params.put("startNum", startNum);
            params.put("rowCount", rowCount);

            response = getHttpClient().executePostJSON(
                getURLAddress(URLContact.METHOD_GET_SEARCH_PROJECT_FUNNEL), params, null);
            if (response.isSuccess()) {
                projectList = new ArrayReleasableList<Project>();
                JSONObject jsonBean = new JSONObject(getSimpleString(response));

                JSONArray array = jsonBean.getJSONArray("list");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Project result = new Project();
                        Customer cust = new Customer();
                        result.setProjectID(json.getString("projectID")); //添加project ID在Project对象里。
                        cust.setProjectID(json.getString("projectID"));
                        cust.setCustName(parsingString(json.get("custName")));                                            //客户名称
        
                        PurchaseCarIntention purchaseCarIntention = new PurchaseCarIntention();
                        purchaseCarIntention.setBrand(parsingString(json.get("brand")));                                   //品牌
                        purchaseCarIntention.setModel(parsingString(json.get("model")));                                   //车型
                        purchaseCarIntention.setFlowStatus(parsingString(json.get("flowStatus")));                         //流程状态
                        purchaseCarIntention.setPreorderDate(parsingLong(json.getString("provideDate")));                        //预售日期
                        purchaseCarIntention.setProjectComment(parsingString(json.get("moment")));                 //备注
                        purchaseCarIntention.setOutsideColor(parsingString(json.get("outsideColor")));                     //外饰颜色
                        purchaseCarIntention.setInsideColor(parsingString(json.get("insideColor")));                       //内饰颜色
                        purchaseCarIntention.setAbandonFlag(parsingString(json.get("abandonFlag")));                                       //成交状态
                        purchaseCarIntention.setPreorderCount(parsingString(json.get("revenue")));                                         //台数
                        purchaseCarIntention.setDealPossibility(parsingString(json.get("win")));                                           //成交可能性
                        purchaseCarIntention.setDealPriceInterval(parsingString(json.get("expect")));                                      //预计销售
                        purchaseCarIntention.setEmployeeName(parsingString(json.get("saleMan")));                          //所属销售顾问

                        result.setCustomer(cust);
                        result.setPurchaseCarIntention(purchaseCarIntention);
                        projectList.add(result);
                    }
                }
            }

        } catch (IOException e) {
            throw new ResponseException(e);
        } catch (JSONException e) {
            throw new ResponseException(e);
        } finally {
            if (response != null) {
                response.release();
            }
        }
        return projectList;
    }

}
