package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.model.Dictionary;
import com.roiland.crm.sm.core.service.exception.ResponseException;

/**
 * 
 * 数据字典接口
 * 
 */
public interface DictionaryAPI {

	/**
	 * 
	 * 获得数据字典接口
	 * @param dicName 字典表标识
	 * @return
	 * @throws ResponseException
	 */
	List<Dictionary> getDictionary(String dicName) throws ResponseException;
	
	
	/**
	 * 
	 * 取级联数据列表接口
	 * @param dicType 参数表
	 * @param parentKey 
	 * @return
	 * @throws ResponseException
	 */
	List<Dictionary> getCascadeDict(String dicType,	String...parentKey) throws ResponseException;
	
}
