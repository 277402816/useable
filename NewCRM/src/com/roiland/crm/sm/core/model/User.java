package com.roiland.crm.sm.core.model;

/**
 * 
 * <pre>
 * 用户对象.
 * </pre>
 *
 * @author Chunji Li
 * @version $Id: User.java, v 0.1 2013-5-13 下午05:08:31 cjyy Exp $
 */
public class User {

	private String dealerOrgID = null;     //经销商ID
	private String dealerOrgName = null;   //经销商名称
	private String departName = null;      //部门名称
	private String posiName = null;        //职位名称
	private String userName = null;        //用户名称
	private String userID = null;          //用户ID
	private String password = null;        //密码
	
	public String getDealerOrgName() {
		return dealerOrgName;
	}
	public void setDealerOrgName(String dealerOrgName) {
		this.dealerOrgName = dealerOrgName;
	}
	public String getDealerOrgID() {
		return dealerOrgID;
	}
	public void setDealerOrgID(String dealerOrgID) {
		this.dealerOrgID = dealerOrgID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getPosiName() {
		return posiName;
	}
	public void setPosiName(String posiName) {
		this.posiName = posiName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
