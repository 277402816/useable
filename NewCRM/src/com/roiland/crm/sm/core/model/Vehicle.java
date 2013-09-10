package com.roiland.crm.sm.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 * 车辆资源信息
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: Vehicle.java, v 0.1 2013-8-2 上午10:44:44 shuang.gao Exp $
 */
public class Vehicle implements Parcelable {
    private String vehicleID        = null; //车辆信息ID
    private String chassisNo        = null; //底盘号
    private String brandCode        = null; //品牌代码
    private String brand            = null; //品牌
    private String modelCode        = null; //车型代码
    private String model            = null; //车型
    private String outsideColorCode = null; //外饰颜色代码
    private String outsideColor     = null; //外饰颜色
    private String insideColorCode  = null; //内饰颜色代码
    private String insideColor      = null; //内饰颜色
    private String vehiConfigCode   = null; //配置代码
    private String vehiConfig       = null; //配置
    private String vehiStatus       = null; //车辆状态
    private String storeStatus      = null; //车辆在库状态
    private String tagStatus        = null; //标识状态
    private String userID           = null; //所属销售顾问
    private String orderID          = null; //订单匹配编号

    public Vehicle() {
    }

    public Vehicle(Parcel in) {
        vehicleID = in.readString();
        chassisNo = in.readString();
        brandCode = in.readString();
        brand = in.readString();
        modelCode = in.readString();
        model = in.readString();
        outsideColorCode = in.readString();
        outsideColor = in.readString();
        insideColorCode = in.readString();
        insideColor = in.readString();
        vehiConfigCode = in.readString();
        vehiConfig = in.readString();
        vehiStatus = in.readString();
        storeStatus = in.readString();
        tagStatus = in.readString();
        userID = in.readString();
        orderID = in.readString();
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOutsideColorCode() {
        return outsideColorCode;
    }

    public void setOutsideColorCode(String outsideColorCode) {
        this.outsideColorCode = outsideColorCode;
    }

    public String getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(String outsideColor) {
        this.outsideColor = outsideColor;
    }

    public String getInsideColorCode() {
        return insideColorCode;
    }

    public void setInsideColorCode(String insideColorCode) {
        this.insideColorCode = insideColorCode;
    }

    public String getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(String insideColor) {
        this.insideColor = insideColor;
    }

    public String getVehiConfigCode() {
        return vehiConfigCode;
    }

    public void setVehiConfigCode(String vehiConfigCode) {
        this.vehiConfigCode = vehiConfigCode;
    }

    public String getVehiConfig() {
        return vehiConfig;
    }

    public void setVehiConfig(String vehiConfig) {
        this.vehiConfig = vehiConfig;
    }

    public String getVehiStatus() {
        return vehiStatus;
    }

    public void setVehiStatus(String vehiStatus) {
        this.vehiStatus = vehiStatus;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public String getTagStatus() {
        if (tagStatus != null && tagStatus.equals("true")) {
            tagStatus = "已匹配";
        } else {
            tagStatus = "未匹配";
        }
        return tagStatus;
    }

    public void setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vehicleID);
        dest.writeString(chassisNo);
        dest.writeString(brandCode);
        dest.writeString(brand);
        dest.writeString(modelCode);
        dest.writeString(model);
        dest.writeString(outsideColorCode);
        dest.writeString(outsideColor);
        dest.writeString(insideColorCode);
        dest.writeString(insideColor);
        dest.writeString(vehiConfigCode);
        dest.writeString(vehiConfig);
        dest.writeString(vehiStatus);
        dest.writeString(storeStatus);
        dest.writeString(tagStatus);
        dest.writeString(userID);
        dest.writeString(orderID);
    }

    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
                                                                public Vehicle createFromParcel(Parcel in) {
                                                                    return new Vehicle(in);
                                                                }

                                                                public Vehicle[] newArray(int size) {
                                                                    return new Vehicle[size];
                                                                }
                                                            };
}
