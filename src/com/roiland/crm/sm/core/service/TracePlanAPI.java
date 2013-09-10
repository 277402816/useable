package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.model.TracePlan;
import com.roiland.crm.sm.core.service.exception.ResponseException;
/**
 * 
 * 跟踪计划接口
 *
 */
public interface TracePlanAPI {
	/**
	 * 获取跟踪计划列表
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @param searchWord 搜索关键字
	 * @param searchColumns 搜索字段包含：客户名称,客户手机号
	 * @param startNum 记录开始索引
	 * @param rowCount 获取的记录条数
	 * @return 取销售线索的跟踪计划列表和客户的跟踪计划列表
	 * @throws ResponseException
	 */
	List<TracePlan> getTracePlanList(String userID, String dealerOrgID,
			String projectID, String customerID, String searchWord,
			String searchColumns, int startNum, int rowCount)
			throws ResponseException;

	/**
	 * 新建跟踪计划
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @param tracePlan 跟踪计划信息
	 * @return 跟踪计划ID
	 * @throws ResponseException
	 */
	String createTracePlan(String userID, String dealerOrgID, String projectID,
			String customerID, TracePlan tracePlan) throws ResponseException;

	/**
	 * 更新跟踪计划
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @param tracePlan 跟踪计划信息
	 * @return 是否创建成功
	 * @throws ResponseException
	 */
	Boolean updateTracePlan(String userID, String dealerOrgID,
			String projectID, String customerID, TracePlan tracePlan)
			throws ResponseException;
	/**
	 * 更新跟踪计划同时新建另一个跟踪计划
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @param editTracePlan 现有跟踪计划信息
	 * @param newTracePlan  新的跟踪计划信息
	 * @return 是否创建成功
	 * @throws ResponseException
	 */
	Boolean updateTracePlan(String userID, String dealerOrgID, String projectID, String customerID,
			TracePlan editTracePlan, TracePlan newTracePlan) throws ResponseException;
	/**
	 * 
	 * <pre>
	 * 跟踪计划高级搜索
	 * </pre>
	 *
	 * @param adaAdvancedSearch  跟踪计划管理的高级搜索对象
	 * @return
	 * @throws ResponseException
	 */
    List<TracePlan> advSearchTracePlan(TracePlan.AdvancedSearch adaAdvancedSearch,int startNum, int rowCount)throws ResponseException;
    
    /**
     * 
     * <pre>
     * 获取过期跟踪计划列表
     * </pre>
     *
     * @param userID 用户ID
     * @param startNum 记录开始索引
     * @param rowCount 获取的记录条数
     * @return 过期跟踪计划列表
     * @throws ResponseException
     */
    List<TracePlan> getExpiredActionPlanList(String userID,int startNum, int rowCount) throws ResponseException;
    
    List<TracePlan> getTodayActionPlanList(String userID,int startNum,int rowCount) throws ResponseException;
}
