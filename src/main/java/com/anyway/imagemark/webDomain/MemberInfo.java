/**
 * 
 */
package com.anyway.imagemark.webDomain;


/**
 * @author Administrator
 *����ϵͳ�Ļ�Ա��Ϣ
 */
public class MemberInfo {
	
	public String getDeleDate() {
		return deleDate;
	}
	public void setDeleDate(String deleDate) {
		this.deleDate = deleDate;
	}
	public String getMemberMail() {
		return memberMail;
	}
	public void setMemberMail(String memberMail) {
		this.memberMail = memberMail;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public int getUnstatisfyComment() {
		return unstatisfyComment;
	}
	public void setUnstatisfyComment(int unstatisfyComment) {
		this.unstatisfyComment = unstatisfyComment;
	}
	public int getStatisfyComment() {
		return statisfyComment;
	}
	public void setStatisfyComment(int statisfyComment) {
		this.statisfyComment = statisfyComment;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	//会员id
	private String _id;
//会员名称
 private String memberName;
 private String memberMail;
 //登陆次数
 private  int loginTimes;
 //注册时间
 private  String registerTime;
 //上次登陆时间
 private  String  lastLoginDate;
 //删除时间
 private String deleDate;
 //会员状态̬
 private  int status;
 //会员积分
 private  int score;
 //会员等级
 private  int level;
 //�������
 private int clickTimes;
 //��������
 private int commentTimes;
 //差评数
 private int unstatisfyComment;
 private int statisfyComment;

}
