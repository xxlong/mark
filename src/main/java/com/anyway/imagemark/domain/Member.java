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
public class Member {
	


	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getAvalPic() {
		return avalPic;
	}

	public void setAvalPic(String avalPic) {
		this.avalPic = avalPic;
	}

	public long getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(long deleteDate) {
		this.deleteDate = deleteDate;
	}

/*	public int getGoodComments() {
		return goodComments;
	}

	public void setGoodComments(int goodComments) {
		this.goodComments = goodComments;
	}

	public int getBadComments() {
		return badComments;
	}

	public void setBadComments(int badComments) {
		this.badComments = badComments;
	}
*/
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	public String getMemberMail() {
		return memberMail;
	}

	public void setMemberMail(String memberMail) {
		this.memberMail = memberMail;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getMemberScore() {
		return memberScore;
	}

	public void setMemberScore(int memberScore) {
		this.memberScore = memberScore;
	}

	public int getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}
	//_id
	private String  _id;
	// 会员名称或昵称
	@Size(min=3,max=20,message="memberName must be between 3 and 20 characters long.")
	@Pattern(regexp="^[a-zA-Z0-9]+$",message="memberName must be alphanumeric with no spaces")
	private String memberName;
	// 会员密码
	@Size(min=6,message="The password must be at least 6 characters long.")
	private String memberPassword;
	// 会员邮箱-----该字段唯一
	//@Pattern(regexp="\\w+@(\\w+\\.){1,3}\\w+",message="Invalid email address.")
	private String memberMail;
	// 注册时间
	private long date;
	//删除时间
	private long deleteDate;
	// 会员积分
	private int memberScore;
	// 会员等级
	private int memberLevel;
	//会员状态
	private int  status;
	/*//会员好评数
	private int goodComments;
	//会员差评数
	private int badComments;*/
	private String avalPic;
	
    // 随机码(激活帐户与生成重设密码链接时使用)  
    private String randomCode;

	public String getRandomCode() {
		return randomCode;
	}
	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}  
}
