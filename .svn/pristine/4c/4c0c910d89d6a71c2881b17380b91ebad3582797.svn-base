/**
 * 
 */
package com.anyway.imagemark.manage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.NewBSONDecoder;
import org.bson.types.ObjectId;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.daoimpl.ClickInfoDao;
import com.anyway.imagemark.daoimpl.CommentDao;
import com.anyway.imagemark.daoimpl.LogInfoDao;
import com.anyway.imagemark.daoimpl.MarkInfoDao;
import com.anyway.imagemark.daoimpl.MemberDao;
import com.anyway.imagemark.domain.ClickInfo;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.MongoHelper;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberFoot;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.google.gson.Gson;
import com.lowagie.text.xml.simpleparser.EntitiesToSymbol;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Administrator
 * 
 */
@Repository
public class MemberManage {
	private MemberDao memberDao = null;
	private LogInfoDao logInfoDao = null;
	private ClickInfoDao cDao = null;
	private CommentDao commentDao = null;
	private MarkInfoDao markInfoDao = null;
	private DateFormat format = new DateFormat();
	private static MongoHelper mHelper = new MongoHelper();
	private static Gson gson = new Gson();
	private static Logger logger = Logger.getLogger(MemberManage.class);

	// 由用户名得到会员信息
	public MemberInfo getInfo(String name) {
		DBObject object = memberDao.search(mHelper.createQuery("memberName",
				name));
		String _idString = object.get("_id").toString();
		Member member = gson.fromJson(object.toString(), Member.class);
		MemberInfo info = new MemberInfo();
		info.set_id(_idString);
		info.setMemberName(name);
		info.setMemberMail(member.getMemberMail());
		info.setRegisterTime(format.generateTime(
				ToolConstants.DATEFORMAT_STRING, new Date(member.getDate())));
		info.setDeleDate(format.generateTime(ToolConstants.DATEFORMAT_STRING,
				new Date(member.getDeleteDate())));
		info.setLevel(member.getMemberLevel());
		info.setScore(member.getMemberScore());
		info.setStatus(member.getStatus());
		info.setLoginTimes(logInfoDao.getLoginTimes(_idString));
		if (logInfoDao.getLastTime(_idString) != 0) {
			info.setLastLoginDate(format.generateTime(
					ToolConstants.DATEFORMAT_STRING,
					new Date(logInfoDao.getLastTime(_idString))));
		} else {
			info.setLastLoginDate("该用户没有登录记录");
		}
		info.setClickTimes(cDao.getClickTime(_idString));
		info.setCommentTimes(commentDao.getCommentTimes(_idString));
		/*info.setUnstatisfyComment(member.getBadComments());
		info.setStatisfyComment(member.getGoodComments());*/
		return info;
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
						memberDao.getNumberOfMemberByMonth(year, i));
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
						memberDao.getNumberOfMemberByMonth(year, i));
				myList.add(object);
			}
		}
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent.setList(myList);
		pageContent.setTotal(myList.size());
		return pageContent;
	}

	/**
	 * @author Kario 统计系统有效会员总量及当月增量
	 * @return int
	 */
	public List<DBObject> getTotalValidMember() {
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		DBObject total = new BasicDBObject();
		total.put("total", memberDao.getNumberOfMember());
		DBObject month = new BasicDBObject();
		Calendar calendar = Calendar.getInstance();
		month.put("month",
				memberDao.getNumberOfMemberByMonth(calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1)));
		dbObjects.add(total);
		dbObjects.add(month);
		return dbObjects;
	}

	/**
	 * @author Kario 统计系统有效点击总量及当月增量
	 * @return int
	 */
	public List<DBObject> getTotalValidClick() {
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		DBObject total = new BasicDBObject();
		total.put("total", cDao.getNumberOfClick());
		DBObject month = new BasicDBObject();
		Calendar calendar = Calendar.getInstance();
		month.put(
				"month",
				cDao.getNumberOfClick(calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1)));
		dbObjects.add(total);
		dbObjects.add(month);
		return dbObjects;
	}

	public PageContent<DBObject> getIncreClickPerMonth(int year, int month) {
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
				object.put("increaseMent", cDao.getNumberOfClick(year, i));
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
				object.put("increaseMent", cDao.getNumberOfClick(year, i));
				myList.add(object);
			}
		}
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent.setList(myList);
		pageContent.setTotal(myList.size());
		return pageContent;
	}

	// 统计本用户的点击信息----分页、排序是针对clickinfo中信息--对点击信息统计
	public PageContent<MemberFoot> getMemberStatisticalClicks(
			String memberName, int sortField, int sortType, int pageNumber,
			int pageSize) {
		AggregationOutput output = null;
		DBObject object = memberDao.search(mHelper.createQuery("memberName",
				memberName));
		String _idString = object.get("_id").toString();
		DBObject query = mHelper.createQuery("mem_id", _idString);
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortOrder = new BasicDBObject();
		AggregationOutput out = null;
		out = cDao.clickTime("mark_id", query, "", 0, 0);
		int total = 0;
		int totalPage = 0;
		if (out != null) {
			for (DBObject obj : out.results()) {
				total++;
			}
		}
		logger.info("the total is: " + total);
		if (total / pageSize == 0)
			totalPage = total / pageSize;
		else
			totalPage = total / pageSize + 1;
		if (sortType == 1) {
			output = cDao.clickTime("mark_id", query, "asc", pageSize,
					(pageNumber - 1) * pageSize);
			return setFoots(output, pageNumber, pageSize, total);
		} else if (sortType == 2) {
			output = cDao.clickTime("mark_id", query, "des", pageSize,
					(pageNumber - 1) * pageSize);
			return setFoots(output, pageNumber, pageSize, total);
		} else {
			logger.info("the other");
			return null;
		}
	}

	// 获取我的评论--由用户名得到---排序只有时间
	public PageContent<MemberComment> getMemberStatisticalComments(
			String memberName, int filter, int sortType, int pageNumber,
			int pageSize) {
		DBObject sortOrder = new BasicDBObject();
		PageContent<DBObject> myList = null;
		DBObject object = memberDao.search(mHelper.createQuery("memberName",
				memberName));
		String _idString = object.get("_id").toString();
		logger.info(_idString);
		DBObject query = mHelper.createQuery("mem_id", _idString);
		logger.info(query.toString());
		/*if (filter == 1) {
			logger.info("the default sort");
		} else if (filter == 2) {
			query.put("comment", 1);
		} else if (filter == 3) {
			query.put("comment", 2);
		} else if (filter == 4) {
			query.put("comment", 3);
		} else {
			logger.info("the other");
		}*/
		if (sortType == 1) {
			sortOrder.put("time", 1);
			myList = commentDao.search(query, sortOrder, pageNumber, pageSize);
		} else if (sortType == 2) {
			sortOrder.put("time", -1);
			myList = commentDao.search(query, sortOrder, pageNumber, pageSize);
		} else {
			sortOrder = null;
			myList = commentDao.search(query, sortOrder, pageNumber, pageSize);
		}
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		if (myList != null) {
			List<MemberComment> list = new ArrayList<MemberComment>();
			for (int i = 0; i < myList.getList().size(); i++) {
				MemberComment mComment = new MemberComment();
				Comment comment = gson.fromJson(myList.getList().get(i)
						.toString(), Comment.class);
				mComment.setCommentTime(format.generateTime(
						ToolConstants.DATEFORMAT_STRING,
						new Date(comment.getTime())));
				mComment.setComment(comment.getTextComment());;
				mComment.set_id(comment.get_id());
				String mark_id = comment.getMark_id();
				mComment.setPicName(mark_id);
				DBObject condition = new BasicDBObject();
				condition.put("_id", mark_id);
				logger.info(mark_id);
				DBObject object2 = markInfoDao.search(condition);
				if (object2 != null) {
					MarkInfo markInfo = gson.fromJson(object2.toString(),
							MarkInfo.class);
					mComment.setUrl(markInfo.getUrl());
					mComment.setComponentLink(markInfo
							.getComponentLinkAddress());
					list.add(mComment);
				}
				// mComment.setTotalCommentTimes(markInfo.getCommentNum());

			}
			pageContent.setCurrentRecords(list.size());
			pageContent.setList(list);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(myList.getTotalPages());
			pageContent.setTotal(myList.getTotal());
			return pageContent;
		} else {
			return null;
		}
	}

	public PageContent<MemberFoot> setFoots(AggregationOutput out,
			int pageNumber, int pageSize, int total) {
		if (out != null) {
			List<MemberFoot> list = new ArrayList<MemberFoot>();
			int currentRecords = 0;
			for (DBObject result : out.results()) {
				// _id ---mark_id
				currentRecords = currentRecords + 1;
				MemberFoot foot = new MemberFoot();
				String _id = result.get("_id").toString();
				// 统计数
				int count = Integer.valueOf(result.get("count").toString());
				DBObject query = mHelper.createQuery("_id", _id);
				query.put("status", ToolConstants.VALID_INT);
				DBObject object = markInfoDao.search(query);
				if (object != null) {
					MarkInfo markInfo = gson.fromJson(object.toString(),
							MarkInfo.class);
					foot.setUrl(markInfo.getUrl());
					foot.setComponentLink(markInfo.getComponentLinkAddress());
					foot.setClickTime(format.generateTime(
							"yyyy/MM/dd hh:mm:ss", cDao.lastClickDate(_id)));
					foot.setTotalClickTime(count);
					foot.setPicName(_id);
				}
				list.add(foot);
			}
			PageContent<MemberFoot> pageContent = new PageContent<MemberFoot>();
			pageContent.setCurrentRecords(currentRecords);
			pageContent.setList(list);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotal(currentRecords);
			pageContent.setTotal(total);
			return pageContent;
		} else {
			return null;
		}
	}

	// sign 为排序方式
	public PageContent<MemberInfo> getAllMembersBehaviours(DBObject query,
			DBObject sortOrder, int pageNumber, int pageSize) {
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		PageContent<DBObject> list = memberDao.search(query, sortOrder,
				pageNumber, pageSize);
		for (int i = 0; i < list.getList().size(); i++) {
			String nameString = list.getList().get(i).get("memberName")
					.toString();
			if (nameString != "" && nameString != null) {
				MemberInfo info = getInfo(nameString);
				myList.add(info);
			} else {
				logger.info("文档不存在");
			}
		}
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		pageContent.setCurrentRecords(myList.size());
		pageContent.setList(myList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(list.getTotalPages());
		pageContent.setTotal(list.getTotal());
		return pageContent;
	}

	// 注册时间
	public PageContent<Member> getMemberByNewAdd(String time1, String time2,
			int filter, int sortType, int pageNumber, int pageSize) {
		DBObject sortOrder = new BasicDBObject();
		DBObject query = new BasicDBObject();
		long startTime = 0;
		long endTime = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			startTime = format.formatToLong(time1);
			endTime = format.formatToLong(time2);
		} else {
			//startTime = format.generateTime(30);
			endTime=System.currentTimeMillis()+(long)86400000;
		}
		query = mHelper.timeScope("date", startTime, endTime);
		query.put("status", ToolConstants.VALID_INT);
		if (filter == 0) {

		} else if (filter == 1) {
			query.put("memberScore", new BasicDBObject("$lt", 50));
		} else if (filter == 2) {
			query.put("memberScore", new BasicDBObject().append("$gt", 50)
					.append("$lt", 300));
		} else if (filter == 3) {
			query.put("memberScore", new BasicDBObject().append("$gt", 300)
					.append("$lt", 1000));
		} else {
			query.put("memberScore", new BasicDBObject("$gt", 1000));
		}
		if (sortType == 1) {
			sortOrder.put("date", 1);
		} else if (sortType == 2) {
			sortOrder.put("date", -1);
		} else {
			sortOrder = null;
		}
		List<Member> myList = new ArrayList<Member>();
		PageContent<DBObject> list = memberDao.search(query, sortOrder,
				pageNumber, pageSize);
		PageContent<Member> pageContent = new PageContent<Member>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				myList.add(gson.fromJson(list.getList().get(i).toString(),
						Member.class));
			}

			pageContent.setCurrentRecords(list.getCurrentRecords());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

		}
		return pageContent;
	}

	/**
	 * @author kario 按会员积分排序
	 * @param time1
	 *            注册开始时间
	 * @param time2
	 *            注册结束时间
	 * @param sortType
	 *            排序方式
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页大小
	 */
	public PageContent<MemberInfo> getMemberBehaviourByScore(String time1,
			String time2, int sortType, int pageNumber, int pageSize) {
		DBObject sortOrder = new BasicDBObject();
		DBObject query = new BasicDBObject();
		long startTime = 0;
		long endTime = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			startTime = format.formatToLong(time1);
			endTime = format.formatToLong(time2);
		} else {
			//startTime = format.generateTime(30);
			endTime=System.currentTimeMillis()+(long)86400000;
		}
		logger.info("startime: " + startTime + "endtime: " + endTime);
		query = mHelper.timeScope("date", startTime, endTime);
		query.put("status", ToolConstants.VALID_INT);
		if (sortType == 1) {
			sortOrder.put("memberScore", 1);
		} else if (sortType == 2) {
			sortOrder.put("memberScore", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = memberDao.search(query, sortOrder,
				pageNumber, pageSize);
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				String name = list.getList().get(i).get("memberName")
						.toString();
				if (name != "" && name != null) {
					MemberInfo info = getInfo(name);
					myList.add(info);
				} else {
					logger.info("文档不存在");
				}
			}

			pageContent.setCurrentRecords(myList.size());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

		}
		return pageContent;
	}

	// 按注册时间来统计会员---传递的时间
	public PageContent<MemberInfo> getMembersBehavioursByRegisterSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// 对time1，time2需要进行判断，只有年、月、日？
		DBObject sortOrder = new BasicDBObject();
		DBObject query = new BasicDBObject();
		long startTime = 0;
		long endTime = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			startTime = format.formatToLong(time1);
			endTime = format.formatToLong(time2);
		} else {
			//startTime = format.generateTime(30);
			endTime=System.currentTimeMillis()+(long)86400000;
		}
		logger.info("startime: " + startTime + "endtime: " + endTime);
		query = mHelper.timeScope("date", startTime, endTime);
		query.put("status", ToolConstants.VALID_INT);
		if (sortType == 1) {
			sortOrder.put("date", 1);
		} else if (sortType == 2) {
			sortOrder.put("date", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = memberDao.search(query, sortOrder,
				pageNumber, pageSize);
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				String name = list.getList().get(i).get("memberName")
						.toString();
				if (name != "" && name != null) {
					MemberInfo info = getInfo(name);
					myList.add(info);
				} else {
					logger.info("文档不存在");
				}
			}

			pageContent.setCurrentRecords(myList.size());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

		}
		return pageContent;
	}
    //统计商家标记点击来源
	public PageContent<DBObject> getClickDomainForMerchant(String starttime,String endTime,int pageNumber,int pageSize,String mer_id){
		DBObject query=new BasicDBObject();
		query.put("mer_id", mer_id);
		query.put("status", ToolConstants.VALID_INT);
		int total=0;
		int totalPage=0;
		AggregationOutput output=cDao.clickTime("domain", query, "", 0, 0);
		if(output!=null){
			for(DBObject out:output.results())
				total++;
			if (total % pageSize == 0) {
				totalPage = total / pageSize;
			} else {
				totalPage = (total / pageSize) + 1;
			}
			AggregationOutput resultOutput= cDao.clickTime("domain", query, "des",
					pageSize, (pageNumber - 1) * pageSize);
			List<DBObject> list = new ArrayList<DBObject>();
			PageContent<DBObject> pageContent = new PageContent<DBObject>();
			if (output != null) {
				for (DBObject resultObject : resultOutput.results()) {
					DBObject result = new BasicDBObject();
					result.put("domain", resultObject.get("_id").toString());
					result.put("count", resultObject.get("count").toString());
					list.add(result);
				}
			}
			pageContent.setTotal(total);
			pageContent.setCurrentRecords(list.size());
			pageContent.setList(list);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(totalPage);
			return pageContent;
		}else{
			return null;
		}
	}
	// 点击信息统计出域名信息
	public PageContent<DBObject> getClickDomain(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize) {
		// starttime and endtime not use,
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VALID_INT);
		String sign = null;
		if (sortType == 1) {
			sign = "asc";
		} else if (sortType == 2) {
			sign = "des";
		} else {
			sign = null;
		}
		int total = 0;
		int totalPage = 0;
		AggregationOutput totalOutput = cDao.clickTime("domain", query, "", 0,
				0);
		if (totalOutput != null) {
			for (DBObject obj : totalOutput.results()) {
				logger.info(obj.toString());
				total++;
			}
		}
		if (total % pageSize == 0) {
			totalPage = total / pageSize;
		} else {
			totalPage = (total / pageSize) + 1;
		}
		AggregationOutput output = cDao.clickTime("domain", query, sign,
				pageSize, (pageNumber - 1) * pageSize);
		List<DBObject> list = new ArrayList<DBObject>();
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		if (output != null) {
			for (DBObject resultObject : output.results()) {
				DBObject result = new BasicDBObject();
				result.put("domain", resultObject.get("_id").toString());
				result.put("count", resultObject.get("count").toString());
				list.add(result);
			}
		}
		pageContent.setTotal(total);
		pageContent.setCurrentRecords(list.size());
		pageContent.setList(list);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(totalPage);
		return pageContent;
	}

	// 由统计得出-登陆次数统计
	public PageContent<MemberInfo> getMembersBehavioursByLoginSpan(
			String time1, String time2, int signType, int pageNumber,
			int pageSize) {
		AggregationOutput output = null;
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		long startTime = 0;
		long endTime = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			startTime = format.formatToLong(time1);
			endTime = format.formatToLong(time2);
		} else {
			//startTime = format.generateTime(30);
			endTime=System.currentTimeMillis()+(long)86400000;
		}
		DBObject query = mHelper.timeScope("loginTime", startTime, endTime);
		query.put("status", ToolConstants.VALID_INT);
		query.put("loginType", "member");
		AggregationOutput outTotal = logInfoDao.loginTime("user_id", query, "",
				0, 0);
		int total = 0;
		if (outTotal != null) {
			for (DBObject result : outTotal.results()) {
				total++;
			}
		}
		logger.info("total:" + total);
		int totalPage = (int) (total / pageSize) + 1;
		int ignore = mHelper.getSkip(pageNumber, totalPage);
		logger.info("ignore:" + ignore);
		if (signType == 1) {
			String sign = "asc";
			output = logInfoDao.loginTime("user_id", query, sign, pageSize,
					ignore);
		} else if (signType == 2) {
			String sign = "des";
			output = logInfoDao.loginTime("user_id", query, sign, pageSize,
					ignore);
		} else {
			output = logInfoDao.loginTime("user_id", query, "", pageSize,
					ignore);
		}
		if (output != null) {
			for (DBObject res : output.results()) {
				String mem_id = res.get("_id").toString();
				DBObject condition = new BasicDBObject("_id", mem_id);
				condition.put("status", ToolConstants.VALID_INT);
				DBObject object = memberDao.search(condition);
				if (object != null) {
					String name = memberDao.search(condition).get("memberName")
							.toString();
					if (name != "" && name != null) {
						MemberInfo info = getInfo(name);
						myList.add(info);
					} else {
						logger.info("文档不存在");
					}
				}
			}
		}
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		pageContent.setCurrentRecords(myList.size());
		pageContent.setList(myList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(totalPage);
		pageContent.setTotal(total);
		return pageContent;
	}

	// 按点击数统计时，统计所有的点击信息
	public PageContent<MemberInfo> getMembersBehavioursByClickSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// 获取统计出来的总数
		String sign = null;
		DBObject query = new BasicDBObject();
		long startTime = 0;
		long endTime = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			startTime = format.formatToLong(time1);
			endTime = format.formatToLong(time2);
		} else {
			//startTime = format.generateTime(30);
			endTime=System.currentTimeMillis()+(long)86400000;
		}
		query = mHelper.timeScope("date", startTime, endTime);
		query.put("status", ToolConstants.VALID_INT);
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		AggregationOutput outTotal = cDao.clickTime("mem_id", query, "", 0, 0);
		int total = 0;
		if (outTotal != null) {
			for (DBObject result : outTotal.results()) {
				total++;
			}
		}
		logger.info("total:" + total);
		int totalPage = (int) (total / pageSize) + 1;
		int ignore = mHelper.getSkip(pageNumber, totalPage);
		logger.info("ignore:" + ignore);
		if (sortType == 1) {
			sign = "asc";
		} else if (sortType == 2) {
			sign = "des";
		} else {
			sign = "";
		}
		AggregationOutput output = cDao.clickTime("mem_id", query, sign,
				pageSize, ignore);
		if (output != null) {
			for (DBObject res : output.results()) {
				String mem_id = res.get("_id").toString();
				String clickTimes = res.get("count").toString();
				logger.info(mem_id);
				DBObject condition = new BasicDBObject("_id", mem_id);
				if (memberDao.search(condition) != null) {
					String name = memberDao.search(condition).get("memberName")
							.toString();
					if (name != "" && name != null) {
						MemberInfo info = getInfo(name);
						info.setClickTimes(Integer.parseInt(clickTimes));
						myList.add(info);
					} else {
						logger.info("文档不存在");
					}
				}
			}
		}
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		pageContent.setCurrentRecords(myList.size());
		pageContent.setList(myList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(totalPage);
		pageContent.setTotal(total);
		return pageContent;
	}

	// 按评论数统计---sortType(1为总评论数升序，2为总评论数降序，3为差评up，4为down，5为好评up，6为down）
	public PageContent<MemberInfo> getMembersBehavioursByCommentSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		String sign = null;
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		DBObject query = new BasicDBObject();
		long startTime = 0;
		long endTime = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			startTime = format.formatToLong(time1);
			endTime = format.formatToLong(time2);
		} else {
			//startTime = format.generateTime(30);
			endTime=System.currentTimeMillis()+(long)86400000;
		}
		query = mHelper.timeScope("criticTime", startTime, endTime);
		query.put("status", ToolConstants.VALID_INT);
		if (sortType == 1) {
			sign = "asc";
		} else if (sortType == 2) {
			sign = "des";
		} else if (sortType == 3) {
			sign = "asc";
			query.put("comment", 0);
		} else if (sortType == 4) {
			sign = "des";
			query.put("comment", 0);
		} else if (sortType == 5) {
			sign = "asc";
			query.put("comment", 2);
		} else if (sortType == 6) {
			sign = "des";
			query.put("comment", 2);
		}
		AggregationOutput out = commentDao.commentTimes("mem_id", query, "", 0,
				0);
		int total = 0;
		if (out != null) {
			for (DBObject result : out.results()) {
				total++;
			}
		}
		logger.info("total:" + total);
		int totalPage = (int) (total / pageSize) + 1;
		int ignore = mHelper.getSkip(pageNumber, totalPage);
		logger.info("ignore:" + ignore);
		AggregationOutput output = commentDao.commentTimes("mem_id", query,
				sign, pageSize, ignore);
		if (output != null) {
			for (DBObject res : output.results()) {
				logger.info(res);
				String mem_id = res.get("_id").toString();
				String countString = res.get("count").toString();
				DBObject condition = new BasicDBObject("_id", mem_id);
				String name = memberDao.search(condition).get("memberName")
						.toString();
				if (name != "" && name != null) {
					MemberInfo info = getInfo(name);
					info.setCommentTimes(Integer.parseInt(countString));
					myList.add(info);
				} else {
					logger.info("文档不存在");
				}
			}
		}
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		pageContent.setTotal(total);
		pageContent.setCurrentRecords(myList.size());
		pageContent.setList(myList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(totalPage);
		return pageContent;
	}

	public PageContent<MemberInfo> deleted(String startTime, String endTime,
			String memberName, int sortType, int pageNumber, int pageSize) {
		long start = 0;
		long end = 0;
		DBObject query = new BasicDBObject();
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			start = format.formatToLong(startTime);
			end = format.formatToLong(endTime);
		} else {
			//start = format.generateTime(30);
			end=System.currentTimeMillis()+(long)86400000;
		}
		query = mHelper.timeScope("deleteDate", start, end);
		if (memberName != null && memberName != "") {
			query.put("memberName", memberName);
		}
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("date", 1);
		} else if (sortType == 2) {
			sortOrder.put("date", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = memberDao.Deleted(query, sortOrder,
				pageNumber, pageSize);
		List<MemberInfo> myList = new ArrayList<MemberInfo>();
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				String nameString = list.getList().get(i).get("memberName")
						.toString();
				if (nameString != "" && nameString != null) {
					MemberInfo info = getInfo(nameString);
					myList.add(info);
				} else {
					logger.info("文档不存在");
				}
			}

			pageContent.setCurrentRecords(myList.size());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

		}
		return pageContent;

	}
	public MemberManage() {
		memberDao = new MemberDao();
		cDao = new ClickInfoDao();
		commentDao = new CommentDao();
		logInfoDao = new LogInfoDao();
		markInfoDao = new MarkInfoDao();
	}
}
