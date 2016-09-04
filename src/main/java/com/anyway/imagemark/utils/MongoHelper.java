/**
 * 
 */
package com.anyway.imagemark.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.NewBSONDecoder;

import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoHelper {
	public MongoHelper() {

	}

	public MongoHelper(String url, int port) {
		mConnection = new MongoConnection(url, port);
	}

	public MongoConnection getConnection() {
		return this.mConnection;
	}

	public void closeClient() {
		mConnection.getClient().close();
	}


	public DBObject findByCondition(DBCollection collection, DBObject condition) {
		DBCursor cursor = collection.find(condition);
		if (cursor.hasNext()) {
			return cursor.next();
		} else
			return null;
	}

	public DBObject findByCondition(DBCollection collection,
			DBObject condition, DBObject fieldIgnore) {
		DBCursor cursor = collection.find(condition, fieldIgnore);
		if (cursor.hasNext())
			return cursor.next();
		else
			return null;
	}

	public List<DBObject> listFindDefault(DBCollection collection, int limit) {
		DBCursor cursor = null;
		if (limit != 0)
			cursor = collection.find().limit(limit);
		else
			cursor = collection.find();
		List<DBObject> myList = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			myList.add(cursor.next());
		}
		// closeClient();
		return myList;
	}

	public List<DBObject> listFindByCondition(DBCollection collection,
			DBObject condition) {
		DBCursor cursor = collection.find(condition);
		List<DBObject> myList = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			myList.add(cursor.next());
		}
		// closeClient();
		return myList;
	}

	public List<DBObject> listFindByCondition(DBCollection collection,
			DBObject condition, DBObject fieldIgnore) {
		DBCursor cursor = collection.find(condition, fieldIgnore);
		List<DBObject> myList = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			myList.add(cursor.next());
		}
		// closeClient();
		return myList;
	}

	public List<DBObject> listFindByCondition(DBCollection collection,
			DBObject condition, DBObject fieldIgnore, DBObject sort) {
		DBCursor cursor = collection.find(condition, fieldIgnore).sort(sort);
		List<DBObject> myList = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			myList.add(cursor.next());
		}
		// closeClient();
		return myList;
	}

	public List<DBObject> listFindByConditionAndSort(DBCollection collection,
			DBObject condition, DBObject sort) {
		DBCursor cursor = collection.find(condition).sort(sort);
		List<DBObject> myList = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			myList.add(cursor.next());
		}
		// closeClient();
		return myList;
	}

	public List<DBObject> listFindByCondition(DBCollection collection,
			DBObject condition, DBObject fieldIgnore, DBObject sort, int limit) {
		DBCursor cursor = collection.find(condition, fieldIgnore).sort(sort)
				.limit(limit);
		List<DBObject> myList = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			myList.add(cursor.next());
		}
		// closeClient();
		return myList;
	}

	public PageContent<DBObject> listPager(DBCollection collection,
			DBObject query, int currentPage, DBObject sortOrder, int num) {
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		logger.info("query info: "+gson.toJson(query));
		int totalCount = collection.find(query).count();
		if (totalCount == 0) {
			logger.info("the database result is null");
			return null;
		}
		int totalPage = 0;
		logger.info("the total: " + totalCount);
		if (totalCount % num == 0) {
			totalPage = totalCount / num;
		} else {
			totalPage = totalCount / num + 1;
		}
		if (currentPage == 0)
			currentPage = currentPage + 1;
		List<DBObject> myList = new ArrayList<DBObject>();
		DBObject firstDbObject = new BasicDBObject("total", totalPage);
		// int ignore = getSkip(flag, currentPage, totalPage);
		if (query == null && sortOrder == null) {
			DBCursor cursor = collection.find().skip((currentPage - 1) * num)
					.limit(num);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("query and sort is null");
		} else if (query == null) {
			DBCursor cursor = collection.find().skip((currentPage - 1) * num)
					.limit(num).sort(sortOrder);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("query is null");
		} else if (sortOrder == null) {
			DBCursor cursor = collection.find(query)
					.skip((currentPage - 1) * num).limit(num);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("sort is null");
		} else {
			DBCursor cursor = collection.find(query)
					.skip((currentPage - 1) * num).limit(num).sort(sortOrder);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("all not null");
		}
		pageContent.setTotal(totalCount);
		pageContent.setTotalPages(totalPage);
		pageContent.setPageSize(num);
		pageContent.setCurrentRecords(myList.size());
		pageContent.setPageNumber(currentPage);
		pageContent.setList(myList);
		// closeClient();
		return pageContent;
	}

	public PageContent<DBObject> listPagerSpecial(DBCollection collection,
			DBObject query, DBObject field, int currentPage,
			DBObject sortOrder, int num) {
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		int totalCount = collection.find(query).count();
		int totalPage = 0;
		if (totalCount % num == 0) {
			totalPage = totalCount / num;
		} else {
			totalPage = totalCount / num + 1;
		}
		List<DBObject> myList = new ArrayList<DBObject>();
		DBObject firstDbObject = new BasicDBObject("total", totalPage);
		// int ignore = getSkip(flag, currentPage, totalPage);
		if (query == null && sortOrder == null) {
			DBCursor cursor = collection.find().skip((currentPage - 1) * num)
					.limit(num);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("query and sort is null");
		} else if (query == null) {
			DBCursor cursor = collection.find().skip((currentPage - 1) * num)
					.limit(num).sort(sortOrder);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("query is null");
		} else if (sortOrder == null) {
			DBCursor cursor = collection.find(query, field)
					.skip((currentPage - 1) * num).limit(num);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("sort is null");
		} else {
			DBCursor cursor = collection.find(query, field)
					.skip((currentPage - 1) * num).limit(num).sort(sortOrder);
			while (cursor.hasNext())
				myList.add(cursor.next());
			logger.info("all not null");
		}
		pageContent.setTotalPages(totalPage);
		pageContent.setPageSize(num);
		pageContent.setCurrentRecords(myList.size());
		pageContent.setPageNumber(currentPage);
		pageContent.setList(myList);
		// closeClient();
		return pageContent;
	}

	// ���ò���Ҫ���ص��ֶ�
	public DBObject fieldIgnore(String field) {
		BasicDBObject fielDbObject = new BasicDBObject();
		fielDbObject.append(field, 0);
		return fielDbObject;
	}

	public DBObject fieldIgnore(String field1, String field2) {
		BasicDBObject fielDbObject = new BasicDBObject();
		fielDbObject.append(field1, 0);
		fielDbObject.append(field2, 0);
		return fielDbObject;
	}

	// ���ò�ѯ����---һ����ϲ�ѯ������2���ֶε����
	public DBObject createQuery(String field, int key) {
		BasicDBObject query = new BasicDBObject();
		query.append(field, key);
		return query;
	}

	public DBObject createQuery(String field, String value) {
		BasicDBObject query = new BasicDBObject();
		query.append(field, value);
		return query;
	}

	public DBObject greatNumQuery(String field, int num) {
		BasicDBObject query = new BasicDBObject();
		query.append(field, new BasicDBObject("$gt", num));
		return query;
	}

	public DBObject lessNumQuery(String field, int num) {
		BasicDBObject query = new BasicDBObject();
		query.append(field, new BasicDBObject("$lt", num));
		return query;
	}

	public DBObject createQuery(String field1, String value1, String field2,
			String value2) {
		BasicDBObject query = new BasicDBObject();
		query.append(field1, value1);
		query.append(field2, value2);
		return query;
	}

	public DBObject timeQuery(String field, long time, String flag) {
		DBObject query = new BasicDBObject();
		if (flag.equals("before"))
			query.put(field, new BasicDBObject("$lt", time));
		else
			query.put(field, new BasicDBObject("$gt", time));
		return query;
	}

	public DBObject timeScope(String field, long time1, long time2) {
		DBObject query = new BasicDBObject();
		// DBObject queryFinal=new BasicDBObject();
		query.put(field,
				new BasicDBObject().append("$gt", time1).append("$lt", time2));
		// query.put(field, new BasicDBObject("$lt", time2));
		return query;
	}

	public int getSkip(int currentPage, int totalPage) {
		int page = 0;
		int ignore = 0;
		/*
		 * if (flag == ToolConstants.PREPAGE) { page = currentPage - 2; if (page
		 * < 0) page = 0; ignore = page * ToolConstants.TABLENUM_INT; } else if
		 * (flag == ToolConstants.NEXTPAGE) { page = currentPage; if
		 * (currentPage == totalPage) page = currentPage - 1; ignore = page *
		 * ToolConstants.TABLENUM_INT; } else if (flag ==
		 * ToolConstants.FIRSTPAGE) { page = 0; ignore = page *
		 * ToolConstants.TABLENUM_INT; } else if (flag ==
		 * ToolConstants.LASTPAGE) { page = totalPage - 1; ignore = page *
		 * ToolConstants.TABLENUM_INT; } else { page = 0; ignore = page *
		 * ToolConstants.TABLENUM_INT; logger.info("��������Ϲ淶��δ�������"); }
		 */
		logger.info("ǰһҳҳ�ţ�" + page);
		logger.info("�����ĵ���Ϊ��" + ignore);
		return ignore;
	}

	// 
	public AggregationOutput aggregate(DBCollection collection, String field,
			DBObject query, DBObject sort, int limit, int skip) {
		DBObject match = new BasicDBObject("$match", query);
		DBObject groupFields = new BasicDBObject("_id", "$" + field);
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		DBObject sortObject = new BasicDBObject("$sort", sort);
		DBObject limitoObject = new BasicDBObject("$limit", limit);
		DBObject skipoObject = new BasicDBObject("$skip", skip);
		List<DBObject> pipeline = null;
		// ��ȡ�ܵ�ͳ����----field��query����Ϊ��
		if (sort == null && limit == 0 && skip == 0) {
			pipeline = Arrays.asList(match, group);
		} else {
			pipeline = Arrays.asList(match, group, sortObject, limitoObject,
					skipoObject);
		}
		if (collection.find(query).hasNext()) {
			AggregationOutput output = collection.aggregate(pipeline);
			return output;
		} else {
			return null;
		}
		// closeClient();

	}

	public AggregationOutput aggregate(DBCollection collection, String field,
			DBObject obj, DBObject query, DBObject sort, int limit, int skip) {
		DBObject match = new BasicDBObject("$match", query);
		obj.put("_id", "$" + field);
		DBObject group = new BasicDBObject("$group", obj);
		DBObject sortObject = new BasicDBObject("$sort", sort);
		DBObject limitoObject = new BasicDBObject("$limit", limit);
		DBObject skipoObject = new BasicDBObject("$skip", skip);
		List<DBObject> pipeline = null;
		//
		if (sort == null && limit == 0 && skip == 0) {
			pipeline = Arrays.asList(match, group);
		} else {
			pipeline = Arrays.asList(match, group, sortObject, limitoObject,
					skipoObject);
		}
		if (collection.find(query).hasNext()) {
			AggregationOutput output = collection.aggregate(pipeline);
			return output;
		} else {
			return null;
		}
	}

	/**
	 * @author Kario
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param field
	 *            相应字段
	 * @return DBobject 月份的起止时间构成的查询条件
	 */
	public DBObject generateStartAndEndTime(String filed, int year, int month) {
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new DateFormat();
		String startString = null;
		String endString = null;
		if (month >= 10)
			startString = "" + year + "-" + month + "-01 00:00:00";
		else
			startString = "" + year + "-0" + month + "-01 00:00:00";
		long starttime = format.formatToLong(startString);
		long endtime;
		if (month == (calendar.get(Calendar.MONTH + 1))) {
			endtime = System.currentTimeMillis();
		} else {
			if (month >= 9) {
				if (month == 12) {
					endString = "" + (year + 1) + "-01" + "-01 00:00:00";
				} else {
					endString = "" + year + "-" + (month + 1) + "-01 00:00:00";
				}
			} else
				endString = "" + year + "-0" + (month + 1) + "-01 00:00:00";
		}
		endtime = format.formatToLong(endString);
		DBObject query = timeScope(filed, starttime, endtime);
		return query;
	}
    private static Gson gson=new Gson();
	private static Logger logger = Logger.getLogger(MongoHelper.class);
	private MongoConnection mConnection;
}
