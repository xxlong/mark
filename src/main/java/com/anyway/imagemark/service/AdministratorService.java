package com.anyway.imagemark.service;

import java.util.List;

import com.anyway.imagemark.domain.Administrator;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.LogInfo;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.mongodb.DBObject;

public interface AdministratorService {

	Administrator getAdministratorByNameOrMail(String adminNameOrMail);

	int addAdministrator(Administrator administrator);

	int login(String adminName, String adminPassword, int type, String ipInfo);

	List<DBObject> getNumForMember();

	List<DBObject> getNumForMerchant();

	List<DBObject> getNumForMark();

	List<DBObject> getNumForComment();
	List<DBObject> getNumForClick();

	PageContent<DBObject> getMerchantIncreseMentPerMonth(int year, int month);

	PageContent<DBObject> getMemberIncreseMentPerMonth(int year, int month);

	PageContent<DBObject> getMarkIncreseMentPerMonth(int year, int month);

	PageContent<DBObject> getCommentIncreseMentPerMonth(int year, int month);
	PageContent<DBObject> getClickIncreseMentPerMonth(int year, int month);
	void deleteLogInfo(LogInfo logInfo);

	void addNotification(Notification notification);

	void deleteNotification(String noticeId);

	void updateNotification(DBObject condition, Notification notification);

	PageContent<Notification> queryNotificationsByType(String startTime,
			String endTime, int type, int pageNumber, int pageSize);

	Notification queryNotificationById(String _id);

	//Merchant queryMerchantByName(String merchantName);

	Merchant queryDeletedMerchantByName(String merchantName);

	void deleteMerchant(String merchantName);

	void restoreMerchant(String merchantName);

	void validateMerchant(String merchantName);

	/* void updateMerchant(Merchant merchant); */

	MarkInfo queryMarkInfoById(String _id);

	MarkInfo queryDeletedMarkInfoById(String _id);

	MarkInfo queryMarkInfoByUrlAndLink(String url, String link);

	void deleteMarkInfo(String _id);

	void restoreMarkInfo(String _id);

	void updateMarkInfo(DBObject condition, MarkInfo markInfo);

	void validateMarkInfo(String _id, int status);

	void setComplainStatus(String _id, int status);
	int changeNodeStatus(String _id, int status);
	Member queryDeletedMemberById(String memberName);

	void deleteMember(String memberName);

	void restoreMember(String memberName);

	void updateMember(Member member);

	Comment queryDeletedCommentById(String _id);

	void deleteComment(String _id);

	void restoreComment(String _id);

	void updateComment(DBObject condition, Comment comment);

	/*
	 * void deleteClickInfo(ClickInfo clickInfo); void updateClickInfo(ClickInfo
	 * clickInfo);
	 */
	PageContent<MerchantMark> getMarksManagementNotVerifiedMarks(
			String startTime, String endTime, int sortType, int pageNumber,int rows);

	PageContent<MemberInfo> getStatisticalCommentsBySpan(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize);

	PageContent<MerchantMark> getStatisticalMarksByCondition(DBObject query,
			DBObject sortOrder, int pageNumber, int pageSize);

	PageContent<MemberInfo> getAllMembersBehaviours(DBObject query,
			DBObject sortOrder, int pageNumber, int pageSize);

	PageContent<MemberInfo> getMembersBehavioursByRegisterSpan(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize);

	PageContent<MemberInfo> getMembersBehavioursByLoginSpan(String starttime,
			String endtime, int signType, int pageNumber, int pageSize);

	PageContent<MemberInfo> getMembersBehavioursByClickSpan(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize);

	public PageContent<MemberInfo> getStatisticalByScore(String time1,
			String time2, int sortType, int pageNumber, int pageSize);

	PageContent<MemberInfo> getMembersBehavioursByCommentSpan(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize);

	PageContent<MerchantInfo> getAllMerchantsBehaviours(DBObject query,
			DBObject sortObject, int pageNumber, int pageSize);

	PageContent<MerchantInfo> getMerchantsBehavioursByRegisterSpan(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize);

	PageContent<MerchantInfo> getMerchantsBehavioursByLoginSpan(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize);

	PageContent<MerchantInfo> getMerchantsBehavioursByMarkAggregate(
			String starttime, String endtime, String field, int sortType,
			int pageNumber, int pageSize);

	List<MerchantInfo> getMerchantsBehavioursByMarkCount(DBObject query,
			String sign, int pageNumber, int pageSize);

	List<MerchantInfo> getMerchantsBehavioursByClickOrCommentCount(
			String field, String sign, int pageNumber, int pageSize);

	PageContent<MerchantMark> getStatisticalMarksByComponentType(
			String starttime, String endtime, boolean flag, int componentType,
			int sortType, int pageNumber, int pageSize);

	public PageContent<MerchantMark> getStatisticalMarkBySort(String starttime,
			String endtime, String Field, int sortType, int pageNumber,
			int pageSize);

	PageContent<MemberComment> getStatisticAnalysisOnComments(String starttime,
			String endtime, int target, int sortType, int pageNumber,
			int pageSize);

	PageContent<MerchantMark> getStatisticalMarksByNewAdd(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize);

	PageContent<MerchantMark> getStatisticalMarksByFilter(String starttime,
			String endtime, String filterName, int filter, int sortType,
			int pageNumber, int pageSize);

	PageContent<MemberComment> getStatisticalCommentByNewAdd(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize);

	PageContent<Member> queryUserManagementOnNewAddedMembers(String starttime,
			String endtime, int filter, int sortType, int pageNumber,
			int pageSize);

	PageContent<DBObject> queryUserManagementOnNotVerifiedMerchants(
			int sortType, int pageNumber, int pageSize);

	PageContent<MerchantMark> getComplainedMark(String startTime,
			String endTime, int sortType, int pageNumber,int pageSize);

	PageContent<Merchant> queryUserManagementOnMerchants(int filter,
			int sortType, int pageNumber, int pageSize);

	PageContent<MerchantMark> queryDeletedMarks(String startTime,String endTime,String merchantName,int sortType, int pageNumber,
			int pageSize);

	PageContent<MemberComment> queryDeletedComments(int sortType,
			int pageNumber, int pageSize);

	PageContent<MemberInfo> queryDeletedMembers(String startTime,String endTime,String memberName,int sortType, int pageNumber,
			int pageSize);

	PageContent<MerchantInfo> queryDeletedMerchants(String startTime,String endTime,String merchantName,int sortType,
			int pageNumber, int pageSize);

	PageContent<MerchantMark> getStatisticalMarksByMerchant(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize);

	PageContent<MemberComment> getMarkCommentsByMarkId(String mark_id,
			int filter, int sortType, int pageNumber, int pageSize);
	PageContent<DBObject> getStatisticalClick(String starttime, String endtime, int sortType,int  pageNumber, int pageSize);
	PageContent<DBObject> getMarkIncreseByTypeMentPerMonth(int year,int month);
	PageContent<DBObject> getVerifyUrl(int page,int pageSize);
	int checkPassword(String name,String pwd);
	int  updatePassword(String name,String oldPwd,String newPwd);
	PageContent<DBObject> getTextComment(String mark_id,int page,int pageSize);
	
	
	//*************************************KG********************************************
	
	public boolean changepasswd(String username,String newPwd);
	public String generateActiveLink(String url,String username,String randomCode);
	public boolean isMailValid(String username,String checkCode);
	public void activeAdministrator(String username,int status) ;
	public Merchant queryMerchantByName(String merchantName,int status) ;
	//*************************************KG********************************************
}
