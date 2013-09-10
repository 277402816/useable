package com.roiland.crm.sm.core.model;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roiland.crm.sm.utils.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 * 客戶信息
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: Customer.java, v 0.1 2013-8-2 上午10:12:30 shuang.gao Exp $
 */
public class Customer implements Parcelable {
    private String  customerID            = null; // 客户ID
    private String  custName              = null; // 客户名称
    private String  custFromCode          = null; // 客户来源代码
    private String  custFrom              = null; // 客户来源
    private String  custTypeCode          = null; // 客户类别代码
    private String  custType              = null; // 客户类别
    private String  infoFromCode          = null; // 信息来源代码
    private String  infoFrom              = null; // 信息来源
    private String  collectFromCode       = null; // 集客方式代码
    private String  collectFrom           = null; // 集客方式
    private String  custMobile            = null; // 客户移动电话
    private String  custOtherPhone        = null; // 客户其他电话
    private String  genderCode            = null; // 性别代码
    private String  gender                = null; // 性别
    private String  birthday              = null; // 生日
    private String  idTypeCode            = null; // 证件类型代码
    private String  idType                = null; // 证件类型
    private String  idNumber              = null; // 证件号码
    private String  provinceCode          = null; // 省区代码
    private String  province              = null; // 省区
    private String  cityCode              = null; // 市县代码
    private String  city                  = null; // 市县
    private String  districtCode          = null; // 行政区划代码
    private String  district              = null; // 行政区划
    private String  qq                    = null; // QQ号码
    private String  address               = null; // 客户地址
    private String  postcode              = null; // 邮编
    private String  email                 = null; // E-mail
    private String  convContactTime       = null; // 方便联系时间
    private String  expectContactWayCode  = null; // 希望联系方式代码
    private String  expectContactWay      = null; // 希望联系方式
    private String  fax                   = null; // 传真
    private String  existingCarCode       = null; // 现用车代码
    private String  existingCar           = null; // 现用车
    private String  existingCarBrand      = null; // 现用车品牌
    private String  industryCode          = null; // 所处行业代码
    private String  industry              = null; // 所处行业
    private String  positionCode          = null; // 职务代码
    private String  position              = null; // 职务
    private String  educationCode         = null; // 教育程度代码
    private String  education             = null; // 教育程度
    private String  custInterestCode1     = null; // 客户爱好1代码
    private String  custInterest1         = null; // 客户爱好1
    private String  custInterestCode2     = null; // 客户爱好2代码
    private String  custInterest2         = null; // 客户爱好2
    private String  custInterestCode3     = null; // 客户爱好3代码
    private String  custInterest3         = null; // 客户爱好3
    private String  existLisenPlate       = null; // 现用车牌照号
    private String  enterpTypeCode        = null; // 企业性质代码
    private String  enterpType            = null; // 企业性质
    private String  enterpPeopleCountCode = null; // 企业人数代码
    private String  enterpPeopleCount     = null; // 企业人数, String
    private String  registeredCapitalCode = null; // 注册资本代码
    private String  registeredCapital     = null; // 注册资本, String
    private String  compeCarModelCode     = null; // 竞争车型代码
    private String  compeCarModel         = null; // 竞争车型, String
    private Boolean rebuyStoreCustTag     = null; // 本店重购客户, Boolean
    private Boolean rebuyOnlineCustTag    = null; // 网络重购客户, Boolean
    private Boolean changeCustTag         = null; // 置换客户标识, Boolean
    private Boolean loanCustTag           = null; // 个贷客户标识, Boolean
    private Boolean headerQuartCustTag    = null; // 总部VIP客户标识, Boolean
    private Boolean regularCustTag        = null; // 老客户推荐标识, Boolean
    private String  regularCustCode       = null; // 老客户选择代码
    private String  regularCust           = null; // 老客户选择
    private String  regularCustContacter  = null; // 老客户联系人
    private Boolean bigCustTag            = null; // 大客户标识 Boolean
    private String  bigCustsCode          = null; // 大客户选择代码
    private String  bigCusts              = null; // 大客户选择
    private String  custComment           = null; // 客户备注

    private String  hasUnexePlan          = null; // 是否存在未执行的跟踪计划
    private String  convContactTimeCode   = null;  //方便联系时间代码

    private String  projectID             = null; // 销售线索ID
    private Boolean dormancy;
    private boolean updateCustInfo;               //是否更新客户信息
    private String  custStatus;                   //追加客户状态字段, 李春吉修改。

    private boolean abandonProject        = false;
    private String  ownerName             = null; //所属销售顾问
    private long    createDate;                    //客户管理建立日期

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Customer() {

    }

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    public boolean isUpdateCustInfo() {
        return updateCustInfo;
    }

    public void setUpdateCustInfo(boolean updateCustInfo) {
        this.updateCustInfo = updateCustInfo;
    }

    public String getRegularCustContacter() {
        return regularCustContacter;
    }

    public void setRegularCustContacter(String regularCustContacter) {
        this.regularCustContacter = regularCustContacter;
    }

    public String getHasUnexePlan() {
        return hasUnexePlan;
    }

    public void setHasUnexePlan(String hasUnexePlan) {
        this.hasUnexePlan = hasUnexePlan;
    }

    public String getConvContactTimeCode() {
        return convContactTimeCode;
    }

    public void setConvContactTimeCode(String convContactTimeCode) {
        this.convContactTimeCode = convContactTimeCode;
    }

    public String getExistingCar() {
        return existingCar;
    }

    public void setExistingCar(String existingCar) {
        this.existingCar = existingCar;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustFromCode() {
        return custFromCode;
    }

    public void setCustFromCode(String custFromCode) {
        this.custFromCode = custFromCode;
    }

    public String getCustFrom() {
        return custFrom;
    }

    public void setCustFrom(String custFrom) {
        this.custFrom = custFrom;
    }

    public String getCustTypeCode() {
        return custTypeCode;
    }

    public void setCustTypeCode(String custTypeCode) {
        this.custTypeCode = custTypeCode;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getInfoFromCode() {
        return infoFromCode;
    }

    public void setInfoFromCode(String infoFromCode) {
        this.infoFromCode = infoFromCode;
    }

    public String getInfoFrom() {
        return infoFrom;
    }

    public void setInfoFrom(String infoFrom) {
        this.infoFrom = infoFrom;
    }

    public String getCollectFromCode() {
        return collectFromCode;
    }

    public void setCollectFromCode(String collectFromCode) {
        this.collectFromCode = collectFromCode;
    }

    public String getCollectFrom() {
        return collectFrom;
    }

    public void setCollectFrom(String collectFrom) {
        this.collectFrom = collectFrom;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getCustOtherPhone() {
        return custOtherPhone;
    }

    public void setCustOtherPhone(String custOtherPhone) {
        this.custOtherPhone = custOtherPhone;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConvContactTime() {
        return convContactTime;
    }

    public void setConvContactTime(String convContactTime) {
        this.convContactTime = convContactTime;
    }

    public String getExpectContactWayCode() {
        return expectContactWayCode;
    }

    public void setExpectContactWayCode(String expectContactWayCode) {
        this.expectContactWayCode = expectContactWayCode;
    }

    public String getExpectContactWay() {
        return expectContactWay;
    }

    public void setExpectContactWay(String expectContactWay) {
        this.expectContactWay = expectContactWay;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getExistingCarCode() {
        return existingCarCode;
    }

    public void setExistingCarCode(String existingCarCode) {
        this.existingCarCode = StringUtils.trimNull(existingCarCode);
    }

    public String getExistingCarBrand() {
        return existingCarBrand;
    }

    public void setExistingCarBrand(String existingCarBrand) {
        this.existingCarBrand = StringUtils.trimNull(existingCarBrand);
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = StringUtils.trimNull(industryCode);
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = StringUtils.trimNull(positionCode);
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = StringUtils.trimNull(educationCode);
    }

    public String getCustInterestCode1() {
        return custInterestCode1;
    }

    public void setCustInterestCode1(String custInterestCode1) {
        this.custInterestCode1 = StringUtils.trimNull(custInterestCode1);
    }

    public String getCustInterest1() {
        return custInterest1;
    }

    public void setCustInterest1(String custInterest1) {
        this.custInterest1 = StringUtils.trimNull(custInterest1);
    }

    public String getCustInterestCode2() {
        return custInterestCode2;
    }

    public void setCustInterestCode2(String custInterestCode2) {
        this.custInterestCode2 = StringUtils.trimNull(custInterestCode2);
    }

    public String getCustInterest2() {
        return custInterest2;
    }

    public void setCustInterest2(String custInterest2) {
        this.custInterest2 = StringUtils.trimNull(custInterest2);
    }

    public String getCustInterestCode3() {
        return custInterestCode3;
    }

    public void setCustInterestCode3(String custInterestCode3) {
        this.custInterestCode3 = StringUtils.trimNull(custInterestCode3);
    }

    public String getCustInterest3() {
        return custInterest3;
    }

    public void setCustInterest3(String custInterest3) {
        this.custInterest3 = StringUtils.trimNull(custInterest3);
    }

    public String getExistLisenPlate() {
        return existLisenPlate;
    }

    public void setExistLisenPlate(String existLisenPlate) {
        this.existLisenPlate = StringUtils.trimNull(existLisenPlate);
    }

    public String getEnterpTypeCode() {
        return enterpTypeCode;
    }

    public void setEnterpTypeCode(String enterpTypeCode) {
        this.enterpTypeCode = StringUtils.trimNull(enterpTypeCode);
    }

    public String getEnterpType() {
        return enterpType;
    }

    public void setEnterpType(String enterpType) {
        this.enterpType = StringUtils.trimNull(enterpType);
    }

    public String getEnterpPeopleCountCode() {
        return enterpPeopleCountCode;
    }

    public void setEnterpPeopleCountCode(String enterpPeopleCountCode) {
        this.enterpPeopleCountCode = StringUtils.trimNull(enterpPeopleCountCode);
    }

    public String getEnterpPeopleCount() {
        return enterpPeopleCount;
    }

    public void setEnterpPeopleCount(String enterpPeopleCount) {
        this.enterpPeopleCount = StringUtils.trimNull(enterpPeopleCount);
    }

    public String getRegisteredCapitalCode() {
        return registeredCapitalCode;
    }

    public void setRegisteredCapitalCode(String registeredCapitalCode) {
        this.registeredCapitalCode = StringUtils.trimNull(registeredCapitalCode);
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = StringUtils.trimNull(registeredCapital);
    }

    public String getCompeCarModelCode() {
        return compeCarModelCode;
    }

    public void setCompeCarModelCode(String compeCarModelCode) {
        this.compeCarModelCode = StringUtils.trimNull(compeCarModelCode);
    }

    public String getCompeCarModel() {
        return compeCarModel;
    }

    public void setCompeCarModel(String compeCarModel) {
        this.compeCarModel = StringUtils.trimNull(compeCarModel);
    }

    public Boolean getRebuyStoreCustTag() {
        return rebuyStoreCustTag;
    }

    public void setRebuyStoreCustTag(Boolean rebuyStoreCustTag) {
        this.rebuyStoreCustTag = rebuyStoreCustTag;
    }

    public Boolean getRebuyOnlineCustTag() {
        return rebuyOnlineCustTag;
    }

    public void setRebuyOnlineCustTag(Boolean rebuyOnlineCustTag) {
        this.rebuyOnlineCustTag = rebuyOnlineCustTag;
    }

    public Boolean getChangeCustTag() {
        return changeCustTag;
    }

    public void setChangeCustTag(Boolean changeCustTag) {
        this.changeCustTag = changeCustTag;
    }

    public Boolean getLoanCustTag() {
        return loanCustTag;
    }

    public void setLoanCustTag(Boolean loanCustTag) {
        this.loanCustTag = loanCustTag;
    }

    public Boolean getHeaderQuartCustTag() {
        return headerQuartCustTag;
    }

    public void setHeaderQuartCustTag(Boolean headerQuartCustTag) {
        this.headerQuartCustTag = headerQuartCustTag;
    }

    public Boolean getRegularCustTag() {
        return regularCustTag;
    }

    public void setRegularCustTag(Boolean regularCustTag) {
        this.regularCustTag = regularCustTag;
    }

    public String getRegularCustCode() {
        return regularCustCode;
    }

    public void setRegularCustCode(String regularCustCode) {
        this.regularCustCode = StringUtils.trimNull(regularCustCode);
    }

    public String getRegularCust() {
        return regularCust;
    }

    public void setRegularCust(String regularCust) {
        this.regularCust = regularCust;
    }

    public Boolean getBigCustTag() {
        return bigCustTag;
    }

    public void setBigCustTag(Boolean bigCustTag) {
        this.bigCustTag = bigCustTag;
    }

    public String getBigCustsCode() {
        return bigCustsCode;
    }

    public void setBigCustsCode(String bigCustsCode) {
        this.bigCustsCode = StringUtils.trimNull(bigCustsCode);
    }

    public String getBigCusts() {
        return bigCusts;
    }

    public void setBigCusts(String bigCusts) {
        this.bigCusts = StringUtils.trimNull(bigCusts);
    }

    public String getCustComment() {
        return custComment;
    }

    public void setCustComment(String custComment) {
        this.custComment = StringUtils.trimNull(custComment);
    }

    public Boolean getDormancy() {
        return dormancy;
    }

    public void setDormancy(Boolean dormancy) {
        this.dormancy = dormancy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public Map<String, Object> createKeyValueMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerID", customerID);
        map.put("custName", custName);
        map.put("custFromCode", custFromCode);
        map.put("custFrom", custFrom);
        map.put("custTypeCode", custTypeCode);
        map.put("custType", custType);
        map.put("infoFromCode", infoFromCode);
        map.put("infoFrom", infoFrom);
        map.put("collectFromCode", collectFromCode);
        map.put("collectFrom", collectFrom);
        map.put("custMobile", custMobile);
        map.put("custOtherPhone", custOtherPhone);
        map.put("genderCode", genderCode);
        map.put("gender", gender);
        map.put("birthday", birthday);
        map.put("idTypeCode", idTypeCode);
        map.put("idType", idType);
        map.put("idNumber", idNumber);
        map.put("provinceCode", provinceCode);
        map.put("province", province);
        map.put("cityCode", cityCode);
        map.put("city", city);
        map.put("districtCode", districtCode);
        map.put("district", district);
        map.put("qq", qq);
        map.put("address", address);
        map.put("postcode", postcode);
        map.put("email", email);
        map.put("convContactTime", convContactTime);
        map.put("expectContactWayCode", expectContactWayCode);
        map.put("expectContactWay", expectContactWay);
        map.put("fax", fax);
        map.put("existingCarCode", existingCarCode);
        map.put("existingCar", existingCar);
        map.put("existingCarBrand", existingCarBrand);
        map.put("industryCode", industryCode);
        map.put("positionCode", positionCode);
        map.put("educationCode", educationCode);
        map.put("industry", industry);
        map.put("position", position);
        map.put("education", education);
        map.put("custInterestCode1", custInterestCode1);
        map.put("custInterest1", custInterest1);
        map.put("custInterestCode2", custInterestCode2);
        map.put("custInterest2", custInterest2);
        map.put("custInterestCode3", custInterestCode3);
        map.put("custInterest3", custInterest3);
        map.put("existLisenPlate", existLisenPlate);
        map.put("enterpTypeCode", enterpTypeCode);
        map.put("enterpType", enterpType);
        map.put("enterpPeopleCountCode", enterpPeopleCountCode);
        map.put("enterpPeopleCount", enterpPeopleCount);
        map.put("registeredCapitalCode", registeredCapitalCode);
        map.put("registeredCapital", registeredCapital);
        map.put("compeCarModelCode", compeCarModelCode);
        map.put("compeCarModel", compeCarModel);
        map.put("rebuyStoreCustTag", rebuyStoreCustTag);
        map.put("rebuyOnlineCustTag", rebuyOnlineCustTag);
        map.put("changeCustTag", changeCustTag);
        map.put("loanCustTag", loanCustTag);
        map.put("headerQuartCustTag", headerQuartCustTag);
        map.put("regularCustTag", regularCustTag);
        map.put("regularCustCode", regularCustCode);
        map.put("regularCust", regularCust);
        map.put("bigCustTag", bigCustTag);
        map.put("bigCustsCode", bigCustsCode);
        map.put("bigCusts", bigCusts);
        map.put("custComment", custComment);
        map.put("ownerName", ownerName);
        map.put("dormancy", dormancy);
        map.put("custStatus", custStatus);

        return map;
    }

    public static Customer createCustomerObject(Map<String, Object> map) {
        Customer customer = new Customer();

        customer.customerID = (String) map.get("customerID");
        customer.custName = (String) map.get("custName");
        customer.custFromCode = (String) map.get("custFromCode");
        customer.custFrom = (String) map.get("custFrom");
        customer.custTypeCode = (String) map.get("custTypeCode");
        customer.custType = (String) map.get("custType");
        customer.infoFromCode = (String) map.get("infoFromCode");
        customer.infoFrom = (String) map.get("infoFrom");
        customer.collectFromCode = (String) map.get("collectFromCode");
        customer.collectFrom = (String) map.get("collectFrom");
        customer.custMobile = (String) map.get("custMobile");
        customer.custOtherPhone = (String) map.get("custOtherPhone");
        customer.genderCode = (String) map.get("genderCode");
        customer.gender = (String) map.get("gender");
        customer.birthday = (String) map.get("birthday");
        customer.idTypeCode = (String) map.get("idTypeCode");
        customer.idType = (String) map.get("idType");
        customer.idNumber = (String) map.get("idNumber");
        customer.provinceCode = (String) map.get("provinceCode");
        customer.province = (String) map.get("province");
        customer.cityCode = (String) map.get("cityCode");
        customer.city = (String) map.get("city");
        customer.districtCode = (String) map.get("districtCode");
        customer.district = (String) map.get("district");
        customer.qq = (String) map.get("qq");
        customer.address = (String) map.get("address");
        customer.postcode = (String) map.get("postcode");
        customer.email = (String) map.get("email");
        customer.convContactTime = (String) map.get("convContactTime");
        customer.expectContactWayCode = (String) map.get("expectContactWayCode");
        customer.expectContactWay = (String) map.get("expectContactWay");
        customer.fax = (String) map.get("fax");
        customer.existingCarCode = (String) map.get("existingCarCode");
        customer.existingCar = (String) map.get("existingCar");
        customer.existingCarBrand = (String) map.get("existingCarBrand");
        customer.industryCode = (String) map.get("industryCode");
        customer.positionCode = (String) map.get("positionCode");
        customer.educationCode = (String) map.get("educationCode");
        customer.industry = (String) map.get("industry");
        customer.position = (String) map.get("position");
        customer.education = (String) map.get("education");
        customer.custInterestCode1 = (String) map.get("custInterestCode1");
        customer.custInterest1 = (String) map.get("custInterest1");
        customer.custInterestCode2 = (String) map.get("custInterestCode2");
        customer.custInterest2 = (String) map.get("custInterest2");
        customer.custInterestCode3 = (String) map.get("custInterestCode3");
        customer.custInterest3 = (String) map.get("custInterest3");
        customer.existLisenPlate = (String) map.get("existLisenPlate");
        customer.enterpTypeCode = (String) map.get("enterpTypeCode");
        customer.enterpType = (String) map.get("enterpType");
        customer.enterpPeopleCountCode = (String) map.get("enterpPeopleCountCode");
        customer.enterpPeopleCount = (String) map.get("enterpPeopleCount");
        customer.registeredCapitalCode = (String) map.get("registeredCapitalCode");
        customer.registeredCapital = (String) map.get("registeredCapital");
        customer.compeCarModelCode = (String) map.get("compeCarModelCode");
        customer.compeCarModel = (String) map.get("compeCarModel");
        customer.rebuyStoreCustTag = (Boolean) map.get("rebuyStoreCustTag");
        customer.rebuyOnlineCustTag = (Boolean) map.get("rebuyOnlineCustTag");
        customer.changeCustTag = (Boolean) map.get("changeCustTag");
        customer.loanCustTag = (Boolean) map.get("loanCustTag");
        customer.headerQuartCustTag = (Boolean) map.get("headerQuartCustTag");
        customer.regularCustTag = (Boolean) map.get("regularCustTag");
        customer.regularCustCode = (String) map.get("regularCustCode");
        customer.regularCust = (String) map.get("regularCust");
        customer.bigCustTag = (Boolean) map.get("bigCustTag");
        customer.bigCustsCode = (String) map.get("bigCustsCode");
        customer.bigCusts = (String) map.get("bigCusts");
        customer.custComment = (String) map.get("custComment");
        customer.ownerName = (String) map.get("ownerName");
        customer.dormancy = (Boolean) map.get("dormancy");
        customer.custStatus = (String) map.get("custStatus");

        return customer;
    }

    public static List<Map<String, Object>> createMapList(List<Customer> customerList) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        for (Customer customer : customerList) {
            mapList.add(customer.createKeyValueMap());
        }
        return mapList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerID); // 客户ID
        dest.writeString(custName); // 客户名称
        dest.writeString(custFromCode); // 客户来源代码
        dest.writeString(custFrom); // 客户来源
        dest.writeString(custTypeCode); // 客户类别代码
        dest.writeString(custType); // 客户类别
        dest.writeString(infoFromCode); // 信息来源代码
        dest.writeString(infoFrom); // 信息来源
        dest.writeString(collectFromCode); // 集客方式代码
        dest.writeString(collectFrom); // 集客方式
        dest.writeString(custMobile); // 客户移动电话
        dest.writeString(custOtherPhone); // 客户其他电话
        dest.writeString(genderCode); // 性别代码
        dest.writeString(gender); // 性别
        dest.writeString(birthday); // 生日
        dest.writeString(idTypeCode); // 证件类型代码
        dest.writeString(idType); // 证件类型
        dest.writeString(idNumber); // 证件号码
        dest.writeString(provinceCode); // 省区代码
        dest.writeString(province); // 省区
        dest.writeString(cityCode); // 市县代码
        dest.writeString(city); // 市县
        dest.writeString(districtCode); // 行政区划代码
        dest.writeString(district); // 行政区划
        dest.writeString(qq); // QQ号码
        dest.writeString(address); // 客户地址
        dest.writeString(postcode); // 邮编
        dest.writeString(email); // E-mail
        dest.writeString(convContactTime); // 方便联系时间
        dest.writeString(expectContactWayCode); // 希望联系方式代码
        dest.writeString(expectContactWay); // 希望联系方式
        dest.writeString(fax); // 传真
        dest.writeString(existingCarCode); // 现用车代码
        dest.writeString(existingCarBrand); // 现用车品牌
        dest.writeString(industryCode); // 所处行业代码
        dest.writeString(positionCode); // 职务代码
        dest.writeString(educationCode); // 教育程度代码
        dest.writeString(existingCar); // 现用车
        dest.writeString(industry); // 所处行业
        dest.writeString(position); // 职务
        dest.writeString(education); // 教育程度
        dest.writeString(custInterestCode1); // 客户爱好1代码
        dest.writeString(custInterest1); // 客户爱好1
        dest.writeString(custInterestCode2); // 客户爱好2代码
        dest.writeString(custInterest2); // 客户爱好2
        dest.writeString(custInterestCode3); // 客户爱好3代码
        dest.writeString(custInterest3); // 客户爱好3
        dest.writeString(existLisenPlate); // 现用车牌照号
        dest.writeString(enterpTypeCode); // 企业性质代码
        dest.writeString(enterpType); // 企业性质
        dest.writeString(enterpPeopleCountCode); // 企业人数代码
        dest.writeString(enterpPeopleCount); // 企业人数, String
        dest.writeString(registeredCapitalCode); // 注册资本代码
        dest.writeString(registeredCapital); // 注册资本, String
        dest.writeString(compeCarModelCode); // 竞争车型代码
        dest.writeString(compeCarModel); // 竞争车型, String
        dest.writeValue(rebuyStoreCustTag); // 本店重购客户, Boolean
        dest.writeValue(rebuyOnlineCustTag); // 网络重购客户, Boolean
        dest.writeValue(changeCustTag); // 置换客户标识, Boolean
        dest.writeValue(loanCustTag); // 个贷客户标识, Boolean
        dest.writeValue(headerQuartCustTag); // 总部VIP客户标识, Boolean
        dest.writeValue(regularCustTag); // 老客户推荐标识, Boolean
        dest.writeString(regularCustCode); // 老客户选择代码
        dest.writeString(regularCust); // 老客户选择
        dest.writeValue(bigCustTag); // 大客户标识 Boolean
        dest.writeString(bigCustsCode); // 大客户选择代码
        dest.writeString(bigCusts); // 大客户选择
        dest.writeString(custComment); // 客户备注
        dest.writeString(hasUnexePlan); // 是否存在未执行的跟踪计划
        dest.writeString(convContactTimeCode);//方便联系时间代码
        dest.writeString(projectID); // 销售线索ID
        dest.writeString(ownerName);
        dest.writeLong(createDate); //建立日期
        dest.writeValue(dormancy);
        dest.writeString(custStatus);

    }

    public Customer(Parcel in) {

        customerID = in.readString();
        custName = in.readString();
        custFromCode = in.readString();
        custFrom = in.readString();
        custTypeCode = in.readString();
        custType = in.readString();
        infoFromCode = in.readString();
        infoFrom = in.readString();
        collectFromCode = in.readString();
        collectFrom = in.readString();
        custMobile = in.readString();
        custOtherPhone = in.readString();
        genderCode = in.readString();
        gender = in.readString();
        birthday = in.readString();
        idTypeCode = in.readString();
        idType = in.readString();
        idNumber = in.readString();
        provinceCode = in.readString();
        province = in.readString();
        cityCode = in.readString();
        city = in.readString();
        districtCode = in.readString();
        district = in.readString();
        qq = in.readString();
        address = in.readString();
        postcode = in.readString();
        email = in.readString();
        convContactTime = in.readString();
        expectContactWayCode = in.readString();
        expectContactWay = in.readString();
        fax = in.readString();
        existingCarCode = in.readString();
        existingCarBrand = in.readString();
        industryCode = in.readString();
        positionCode = in.readString();
        educationCode = in.readString();
        existingCar = in.readString();
        industry = in.readString();
        position = in.readString();
        education = in.readString();
        custInterestCode1 = in.readString();
        custInterest1 = in.readString();
        custInterestCode2 = in.readString();
        custInterest2 = in.readString();
        custInterestCode3 = in.readString();
        custInterest3 = in.readString();
        existLisenPlate = in.readString();
        enterpTypeCode = in.readString();
        enterpType = in.readString();
        enterpPeopleCountCode = in.readString();
        enterpPeopleCount = in.readString();
        registeredCapitalCode = in.readString();
        registeredCapital = in.readString();
        compeCarModelCode = in.readString();
        compeCarModel = in.readString();
        rebuyStoreCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 本店重购客户, Boolean
        rebuyOnlineCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 网络重购客户, Boolean
        changeCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 置换客户标识, Boolean
        loanCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 个贷客户标识, Boolean
        headerQuartCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 总部VIP客户标识, Boolean
        regularCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 老客户推荐标识, Boolean
        regularCustCode = in.readString();
        regularCust = in.readString();
        bigCustTag = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 大客户标识 Boolean
        bigCustsCode = in.readString();
        bigCusts = in.readString();
        custComment = in.readString();
        hasUnexePlan = in.readString();
        convContactTimeCode = in.readString();
        projectID = in.readString();
        ownerName = in.readString();
        createDate = in.readLong();
        dormancy = (Boolean) in.readValue(Boolean.class.getClassLoader());
        custStatus = in.readString();
    }

    public boolean isAbandonProject() {
        return abandonProject;
    }

    public void setAbandonProject(boolean abandonProject) {
        this.abandonProject = abandonProject;
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
                                                                 public Customer createFromParcel(Parcel in) {
                                                                     return new Customer(in);
                                                                 }

                                                                 public Customer[] newArray(int size) {
                                                                     return new Customer[size];
                                                                 }
                                                             };
}
