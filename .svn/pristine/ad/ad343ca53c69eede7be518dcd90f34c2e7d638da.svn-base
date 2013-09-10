package com.roiland.crm.sm.core.model;

import java.util.List;

import com.roiland.crm.sm.core.model.Project.AdvancedSearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * <pre>
 * 跟踪计划信息
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: TracePlan.java, v 0.1 2013-8-2 上午10:43:28 shuang.gao Exp $
 */
public class TracePlan implements Parcelable {

    private Customer       customer;             // 客戶信息
    private String         activityID;           // 跟踪计划ID
    private String         activityType;         // 活动类型
    private String         activityTypeCode;     // 活动类型代码
    private long           executeTime;          // 执行时间
    private long           createDate;            //建立时间
    private String         executeStatus;        // 执行状态
    private String         executeStatusCode;    // 执行状态代码
    private String         activityContent;      // 活动内容
    private String         contactResultCode;    // 联系结果代码
    private String         contactResult;        // 联系结果
    private String         custFeedback;         // 客户反馈
    private String         leaderComment;        // 領導批示
    private String         collcustomerId;       // 客户接待ID
    private String         owerName;              //所属销售顾问名称 
    private AdvancedSearch advancedSearch = null; //高级搜索
    List<TracePlanList>    TracePlanVo;

    public TracePlan() {
    }

    public TracePlan(Parcel in) {
        activityID = in.readString();
        activityType = in.readString();
        activityTypeCode = in.readString();
        executeTime = in.readLong();
        createDate = in.readLong();
        executeStatus = in.readString();
        executeStatusCode = in.readString();
        activityContent = in.readString();
        contactResultCode = in.readString();
        contactResult = in.readString();
        custFeedback = in.readString();
        leaderComment = in.readString();
        collcustomerId = in.readString();
        owerName = in.readString();
        if (customer == null) {
            customer = new Customer();
        }
        customer.setCustomerID(in.readString());
        customer.setCustName(in.readString());
        customer.setCustMobile(in.readString());
        customer.setCustOtherPhone(in.readString());
        customer.setProjectID(in.readString());
        if (advancedSearch == null) {
            advancedSearch = new AdvancedSearch();
        }
        advancedSearch = (AdvancedSearch) in.readValue(AdvancedSearch.class.getClassLoader());
    }

    public List<TracePlanList> getTracePlanVo() {
        return TracePlanVo;
    }

    public void setTracePlanVo(List<TracePlanList> tracePlanVo) {
        TracePlanVo = tracePlanVo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getExecuteStatusCode() {
        return executeStatusCode;
    }

    public void setExecuteStatusCode(String executeStatusCode) {
        this.executeStatusCode = executeStatusCode;
    }

    public String getContactResult() {
        return contactResult;
    }

    public void setContactResult(String contactResult) {
        this.contactResult = contactResult;
    }

    public String getLeaderComment() {
        return leaderComment;
    }

    public void setLeaderComment(String leaderComment) {
        this.leaderComment = leaderComment;
    }

    public String getActivityTypeCode() {
        return activityTypeCode;
    }

    public void setActivityTypeCode(String activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getContactResultCode() {
        return contactResultCode;
    }

    public void setContactResultCode(String contactResultCode) {
        this.contactResultCode = contactResultCode;
    }

    public String getCustFeedback() {
        return custFeedback;
    }

    public void setCustFeedback(String custFeedback) {
        this.custFeedback = custFeedback;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public String getCollcustomerId() {
        return collcustomerId;
    }

    public void setCollcustomerId(String collcustomerId) {
        this.collcustomerId = collcustomerId;
    }

    public String getOwerName() {
        return owerName;
    }

    public void setOwerName(String owerName) {
        this.owerName = owerName;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public AdvancedSearch getAdvancedSearch() {
        return advancedSearch;
    }

    public void setAdvancedSearch(AdvancedSearch advancedSearch) {
        this.advancedSearch = advancedSearch;
    }

    @Override
    public void writeToParcel(Parcel param, int arg1) {
        if (customer == null) {
            customer = new Customer();
        }
        param.writeString(activityID);
        param.writeString(activityType);
        param.writeString(activityTypeCode);
        param.writeLong(executeTime);
        param.writeLong(createDate);
        param.writeString(executeStatus);
        param.writeString(executeStatusCode);
        param.writeString(activityContent);
        param.writeString(contactResultCode);
        param.writeString(contactResult);
        param.writeString(custFeedback);
        param.writeString(leaderComment);
        param.writeString(collcustomerId);
        param.writeString(owerName);
        param.writeString(customer.getCustomerID());
        param.writeString(customer.getCustName());
        param.writeString(customer.getCustMobile());
        param.writeString(customer.getCustOtherPhone());
        param.writeString(customer.getProjectID());
        param.writeValue(advancedSearch);
    }

    public static final Parcelable.Creator<TracePlan> CREATOR = new Parcelable.Creator<TracePlan>() {
                                                                  public TracePlan createFromParcel(Parcel in) {
                                                                      return new TracePlan(in);
                                                                  }

                                                                  public TracePlan[] newArray(int size) {
                                                                      return new TracePlan[size];
                                                                  }
                                                              };

    /**
     * 
     * <pre>
     * 跟踪计划管理的高级搜索对象
     * </pre>
     *
     * @author shuang.gao
     * @version $Id: TracePlan.java, v 0.1 2013-5-28 上午9:56:00 shuang.gao Exp $
     */
    public class AdvancedSearch implements Parcelable {
        private String executeStatus = null; //执行状态（字典表）
        private String leaderComment = null; //是否批示： 1已批示  2未批示
        private String sort          = null; //排序方式： 1建立时间正序排列 2执行时间正序排列
        private long   startDate     = 0;   //开始时间
        private long   endDate       = 0;   //结束时间
        private String actionType    = null; //活动类型（字典表）
        private String ownerId       = null; //销售顾问ID

        public AdvancedSearch() {
        }

        public AdvancedSearch(Parcel in) {
            executeStatus = in.readString();
            leaderComment = in.readString();
            sort = in.readString();
            startDate = in.readLong();
            endDate = in.readLong();
            actionType = in.readString();
            ownerId = in.readString();
        }

        public String getExecuteStatus() {
            return executeStatus;
        }

        public void setExecuteStatus(String executeStatus) {
            this.executeStatus = executeStatus;
        }

        public String getLeaderComment() {
            return leaderComment;
        }

        public void setLeaderComment(String leaderComment) {
            this.leaderComment = leaderComment;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = String.valueOf(ownerId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel param, int flags) {

            param.writeString(advancedSearch.getExecuteStatus());
            param.writeString(advancedSearch.getLeaderComment());
            param.writeString(advancedSearch.getSort());
            param.writeLong(advancedSearch.getStartDate());
            param.writeLong(advancedSearch.getEndDate());
            param.writeString(advancedSearch.getActionType());
            param.writeString(advancedSearch.getOwnerId());

        }
    }
}
