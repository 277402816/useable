package com.roiland.crm.sm.core.service;

import java.util.List;
import java.util.Map;

import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.model.OppoFunnel;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
/**
 * 
 * 销售线索接口
 *
 */
public interface ProjectAPI {
	
		
	/**
	 * 
     * <pre>
     * 获取销售线索列表信息
     * </pre>
     *
     * @param searchText           模糊搜索内容
     * @param searchColumns        模糊搜索字段
	 * @param expired 
     * @param beginnum             记录开始索引
     * @param percount             获取的记录条数
     * @param advancedSearch       高级搜索条件对象
     * @return                     销售线索列表
     * @throws ResponseException
	 */
	List<Project> getProjectList(String searchWord, String searchColumns, String expired, Integer startNum,	Integer rowCount, Project.AdvancedSearch advancedSearch) throws ResponseException;


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
     * @return
     * @throws ResponseException
     */
     List<Project> getTodayProjectList(String searchText, String searchColumns, int startNum,int rowCount, String status) throws ResponseException;
	
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
	 * 获取销售线索详细信息
	 * 
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @return Project信息
	 * @throws ResponseException
	 */
	Project getProjectInfo(String userID, String dealerOrgID, String projectID,
			String customerID) throws ResponseException;
	/**
	 * 创建销售线索
	 * 
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param project project信息
	 * @param TracePlan	TracePlan信息
	 * @return	是否创建成功
	 * @throws ResponseException
	 */
	Boolean createProject(String userID, String dealerOrgID, Project project,
			TracePlan TracePlan)
			throws ResponseException;

	/**
	 * 获取销售线索详细信息
	 * 
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param project project信息
	 * @return 是否创建成功
	 * @throws ResponseException
	 */
	Boolean updateProjectInfo(String userID, String dealerOrgID,Project project)
			throws ResponseException;
	
	/**
	 * 获取客户的销售线索列表
	 * 
	 * @param userID
	 * @param customerID
	 * @return 客户的销售线索列表
	 * @throws ResponseException
	 */
	List<Project> getCustomerProjectList(String userID,String customerID)
			throws ResponseException;
	
	/**
	 * 
	 * @param mobileNumber
	 * @param otherPhone
	 * @param dealerOrgID
	 * @return 信息
	 * @throws ResponseException
	 */
	Map<String, String> isExistProject (String mobileNumber, String otherPhone,String dealerOrgID)
	       throws ResponseException;
	
	/**
	 * 同步联系人
	 * @param projectID
	 * @param customer
	 * @return
	 * @throws ResponseException
	 */
	boolean syncContacter(String userID, String dealerOrgID, String projectID, Customer customer) throws ResponseException;
	
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
     * 搜索销售漏斗列表方法
     * </pre>
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return
     */
	List<Project> searchSalesOppoFunncelList(long startDate, long endDate ,int startNum ,int rowCount) throws ResponseException;
}
