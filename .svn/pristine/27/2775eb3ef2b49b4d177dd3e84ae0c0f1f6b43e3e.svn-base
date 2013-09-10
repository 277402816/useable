package com.roiland.crm.sm.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 * 试乘试驾信息
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: DriveTest.java, v 0.1 2013-8-2 上午10:15:29 shuang.gao Exp $
 */
public class DriveTest implements Parcelable {
    private String projectID;        //销售线索ID
    private String driveTestID;      //试驾试乘ID
    private String driverName;       //试驾人姓名
    private String driverMobile;     //试驾人手机
    private String driverLicenseNo;  //试驾人驾驶证号
    private String driveChassisNo;   //试驾车底盘号
    private String driveLicensePlate; //试驾车牌照号
    private String driveStatusCode;  //驾驶状态代码
    private String driveStatus;      //驾驶状态
    private String driveStartTime;   //试驾开始时间
    private String driveEndTime;     //试驾结束时间
    private String driveStartKM;     //试驾起始里程
    private String driveEndKM;       //试驾结束里程
    private String driveComment;     //备注

    public DriveTest() {

    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getDriveStatusCode() {
        return driveStatusCode;
    }

    public void setDriveStatusCode(String driveStatusCode) {
        this.driveStatusCode = driveStatusCode;
    }

    public String getDriveTestID() {
        return driveTestID;
    }

    public void setDriveTestID(String driveTestID) {
        this.driveTestID = driveTestID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverLicenseNo() {
        return driverLicenseNo;
    }

    public void setDriverLicenseNo(String driverLicenseNo) {
        this.driverLicenseNo = driverLicenseNo;
    }

    public String getDriveChassisNo() {
        return driveChassisNo;
    }

    public void setDriveChassisNo(String driveChassisNo) {
        this.driveChassisNo = driveChassisNo;
    }

    public String getDriveLicensePlate() {
        return driveLicensePlate;
    }

    public void setDriveLicensePlate(String driveLicensePlate) {
        this.driveLicensePlate = driveLicensePlate;
    }

    public String getDriveStatus() {
        return driveStatus;
    }

    public void setDriveStatus(String driveStatus) {
        this.driveStatus = driveStatus;
    }

    public String getDriveStartTime() {
        return driveStartTime;
    }

    public void setDriveStartTime(String driveStartTime) {
        this.driveStartTime = driveStartTime;
    }

    public String getDriveEndTime() {
        return driveEndTime;
    }

    public void setDriveEndTime(String driveEndTime) {
        this.driveEndTime = driveEndTime;
    }

    public String getDriveStartKM() {
        return driveStartKM;
    }

    public void setDriveStartKM(String driveStartKM) {
        this.driveStartKM = driveStartKM;
    }

    public String getDriveEndKM() {
        return driveEndKM;
    }

    public void setDriveEndKM(String driveEndKM) {
        this.driveEndKM = driveEndKM;
    }

    public String getDriveComment() {
        return driveComment;
    }

    public void setDriveComment(String driveComment) {
        this.driveComment = driveComment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeString(projectID);
        arg0.writeString(driveTestID);
        arg0.writeString(driverName);
        arg0.writeString(driverMobile);
        arg0.writeString(driverLicenseNo);
        arg0.writeString(driveChassisNo);
        arg0.writeString(driveLicensePlate);
        arg0.writeString(driveStatusCode);
        arg0.writeString(driveStatus);
        arg0.writeString(driveStartTime);
        arg0.writeString(driveEndTime);
        arg0.writeString(driveStartKM);
        arg0.writeString(driveEndKM);
        arg0.writeString(driveComment);
    }

    public DriveTest(Parcel in) {
        projectID = in.readString();
        driveTestID = in.readString();
        driverName = in.readString();
        driverMobile = in.readString();
        driverLicenseNo = in.readString();
        driveChassisNo = in.readString();
        driveLicensePlate = in.readString();
        driveStatusCode = in.readString();
        driveStatus = in.readString();
        driveStartTime = in.readString();
        driveEndTime = in.readString();
        driveStartKM = in.readString();
        driveEndKM = in.readString();
        driveComment = in.readString();
    }

    public static final Parcelable.Creator<DriveTest> CREATOR = new Parcelable.Creator<DriveTest>() {
                                                                  public DriveTest createFromParcel(Parcel in) {
                                                                      return new DriveTest(in);
                                                                  }

                                                                  public DriveTest[] newArray(int size) {
                                                                      return new DriveTest[size];
                                                                  }
                                                              };
}
