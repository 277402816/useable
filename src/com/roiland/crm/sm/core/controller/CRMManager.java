package com.roiland.crm.sm.core.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.core.model.OppoFunnel;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.TodayWorkContent;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.core.service.exception.ResponseException;

/**
 * 
 * <pre>
 * CRM业务管理类接口，所有从UI调用到后端的数据请求方法都注册在改接口中。
 * </pre>
 *
 * @author cjyy
 * @version $Id: CRMManager.java, v 0.1 2013-5-24 下午01:20:13 cjyy Exp $
 */
public interface CRMManager {
    
    /**
     * <pre>
     * 登陆 获得User信息
     * </pre>
     * 
     * @param user 包含用户名和密码的用户信息
     * @return user
     * @throws ResponseException
     */
    User login(User user) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取待办事项信息
     * </pre>
     *
     * @return TodayWorkContent 返回待办事项信息对象
     * @throws ResponseException
     */
    TodayWorkContent getTodayWorking() throws ResponseException;

    /**
     * 
     * <pre>
     * 获取销售先说列表信息
     * </pre>
     *
     * @param searchText           模糊搜索内容
     * @param searchColumns        模糊搜索字段
     * @param beginnum             记录开始索引
     * @param percount             获取的记录条数
     * @param advancedSearch       高级搜索条件对象
     * @return                     销售线索列表
     * @throws ResponseException
     */
    List<Project> getProjectList(String searchText, String searchColumns,String expired,int startNum,
        int rowCount, Project.AdvancedSearch advancedSearch)throws ResponseException;
    /**
     * 获取销售线索详细信息
     * 
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @return Project信息
     * @throws ResponseException
     */
    Project getProjectInfo(String projectID, String customerID)
            throws ResponseException;
    
    /**
     * 通过dicTYPE类型获得数据字典
     * 
     * @param dicType 想要获得的数据字典类型
     * @return
     * @throws ResponseException 
     */
    List<Dictionary> getDictionariesByType(String dicType) throws ResponseException;

    /**
     * 通过类型和父键获得关联字典
     * 
     * @param dicType 想要获得的数据字典类型
     * @param parentKey 父键
     * @return
     * @throws ResponseException 
     */
    List<Dictionary> getRelativeDictionaries(String dicType, String...parentKey) throws ResponseException;
    
    /**
     * 创建销售线索
     * 
     * @param project project信息
     * @param TracePlan TracePlan信息
     * @return 是否创建成功
     * @throws ResponseException
     */
    Boolean createProject(Project project, TracePlan TracePlan)
            throws ResponseException;

    /**
     * 更新销售线索详细信息
     * 
     * @param project project信息
     * @return 是否创建成功
     * @throws ResponseException
     */
    Boolean updateProjectInfo(Project project) throws ResponseException;
    
    /**
     * 获取客户信息列表
     * 
     * @param searchWord 搜索关键字 Optional
     * @param searchColumns 搜索字段, 规则：[custName,custMobile,] Optional
     * @param startNum 记录开始索引 Optional
     * @param rowCount 获取的记录条数 Optional
     * @param sortRule 排序规则 Optional
     * @param asvanceOwnerID 高级搜索客户归属
     * @param advanceCustStatus 高级搜索销售顾问ID
     * @param advanceCustOwer 高级搜索客户状态
     * @return 客户信息列表
     * @throws ResponseException
     */
    List<Customer> getCustomerList(String searchWord, String searchColumns,
            Integer startNum, Integer rowCount, String sortRule, String advanceCustOwer, String advanceCustStatus, String asvanceOwnerID)
            throws ResponseException;

    /**
     * 获取客户详细信息列表
     * 
     * @param customerID 客户ID
     * @return 客户详细信息列表
     * @throws ResponseException
     */
    Customer getCustomerDetail(String customerID) throws ResponseException;
    
    /**
     * 获取客户购车意向列表
     * 
     * @param customer 客户信息实体
     * @return 购车意向列表
     * @throws ResponseException
     */
    String getCustomerCarIntention(String customerID) throws ResponseException;
    
    /**
     * 获取车辆资源信息列表
     * 
     * @param searchType 搜索类型 0：模糊搜索，1：高级搜索
     * @param searchWord 搜索关键字
     * @param searchColumns 搜索字段,规则：底盘号，配置
     * @param vehicle 车辆资源信息实体
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @param sortRule 排序规则 前台传递参数
     * @return 车辆信息列表
     * @throws ResponseException
     */
    List<Vehicle> getCarResList(String searchType, String searchWord,
            String searchColumns, Vehicle vehicle, Integer startNum,
            Integer rowCount, String sortRule) throws ResponseException;
    /**
     * 获取跟踪计划列表
     * 
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @param searchWord 搜索关键字
     * @param searchColumns 搜索字段包含：客户名称,客户手机号
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @param advancedSearch       高级搜索条件对象
     * @return 取销售线索的跟踪计划列表和客户的跟踪计划列表
     * @throws ResponseException
     */
    List<TracePlan> getTracePlanList(String projectID, String customerID,
            String searchWord, String searchColumns, int startNum, int rowCount,TracePlan.AdvancedSearch adaAdvancedSearch)
            throws ResponseException;
    
    /**
     * 新建跟踪计划
     * 
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @param tracePlan 跟踪计划信息
     * @return 跟踪计划ID
     * @throws ResponseException
     */
    String createTracePlan(String projectID, String customerID,
            TracePlan tracePlan) throws ResponseException;
    
    /**
     * 更新跟踪计划
     * 
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @param tracePlan 跟踪计划信息
     * @return 是否创建成功
     * @throws ResponseException
     */
    Boolean updateTracePlan(String projectID, String customerID, TracePlan tracePlan) throws ResponseException;

    /**
     * 更新跟踪计划和新建跟踪计划
     * 
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @param tracePlan 跟踪计划信息
     * @return 是否创建成功
     * @throws ResponseException
     */
    Boolean updateTracePlan(String projectID, String customerID, TracePlan existTracePlan, TracePlan newTracePlan) throws ResponseException;

    /**
     * 检查客户是否已经存在
     * 
     * @param mobileNumber
     * @param otherPhone
     * @return 
     * @throws ResponseException
     */
    Map<String, String> isExistProject (String mobileNumber,String otherPhone) throws ResponseException;
    
    
    /**
     * 获取客户的销售线索列表
     * 
     * @param customerID
     * @return 客户的销售线索列表
     * @throws ResponseException
     */
    List<Project> getCustomerProjectList(String customerID) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取客户订单信息列表
     * </pre>
     *
     * @param userID 用户ID
     * @param customerID 客户ID
     * @param projectID 销售线索ID
     * @param needCommit 需求提报标识
     * @param dealerOrgID (经销商ID)销售机构ID
     * @param searchWord 搜索关键字
     * @param searchColumns 搜索字段
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @return 客户订单列表
     * @throws ResponseException
     */
   
    List<CustOrder> getOrderList(String projectID,String customerID,String searchWord, String searchColumns,
        Integer startNum, Integer rowCount) throws ResponseException;

    /**
     * 
     * <pre>
     * 是否存在有效订单
     * </pre>
     *
     * @param projectID 销售机会ID
     * @return
     * @throws ResponseException
     */
    Project isProjectActive(String projectID) throws ResponseException;
 /**
  * 
  * <pre>
  * 3日内到期订单数量
  * </pre>
  *
  * @param searchWord 搜索关键字
  * @param sEARCH_COLUMNS 搜索字段
  * @param startNum 记录开始索引
  * @param rowCount 获取的记录条数
  * @return 3日内到期客户订单列表
 * @throws ResponseException 
  */
    List<CustOrder> getThreeDaysDeliveryOrderList(String searchWord, String sEARCH_COLUMNS, int startNum, int rowCount) throws ResponseException;
    /**
     * 
     * <pre>
     * 获得购车订单列表（详细），包含模糊查询
     * </pre>
     *
     * @param orderNo
     * @return 客户订单详细信息
     * @throws ResponseException
     */
    CustOrder getOrderDetailInfo(String orderNo) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取文档信息列表
     * </pre>
     *
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @return 文档信息列表
     * @throws ResponseException
     */
    List<Attach> getAttachmentList(String projectID, String customerID)
            throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取联系人列表
     * </pre>
     *
     * @param projectID 销售线索ID
     * @param customerID 客户ID
     * @return 联系人列表（包含联系人详细信息）
     * @throws ResponseException
     */
    List<Contacter> getContacterList(String projectID, String customerID)
            throws ResponseException;
    
   
    /**
     * 
     * <pre>
     * 销售线索试乘试驾
     * </pre>
     *
     * @param projectID 销售线索ID
     * @return 销售线索试乘试驾列表
     */
    List<DriveTest> getProjectDriveTest(String projectID) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取销售顾问列表
     * </pre>
     *
     * @return 销售顾问列表
     * @throws ResponseException
     */
    List<Dictionary> getEmployeeList()throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取今日销售线索列表
     * </pre>
     *
     * @param searchText 查询关键字
     * @param searchColumns 搜索字段,规则：[custName,custMobile, custOtherPhone]
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @param status 线索状态
     * @return 销售线索列表
     * @throws ResponseException
     */
    
    List<Project> getTodayProjectList(String searchText, String searchColumns, int startNum,
        int rowCount,String status)throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取过期销售线索列表
     * </pre>
     *
     * @param searchText 查询关键字
     * @param searchColumns 搜索字段,规则：[custName,custMobile, custOtherPhone]
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @param status 线索状态
     * @return 销售线索列表
     * @throws ResponseException
     */
    List<Project> getExpiredProjectList(String searchText, String searchColumns, int startNum,
        int rowCount)throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取过期跟踪计划列表
     * </pre>
     *
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @return 过期跟踪计划列表
     * @throws ResponseException
     */
    List<TracePlan> getExpiredActionPlanList(int startNum,int rowCount)throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取今天跟踪计划列表
     * </pre>
     *
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @return 今日跟踪计划列表
     * @throws ResponseException
     */
    List<TracePlan> getTodayActionPlanList (int startNum,int rowCount)throws ResponseException;
    
    /**
     * 
     * <pre>
     * 没有跟踪计划的客户
     * </pre>
     *
     * @param searchWord 搜索关键字
     * @param searchColumns 搜索字段,
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @return
     * @throws ResponseException
     */
    List<Customer> noActionPlanProjectCount(String searchWord,String searchColumns,int startNum,int rowCount) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取销售漏斗统计图数据
     * </pre>
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return
     * @throws ResponseException
     */
    OppoFunnel getOppoFunnel(long startDate, long endDate) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 销售漏斗搜索方法
     * </pre>
     *
     * @param startDate 预购日期开始时间
     * @param endDate   预购日期结束时间
     * @return
     */
    List<Project> searchSalesOppoFunncelList(long startDate, long endDate,int startNum ,int rowCount) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 销售总监分配客户给销售顾问
     * </pre>
     *
     * @param customerID 客户ID
     * @param ownerID 销售顾问ID（雇员ID）
     * @return true 操作成功
     * @throws ResponseException
     */
    boolean assignCustomer(String customerID, String ownerID) throws ResponseException;
        
    /**
     * 
     * <pre>
     * 销售总监分配客户给销售顾问
     * </pre>
     *
     * @param attachmentID 文档ID
     * @return RLHttpResponse 响应对象
     * @throws ResponseException
     */
    RLHttpResponse downloadFile(String attachmentID) throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取版本号和url
     * </pre>
     *
     * @return 版本号和url
     * @throws ResponseException
     */
    String[] getVersion () throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取老客户列表
     * </pre>
     *
     * @return 老客户列表
     * @throws ResponseException
     */
    List<Customer> getOldCustomer() throws ResponseException;
    
    /**
     * 同步销售线索/联系人
     * @param projectID
     * @param customer
     * @return
     * @throws ResponseException
     */
    boolean syncContacter(String projectID, Customer customer) throws ResponseException;
    
    /**
     * 更新客户信息
     * 
     * @param customer 客户信息实体
     * @return 是否更新成功
     * @throws ResponseException
     */
    Boolean updateCustomer(Customer customer) throws ResponseException;
    
    /**
     * 创建联系人（创建销售线索联系人或者客户的联系人）
     * @param contacter 联系人信息
     * @return 是否创建成功 联系人ID
     * @throws ResponseException
     */
    Contacter createContacter(Contacter contacter) throws ResponseException;

    /**
     * 更新销售线索联系人信息
     * 
     * @param contacter 联系人信息
     * @return 是否创建成功
     * @throws ResponseException
     */
    Contacter updateContacter(Contacter contacter) throws ResponseException;


}
