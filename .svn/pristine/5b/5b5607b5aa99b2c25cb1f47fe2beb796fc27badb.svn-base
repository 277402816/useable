package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.model.Vehicle;
import com.roiland.crm.sm.core.service.exception.ResponseException;

public interface VehicleAPI {
	
	/**
	 * 获取车辆资源信息列表
	 * @param user 
	 * @param searchType 搜索类型  0：模糊搜索，1：高级搜索
	 * @param searchWord 搜索关键字
	 * @param searchColumns 搜索字段,规则：底盘号，配置
	 * @param vehicle 车辆资源信息实体
	 * @param startNum 记录开始索引
	 * @param rowCount 获取的记录条数
	 * @param sortRule 排序规则 前台传递参数
	 * @return 车辆信息列表
	 * @throws ResponseException
	 */
	// List<Vehicle> getCarResList(String dealerOrgID,String userID, String
	// searchType,
	// String searchWord, String searchColumns,String brandCode, String
	// modelCode, String outsideColorCode,
	// String insideColorCode, String vehiConfigCode, Integer startNum, Integer
	// rowCount,
	// String sortRule) throws ResponseException;
	List<Vehicle> getCarResList(User user, String searchType,
			String searchWord, String searchColumns, Vehicle vehicle,
			Integer startNum, Integer rowCount, String sortRule)
			throws ResponseException;
	List<Vehicle> getCarResList(User user, String searchType,
			String searchWord, String[] searchColumns, Vehicle vehicle,
			Integer startNum, Integer rowCount, String sortRule)
			throws ResponseException;
}
