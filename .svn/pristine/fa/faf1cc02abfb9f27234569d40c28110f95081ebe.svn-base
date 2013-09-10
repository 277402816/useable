package com.roiland.crm.sm.core.service;

import java.util.List;
import com.roiland.crm.sm.core.model.CustOrder;
import com.roiland.crm.sm.core.model.Project;
import com.roiland.crm.sm.core.service.exception.ResponseException;
/**
 * 
 * 客户订单接口
 *
 */
public interface CustOrderAPI {
	/**
	 * 获取客户订单编号
	 * @param userID
	 * @param dealerOrgID
	 * @param projectID
	 * @param customerID
	 * @return 订单编号
	 * @throws ResponseException
	 */
	String getOrderNo(String userID, String dealerOrgID, String projectID,
			String customerID) throws ResponseException;

	/**
	 * 是否存在有效订单
	 * @param projectID 销售机会ID
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @return 订单是否有效
	 * @throws ResponseException
	 * @throws com.roiland.crm.sm.core.service.exception.ResponseException 
	 */
	Project isProjectActive(String projectID, String dealerOrgID)
			throws ResponseException;

	/**
	 * 是否有预匹配权限
	 * @param userID 销售顾问ID
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @return 订单是否有效
	 * @throws ResponseException
	 */
	Boolean isRoleForMatch(String userID, String dealerOrgID)
			throws ResponseException;

	/**
	 * 创建新购车订单
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)
	 * @param customerID 客户ID
	 * @param projectID 销售线索ID
	 * @param custOrder 订单信息
	 * @return 创建是否成功
	 * @throws ResponseException
	 */
	Boolean createOrder(String userID, String dealerOrgID, String customerID,
			String projectID, CustOrder custOrder) throws ResponseException;

	/**
	 * 获得购车订单列表，包含模糊查询
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param searchWord 搜索关键字
	 * @param searchColumns 搜索字段 规则: 客户名称,客户手机号码[custName,custPhone]
	 * @param startNum 记录开始索引 Optional
	 * @param rowCount 获取的记录条数 Optional
	 * @return 购车订单列表
	 * @throws ResponseException
	 */
	List<CustOrder> getOrderList(String userID, String dealerOrgID,String projectID,String customerID,
			String searchWord, String searchColumns, Integer startNum,
			Integer rowCount) throws ResponseException;

	/**
	 * 获得购车订单列表（詳細），包含模糊查询
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param orderNo 订单编号
	 * @return 购车订单列表（詳細）
	 * @throws ResponseException
	 */
	CustOrder getOrderDetailInfo(String userID, String dealerOrgID,
			String orderNo) throws ResponseException;

	/**
	 * 更改订单
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param custOrder 订单信息
	 * @return 更新是否成功
	 * @throws ResponseException
	 */
	Boolean updateOrder(String userID, String dealerOrgID, String projectID,
			CustOrder custOrder) throws ResponseException;

	/**
	 * 资源提报
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param orderNo 订单编号
	 * @return 是否成功
	 * @throws ResponseException
	 */
	Boolean submitResRequirement(String userID, String dealerOrgID,
			String orderNo) throws ResponseException;
	/**
	 * 
	 * <pre>
	 * 获取三日内到期订单
	 * </pre>
	 *
	 * @param searchWord 查询关键字
	 * @param searchColumns 搜索字段
	 * @param startNum 记录开始索引
	 * @param rowCount 获取的记录条数
	 * @return
	 * @throws ResponseException
	 */
    List<CustOrder> getThreeDaysDeliveryOrderList(String searchWord, String searchColumns,
                                                  Integer startNum, Integer rowCount)
                                                                                     throws ResponseException;
}
