/**
 * 
 */
package com.anyway.imagemark.webDomain;

/**
 * @author Kario
 *
 */
public class MerchantInfo {
	public int getPraise() {
		return praise;
	}
	public void setPraise(int praise) {
		this.praise = praise;
	}
	public String getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getGoodCommentsNum() {
		return goodCommentsNum;
	}
	public void setGoodCommentsNum(int goodCommentsNum) {
		this.goodCommentsNum = goodCommentsNum;
	}
	public int getBadCommentsNum() {
		return badCommentsNum;
	}
	public void setBadCommentsNum(int badCommentsNum) {
		this.badCommentsNum = badCommentsNum;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	public String getRegisteTime() {
		return registeTime;
	}
	public void setRegisteTime(String registeTime) {
		this.registeTime = registeTime;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public float getTrust() {
		return trust;
	}
	public void setTrust(float trust) {
		this.trust = trust;
	}
	public int getClickTimes() {
		return clickTimes;
	}
	public void setClickTimes(int clickTimes) {
		this.clickTimes = clickTimes;
	}
	public int getCommentTimes() {
		return commentTimes;
	}
	public void setCommentTimes(int commentTimes) {
		this.commentTimes = commentTimes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	//商家id
	private String _id;
	//name
	private String merchantName;
	//登陆次数
     private int loginTimes;
     //注册时间
     private String registeTime;
     //删除时间
     private String deleteDate;
     //上次登陆时间
     private String lastLogin;
     //商家信用
     private float trust;
     //点击次数
     private int clickTimes;
     //评论次数
     private int commentTimes;
     //商家状态
     private String status;
     //标记个数
     private int num;
     //好评总数
     private int goodCommentsNum;
     //差评总数
     private int badCommentsNum;
     private int praise;
}
