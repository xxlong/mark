/**
 * 
 */
package com.anyway.imagemark.manage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.daoimpl.LogInfoDao;
import com.anyway.imagemark.daoimpl.MarkInfoDao;
import com.anyway.imagemark.daoimpl.MerchantDao;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.MongoHelper;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.utils.VerifyUtil;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Kario 管理系统商家管理
 */
@Repository
public class MerchantManage {
	private LogInfoDao logInfoDao = null;
	private DateFormat format = new DateFormat();
	private MarkInfoDao markInfoDao = null;
	private MerchantDao merchantDao = null;
	private static MongoHelper mHelper = new MongoHelper();
	private static Gson gson = new Gson();
	private static Logger logger = Logger.getLogger(MerchantManage.class);

	public MerchantInfo getInfo(String name, int flag) {
		String id = merchantDao.get_id(name);
		DBObject queryDbObject = mHelper.createQuery("merchantName", name);
		if (flag == 1) {
			queryDbObject.put("status", ToolConstants.VERIFY_INT);
		} else if (flag == 2) {
			queryDbObject.put("status", ToolConstants.VALID_INT);
		} else {
			queryDbObject.put("status", ToolConstants.DELETE_INT);
		}
		DBObject object = merchantDao.search(queryDbObject);
		Merchant merchant = null;
		if (object != null) {
			merchant = gson.fromJson(object.toString(), Merchant.class);

			MerchantInfo merchantInfo = new MerchantInfo();
			merchantInfo.setRegisteTime(format.generateTime(
					ToolConstants.DATEFORMAT_STRING,
					new Date(merchant.getDate())));
			merchantInfo.setDeleteDate(format.generateTime(
					ToolConstants.DATEFORMAT_STRING,
					new Date(merchant.getDeleteDate())));
			merchantInfo.setLoginTimes(logInfoDao.getLoginTimes(id));
			long time = logInfoDao.getLastTime(id);
			if (time != 0) {
				merchantInfo.setLastLogin(format.generateTime(
						ToolConstants.DATEFORMAT_STRING, new Date(time)));
			} else {
				merchantInfo.setLastLogin("该用户没有登陆记录");
			}
			merchantInfo.set_id(id);
			merchantInfo.setClickTimes(markInfoDao.getClickTimes(id));
			merchantInfo.setCommentTimes(markInfoDao.getCommentTimes(id));
			merchantInfo.setTrust(merchant.getMerchantTrust());
			merchantInfo.setNum(markInfoDao.getMarkNum(id));
			/*merchantInfo.setBadCommentsNum(markInfoDao.getBadComments(id));
			merchantInfo.setGoodCommentsNum(markInfoDao.getGoodComments(id));*/
			//merchantInfo.setPraise(merchant.getPraise());
			merchantInfo.setMerchantName(name);
			return merchantInfo;
		} else {
			return null;
		}
	}

	// 获是满足条件的统计的商家信息
	public PageContent<MerchantInfo> getAllMerchantsBehaviours(DBObject query,
			DBObject sortObject, int pageNumber, int pageSize) {
		List<MerchantInfo> myList = new ArrayList<MerchantInfo>();
		PageContent<DBObject> list = merchantDao.search(query, sortObject,
				pageNumber, pageSize);
		for (int i = 0; i < list.getList().size(); i++) {
			String nameString = list.getList().get(i).get("merchantName")
					.toString();
			if (nameString != null && nameString != "") {
				myList.add(getInfo(nameString, 2));
			}
		}
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		pageContent.setCurrentRecords(myList.size());
		pageContent.setList(myList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(list.getTotalPages());
		pageContent.setTotal(list.getTotal());
		return pageContent;
	}

	public PageContent<DBObject> getMerchantByNotVerify(int sortType,
			int pageNumber, int pageSize) {
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VERIFY_INT);
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("date", -1);
		} else if (sortType == 2) {
			sortOrder.put("date", 1);
		} else {
			sortOrder = null;
		}
		List<DBObject> myList = new ArrayList<DBObject>();
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		logger.info("Start to execute VerifyUtil.revertExpiredMerchantName()!");
		VerifyUtil.revertExpiredMerchantName();
		logger.info("Have executed VerifyUtil.revertExpiredMerchantName()!");
		String merchantName = VerifyUtil.verifiedMerchantNameList.pollLast();
		if (merchantName == null) {
			logger.info("There is no merchant currently required to verify!");
			return pageContent;
		} else {
			VerifyUtil.polledMerchantNameMap.put(merchantName, new Date());
			Merchant merchant = merchantDao.getMerchantByNameOrMail(merchantName,ToolConstants.VERIFY_INT);
			long registTime=merchant.getDate();
	        DBObject object=(DBObject) JSON.parse(gson.toJson(merchant));
	        DateFormat format=new DateFormat();
	        object.put("time", format.generateTime("yyyy-MM-dd", registTime));
			myList.add(object);
			logger.info("pollLast merchant successfully!");
			pageContent.setCurrentRecords(1);
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageSize);
			pageContent.setTotalPages(1);
			pageContent.setTotal(1);
			return pageContent;
		}

	}

	// sortType 0--为信用升序，1为信用降序；2为注册时间升序，3为注册时间降序
	public PageContent<Merchant> getMerchantByTrust(int filter, int sortType,
			int pageNumber, int pageSize) {
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VALID_INT);
		if (filter == 0) {

		} else if (filter == 1) {
			query.put("merchantTrust", new BasicDBObject("$lt", 200));
		} else if (filter == 2) {
			query.put("merchantTrust", new BasicDBObject().append("$gt", 200)
					.append("$lt", 800));
		} else if (filter == 3) {
			query.put("merchantTrust", new BasicDBObject().append("$gt", 800)
					.append("$lt", 1600));
		} else {
			query.put("merchantTrust", new BasicDBObject("$gt", 1600));
		}
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("merchantTrust", 1);
		} else if (sortType == 2) {
			sortOrder.put("merchantTrust", -1);
		} else if (sortType == 3) {
			sortOrder.put("date", 1);
		} else if (sortType == 4) {
			sortOrder.put("date", -1);
		} else {
			sortOrder = null;
		}
		List<Merchant> myList = new ArrayList<Merchant>();
		PageContent<DBObject> list = merchantDao.search(query, sortOrder,
				pageNumber, pageSize);
		PageContent<Merchant> pageContent = new PageContent<Merchant>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				myList.add(gson.fromJson(list.getList().get(i).toString(),
						Merchant.class));
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

	// // time1,time2为起止时间，sortType为排序标记（时间升序、降序），currentPage为要显示的页号，，num为显示行数
	public PageContent<MerchantInfo> getMerchantsBehavioursByRegisterSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// 对time1，time2需要进行判断，只有年、月、日？
		List<MerchantInfo> myList = new ArrayList<MerchantInfo>();
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			start = format.formatToLong(time1);
			end = format.formatToLong(time2);
		} else {
			//start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("date", start, end);
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortDbObject = new BasicDBObject();
		if (sortType == 1) {
			sortDbObject.put("date", 1);
		} else if (sortType == 2) {
			sortDbObject.put("date", -1);
		} else {
			sortDbObject = null;
		}
		PageContent<DBObject> list = merchantDao.search(query, sortDbObject,
				pageNumber, pageSize);
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				String nameString = list.getList().get(i).get("merchantName")
						.toString();
				if (nameString != null && nameString != "") {
					myList.add(getInfo(nameString, 2));
				}
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

	// 时间段内的登陆次数统计---
	public PageContent<MerchantInfo> getMerchantsBehavioursByLoginSpan(
			String time1, String time2, int sortType, int pageNumber,
			int pageSize) {
		// sortType -- 0--up,1---down
		int total = 0;
		List<MerchantInfo> myList = new ArrayList<MerchantInfo>();
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			start = format.formatToLong(time1);
			end = format.formatToLong(time2);
		} else {
			//start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("loginTime", start, end);
		query.put("status", ToolConstants.VALID_INT);
		query.put("loginType", "merchant");
		AggregationOutput output = logInfoDao.loginTime("user_id", query, "",
				0, 0);
		if (output != null) {
			for (DBObject res : output.results()) {
				total++;
			}
		}
		logger.info(total);
		int totalPage = (int) (total / pageSize) + 1;
		int ignore = 0;
		if (pageNumber != 1) {
			ignore = (pageNumber - 1) * pageSize;
		}
		String sort;
		if (sortType == 1) {
			sort = "asc";
		} else if (sortType == 2) {
			sort = "des";
		} else {
			sort = "";
		}
		AggregationOutput out = logInfoDao.loginTime("user_id", query, sort,
				pageSize, ignore);
		// 总页数在list.get(0)中
		if (out != null) {
			for (DBObject result : output.results()) {
				String idString = result.get("_id").toString();
				String count = result.get("count").toString();
				logger.info(idString);
				DBObject queryObject = new BasicDBObject("_id", idString);
				if (merchantDao.search(queryObject) != null) {
					String nameString = merchantDao.search(queryObject)
							.get("merchantName").toString();
					if (nameString != "" && nameString != null) {
						MerchantInfo info = getInfo(nameString, 2);
						info.setLoginTimes(Integer.parseInt(count));
						myList.add(info);
					}
				}
			}
		}
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		pageContent.setCurrentRecords(myList.size());
		pageContent.setList(myList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(totalPage);
		pageContent.setTotal(total);
		return pageContent;
	}

	public PageContent<MerchantInfo> getMerchantsBehavioursByMarkAggregate(
			String time1, String time2, String field, int sortType,
			int pageNumber, int pageSize) {
		List<MerchantInfo> myList = new ArrayList<MerchantInfo>();
		DBObject query = new BasicDBObject();
		long start = 0;
		long end = 0;
		if (time1 != null && time1 != "" && time2 != null && time2 != "") {
			start = format.formatToLong(time1);
			end = format.formatToLong(time2);
		} else{
			//start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("markDate", start, end);
		AggregationOutput output = markInfoDao.markTimes("mer_id", query, 4, 0,
				0);
		int total = 0;
		if (output != null) {
			for (DBObject res : output.results()) {
				total++;
			}
		}
		int totalPage = (int) (total / pageSize) + 1;
		int ignore = 0;
		if (pageNumber != 1) {
			ignore = (pageNumber - 1) * pageSize;
		}
		String sortSign;
		if (sortType == 1) {
			sortSign = "asc";
		} else if (sortType == 2) {
			sortSign = "des";
		} else {
			sortSign = "";
		}
		AggregationOutput out = markInfoDao.totalNum("mer_id", field, query,
				sortSign, pageSize, ignore);
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		if (out != null) {
			for (DBObject result : out.results()) {
				System.out.println(result.toString());
				String idString = result.get("_id").toString();
				String fieldString = result.get(field).toString();
				logger.info(fieldString);
				DBObject queryObject = new BasicDBObject("_id", idString);
				DBObject object = merchantDao.search(queryObject);
				if (object != null) {
					MerchantInfo info = getInfo(object.get("merchantName")
							.toString(), 2);
					myList.add(info);
				} else {
					return null;
				}
			}
			pageContent.setCurrentRecords(myList.size());
			pageContent.setList(myList);
			pageContent.setPageNumber(pageNumber);
			pageContent.setPageSize(pageNumber);
			pageContent.setTotalPages(totalPage);
			pageContent.setTotal(total);
		}
		return pageContent;
	}

	public PageContent<MerchantInfo> deleted(String startTime, String endTime,
			String merchantName, int sortType, int pageNumber, int pageSize) {
		long start = 0;
		long end = 0;
		DBObject query = new BasicDBObject();
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			start = format.formatToLong(startTime);
			end = format.formatToLong(endTime);
		} else {
			//start = format.generateTime(30);
			end = System.currentTimeMillis() + (long) 86400000;
		}
		query = mHelper.timeScope("deleteDate", start, end);
		if (merchantName != null && merchantName != "") {
			query.put("merchantName", merchantName);
		}
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("date", 1);
		} else if (sortType == 2) {
			sortOrder.put("date", -1);
		} else {
			sortOrder = null;
		}
		PageContent<DBObject> list = merchantDao.Deleted(query, sortOrder,
				pageNumber, pageSize);
		List<MerchantInfo> myList = new ArrayList<MerchantInfo>();
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		if (list != null) {
			for (int i = 0; i < list.getList().size(); i++) {
				String nameString = list.getList().get(i).get("merchantName")
						.toString();
				logger.info("the query merchant name : " + nameString);
				if (nameString != "" && nameString != null) {
					myList.add(getInfo(nameString, 6));
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
						merchantDao.getNumberOfMerchant(year, i));
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
						merchantDao.getNumberOfMerchant(year, i));
				myList.add(object);
			}
		}
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent.setList(myList);
		pageContent.setTotal(myList.size());
		return pageContent;
	}

	/**
	 * @author Kario 统计系统有效商家总量及当月增量
	 * @return int
	 */
	public List<DBObject> getTotalValidMerchant() {
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		DBObject total = new BasicDBObject();
		total.put("total", merchantDao.getNumberOfMember());
		DBObject month = new BasicDBObject();
		Calendar calendar = Calendar.getInstance();
		month.put("month",
				merchantDao.getNumberOfMerchant(calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1)));
		dbObjects.add(total);
		dbObjects.add(month);
		return dbObjects;
	}

	public MerchantManage() {
		logInfoDao = new LogInfoDao();
		markInfoDao = new MarkInfoDao();
		merchantDao = new MerchantDao();
	}
}
