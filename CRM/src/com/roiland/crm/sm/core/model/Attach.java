package com.roiland.crm.sm.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 * 文档图片实体类
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: Attach.java, v 0.1 2013-8-2 上午10:10:25 shuang.gao Exp $
 */
public class Attach implements Parcelable {
    private String attachmentID; //文档ID
    private String attachName;   //名称
    private String uploadDate;   //上传时间
    private String pictureSuffix; //图片格式
    private String comment;      //备注
    private String pictureURL;   //图片地址

    public Attach() {
        attachmentID = null;
        attachName = null;
        uploadDate = null;
        pictureSuffix = null;
        comment = null;
        pictureURL = null;
    }

    public Attach(Parcel in) {
        attachmentID = in.readString();
        attachName = in.readString();
        uploadDate = in.readString();
        pictureSuffix = in.readString();
        comment = in.readString();
        pictureURL = in.readString();
    }

    public String getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(String attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getPictureSuffix() {
        return pictureSuffix;
    }

    public void setPictureSuffix(String pictureSuffix) {
        this.pictureSuffix = pictureSuffix;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(attachmentID);
        dest.writeString(attachName);
        dest.writeString(uploadDate);
        dest.writeString(pictureSuffix);
        dest.writeString(comment);
        dest.writeString(pictureURL);
    }

    public static final Parcelable.Creator<Attach> CREATOR = new Parcelable.Creator<Attach>() {
                                                               public Attach createFromParcel(Parcel in) {
                                                                   return new Attach(in);
                                                               }

                                                               public Attach[] newArray(int size) {
                                                                   return new Attach[size];
                                                               }
                                                           };

    @Override
    public int describeContents() {
        return 0;
    }
}
