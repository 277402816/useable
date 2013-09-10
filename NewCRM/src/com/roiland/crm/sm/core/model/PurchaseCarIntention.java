package com.roiland.crm.sm.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 * 购车意向
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: PurchaseCarIntention.java, v 0.1 2013-8-2 上午10:39:26 shuang.gao Exp $
 */
public class PurchaseCarIntention implements Parcelable {
    private String  brandCode;               //品牌（车系）代码
    private String  brand;                   //品牌（车系）
    private String  modelCode;               //车型代码
    private String  model;                   //车型
    private String  outsideColorCode;        //外部颜色代码
    private String  outsideColor;            //外部颜色
    private String  insideColorCode;         //内部颜色代码
    private String  insideColor;             //内部颜色
    private String  carConfigurationCode;    //配置代码
    private String  carConfiguration;        //配置
    private String  salesQuote;              //销售报价
    private String  dealPriceIntervalCode;   //交易价格区间代码
    private String  dealPriceInterval;       //交易价格区间
    private String  paymentCode;             //付款方式代码
    private String  payment;                 //付款方式
    private String  preorderCount   = "1";   //预购台数, 默认1台
    private long    preorderDate;            //预购日期
    private long    createDate;              //建立日期
    private String  lowStatusCode;           //流程状态代码
    private String  flowStatus      = "初次接触"; //流程状态, 默认"初次接触"
    private String  dealPossibility = "0.05"; //成交可能性, 默认 0.05
    private String  purchMotivationCode;     //购买动机代码
    private String  purchMotivation;         //购买动机
    private String  chassisNo;               //底盘号
    private String  engineNo;                //发动机号
    private String  licensePlate;            //牌照号
    private String  licensePropCode;         //牌照属性代码
    private String  licenseProp;             //牌照属性
    private String  pickupDate;              //提车日期
    private String  preorderTag;             //预定标识
    private Boolean giveupTag       = false; //放弃销售机会
    private String  giveupReason;            //放弃原因
    private String  giveupReasonCode;        //放弃原因代码
    private String  invoiceTitle;            //发票名称
    private String  projectComment;          //购车备注
    private String  flowStatusCode;          //流程状态代码
    private Boolean insideColorCheck;        //内饰必选
    private boolean hasUnexePlan;            //有未执行的跟踪计划
    private boolean hasActiveOrder;          //有活动订单
    private boolean hasActiveDrive;          //有试乘试驾
    private String  orderStatus;             //订单状态
    private String  abandonFlag;             //成交状态
    private String  employeeName;            //所属销售顾问

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAbandonFlag() {
        return abandonFlag;
    }

    public void setAbandonFlag(String abandonFlag) {
        this.abandonFlag = abandonFlag;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isHasActiveOrder() {
        return hasActiveOrder;
    }

    public void setHasActiveOrder(boolean hasActiveOrder) {
        this.hasActiveOrder = hasActiveOrder;
    }

    public boolean isHasActiveDrive() {
        return hasActiveDrive;
    }

    public void setHasActiveDrive(boolean hasActiveDrive) {
        this.hasActiveDrive = hasActiveDrive;
    }

    public boolean isHasUnexePlan() {
        return hasUnexePlan;
    }

    public void setHasUnexePlan(boolean hasUnexePlan) {
        this.hasUnexePlan = hasUnexePlan;
    }

    public Boolean getInsideColorCheck() {
        return insideColorCheck;
    }

    public String getGiveupReasonCode() {
        return giveupReasonCode;
    }

    public void setGiveupReasonCode(String giveupReasonCode) {
        this.giveupReasonCode = giveupReasonCode;
    }

    public Boolean isInsideColorCheck() {
        return insideColorCheck != null ? insideColorCheck : false;
    }

    public void setInsideColorCheck(Boolean insideColorCheck) {
        this.insideColorCheck = insideColorCheck;
    }

    public PurchaseCarIntention() {

    }

    public String getFlowStatusCode() {
        return flowStatusCode;
    }

    public void setFlowStatusCode(String flowStatusCode) {
        this.flowStatusCode = (flowStatusCode);
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = (brandCode);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = (brand);
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = (modelCode);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = (model);
    }

    public String getOutsideColorCode() {
        return outsideColorCode;
    }

    public void setOutsideColorCode(String outsideColorCode) {
        this.outsideColorCode = (outsideColorCode);
    }

    public String getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(String outsideColor) {
        this.outsideColor = (outsideColor);
    }

    public String getInsideColorCode() {
        return insideColorCode;
    }

    public void setInsideColorCode(String insideColorCode) {
        this.insideColorCode = (insideColorCode);
    }

    public String getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(String insideColor) {
        this.insideColor = (insideColor);
    }

    public String getCarConfigurationCode() {
        return carConfigurationCode;
    }

    public void setCarConfigurationCode(String carConfigurationCode) {
        this.carConfigurationCode = (carConfigurationCode);
    }

    public String getCarConfiguration() {
        return carConfiguration;
    }

    public void setCarConfiguration(String carConfiguration) {
        this.carConfiguration = (carConfiguration);
    }

    public String getSalesQuote() {
        return salesQuote;
    }

    public void setSalesQuote(String salesQuote) {
        this.salesQuote = (salesQuote);
    }

    public String getDealPriceIntervalCode() {
        return dealPriceIntervalCode;
    }

    public void setDealPriceIntervalCode(String dealPriceIntervalCode) {
        this.dealPriceIntervalCode = (dealPriceIntervalCode);
    }

    public String getDealPriceInterval() {
        return dealPriceInterval;
    }

    public void setDealPriceInterval(String dealPriceInterval) {
        this.dealPriceInterval = (dealPriceInterval);
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = (paymentCode);
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = (payment);
    }

    public String getPreorderCount() {
        return preorderCount;
    }

    public void setPreorderCount(String preorderCount) {
        this.preorderCount = (preorderCount);
    }

    public long getPreorderDate() {
        return preorderDate;
    }

    public void setPreorderDate(long preorderDate) {
        this.preorderDate = preorderDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getLowStatusCode() {
        return lowStatusCode;
    }

    public void setLowStatusCode(String lowStatusCode) {
        this.lowStatusCode = (lowStatusCode);
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = (flowStatus);
    }

    public String getDealPossibility() {
        return dealPossibility;
    }

    public void setDealPossibility(String dealPossibility) {
        this.dealPossibility = (dealPossibility);
    }

    public String getPurchMotivationCode() {
        return purchMotivationCode;
    }

    public void setPurchMotivationCode(String purchMotivationCode) {
        this.purchMotivationCode = (purchMotivationCode);
    }

    public String getPurchMotivation() {
        return purchMotivation;
    }

    public void setPurchMotivation(String purchMotivation) {
        this.purchMotivation = (purchMotivation);
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = (chassisNo);
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = (engineNo);
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = (licensePlate);
    }

    public String getLicensePropCode() {
        return licensePropCode;
    }

    public void setLicensePropCode(String licensePropCode) {
        this.licensePropCode = (licensePropCode);
    }

    public String getLicenseProp() {
        return licenseProp;
    }

    public void setLicenseProp(String licenseProp) {
        this.licenseProp = (licenseProp);
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = (pickupDate);
    }

    public String getPreorderTag() {
        return preorderTag;
    }

    public void setPreorderTag(String preorderTag) {
        this.preorderTag = (preorderTag);
    }

    public String getGiveupReason() {
        return giveupReason;
    }

    public void setGiveupReason(String giveupReason) {
        this.giveupReason = (giveupReason);
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = (invoiceTitle);
    }

    public String getProjectComment() {
        return projectComment;
    }

    public void setProjectComment(String projectComment) {
        this.projectComment = (projectComment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandCode); //品牌（车系）代码
        dest.writeString(brand); //品牌（车系）
        dest.writeString(modelCode); //车型代码
        dest.writeString(model); //车型
        dest.writeString(outsideColorCode); //外部颜色代码
        dest.writeString(outsideColor); //外部颜色
        dest.writeString(insideColorCode); //内部颜色代码
        dest.writeString(insideColor); //内部颜色
        dest.writeString(carConfigurationCode); //配置代码
        dest.writeString(carConfiguration); //配置
        dest.writeString(salesQuote); //销售报价
        dest.writeString(dealPriceIntervalCode); //交易价格区间代码
        dest.writeString(dealPriceInterval); //交易价格区间
        dest.writeString(paymentCode); //付款方式代码
        dest.writeString(payment); //付款方式
        dest.writeString(preorderCount); //预购台数
        dest.writeLong(preorderDate); //预购日期
        dest.writeLong(createDate); //建立日期
        dest.writeString(lowStatusCode); //流程状态代码
        dest.writeString(flowStatus); //流程状态
        dest.writeString(dealPossibility); //成交可能性
        dest.writeString(purchMotivationCode); //购买动机代码
        dest.writeString(purchMotivation); //购买动机
        dest.writeString(chassisNo); //底盘号
        dest.writeString(engineNo); //发动机号
        dest.writeString(licensePlate); //牌照号
        dest.writeString(licensePropCode); //牌照属性代码
        dest.writeString(licenseProp); //牌照属性
        dest.writeString(pickupDate); //提车日期
        dest.writeString(preorderTag); //预定标识，放弃销售机会
        dest.writeValue(giveupTag);
        dest.writeString(giveupReason); //放弃原因
        dest.writeString(invoiceTitle); //发票名称
        dest.writeString(projectComment); //购车备注
        dest.writeString(flowStatusCode); //流程状态代码
        dest.writeValue(insideColorCheck);
        dest.writeString(orderStatus);
        dest.writeString(abandonFlag); //成交状态
        boolean[] flagArray = { hasUnexePlan, hasActiveOrder, hasActiveDrive };
        dest.writeBooleanArray(flagArray);
        dest.writeString(employeeName);
    }

    public PurchaseCarIntention(Parcel in) {

        brandCode = in.readString();
        brand = in.readString();
        modelCode = in.readString();
        model = in.readString();
        outsideColorCode = in.readString();
        outsideColor = in.readString();
        insideColorCode = in.readString();
        insideColor = in.readString();
        carConfigurationCode = in.readString();
        carConfiguration = in.readString();
        salesQuote = in.readString();
        dealPriceIntervalCode = in.readString();
        dealPriceInterval = in.readString();
        paymentCode = in.readString();
        payment = in.readString();
        preorderCount = in.readString();
        preorderDate = in.readLong();
        createDate = in.readLong();
        lowStatusCode = in.readString();
        flowStatus = in.readString();
        dealPossibility = in.readString();
        purchMotivationCode = in.readString();
        purchMotivation = in.readString();
        chassisNo = in.readString();
        engineNo = in.readString();
        licensePlate = in.readString();
        licensePropCode = in.readString();
        licenseProp = in.readString();
        pickupDate = in.readString();
        preorderTag = in.readString();
        giveupTag = (Boolean) in.readValue(Boolean.class.getClassLoader());
        giveupReason = in.readString();
        invoiceTitle = in.readString();
        projectComment = in.readString();
        flowStatusCode = in.readString();
        insideColorCheck = (Boolean) in.readValue(Boolean.class.getClassLoader());
        orderStatus = in.readString();
        abandonFlag = in.readString();
        boolean[] booleanArray = new boolean[3];
        in.readBooleanArray(booleanArray);
        hasUnexePlan = booleanArray[0];
        hasActiveOrder = booleanArray[1];
        hasActiveDrive = booleanArray[2];
        employeeName = in.readString();
    }

    public static final Parcelable.Creator<PurchaseCarIntention> CREATOR = new Parcelable.Creator<PurchaseCarIntention>() {
                                                                             public PurchaseCarIntention createFromParcel(Parcel in) {
                                                                                 return new PurchaseCarIntention(
                                                                                     in);
                                                                             }

                                                                             public PurchaseCarIntention[] newArray(int size) {
                                                                                 return new PurchaseCarIntention[size];
                                                                             }
                                                                         };

    public Boolean isGiveupTag() {
        return giveupTag;
    }

    public void setGiveupTag(Boolean giveupTag) {
        this.giveupTag = giveupTag;
    }
}
