package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.model.Contacter;
import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.service.exception.ResponseException;

/**
 * 
 * <pre>
 * 联系人接口
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: ContacterAPI.java, v 0.1 2013-8-2 上午11:22:39 shuang.gao Exp $
 */
public interface ContacterAPI {
	/**
	 * 获取联系人列表
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @return 联系人列表（包含联系人详细信息） 
	 * @throws ResponseException
	 */
	List<Contacter> getContacterList(String userID, String dealerOrgID,
			String projectID, String customerID) throws ResponseException;

	/**
	 * 创建联系人（创建销售线索联系人或者客户的联系人）
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param contacter 联系人信息
	 * @return 是否创建成功  联系人ID
	 * @throws ResponseException
	 */
	Contacter createContacter(String userID, String dealerOrgID,
			Contacter contacter) throws ResponseException;

	/**
	 * 更新销售线索联系人信息
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param contacter 联系人信息
	 * @return 是否创建成功
	 * @throws ResponseException
	 */
	Contacter updateContacter(String userID, String dealerOrgID,
			Contacter contacter) throws ResponseException;
	
	/**
	 * 
	 * <pre>
	 * 获取销售顾问列表
	 * </pre>
	 *
	 * @return 销售顾问列表
	 * @throws ResponseException
	 */
	List<Dictionary> getEmployeeList() throws ResponseException;
}
