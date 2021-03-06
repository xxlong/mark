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
public class Merchant {


	public long getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(long deleteDate) {
		this.deleteDate = deleteDate;
	}

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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}



	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantPassword() {
		return merchantPassword;
	}

	public void setMerchantPassword(String merchantPassword) {
		this.merchantPassword = merchantPassword;
	}

	public String getMerchantMail() {
		return merchantMail;
	}

	public void setMerchantMail(String merchantMail) {
		this.merchantMail = merchantMail;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public float getMerchantTrust() {
		return merchantTrust;
	}

	public void setMerchantTrust(float merchantTrust) {
		this.merchantTrust = merchantTrust;
	}
	//_id
		private String  _id;
	// 商家姓名或者是昵称
	@Size(min=3,max=20,message="merchantName must be between 3 and 20 characters long.")
	@Pattern(regexp="^[a-zA-Z0-9]+$",message="merchantName must be alphanumeric with no spaces")
	private String merchantName;
	// 商家密码
	@Size(min=6,message="The password must be at least 6 characters long.")
	private String merchantPassword;
	// 商家邮箱-----该字段唯一
	//@Pattern(regexp="\\w+@(\\w+\\.){1,3}\\w+",message="Invalid email address.")
	private String merchantMail;
	// 商家手机号，用于审查
	private String phoneNumber;
	// 商家居住地址？
	private String merchantAddress;
	// 商家身份证
	private String merchantID;
	// 商家店铺名称
	private String storeName;
	// 商家店铺类型（网店or实体店）
	private String storeType;
	// 商家店铺地址(实体店填地址，网店写网址）
	private String storeAddress;
	// 商家信用，由标记信息取得？
	private float merchantTrust;
	//
	private int role;
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
	//注册时间
	private long date;
	//删除时间
	private long deleteDate;
	//商家状态
	private int status;
	
	
	//***************************************KG******************************************
/*    // 是否激活  
    private boolean activated;  
    	public boolean isActivated() {
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
