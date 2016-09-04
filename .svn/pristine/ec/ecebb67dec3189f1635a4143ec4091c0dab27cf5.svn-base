/**
 * 
 */
package com.anyway.imagemark.manage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.anyway.imagemark.daoimpl.CommentDao;
import com.anyway.imagemark.daoimpl.MarkInfoDao;
import com.anyway.imagemark.daoimpl.MemberDao;
import com.anyway.imagemark.daoimpl.MerchantDao;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.MongoHelper;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.webDomain.MemberComment;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author Kario
 * 
 */
@Repository
public class CommentManage {

	// //统计用户自己做出的评论信息--用户id
	public List<MemberComment> getComment(String mem_id) {
		DBObject query = mHelper.createQuery("mem_id", mem_id);
		List<DBObject> commentList = commentDao.searchList(query);
		List<MemberComment> myList = new ArrayList<MemberComment>();
		for (int i = 0; i < commentList.size(); i++) {
			MemberComment mComment = new MemberComment();
			Comment comment = gson.fromJson(commentList.get(i).toString(),
					Comment.class);
			String mark_idString = comment.getMark_id();
			DBObject queryObject = new BasicDBObject("_id", mark_idString);
			DBObject object = markInfoDao.search(queryObject);
			if (object != null) {
				MarkInfo markInfo = gson.fromJson(object.toString(),
						MarkInfo.class);
				mComment.setComponentLink(markInfo.getComponentLinkAddress());
				mComment.setUrl(markInfo.getUrl());
			}
			mComment.setPicName(mark_idString);
			mComment.set_id(comment.get_id());
			mComment.setComment(comment.getTextComment());
			mComment.setCommentTime(format.generateTime(
					ToolConstants.DATEFORMAT_STRING, comment.getTime()));
			// mComment.setTotalCommentTimes(markInfo.getCommentNum());
			myList.add(mComment);
		}
		return myList;
	}

	// 统计某一商品下的所有评论信息
	public PageContent<MemberComment> getMarkCommentsByMarkId(String mark_id,
			int filter, int sortType, int pageNumber, int pageSize) {
		DBObject query = mHelper.createQuery("mark_id", mark_id);
		if (filter != 0) {
			query.put("comment", filter);
		}
		DBObject sort = new BasicDBObject();
		if (sortType == 1) {
			sort.put("criticTime", 1);
		} else {
			sort.put("criticTime", -1);
		}

		PageContent<DBObject> commentList = commentDao.search(query, sort,
				pageNumber, pageSize);
		List<MemberComment> myList = new ArrayList<MemberComment>();
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		if (commentList != null) {
			for (int i = 0; i < commentList.getList().size(); i++) {
				MemberComment mComment = new MemberComment();
				Comment comment = gson.fromJson(commentList.getList().get(i)
						.toString(), Comment.class);
				String mark_idString = comment.getMark_id();
				mComment.set_id(comment.get_id());
				mComment.setComment(comment.getTextComment());
				mComment.setCommentTime(format.generateTime(
						ToolConstants.DATEFORMAT_STRING,
						comment.getTime()));
				mComment.setPicName(mark_id);
				DBObject queryObject = new BasicDBObject("_id", mark_idString);
				if (markInfoDao.search(queryObject) != null) {
					MarkInfo markInfo = gson.fromJson(
							markInfoDao.search(queryObject).toString(),
							MarkInfo.class);
					mComment.setComponentLink(markInfo
							.getComponentLinkAddress());
					mComment.setUrl(markInfo.getUrl());
				}
				// mComment.setTotalCommentTimes(markInfo.getCommentNum());
				myList.add(mComment);
			}

			pageContent.setCurrentRecords(commentList.getCurrentRecords());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(commentList.getTotalPages());
			pageContent.setTotal(commentList.getTotal());

		}
		return pageContent;
	}

	// 统计在某段时间内新增的评论
	public PageContent<MemberComment> getComments(String time1, String time2,
			int sortType, int pageNumber, int pageSize) {
		DBObject sortOrder = new BasicDBObject();
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			start = format.formatToLong(time1);
			end = format.formatToLong(time2);
		} else {
			/*start = format.generateTime(30);*/
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("time", start, end);
		query.put("status", ToolConstants.VALID_INT);
		if (sortType == 1) {
			sortOrder.put("criticTime", 1);
		} else if (sortType == 2) {
			sortOrder.put("criticTime", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = commentDao.search(query, sortOrder,
				pageNumber, pageSize);
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		if (list != null) {
			List<DBObject> objects = list.getList();
			List<MemberComment> myList = new ArrayList<MemberComment>();
			MemberDao memberDao = new MemberDao();
			for (int i = 0; i < objects.size(); i++) {
				MemberComment mComment = new MemberComment();
				Comment comment = gson.fromJson(objects.get(i).toString(),
						Comment.class);
				String mark_id = comment.getMark_id();
				String mem_id = comment.getMem_id();
				DBObject queryObject = new BasicDBObject("_id", mark_id);
				if (markInfoDao.search(queryObject) != null) {
					MarkInfo markInfo = gson.fromJson(
							markInfoDao.search(queryObject).toString(),
							MarkInfo.class);
					mComment.setComponentLink(markInfo
							.getComponentLinkAddress());
					mComment.setUrl(markInfo.getUrl());
					mComment.set_id(comment.get_id());
					// logger.info(comment.get_id());
					mComment.setComment(comment.getTextComment());
					mComment.setCommentTime(format.generateTime(
							ToolConstants.DATEFORMAT_STRING,
							comment.getTime()));
					mComment.setPicName(mark_id);
					mComment.setMemberName(memberDao.search("_id", mem_id)
							.get("memberName").toString());
				}
				// mComment.setTotalCommentTimes(markInfo.getCommentNum());
				myList.add(mComment);
			}

			pageContent.setCurrentRecords(list.getCurrentRecords());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(ToolConstants.BackGround_Default_PageSize);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

		}
		return pageContent;

	}
	public PageContent<MemberComment> deleted(DBObject query, int sortType,
			int pageNumber, int pageSize) {
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("criticTime", 1);
		} else if (sortType == 2) {
			sortOrder.put("criticTime", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = commentDao.Deleted(query, sortOrder,
				pageNumber, pageSize);
		List<MemberComment> myList = new ArrayList<MemberComment>();
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				MemberComment mComment = new MemberComment();
				Comment comment = gson.fromJson(list.getList().get(i)
						.toString(), Comment.class);
				String mark_idString = comment.getMark_id();
				logger.info(mark_idString);
				DBObject queryObject = new BasicDBObject("_id", mark_idString);
				mComment.set_id(comment.get_id());
				mComment.setComment(comment.getTextComment());
				mComment.setCommentTime(format.generateTime(
						ToolConstants.DATEFORMAT_STRING,
						comment.getTime()));
				mComment.setPicName(mark_idString);
				if (markInfoDao.search(queryObject) != null) {
					MarkInfo markInfo = gson.fromJson(
							markInfoDao.search(queryObject).toString(),
							MarkInfo.class);
					mComment.setComponentLink(markInfo
							.getComponentLinkAddress());
					mComment.setUrl(markInfo.getUrl());
				}
				// mComment.setTotalCommentTimes(markInfo.getCommentNum());
				myList.add(mComment);
			}

			pageContent.setList(myList);
			pageContent.setPageSize(pageSize);
			pageContent.setCurrentRecords(myList.size());
			pageContent.setPageNumber(pageNumber);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

		}
		return pageContent;
	}

	public CommentManage() {
		commentDao = new CommentDao();
		markInfoDao = new MarkInfoDao();
		merchantDao = new MerchantDao();
	}

	/**
	 * @author Kario 统计每月会员的增量，返回list
	 * @param year
	 *            要统计的年份；
	 * @param month
	 *            统计截止的月份(可以不要)
	 */
	public PageContent<DBObject> getIncrePerMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		List<DBObject> myList = new ArrayList<DBObject>();
		if (year < currentYear) {
			for (int i = 1; i <= 12; i++) {
				DBObject object = new BasicDBObject();
				if (i > 9) {
					object.put("date", "" + year + "/" + i);
				} else {
					object.put("date", "" + year + "/0" + i);
				}
				object.put("increaseMent",
						commentDao.getNumberOfComment(year, i));
				myList.add(object);
			}
		} else {
			for (int i = 1; i <= currentMonth; i++) {
				DBObject object = new BasicDBObject();
				if (i > 9) {
					object.put("date", "" + year + "/" + i);
				} else {
					object.put("date", "" + year + "/0" + i);
				}
				object.put("increaseMent",
						commentDao.getNumberOfComment(year, i));
				myList.add(object);
			}
		}
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent.setList(myList);
		pageContent.setTotal(myList.size());
		return pageContent;
	}

	/**
	 * @author Kario 统计系统有效评论总量及当月增量
	 * @return int
	 */
	public List<DBObject> getTotalValidComment() {
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		DBObject total = new BasicDBObject();
		total.put("total", commentDao.getNumberOfMember());
		DBObject month = new BasicDBObject();
		Calendar calendar = Calendar.getInstance();
		month.put("month",
				commentDao.getNumberOfComment(calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1)));
		dbObjects.add(total);
		dbObjects.add(month);
		return dbObjects;
	}

	public PageContent<DBObject> getTextComment(String mark_id, int page,
			int pageSize) {
		PageContent<DBObject> pageContent = commentDao.getTextComment(mark_id,
				page, pageSize);
		PageContent<DBObject> resultContent = new PageContent<DBObject>();
		if (pageContent != null) {
			List<DBObject> list = pageContent.getList();
			List<DBObject> tempList = new ArrayList<DBObject>();
			MemberDao memberDao = new MemberDao();
			for (int i = 0; i < list.size(); i++) {
				DBObject tempObject = list.get(i);
				DBObject userInfo = memberDao.search(new BasicDBObject("_id",
						tempObject.get("mem_id").toString()));
				if(userInfo!=null){
					String userName = userInfo.get("memberName").toString();
					DBObject result = new BasicDBObject();
					result.put("memberName", userName);
					if (userInfo.containsField("avalPic")) {
						result.put("avalPic", userInfo.get("avalPic").toString());
					}
					result.put("textComment", tempObject.get("textComment")
							.toString());
					result.put("time", format.getTime(Long.parseLong(tempObject.get("time").toString())));
					tempList.add(result);
				}
			}
			resultContent.setList(tempList);
			resultContent.setPageNumber(pageContent.getPageNumber());
			resultContent.setTotal(pageContent.getTotal());
			resultContent.setPageSize(pageContent.getPageSize());
			resultContent.setTotalPages(pageContent.getTotalPages());
			return resultContent;
		} else {
			return null;
		}

	}

	public static void main(String[] args) {
		CommentManage manage = new CommentManage();
		PageContent<MemberComment> comments=manage.getComments("", "", 1, 1, 10);
		logger.info(gson.toJson(comments));
	}

	private MerchantDao merchantDao = null;
	private CommentDao commentDao = null;
	private MarkInfoDao markInfoDao = null;
	private static Gson gson = new Gson();
	private static MongoHelper mHelper = new MongoHelper();
	private static DateFormat format = new DateFormat();
	private static Logger logger = Logger.getLogger(CommentManage.class);
}
