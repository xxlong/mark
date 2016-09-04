/**
 * 
 */
package com.anyway.imagemark.manage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.plaf.basic.BasicViewportUI;

import org.apache.log4j.Logger;
import org.aspectj.weaver.NewMemberClassTypeMunger;
import org.bson.types.ObjectId;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.daoimpl.CommentDao;
import com.anyway.imagemark.daoimpl.ComplainDao;
import com.anyway.imagemark.daoimpl.MarkInfoDao;
import com.anyway.imagemark.daoimpl.MemberDao;
import com.anyway.imagemark.daoimpl.MerchantDao;
import com.anyway.imagemark.daoimpl.NodeDao;
import com.anyway.imagemark.domain.Complain;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.MongoHelper;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * @author Kario
 * 
 */
@Repository
public class MarkManage {
	public MerchantMark getMark(String mark_id) {
		MerchantMark mark = new MerchantMark();
		DBObject query = new BasicDBObject("_id", mark_id);
		DBObject object = markInfoDao.search(query);
		MerchantDao merchantDao = new MerchantDao();
		if (object != null) {
			MarkInfo markInfo = gson
					.fromJson(object.toString(), MarkInfo.class);
			String merchantID = markInfo.getMer_id();
			mark.set_id(mark_id);
			mark.setComponentName(markInfo.getComponentName());
			mark.setComponentPrice(markInfo.getComponentPrice());
			mark.setComponentType(ToolConstants.COMPONENTTYPE_STRINGS[markInfo
					.getComponentType()]);
			mark.setClickTimes(markInfo.getDirectCount());
			mark.setCommentTimes(markInfo.getTextCount());
			mark.setLink(markInfo.getComponentLinkAddress());
			mark.setUrl(markInfo.getUrl());
			mark.setNode_id(markInfo.getNode_id());
			mark.setTrust(markInfo.getComponentTrust());
			mark.setMarkDate(format.generateTime(
					ToolConstants.DATEFORMAT_STRING, markInfo.getMarkDate()));
			mark.setDeleteDate(format.generateTime(
					ToolConstants.DATEFORMAT_STRING, markInfo.getDeleteDate()));
			logger.info(merchantID);
			if (merchantDao.search("_id", merchantID) != null) {
				mark.setMerchantName(merchantDao.search("_id", merchantID)
						.get("merchantName").toString());
			}
			return mark;
		} else {
			return null;
		}
	}

	// // 标记--标记管理显示---query为查询条件（时间段、行业、时间范围+点击数、时间范围+评论数）
	public PageContent<MerchantMark> getStatisticalMarksByCondition(
			DBObject query, DBObject sortOrder, boolean flag, int pageNumber,
			int pageSize) {
		logger.info("execute MarkManage--getStatisticalMarksByCondition");
		logger.info("execute MarkInfoDao--search(query, sortOrder,"
				+ "pageNumber, pageSize)");
		if (flag) {
			query.put("commentNum", new BasicDBObject().append("$gte", 1));
		}
		PageContent<DBObject> list = markInfoDao.search(query, sortOrder,
				pageNumber, pageSize);
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		if (list != null) {
			List<DBObject> objects = list.getList();
			List<MerchantMark> myList = new ArrayList<MerchantMark>();
			for (int i = 0; i < objects.size(); i++) {
				String idString = objects.get(i).get("_id").toString();
				MerchantMark mark = getMark(idString);
				if (mark != null)
					myList.add(getMark(idString));
			}
			pageContent.setList(myList);
			pageContent.setPageSize(pageSize);
			pageContent.setCurrentRecords(myList.size());
			pageContent.setPageNumber(pageNumber);
			pageContent.setTotalPages(list.getTotalPages());
			pageContent.setTotal(list.getTotal());

			return pageContent;
		} else {
			return null;
		}
	}

	public PageContent<MerchantMark> getStatisticalMarkNotVerify(
			String startTime, String endTime, int sortType, int pageNumber,
			int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			start = format.formatToLong(startTime);
			end = format.formatToLong(endTime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		logger.info(gson.toJson(query));
		query.put("status", ToolConstants.VERIFY_INT);
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("markDate", 1);
		} else if (sortType == 2) {
			sortOrder.put("markDate", -1);
		} else {
			sortOrder = null;
		}
		return getStatisticalMarksByCondition(query, sortOrder, false,
				pageNumber, pageSize);
	}

	// 获取被投诉的标记
	public PageContent<MerchantMark> getComplainedMark(String startTime,
			String endTime, int sortType, int pageNumber, int pageSize) {
		long start = 0;
		long end = 0;
		DBObject sortOrder = new BasicDBObject();
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			start = format.formatToLong(startTime);
			end = format.formatToLong(endTime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		if (sortType == 1) {
			sortOrder.put("complainTime", 1);
		} else if (sortType == 2) {
			sortOrder.put("complainTime", -1);
		} else {
			sortOrder = null;
		}
		DBObject query = mHelper.timeScope("complainTime", start, end);
		query.put("status", ToolConstants.VERIFY_INT);
		ComplainDao complainDao = new ComplainDao();
		PageContent<DBObject> pageContent = complainDao.search(query,
				sortOrder, pageNumber, pageSize);
		logger.info(gson.toJson(pageContent));
		PageContent<MerchantMark> resultContent = new PageContent<MerchantMark>();
		List<MerchantMark> myList = new ArrayList<MerchantMark>();
		if (pageContent != null) {
			List<DBObject> list = pageContent.getList();
			for (int i = 0; i < list.size(); i++) {
				DBObject object = list.get(i);
				logger.info(object);
				Complain complain = gson.fromJson(object.toString(),
						Complain.class);
				String mark_id = complain.getMark_id();
				String _idString = complain.get_id();
				MerchantMark mark = getMark(mark_id);
				if (mark != null) {
					mark.setComplainId(_idString);
					mark.setComplainReason(complain.getComplainReason());
					mark.setComplainTime(format.generateTime(
							ToolConstants.DATEFORMAT_STRING,
							complain.getComplainTime()));
					myList.add(mark);
				}
			}
			resultContent.setList(myList);
			resultContent.setPageSize(pageSize);
			resultContent.setCurrentRecords(myList.size());
			resultContent.setPageNumber(pageNumber);
			resultContent.setTotalPages(pageContent.getTotalPages());
			resultContent.setTotal(pageContent.getTotal());
			return resultContent;
		} else {
			return null;
		}

	}

	public PageContent<MerchantMark> getStatisticalMarkBySort(String starttime,
			String endtime, String Field, int sortType, int pageNumber,
			int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (starttime != null && starttime != "" && endtime != null
				&& endtime != "") {
			start = format.formatToLong(starttime);
			end = format.formatToLong(endtime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put(Field, 1);
		} else if (sortType == 2) {
			sortOrder.put(Field, -1);
		} else {
			sortOrder = null;
		}
		return getStatisticalMarksByCondition(query, sortOrder, false,
				pageNumber, pageSize);
	}

	/**
	 * @author Kario 统计
	 * 
	 */
	public List<DBObject> markAggreate(String starttime, String endtime,
			int sortType, String Field, int pageNumber, int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (starttime != null && starttime != "" && endtime != null
				&& endtime != "") {
			start = format.formatToLong(starttime);
			end = format.formatToLong(endtime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		query.put("status", ToolConstants.VALID_INT);
		int sign = 0;
		if (sortType == 1) {
			sign = 0;
		} else if (sortType == 2) {
			sign = 1;
		}
		AggregationOutput output = markInfoDao.markTimes(Field, query, sign,
				pageSize, (pageNumber - 1) * pageSize);
		List<DBObject> myList = new ArrayList<DBObject>();
		if (output != null) {
			for (DBObject object : output.results()) {
				DBObject temp = new BasicDBObject();
				temp.put("type", object.get("_id").toString());
				temp.put("count", object.get("count").toString());
				myList.add(temp);
			}
		}
		return myList;
	}

	public PageContent<MerchantMark> getStatisticalMarksByNewAdd(
			String starttime, String endtime, int sortType, int pageNumber,
			int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (starttime != null && starttime != "" && endtime != null
				&& endtime != "") {
			start = format.formatToLong(starttime);
			end = format.formatToLong(endtime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("directCount", -1);
		} else if (sortType == 2) {
			sortOrder.put("commentNum", -1);
		} else if (sortType == 3) {
			sortOrder.put("directCount", -1);
			sortOrder.put("commentNum", -1);
		} else {
			sortOrder = null;
		}
		return getStatisticalMarksByCondition(query, sortOrder, false,
				pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarksByFilter(
			String starttime, String endtime, String filterField, int filter,
			int sortType, int pageNumber, int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (starttime != null && starttime != "" && endtime != null
				&& endtime != "") {
			start = format.formatToLong(starttime);
			end = format.formatToLong(endtime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		query.put("status", ToolConstants.VALID_INT);
		if (filter == 0) {
			query.put(filterField, new BasicDBObject("$gt", 0));
		} else if (filter == 1) {
			query.put(filterField,
					new BasicDBObject("$gt", 0).append("$lt", 50));
		} else if (filter == 2) {
			query.put(filterField, new BasicDBObject().append("$gt", 50)
					.append("$lt", 300));
		} else if (filter == 3) {
			query.put(filterField, new BasicDBObject().append("$gt", 300)
					.append("$lt", 1000));
		} else {
			query.put(filterField, new BasicDBObject("$gt", 1000));
		}
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put(filterField, 1);
		} else if (sortType == 2) {
			sortOrder.put(filterField, -1);
		}
		return getStatisticalMarksByCondition(query, sortOrder, false,
				pageNumber, pageSize);
	}

	public PageContent<MerchantMark> getStatisticalMarksByComponentType(
			String starttime, String endtime, boolean flag, int componentType,
			int sortType, int pageNumber, int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (starttime != null && starttime != "" && endtime != null
				&& endtime != "") {
			start = format.formatToLong(starttime);
			end = format.formatToLong(endtime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		query.put("status", ToolConstants.VALID_INT);
		if (componentType != 0) {
			query.put("componentType", componentType);
		}
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("directCount", -1);
		} else if (sortType == 2) {
			sortOrder.put("commentNum", -1);
		} else if (sortType == 3) {
			sortOrder.put("directCount", -1);
			sortOrder.put("commentNum", -1);
		} else {
			sortOrder = null;
		}
		return getStatisticalMarksByCondition(query, sortOrder, flag,
				pageNumber, pageSize);
	}

	// 统计此商家名下的标记----标记和话题公用这个不行
	public PageContent<MerchantMark> getStatisticalMarksByMerchantName(
			String merchantName, int sortType, int pageNumber, int pageSize) {
		logger.info("execute MarkManage--getStatisticalMarksByMerchantName with: "
				+ "merchantName" + merchantName);
		String mer_id = "";
		DBObject sortOrder = new BasicDBObject();
		DBObject condition = new BasicDBObject();
		condition.put("merchantName", merchantName);
		condition.put("status", ToolConstants.VALID_INT);
		DBObject object = merDao.search(condition);
		if (object != null) {
			mer_id = object.get("_id").toString();
		}
		DBObject query = new BasicDBObject();
		query.put("mer_id", mer_id);
		query.put("status", ToolConstants.VALID_INT);
		// flag 为true 为话题
		if (sortType == 1) {
			sortOrder.put("markDate", 1);
		} else if (sortType == 2) {
			sortOrder.put("markDate", -1);
		} else {
			sortOrder = null;
		}
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = getStatisticalMarksByCondition(query, sortOrder, false,
				pageNumber, pageSize);
		if (pageContent != null) {
			logger.info("the first Url part of list of PageContent<MerchantMark> :"
					+ pageContent.getList().get(0).getUrl());
		}
		return pageContent;
	}

	public PageContent<MerchantMark> getStatisticalMarksByMerchant(
			String idString, int pageNumber, int pageSize) {
		logger.info("execute MarkManage--getStatisticalMarksByMerchantName with: "
				+ "merchant" + idString);
		DBObject sortOrder = new BasicDBObject();
		DBObject condition = new BasicDBObject();
		condition.put("mer_id", idString);
		condition.put("status", ToolConstants.VALID_INT);
		sortOrder.put("praise", -1);
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = getStatisticalMarksByCondition(condition, sortOrder,
				false, pageNumber, pageSize);
		if (pageContent != null) {
			logger.info("the first Url part of list of PageContent<MerchantMark> :"
					+ pageContent.getList().get(0).getUrl());
		}
		return pageContent;
	}

	public PageContent<MerchantMark> getStatisticalTopicByMerchantName(
			String merchantName, int sortType, int pageNumber, int pageSize) {
		logger.info("execute MarkManage--getStatisticalMarksByMerchantName with: "
				+ "merchantName" + merchantName);
		String mer_id = "";
		DBObject sortOrder = new BasicDBObject();
		DBObject condition = new BasicDBObject();
		condition.put("merchantName", merchantName);
		condition.put("status", ToolConstants.VALID_INT);
		DBObject object = merDao.search(condition);
		if (object != null) {
			mer_id = object.get("_id").toString();
		}
		DBObject query = new BasicDBObject();
		query.put("mer_id", mer_id);
		// flag 为true 为话题
		if (sortType == 1) {
			sortOrder.put("markDate", 1);
		} else if (sortType == 2) {
			sortOrder.put("markDate", -1);
		} else {
			sortOrder = null;
		}
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = getStatisticalMarksByCondition(query, sortOrder, true,
				pageNumber, pageSize);
		if (pageContent != null) {
			logger.info("the first Url part of list of PageContent<MerchantMark> :"
					+ pageContent.getList().get(0).getUrl());
		}
		return pageContent;
	}

	// ?
	public PageContent<MerchantMark> getMarkByMerchant(String starttime,
			String endtime, int sortType, int pageNumber, int pageSize) {
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (starttime != null && starttime != "" && endtime != null
				&& endtime != "") {
			start = format.formatToLong(starttime);
			end = format.formatToLong(endtime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		query.put("status", ToolConstants.VALID_INT);
		int sign = 0;
		if (sortType == 1) {
			sign = 0;
		} else if (sortType == 2) {
			sign = 1;
		}
		AggregationOutput output = markInfoDao.markTimes("mer_id", query, sign,
				pageSize, (pageSize - 1) * pageNumber);

		return null;
	}

	// 标记管理----
	public PageContent<MerchantMark> deleted(String startTime, String endTime,
			String merchantName, int sortType, int pageNumber, int pageSize) {
		long start = 0;
		long end = 0;
		DBObject query = new BasicDBObject();
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			start = format.formatToLong(startTime);
			end = format.formatToLong(endTime);
		} else {
			// start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("deleteDate", start, end);
		if (merchantName != null && merchantName != "") {
			String mer_id = merDao.get_id(merchantName);
			query.put("mer_id", mer_id);
		}
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("markDate", 1);
		} else if (sortType == 2) {
			sortOrder.put("markDate", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = markInfoDao.Deleted(query, sortOrder,
				pageNumber, pageSize);
		List<MerchantMark> myList = new ArrayList<MerchantMark>();
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				String idString = list.getList().get(i).get("_id").toString();
				if (idString != "" && idString != null) {
					MerchantMark info = getMark(idString);
					myList.add(info);
				} else {
					logger.info("文档不存在");
				}
				/*
				 * MarkInfo markInfo = gson.fromJson(list.getList().get(i)
				 * .toString(), MarkInfo.class); myList.add(markInfo);
				 */
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

	public MarkManage() {
		this.markInfoDao = new MarkInfoDao();
		this.merDao = new MerchantDao();
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
				object.put("increaseMent", markInfoDao.getNumberOfMark(year, i));
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
				object.put("increaseMent", markInfoDao.getNumberOfMark(year, i));
				myList.add(object);
			}
		}
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent.setList(myList);
		pageContent.setTotal(myList.size());
		return pageContent;
	}

	/**
	 * @author Kario 统计系统有效标记总量及当月增量
	 * @return int
	 */
	public List<DBObject> getTotalValidMark() {
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		DBObject total = new BasicDBObject();
		total.put("total", markInfoDao.getNumberOfMember());
		DBObject month = new BasicDBObject();
		Calendar calendar = Calendar.getInstance();
		month.put("month",
				markInfoDao.getNumberOfMark(calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1)));
		dbObjects.add(total);
		dbObjects.add(month);
		return dbObjects;
	}

	/**
	 * @author Kario 统计系统标记各类总量及当月增量
	 * @return
	 */
	public PageContent<DBObject> getIncreaseMentByType(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		AggregationOutput out = null;
		int[][] arrIncrease = null;
		if (year < currentYear) {
			arrIncrease = new int[8][12];
			for (int i = 1; i <= 12; i++) {
				out = markInfoDao.countNumByMerchantType("asc",
						mHelper.generateStartAndEndTime("markDate", year, i));
				if (out != null) {
					for (DBObject agg : out.results()) {
						int type = Integer.parseInt(agg.get("_id").toString());
						int count = Integer.parseInt(agg.get("count")
								.toString());
						arrIncrease[type - 1][i - 1] = count;
					}
				}
			}
		} else {
			arrIncrease = new int[8][12];
			for (int i = 1; i <= currentMonth; i++) {
				out = markInfoDao.countNumByMerchantType("asc",
						mHelper.generateStartAndEndTime("markDate", year, i));
				if (out != null) {
					for (DBObject agg : out.results()) {
						int type = Integer.parseInt(agg.get("_id").toString());
						int count = Integer.parseInt(agg.get("count")
								.toString());
						arrIncrease[type - 1][i - 1] = count;
					}
				}
			}
		}
		List<DBObject> resultList = new ArrayList<DBObject>();
		for (int i = 0; i < 8; i++) {
			DBObject object = new BasicDBObject();
			List<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < 12; j++) {
				if (i == 0) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 1) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 2) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 3) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 4) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 5) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 6) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else if (i == 7) {
					object.put("name",
							ToolConstants.COMPONENTTYPE_STRINGS[i + 1]);
					list.add(arrIncrease[i][j]);
					object.put("data", list);
				} else {
					logger.info("error");
				}
			}
			resultList.add(object);
		}
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent.setList(resultList);
		pageContent.setTotal(resultList.size());
		return pageContent;
	}

	public PageContent<DBObject> getVerifyUrl(int page, int pageSize) {
		nodeDao = new NodeDao();
		AggregationOutput temp = nodeDao.getAggregationOutput(null, 0, 0);
		int total = 0;
		if (temp != null) {
			for (DBObject obj : temp.results()) {
				total++;
			}
		}
		logger.info("total" + total);
		DBObject sort = new BasicDBObject("Url", 1);
		AggregationOutput output = nodeDao.getAggregationOutput(sort, pageSize,
				page);
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		List<DBObject> list = new ArrayList<DBObject>();
		if (output != null) {
			for (DBObject object : output.results()) {
				logger.info(gson.toJson(object));
				DBObject result = new BasicDBObject();
				String urlString = object.get("_id").toString();
				String countString = object.get("count").toString();
				result.put("url", urlString);
				result.put("count", countString);
				list.add(result);
			}
		}
		pageContent.setList(list);
		pageContent.setTotal(total);
		logger.info(gson.toJson(pageContent));
		return pageContent;
	}

	// 用户点赞过得商品
	public PageContent<DBObject> getUserPrased(String userName, int page,
			int pageSize, int flag) {
		if (userName != null && userName != "") {
			DBObject queryDbObject = new BasicDBObject();
			MemberDao memberDao = new MemberDao();
			CommentDao comm = new CommentDao();
			String mem_id = memberDao.get_id(userName);
			if (mem_id != ToolConstants.DOCNOTFOUND_STRING) {
				queryDbObject.put("mem_id", mem_id);
				if (flag == 1)
					queryDbObject.put("phraise", true);
				else
					queryDbObject.put("collected", true);
				DBObject sort = new BasicDBObject("_id", -1);
				PageContent<DBObject> pageContent = comm.search(queryDbObject,
						sort, page, pageSize);
				PageContent<DBObject> resultContent = new PageContent<DBObject>();
				List<DBObject> list = new ArrayList<DBObject>();
				if (pageContent.getList() != null) {
					for (int i = 0; i < pageContent.getList().size(); i++) {
						String mark_id = pageContent.getList().get(i)
								.get("mark_id").toString();
						DBObject temp = markInfoDao.search(new BasicDBObject(
								"_id", mark_id));
						if (temp != null)
							list.add(temp);
					}
					resultContent.setList(list);
					resultContent.setPageSize(pageSize);
					resultContent.setTotal(pageContent.getTotal());
					resultContent.setCurrentRecords(pageContent
							.getCurrentRecords());
					resultContent.setPageNumber(page);
				}
				return resultContent;
			}
			return null;
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		MarkManage manage = new MarkManage();
		logger.info(gson.toJson(manage.getIncreaseMentByType(2015, 5)));
	}

	private MongoHelper mHelper = new MongoHelper();
	private MerchantDao merDao = null;
	private NodeDao nodeDao = null;
	private MarkInfoDao markInfoDao = null;
	private static Gson gson = new Gson();
	private static DateFormat format = new DateFormat();
	private static Logger logger = Logger.getLogger(MarkManage.class);
}
