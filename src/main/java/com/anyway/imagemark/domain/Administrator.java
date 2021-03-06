/**
 * 
 */
package com.anyway.imagemark.domain;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;

/**
 * @author Kario
 *
 */
public class Administrator {
	

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
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	public String getAdminMail() {
		return adminMail;
	}
	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}
	//_id
	private String  _id;
	//管理员名称
	@Size(min=3,max=20,message="adminName must be between 3 and 20 characters long.")
	@Pattern(regexp="^[a-zA-Z0-9]+$",message="adminName must be alphanumeric with no spaces")
	private String adminName;
	//管理员密码
	@Size(min=5,max=20,message="The password must be at least 6 characters long.")
	private String adminPassword;
	//管理员邮箱
	//@Pattern(regexp="\\w+@(\\w+\\.){1,3}\\w+",message="Invalid email address.")
	private String adminMail;
	//管理员角色
	/*private Role affiliatedRole;
	public Role getAffiliatedRole() {
		return affiliatedRole;
	}
	public void setAffiliatedRole(Role affiliatedRole) {
		this.affiliatedRole = affiliatedRole;
	}*/
	private int role;
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	//管理员状态
	private int status;
	
	
	
	
	//***************************************KG******************************************
/*    // 是否激活  
    private boolean activated;  */
	/*	public boolean isActivated() {
	return activated;
}
public void setActivated(boolean activated) {
	this.activated = activated;
}*/
    // 随机码(激活帐户与生成重设密码链接时使用)  
    private String randomCode;

	public String getRandomCode() {
		return randomCode;
	}
	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}  

	//***************************************KG******************************************
}
