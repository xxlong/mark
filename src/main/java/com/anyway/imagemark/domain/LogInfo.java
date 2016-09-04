/**
 * 
 */
package com.anyway.imagemark.domain;

import org.bson.types.ObjectId;

/**
 * @author Kario
 * 
 */
public class LogInfo {
	
	

	public String get_id() {
		return _id;
	}

	public void set_id() {
		this._id = new ObjectId().toString();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getIpInfo() {
		return ipInfo;
	}

	public void setIpInfo(String ipInfo) {
		this.ipInfo = ipInfo;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	//_id
		private String  _id;
	// 登陆者id
	private String user_id;
	// 登陆者类型
	private String loginType;
	// 登陆ip
	private String ipInfo;
	// 登陆时间
	private long loginTime;
	//状态
	private int status;
}
