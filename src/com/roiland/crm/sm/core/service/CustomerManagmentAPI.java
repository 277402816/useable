package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.model.Customer;
import com.roiland.crm.sm.core.service.exception.ResponseException;

public interface CustomerManagmentAPI {

	/**
	 * 获取客户信息列表
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
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
//	List<Customer> getCustomerList(String userID, String dealerOrgID,
//			String searchWord, String searchColumns, Integer startNum,
//			Integer rowCount, String sortRule) throws ResponseException;
    List<Customer> getCustomerList(String userID, String dealerOrgID, String searchWord,
        String searchColumns, Integer startNum, Integer rowCount,
        String sortRule, String advanceCustOwer,
        String advanceCustStatus, String asvanceOwnerID) throws ResponseException;

	/**
	 * 获取客户详细信息列表
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param customerID 客户ID
	 * @return 客户详细信息列表
	 * @throws ResponseException
	 */
	Customer getCustomerDetail(String userID, String dealerOrgID,
			String customerID) throws ResponseException;

	/**
	 * 更新客户信息
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param customer 客户信息实体
	 * @return 是否更新成功
	 * @throws ResponseException
	 */
	Boolean updateCustomer(String userID, String dealerOrgID, Customer customer)
			throws ResponseException;

	/**
	 * 获取客户购车意向列表
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param customer 客户信息实体
	 * @return 购车意向列表
	 * @throws ResponseException
	 */
	String getCustomerCarIntention(String userID, String dealerOrgID,
			String customerID) throws ResponseException;
	
	
	/**
	 * 获取老客户列表
	 * @return 老客户列表
	 * @throws ResponseException
	 */
	List<Customer> getOldCustomer() throws ResponseException;
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
	 * @return 没有跟踪计划的客户列表
	 * @throws ResponseException
	 */
    List<Customer> noPlanCustomerList(String searchWord, String searchColumns, Integer startNum,
                                      Integer rowCount)throws ResponseException;

    /**
     * 
     * <pre>
     * 销售总监分配客户给销售顾问
     * </pre>
     *
     * @param customerID 客户ID
     * @param ownerID 销售顾问ID（雇员ID）
     * @return
     * @throws ResponseException 
     */
    boolean assignCustomer(String customerID, String ownerID) throws ResponseException;

}
