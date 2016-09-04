package com.anyway.imagemark.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.anyway.imagemark.controller.AdministratorController;
import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.daoimpl.AdministratorDao;
import com.anyway.imagemark.daoimpl.ClickInfoDao;
import com.anyway.imagemark.daoimpl.CommentDao;
import com.anyway.imagemark.daoimpl.ComplainDao;
import com.anyway.imagemark.daoimpl.MarkInfoDao;
import com.anyway.imagemark.daoimpl.MemberDao;
import com.anyway.imagemark.daoimpl.MerchantDao;
import com.anyway.imagemark.daoimpl.NodeDao;
import com.anyway.imagemark.daoimpl.NotificationDao;
import com.anyway.imagemark.domain.Administrator;
import com.anyway.imagemark.domain.ClickInfo;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.LogInfo;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.manage.CommentManage;
import com.anyway.imagemark.manage.MarkManage;
import com.anyway.imagemark.manage.MemberManage;
import com.anyway.imagemark.manage.MerchantManage;
import com.anyway.imagemark.service.AdministratorService;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.MongoHelper;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberFoot;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Service
public class AdministratorServiceImpl implements AdministratorService {
	private static Logger log = Logger
			.getLogger(AdministratorServiceImpl.class);
	@Autowired
	@Qualifier("administratorDao")
	private BasicDao basicDao;
	@Autowired
	private CommentManage commentManage;
	@Autowired
	private MemberManage memberManage;
	@Autowired
	private MerchantManage merchantManage;
	@Autowired
	private MarkManage markManage;
	private AdministratorDao administratorDao = new AdministratorDao();
	private NotificationDao notificationDao = new NotificationDao();
	private MarkInfoDao markInfoDao = new MarkInfoDao();
	private MerchantDao merchantDao = new MerchantDao();
	private MemberDao memberDao = new MemberDao();
	private ClickInfoDao clickInfoDao = new ClickInfoDao();
	private CommentDao commentDao = new CommentDao();
	private NodeDao nodeDao = new NodeDao();
	private static Gson gson = new Gson();

	public void deleteLogInfo(LogInfo logInfo) {
		// TODO Auto-generated method stub

	}

	public void addNotification(Notification notification) {
		// TODO Auto-generated method stub
		notificationDao.save(notification);
	}

	public void deleteNotification(String noticeId) {
		// TODO Auto-generated method stub
		notificationDao.delete(noticeId);
	}

	public void updateNotification(DBObject condition, Notification notification) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		notificationDao.update(condition,
				(DBObject) JSON.parse(gson.toJson(notification)));
	}

/*	public Merchant queryMerchantByName(String merchantName) {
		// TODO Auto-generated method stub
		return merchantDao.getMerchantByNameOrMail(merchantName);
	}*/

	public Merchant queryDeletedMerchantByName(String merchantName) {
		// TODO Auto-generated method stub
		return merchantDao.queryDeletedMerchantByName(merchantName);
	}

	public void deleteMerchant(String merchantName) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("merchantName", merchantName);
		merchantDao.deleteByCondition(condition);
	}

	public void restoreMerchant(String merchantName) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("merchantName", merchantName);
		merchantDao.Restore(condition);
	}

	public void validateMerchant(String merchantName) {
		// TODO Auto-generated method stub
		merchantDao.setStatusMerchant(merchantName);
	}

	public MarkInfo queryMarkInfoById(String _id) {
		// TODO Auto-generated method stub
		return markInfoDao.queryMarkInfoByMarkId(_id);
	}

	public MarkInfo queryMarkInfoByUrlAndLink(String url, String link) {
		String mark_id = markInfoDao.get_id(url, link);
		return markInfoDao.queryMarkInfoByMarkId(mark_id);
	}

	public MarkInfo queryDeletedMarkInfoById(String _id) {
		// TODO Auto-generated method stub
		return markInfoDao.queryDeletedMarkInfoByMarkId(_id);
	}

	public void deleteMarkInfo(String _id) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("_id", _id);
		markInfoDao.deleteByCondition(condition);
	}

	public void restoreMarkInfo(String _id) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("_id", _id);
		markInfoDao.Restore(condition);
	}

	public void updateMarkInfo(DBObject condition, MarkInfo markInfo) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		markInfoDao.update(condition,
				(DBObject) JSON.parse(gson.toJson(markInfo)));
	}

	public Member queryDeletedMemberById(String memberName) {
		// TODO Auto-generated method stub
		return memberDao.queryDeletedMemberByName(memberName);
	}

	public void deleteMember(String memberName) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("memberName", memberName);
		memberDao.deleteByCondition(condition);
	}

	public void restoreMember(String memberName) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("memberName", memberName);
		memberDao.Restore(condition);
	}

	public void updateMember(Member member) {
		// TODO Auto-generated method stub

	}

	public Comment queryDeletedCommentById(String _id) {
		// TODO Auto-generated method stub
		return commentDao.queryDeletedCommentById(_id);
	}

	public void deleteComment(String _id) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("_id", _id);
		commentDao.deleteByCondition(condition);
	}

	public void restoreComment(String _id) {
		// TODO Auto-generated method stub
		DBObject condition = new BasicDBObject();
		condition.put("_id", _id);
		commentDao.Restore(condition);
	}

	public int changeNodeStatus(String _id, int status) {
		int result = 0;
		DBObject query = new BasicDBObject("nodeId", _id);
		if (status == ToolConstants.VALID_INT) {
			result = nodeDao.validateNode(_id, status);
		} else if (status == ToolConstants.DELETE_INT) {
			nodeDao.deleteByCondition(query);
		} else {

		}
		return result;
	}

	public void updateComment(DBObject condition, Comment comment) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		commentDao.update(condition,
				(DBObject) JSON.parse(gson.toJson(comment)));
	}

	public PageContent<MemberInfo> getStatisticalByScore(String time1,
			String time2, int sortType, int pageNumber, int pageSize) {
		return memberManage.getMemberBehaviourByScore(time1, time2, sortType,
				pageNumber, pageSize);
	}

	public PageContent<MemberInfo> getStatisticalCommentsBySpan(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMembersBehavioursByCommentSpan(starttime,
				endtime, sortType, pageNumber, pageSize);
		// return commentManage.getComments(starttime, endtime, sortType,
		// pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarksByCondition(
			DBObject query, DBObject sortOrder, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return markManage.getStatisticalMarksByCondition(query, sortOrder,
				false, pageNumber, pageSize);
	}

	public PageContent<MemberInfo> getAllMembersBehaviours(DBObject query,
			DBObject sortOrder, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getAllMembersBehaviours(query, sortOrder,
				pageNumber, pageSize);
	}

	public PageContent<MemberInfo> getMembersBehavioursByRegisterSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMembersBehavioursByRegisterSpan(time1, time2,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MemberInfo> getMembersBehavioursByLoginSpan(
			String time1, String time2, int signType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMembersBehavioursByLoginSpan(time1, time2,
				signType, pageNumber, pageSize);
	}

	public PageContent<MemberInfo> getMembersBehavioursByClickSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMembersBehavioursByClickSpan(time1, time2,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MemberInfo> getMembersBehavioursByCommentSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMembersBehavioursByCommentSpan(time1, time2,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantInfo> getAllMerchantsBehaviours(DBObject query,
			DBObject sortObject, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return merchantManage.getAllMerchantsBehaviours(query, sortObject,
				pageNumber, pageSize);
	}

	public PageContent<MerchantInfo> getMerchantsBehavioursByRegisterSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return merchantManage.getMerchantsBehavioursByRegisterSpan(time1,
				time2, sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantInfo> getMerchantsBehavioursByLoginSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// TODO Auto-generated method stub
		return merchantManage.getMerchantsBehavioursByLoginSpan(time1, time2,
				sortType, pageNumber, pageSize);
	}

	public List<MerchantInfo> getMerchantsBehavioursByMarkCount(DBObject query,
			String sign, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public PageContent<MerchantInfo> getMerchantsBehavioursByMarkAggregate(
			String starttime, String endtime, String field, int sortType,
			int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return merchantManage.getMerchantsBehavioursByMarkAggregate(starttime,
				endtime, field, sortType, pageNumber, pageSize);

	}

	public List<MerchantInfo> getMerchantsBehavioursByClickOrCommentCount(
			String field, String sign, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public PageContent<MerchantMark> getStatisticalMarksByComponentType(
			String starttime, String endtime, boolean flag, int componentType,
			int sortType, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return markManage.getStatisticalMarksByComponentType(starttime,
				endtime, flag, componentType, sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarkBySort(String starttime,
			String endtime, String Field, int sortType, int pageNumber,
			int pageSize) {
		return markManage.getStatisticalMarkBySort(starttime, endtime, Field,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarksByNewAdd(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize) {
		return markManage.getStatisticalMarksByNewAdd(starttime, endtime,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarksFilter(
			String starttime, String endtime, String filterName, int filter,
			int sortType, int pageNumber, int pageSize) {
		return markManage.getStatisticalMarksByFilter(starttime, endtime,
				filterName, filter, sortType, pageNumber, pageSize);
	}

	public PageContent<MemberComment> getStatisticalCommentByNewAdd(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize) {
		return commentManage.getComments(starttime, endtime, sortType,
				pageNumber, pageSize);
	}

	public PageContent<Member> queryUserManagementOnNewAddedMembers(
			String starttime, String endtime, int filter, int sortType,
			int pageNumber, int pageSize) {
		return memberManage.getMemberByNewAdd(starttime, endtime, filter,
				sortType, pageNumber, pageSize);
	}

	public PageContent<DBObject> queryUserManagementOnNotVerifiedMerchants(
			int sortType, int pageNumber, int pageSize) {
		return merchantManage.getMerchantByNotVerify(sortType, pageNumber,
				pageSize);
	}

	public PageContent<Merchant> queryUserManagementOnMerchants(int filter,
			int sortType, int pageNumber, int pageSize) {
		return merchantManage.getMerchantByTrust(filter, sortType, pageNumber,
				pageSize);
	}

	public PageContent<MerchantMark> queryDeletedMarks(String startTime,
			String endTime, String merchantName, int sortType, int pageNumber,
			int pageSize) {
		return markManage.deleted(startTime, endTime, merchantName, sortType,
				pageNumber, pageSize);
	}

	public PageContent<MemberComment> queryDeletedComments(int sortType,
			int pageNumber, int pageSize) {
		return commentManage.deleted(null, sortType, pageNumber, pageSize);
	}

	public PageContent<MemberInfo> queryDeletedMembers(String startTime,
			String endTime, String memberName, int sortType, int pageNumber,
			int pageSize) {
		return memberManage.deleted(startTime, endTime, memberName, sortType,
				pageNumber, pageSize);
	}

	public PageContent<MerchantInfo> queryDeletedMerchants(String startTime,
			String endTime, String merchantName, int sortType, int pageNumber,
			int pageSize) {
		return merchantManage.deleted(startTime, endTime, merchantName,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MemberComment> getStatisticAnalysisOnComments(
			String starttime, String endtime, int target, int sortType,
			int pageNumber, int pageSize) {
		// 1是会员,2是商家
		if (target == 1) {

		}
		return null;
	}

	public PageContent<MerchantMark> getStatisticalMarksByFilter(
			String starttime, String endtime, String filterName, int filter,
			int sortType, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return markManage.getStatisticalMarksByFilter(starttime, endtime,
				filterName, filter, sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarksByMerchant(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize) {
		return markManage.getMarkByMerchant(starttime, endtime, sortType,
				pageNumber, pageSize);
	}

	public int login(String adminName, String adminPassword, int type,
			String ipInfo) {
		// TODO Auto-generated method stub
		return administratorDao.login(adminName, adminPassword, type, ipInfo);
	}

	public Administrator getAdministratorByNameOrMail(String adminNameOrMail) {
		// TODO Auto-generated method stub
		return administratorDao.getAdministratorByNameOrMail(adminNameOrMail);
	}

	public int addAdministrator(Administrator administrator) {
		// TODO Auto-generated method stub
		return administratorDao.save(administrator);
	}

	public PageContent<Notification> queryNotificationsByType(String startTime,
			String endTime, int type, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		log.info("execute AdministratorServiceImpl--queryNotificationsByType");
		DBObject condition = new BasicDBObject();
		DBObject sortOrder = new BasicDBObject();
		DateFormat format = new DateFormat();
		MongoHelper mHelper = new MongoHelper();
		long start = 0;
		long end = 0;
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			start = format.formatToLong(startTime);
			end = format.formatToLong(endTime);
		} else {
			start = format.generateTime(30);
			end = System.currentTimeMillis();
		}
		condition = mHelper.timeScope("sendTime", start, end);
		condition.put("status", ToolConstants.VALID_INT);
		if (type != 0) {
			condition.put("type", type);
		}
		sortOrder.put("sendTime", -1);
		List<Notification> notificationList = new ArrayList<Notification>();
		PageContent<DBObject> list = notificationDao.search(condition,
				sortOrder, pageNumber, pageSize);
		PageContent<Notification> pageContent = new PageContent<Notification>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				/*
				 * Notification notification=gson.fromJson(list.getList().get(i)
				 * .toString(), Notification.class); DBObject object=new
				 * BasicDBObject(); object.put("_id",notification.get_id());
				 * object.put("notifier",notification.getNotifier());
				 * object.put("", )
				 */
				notificationList.add(gson.fromJson(list.getList().get(i)
						.toString(), Notification.class));
			}
			pageContent.setCurrentRecords(notificationList.size());
			pageContent.setList(notificationList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());
		} else {
			log.info("The query result is null");
		}
		return pageContent;
	}

	public Notification queryNotificationById(String _id) {
		// TODO Auto-generated method stub
		DBObject dbObject = notificationDao.search("_id", _id);
		return gson.fromJson(dbObject.toString(), Notification.class);
	}

	public PageContent<MemberComment> getMarkCommentsByMarkId(String mark_id,
			int filter, int sortType, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return commentManage.getMarkCommentsByMarkId(mark_id, filter, sortType,
				pageNumber, pageSize);
	}

	public void validateMarkInfo(String _id, int status) {
		// TODO Auto-generated method stub
		DBObject dbObject = new BasicDBObject();
		dbObject.put("_id", _id);
		markInfoDao.setMarkStatus(dbObject, status);
	}

	public void setComplainStatus(String _id, int status) {
		ComplainDao dao = new ComplainDao();
		dao.changeStatus(_id, status);
	}

	public PageContent<MerchantMark> getMarksManagementNotVerifiedMarks(
			String startTime, String endTime, int sortType, int pageNumber,
			int pageSize) {
		// 1为up,2为down
		return markManage.getStatisticalMarkNotVerify(startTime, endTime,
				sortType, pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getComplainedMark(String startTime,
			String endTime, int sortType, int pageNumber, int pageSize) {
		return markManage.getComplainedMark(startTime, endTime, sortType,
				pageNumber, pageSize);
	}

	/**
	 * @author Kario 统计系统有效会员总量及当前会员增量
	 * @return List
	 */
	public List<DBObject> getNumForMember() {
		return memberManage.getTotalValidMember();
	}

	/**
	 * @author Kario统计系统有效会员总量及当前会员增量
	 * @return List<DBobject>
	 */
	public List<DBObject> getNumForMerchant() {
		return merchantManage.getTotalValidMerchant();
	}

	/**
	 * @author Kario统计系统有效会员总量及当前会员增量
	 * @return List<DBobject>
	 */
	public List<DBObject> getNumForMark() {
		return markManage.getTotalValidMark();
	}

	/**
	 * @author Kario统计系统有效评论总量及当前评论增量
	 * @return List<DBobject>
	 */
	public List<DBObject> getNumForComment() {
		return commentManage.getTotalValidComment();
	}

	public List<DBObject> getNumForClick() {
		return memberManage.getTotalValidClick();
	}

	/**
	 * @author Kario统计系统每月增量
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return List<DBobject>
	 */
	public PageContent<DBObject> getMerchantIncreseMentPerMonth(int year,
			int month) {
		return merchantManage.getIncrePerMonth(year, month);
	}

	public PageContent<DBObject> getMemberIncreseMentPerMonth(int year,
			int month) {
		return memberManage.getIncrePerMonth(year, month);
	}

	public PageContent<DBObject> getMarkIncreseMentPerMonth(int year, int month) {
		return markManage.getIncrePerMonth(year, month);
	}

	public PageContent<DBObject> getCommentIncreseMentPerMonth(int year,
			int month) {
		return commentManage.getIncrePerMonth(year, month);
	}

	public PageContent<DBObject> getClickIncreseMentPerMonth(int year, int month) {
		return memberManage.getIncreClickPerMonth(year, month);
	}

	public PageContent<DBObject> getStatisticalClick(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize) {
		return memberManage.getClickDomain(starttime, endtime, sortType,
				pageNumber, pageSize);
	}

	public PageContent<DBObject> getMarkIncreseByTypeMentPerMonth(int year,
			int month) {
		return markManage.getIncreaseMentByType(year, month);
	}

	public PageContent<DBObject> getVerifyUrl(int page, int pageSize) {
		return markManage.getVerifyUrl(page, pageSize);
	}

	public int checkPassword(String name, String pwd) {
		DBObject queryDbObject = new BasicDBObject();
		queryDbObject.put("adminName", name);
		queryDbObject.put("adminPassword", pwd);
		if (administratorDao.search(queryDbObject) != null)
			return ToolConstants.ResultStatus_Success;
		else
			return ToolConstants.ResultStatus_Fail_Password;
	}

	public int updatePassword(String name, String oldPwd, String newPwd) {
		DBObject queryDbObject = new BasicDBObject();
		queryDbObject.put("adminName", name);
		queryDbObject.put("adminPassword", oldPwd);
		DBObject object = administratorDao.search(queryDbObject);
		if (object != null) {
			int role = Integer.parseInt(object.get("role").toString());
			if (role == 10) {
				// 管理員
				administratorDao.update(queryDbObject, new BasicDBObject(
						"$set", new BasicDBObject("adminPassword", newPwd)));
			} else if (role == 2) {
				// 商家
				DBObject queryMerchant = new BasicDBObject();
				queryMerchant.put("merchantName", name);
				merchantDao.update(queryMerchant, new BasicDBObject("$set",
						new BasicDBObject("merchantPassword", newPwd)));
				administratorDao.update(queryDbObject, new BasicDBObject(
						"$set", new BasicDBObject("adminPassword", newPwd)));
			} else {
				// 會員
				DBObject queryMember = new BasicDBObject();
				queryMember.put("memberName", name);
				merchantDao.update(queryMember, new BasicDBObject("$set",
						new BasicDBObject("memberPassword", newPwd)));
				administratorDao.update(queryDbObject, new BasicDBObject(
						"$set", new BasicDBObject("memberPassword", newPwd)));
			}
			return ToolConstants.ResultStatus_Success;
		} else {
			return ToolConstants.ResultStatus_NoData;
		}
	}
	public PageContent<DBObject> getTextComment(String mark_id,int page,int pageSize){
		return commentManage.getTextComment(mark_id, page, pageSize);
	}
	
	//*************************************KG********************************************

	//激活用户
	public void activeAdministrator(String username,int status) {
		administratorDao.activeAdministrator(username,status);
	}
	
	//修改密码
	public boolean changepasswd(String username,String newPwd){
		return administratorDao.changePwd_AM(username, newPwd);
	}

	//生成邮件链接
	public String generateActiveLink(String url, String username, String randomCode) {
		return administratorDao.generateActiveLink(url, username, randomCode);
	}
	
	//判断邮件链接是否有效
	public boolean isMailValid(String username,String checkCode){
		Administrator administrator = administratorDao.isUserExist(username);
		System.out.println("**************AdministratorServiceImpl 694************"+administrator);
		if(administrator!=null&&administratorDao.isCheckCodeValid(administrator,checkCode)){
			System.out.println("*********AdministratorServiceImpl696***********");
			return true;
		}
		return false;
	}
	
	public Merchant queryMerchantByName(String merchantName,int status) {
		// TODO Auto-generated method stub
		return merchantDao.getMerchantByNameOrMail(merchantName,status);
	}
	//*************************************KG********************************************
}
