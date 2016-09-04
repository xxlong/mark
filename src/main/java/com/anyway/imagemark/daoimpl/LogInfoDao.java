/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.LogInfo;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Administrator
 * 
 */
@Repository("logInfoDao")
public class LogInfoDao implements BasicDao<LogInfo> {
	public int save(LogInfo logInfo) {
		logInfo.set_id();
		logInfo.setStatus(ToolConstants.VALID_INT);
		DBObject object = (DBObject) JSON.parse(gson.toJson(logInfo));
		collection.insert(object);
		return ToolConstants.ResultStatus_Success;
	}

	public DBObject search(String field, String value) {
		// TODO Auto-generated method stub
		DBObject query = mHelper.createQuery(field, value);
		return mHelper.findByCondition(collection, query);
	}

	public DBObject search(String field1, String value1, String field2,
			String value2) {
		// TODO Auto-generated method stub
		DBObject query = mHelper.createQuery(field1, value1, field2, value2);
		return mHelper.findByCondition(collection, query);
	}

	public DBObject search(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.findByCondition(collection, query);
	}

	public List<DBObject> search(int num) {
		// TODO Auto-generated method stub
		return mHelper.listFindDefault(collection, num);
	}

	public PageContent<DBObject> search(DBObject query,DBObject sortOrder, int currentPage,int num
			) {
			// TODO Auto-generated method stub
			return mHelper.listPager(collection, query, currentPage,
					sortOrder,num);
		}

	public void update(DBObject query, DBObject update) {
		// TODO Auto-generated method stub
		while (collection.find(query).hasNext()) {
			collection.update(query, update);
		}
	}

	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub
		if(collection.find(condition).hasNext()){
			update(condition, new BasicDBObject().append("$set",
					new BasicDBObject().append("status", ToolConstants.DELETE_INT)));
		}
	}

	public PageContent<DBObject> Deleted(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		if (query != null) {
			query.put("status", ToolConstants.DELETE_INT);
			logger.info("query in not null");
		} else {
			query = new BasicDBObject();
			query.put("status", ToolConstants.DELETE_INT);
			logger.info("query in null");
		}
		return mHelper
				.listPager(collection, query, currentPage, sortOrder, num);
	}

	public void Restore() {
		// TODO Auto-generated method stub
		collection.update(mHelper.createQuery("status",
				ToolConstants.DELETE_INT), new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)),
				false, true);
	}

	public void Restore(DBObject query) {
		// TODO Auto-generated method stub
		collection.update(query, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)),
				false, true);
	}

	public void closeClient() {
		// TODO Auto-generated method stub
		mHelper.getConnection().getClient().close();
	}

	// 统计登陆次数
	public int getLoginTimes(String user_id) {
		return collection.find(mHelper.createQuery("user_id", user_id)).count();
	}

	// 获取上次登陆时间
	public long getLastTime(String user_id) {
		BasicDBObject odery = new BasicDBObject();
		odery.append("loginTime", -1);
		DBCursor cursor = collection
				.find(mHelper.createQuery("user_id", user_id)).sort(odery)
				.limit(5);
		if(cursor.hasNext()){
		DBObject object=cursor.next();
		LogInfo info = gson.fromJson(object.toString(), LogInfo.class);
		return info.getLoginTime();
		}else{
			logger.info("the sort result is null");
			return 0;
		}
	}

	// sign 为排序方式，统计数升序或降序
	public AggregationOutput loginTime(String field, DBObject query,
			String sign, int limit, int skip) {
		DBObject sort = new BasicDBObject();
		if (sign.equals("asc")) {
			sort.put("count", 1);
		} else if (sign.equals("des")) {
			sort.put("count", -1);
		} else {
			sort = null;
		}
		return mHelper.aggregate(collection, field, query, sort, limit, skip);
	}

	private DBCollection collection = mHelper.getConnection().getCollection(
			"loginfo");
	private static Logger logger = Logger.getLogger(LogInfoDao.class);

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}
}
