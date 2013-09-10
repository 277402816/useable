package com.roiland.crm.sm.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 *  联系人信息
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: Contacter.java, v 0.1 2013-8-2 上午10:11:35 shuang.gao Exp $
 */
public class Contacter implements Parcelable {
    private String  contacterID;     // 联系人
    private String  projectID;       // 销售线索ID
    private String  customerID;      // 客户ID
    private String  contName;        // 联系人名称
    private String  contMobile;      // 联系人手机
    private String  contOtherPhone;  // 联系人其他电话
    private String  isPrimContanter; // 是否为主联系人
    private String  contGenderCode;  // 性别代码
    private String  contGender;      // 性别
    private String  contBirthday;    // 生日
    private String  idNumber;        // 身份证号码
    private String  ageScopeCode;    // 年龄范围代码
    private String  ageScope;        // 年龄范围
    private String  contTypeCode;    // 联系人类型代码
    private String  contType;        // 联系人类型
    private String  contRelationCode; // 与客户关系代码
    private String  contRelation;    // 与客户关系
    private Long    licenseValid;    // 驾驶证有效期
    private Boolean isSussece;       // 是否创建成功flag

    public Contacter() {
        super();
    }

    public Contacter(Parcel in) {
        contacterID = in.readString(); // 联系人
        projectID = in.readString(); // 销售线索ID
        customerID = in.readString(); // 客户ID
        contName = in.readString(); // 联系人名称
        contMobile = in.readString(); // 联系人手机
        contOtherPhone = in.readString(); // 联系人其他电话
        isPrimContanter = in.readString(); // 是否为主联系人
        contGenderCode = in.readString(); // 性别代码
        contGender = in.readString(); // 性别
        contBirthday = in.readString(); // 生日
        idNumber = in.readString(); // 身份证号码
        ageScopeCode = in.readString(); // 年龄范围代码
        ageScope = in.readString(); // 年龄范围
        contTypeCode = in.readString(); // 联系人类型代码
        contType = in.readString(); // 联系人类型
        contRelationCode = in.readString(); // 与客户关系代码
        contRelation = in.readString(); // 与客户关系
        licenseValid = (Long) in.readValue(Long.class.getClassLoader()); // 驾驶证有效期
        isSussece = (Boolean) in.readValue(Boolean.class.getClassLoader()); // 是否创建成功flag
    }

    public Boolean getIsSussece() {
        return isSussece;
    }

    public void setIsSussece(Boolean isSussece) {
        this.isSussece = isSussece;
    }

    public String getContacterID() {
        return contacterID;
    }

    public void setContacterID(String contacterID) {
        this.contacterID = contacterID;
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

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getContMobile() {
        return contMobile;
    }

    public void setContMobile(String contMobile) {
        this.contMobile = contMobile;
    }

    public String getContOtherPhone() {
        return contOtherPhone;
    }

    public void setContOtherPhone(String contOtherPhone) {
        this.contOtherPhone = contOtherPhone;
    }

    public String getIsPrimContanter() {
        return isPrimContanter;
    }

    public void setIsPrimContanter(String isPrimContanter) {
        this.isPrimContanter = isPrimContanter;
    }

    public String getContGenderCode() {
        return contGenderCode;
    }

    public void setContGenderCode(String contGenderCode) {
        this.contGenderCode = contGenderCode;
    }

    public String getContGender() {
        return contGender;
    }

    public void setContGender(String contGender) {
        this.contGender = contGender;
    }

    public String getContBirthday() {
        return contBirthday;
    }

    public void setContBirthday(String contBirthday) {
        this.contBirthday = contBirthday;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAgeScopeCode() {
        return ageScopeCode;
    }

    public void setAgeScopeCode(String ageScopeCode) {
        this.ageScopeCode = ageScopeCode;
    }

    public String getAgeScope() {
        return ageScope;
    }

    public void setAgeScope(String ageScope) {
        this.ageScope = ageScope;
    }

    public String getContTypeCode() {
        return contTypeCode;
    }

    public void setContTypeCode(String contTypeCode) {
        this.contTypeCode = contTypeCode;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getContRelationCode() {
        return contRelationCode;
    }

    public void setContRelationCode(String contRelationCode) {
        this.contRelationCode = contRelationCode;
    }

    public String getContRelation() {
        return contRelation;
    }

    public void setContRelation(String contRelation) {
        this.contRelation = contRelation;
    }

    public Long getLicenseValid() {
        return licenseValid;
    }

    public void setLicenseValid(Long licenseValid) {
        this.licenseValid = licenseValid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contacterID);
        dest.writeString(projectID);
        dest.writeString(customerID);
        dest.writeString(contName);
        dest.writeString(contMobile);
        dest.writeString(contOtherPhone);
        dest.writeString(isPrimContanter);
        dest.writeString(contGenderCode);
        dest.writeString(contGender);
        dest.writeString(contBirthday);
        dest.writeString(idNumber);
        dest.writeString(ageScopeCode);
        dest.writeString(ageScope);
        dest.writeString(contTypeCode);
        dest.writeString(contType);
        dest.writeString(contRelationCode);
        dest.writeString(contRelation);
        dest.writeValue(licenseValid);
        dest.writeValue(isSussece);
    }

    public static final Parcelable.Creator<Contacter> CREATOR = new Parcelable.Creator<Contacter>() {
                                                                  public Contacter createFromParcel(Parcel in) {
                                                                      return new Contacter(in);
                                                                  }

                                                                  public Contacter[] newArray(int size) {
                                                                      return new Contacter[size];
                                                                  }
                                                              };
}
