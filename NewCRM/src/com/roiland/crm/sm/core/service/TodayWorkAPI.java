package com.roiland.crm.sm.core.service;

import com.roiland.crm.sm.core.model.TodayWorkContent;
import com.roiland.crm.sm.core.model.User;
import com.roiland.crm.sm.core.service.exception.ResponseException;

public interface TodayWorkAPI {
	
	/**
	 * Do login funciton
	 * @param user
	 * @return
	 * @throws ResponseException 
	 */
	boolean login(User user) throws ResponseException;
	
	/**
	 * Get today working list
	 * @return
	 */
	TodayWorkContent getTodayWorking(User user) throws ResponseException;
}
