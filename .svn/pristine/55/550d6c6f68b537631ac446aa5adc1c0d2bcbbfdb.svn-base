package com.roiland.crm.sm.core.service;

import java.util.List;

import com.roiland.crm.sm.core.http.RLHttpResponse;
import com.roiland.crm.sm.core.model.Attach;
import com.roiland.crm.sm.core.service.exception.ResponseException;
/**
 * 
 * <pre>
 * 文档图片接口
 * </pre>
 *
 * @author shuang.gao
 * @version $Id: AttachAPI.java, v 0.1 2013-8-2 上午11:21:57 shuang.gao Exp $
 */
public interface AttachAPI {
	/**
	 * 获取文档信息列表
	 * @param userID 用户ID(销售顾问)
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @return 文档信息列表
	 * @throws ResponseException
	 */
	List<Attach> getAttachmentList(String userID, String dealerOrgID, String projectID, String customerID) throws ResponseException;
	
	
	/**
	 * 上传文档
	 * @param userID 用户ID(销售顾问)
	 * @param dealerOrgID (经销商ID)销售机构ID
	 * @param projectID 销售线索ID
	 * @param customerID 客户ID
	 * @param documentName 文档名称
	 * @param pictureSuffix 上传图片
	 * @return 返回文档的ID
	 * @throws ResponseException
	 */
	Attach uploadDocument(String userID, String dealerOrgID, String projectID, String customerID,String documentName, String pictureSuffix) throws ResponseException;


	/**
	 * 下载文档
	 * @param attachmentID 文档ID
	 * @return RLHttpResponse 响应对象
	 * @throws ResponseException
	 */
	RLHttpResponse downloadFile(String attachmentID) throws ResponseException;
}
