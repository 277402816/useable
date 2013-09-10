package com.roiland.crm.sm.core.model;

/**
 * 
 * <pre>
 * 今日待办事项
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: TodayWorkContent.java, v 0.1 2013-8-2 上午10:41:52 shuang.gao Exp $
 */
public class TodayWorkContent {
    private User   user;                        //用户
    private String expiredActionPlanCount;     //过期跟踪计划数量
    private String todayActionPlanCount;       //今日跟踪计划数量
    private String waitingTransCollectCount;   //待转化客户数量
    private String expiredSalesProjectCount;   //已过期销售线索数量
    private String threeDaysSalesProjectCount; //3日内预计购车线索数量
    private String threeDaysDeliveryOrderCount; //3日内到期订单数量
    private String noActionPlanProjectCount;   //没有跟踪计划的客户
    private String todayCollectCustomers;      //今日客户接待
    private String todayCustomerOrder;         // 今日客户订单
    private String todaySalesProject;          //今日销售线索
    private String todayCompleteSalesProject;  // 今日成交线索
    private String completeTracPlan;           // 完成跟踪计划
    private String todayDestroySalesProject;   // 今日废弃线索
    private String todayTestDrive;             //今日试乘试驾

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTodayCompleteSalesProject() {
        return todayCompleteSalesProject;
    }

    public void setTodayCompleteSalesProject(String todayCompleteSalesProject) {
        this.todayCompleteSalesProject = todayCompleteSalesProject;
    }

    public String getTodayActionPlanCount() {
        return todayActionPlanCount;
    }

    public void setTodayActionPlanCount(String todayActionPlanCount) {
        this.todayActionPlanCount = todayActionPlanCount;
    }

    public String getExpiredActionPlanCount() {
        return expiredActionPlanCount;
    }

    public void setExpiredActionPlanCount(String expiredActionPlanCount) {
        this.expiredActionPlanCount = expiredActionPlanCount;
    }

    public String getWaitingTransCollectCount() {
        return waitingTransCollectCount;
    }

    public void setWaitingTransCollectCount(String waitingTransCollectCount) {
        this.waitingTransCollectCount = waitingTransCollectCount;
    }

    public String getExpiredSalesProjectCount() {
        return expiredSalesProjectCount;
    }

    public void setExpiredSalesProjectCount(String expiredSalesProjectCount) {
        this.expiredSalesProjectCount = expiredSalesProjectCount;
    }

    public String getThreeDaysSalesProjectCount() {
        return threeDaysSalesProjectCount;
    }

    public void setThreeDaysSalesProjectCount(String threeDaysSalesProjectCount) {
        this.threeDaysSalesProjectCount = threeDaysSalesProjectCount;
    }

    public String getThreeDaysDeliveryOrderCount() {
        return threeDaysDeliveryOrderCount;
    }

    public void setThreeDaysDeliveryOrderCount(String threeDaysDeliveryOrderCount) {
        this.threeDaysDeliveryOrderCount = threeDaysDeliveryOrderCount;
    }

    public String getNoActionPlanProjectCount() {
        return noActionPlanProjectCount;
    }

    public void setNoActionPlanProjectCount(String noActionPlanProjectCount) {
        this.noActionPlanProjectCount = noActionPlanProjectCount;
    }

    public String getTodayCollectCustomers() {
        return todayCollectCustomers;
    }

    public void setTodayCollectCustomers(String todayCollectCustomers) {
        this.todayCollectCustomers = todayCollectCustomers;
    }

    public String getTodayCustomerOrder() {
        return todayCustomerOrder;
    }

    public void setTodayCustomerOrder(String todayCustomerOrder) {
        this.todayCustomerOrder = todayCustomerOrder;
    }

    public String getTodaySalesProject() {
        return todaySalesProject;
    }

    public void setTodaySalesProject(String todaySalesProject) {
        this.todaySalesProject = todaySalesProject;
    }

    public String getCompleteTracPlan() {
        return completeTracPlan;
    }

    public void setCompleteTracPlan(String completeTracPlan) {
        this.completeTracPlan = completeTracPlan;
    }

    public String getTodayDestroySalesProject() {
        return todayDestroySalesProject;
    }

    public void setTodayDestroySalesProject(String todayDestroySalesProject) {
        this.todayDestroySalesProject = todayDestroySalesProject;
    }

    public String getTodayTestDrive() {
        return todayTestDrive;
    }

    public void setTodayTestDrive(String todayTestDrive) {
        this.todayTestDrive = todayTestDrive;
    }

}
