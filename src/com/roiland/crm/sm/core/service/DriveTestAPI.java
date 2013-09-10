package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.model.DriveTest;
import com.roiland.crm.sm.core.service.exception.ResponseException;
/**
 * 
 * 试乘试驾接口
 *
 */
public interface DriveTestAPI {
	
	/**
	 * 新建试乘试驾
	 *
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID 经销商ID
	 * @param driveTest DriveTest信息
	 * @return driveTestID 试乘试驾ID 
	 * @throws ResponseException
	 */
	String createDriveTest (String userID, String dealerOrgID,
			DriveTest driveTest) throws ResponseException;

	/**
	 * 更新试乘试驾
	 *
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID 经销商ID
	 * @param driveTest DriveTest信息
	 * @return	是否创建成功
	 * @throws ResponseException
	 */
	Boolean updateDriveTest(String userID, String dealerOrgID,
			DriveTest driveTest)throws ResponseException;
	
	/**
	 * 获取试乘试驾列表
	 * 
	 * @param userID 用户ID（销售顾问）
	 * @param dealerOrgID 经销商ID
	 * @param searchWord 搜索关键字
	 * @param searchColumns 搜索字段
	 * @param startNum 记录开始索引
	 * @param rowCount 获取的记录条数
	 * @param sortRule 排序规则
	 * @return  DriveTest信息
	 * @throws ResponseException
	 */
	List<DriveTest> getDriveTestList(String userID, String dealerOrgID,String searchWord,
			String searchColumns,
			int startNum,
			int rowCount,
			String sortRule)throws ResponseException;
	
	/**
	 * 获取销售线索的试乘试驾列表
	 * @param projectID 销售线索ID
	 * @return
	 * @throws ResponseException
	 */
	List<DriveTest> getProjectDriveTest(String projectID) throws ResponseException;
}
