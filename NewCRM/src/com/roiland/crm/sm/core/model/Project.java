package com.roiland.crm.sm.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.roiland.crm.sm.core.model.PurchaseCarIntention;
import com.roiland.crm.sm.core.model.Customer;

/**
 * 
 * <pre>
 * 销售线索信息
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: Project.java, v 0.1 2013-8-2 上午10:38:44 shuang.gao Exp $
 */
public class Project implements Parcelable {
    private String               projectID;
    private Customer             customer             = null;
    private PurchaseCarIntention purchaseCarIntention = null;
    private AdvancedSearch       advancedSearch       = null;

    public Project() {

    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PurchaseCarIntention getPurchaseCarIntention() {
        return purchaseCarIntention;
    }

    public void setPurchaseCarIntention(PurchaseCarIntention purchaseCarIntention) {
        this.purchaseCarIntention = purchaseCarIntention;
    }

    public void setAdvancedSearch(AdvancedSearch advancedSearch) {
        this.advancedSearch = advancedSearch;
    }

    public AdvancedSearch getAdvancedSearch() {
        return advancedSearch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (customer == null) {
            customer = new Customer();
        }
        if (purchaseCarIntention == null) {
            purchaseCarIntention = new PurchaseCarIntention();
        }
        dest.writeString(projectID);
        dest.writeValue(customer);
        dest.writeValue(purchaseCarIntention);
        dest.writeValue(advancedSearch);
    }

    public Project(Parcel in) {
        projectID = in.readString();
        customer = (Customer) in.readValue(Customer.class.getClassLoader());
        purchaseCarIntention = (PurchaseCarIntention) in.readValue(PurchaseCarIntention.class
            .getClassLoader());
        advancedSearch = (AdvancedSearch) in.readValue(AdvancedSearch.class.getClassLoader());
    }

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
                                                                public Project createFromParcel(Parcel in) {
                                                                    return new Project(in);
                                                                }

                                                                public Project[] newArray(int size) {
                                                                    return new Project[size];
                                                                }
                                                            };

    /**
     * 
     * <pre>
     * 销售线索的高级搜索对象
     * </pre>
     *
     * @author cjyy
     * @version $Id: Project.java, v 0.1 2013-5-24 上午10:35:07 cjyy Exp $
     */
    public class AdvancedSearch implements Parcelable {
        public static final String ORDER_BY_BUILDUP_DATE  = "startDate";   //建立时间正序排序
        public static final String ORDER_BY_PREORDER_DATE = "preorderDate"; //预购日期正序排序
        private String             brand                  = null;          //品牌Code
        private String             model                  = null;          //车型Code
        private String             followStatus           = null;          //流程状态Code
        private String             owner                  = null;          //所属销售顾问ID
        private long               startDate              = 0;             //开始时间
        private long               endDate                = 0;             //结束时间
        private String             orderBy                = null;          //排序方式  开始时间正序排序：'startDate', 预购日期正序排序：'preorderDate'

        public AdvancedSearch() {
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(String followStatus) {
            this.followStatus = followStatus;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(Long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(Long endDate) {
            this.endDate = endDate;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(brand);
            dest.writeString(model);
            dest.writeString(followStatus);
            dest.writeString(owner);
            dest.writeLong(startDate);
            dest.writeLong(endDate);
            dest.writeString(orderBy);
        }

        public AdvancedSearch(Parcel in) {
            brand = in.readString();
            model = in.readString();
            followStatus = in.readString();
            owner = in.readString();
            startDate = in.readLong();
            endDate = in.readLong();
            orderBy = in.readString();
        }

        public final Parcelable.Creator<AdvancedSearch> CREATE = new Parcelable.Creator<AdvancedSearch>() {
                                                                   public AdvancedSearch createFromParcel(Parcel in) {
                                                                       return new AdvancedSearch(in);
                                                                   }

                                                                   public AdvancedSearch[] newArray(int size) {
                                                                       return new AdvancedSearch[size];
                                                                   }
                                                               };
    }
}
