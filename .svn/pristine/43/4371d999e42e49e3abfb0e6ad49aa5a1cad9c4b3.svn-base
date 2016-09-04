/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.ClickInfo;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Kario
 */
@Repository("clickInfoDao")
public class ClickInfoDao implements BasicDao<ClickInfo> {
	public int save(ClickInfo clickInfo) {
		clickInfo.set_id();
		clickInfo.setStatus(ToolConstants.VALID_INT);
		DBObject object = (DBObject) JSON.parse(gson.toJson(clickInfo));
		collection.insert(object);
		logger.info("点击信息保存成功");
		MarkInfoDao markInfoDao = new MarkInfoDao();
		markInfoDao.addDirectCount(clickInfo.getMark_id());
		markInfoDao.generateTrust(clickInfo.getMark_id(), 1);
		// markInfoDao.closeClient();
		logger.info("标记信息修改成功");
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
		if (collection.find(query).hasNext())
			collection.update(query, update);
		else {
			logger.info(ToolConstants.DOCNOTFOUND_STRING);
		}
	}

	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub

		if (collection.find(condition).hasNext()) {
			List<DBObject> list = searchList(condition);
			MarkInfoDao markInfoDao = new MarkInfoDao();
			for (int i = 0; i < list.size(); i++) {
				ClickInfo clickInfo = gson.fromJson(list.get(i).toString(),
						ClickInfo.class);
				String mark_id = clickInfo.getMark_id();
				DBObject query = new BasicDBObject("_id", new ObjectId(mark_id));
				markInfoDao.update(query, new BasicDBObject("$inc",
						new BasicDBObject().append("directCount", -1)));
				update(mHelper.createQuery("mem_id", clickInfo.getMem_id()),
						new BasicDBObject().append("$set", new BasicDBObject()
								.append("status", ToolConstants.DELETE_INT)));
			}
		} else {
			logger.info(ToolConstants.DOCNOTFOUND_STRING);
			return;
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

	/**
	 * 
	 * 保存点击记录----点击将影响标记信息的链接数
	 */
	// 统计某一用户的点击量
	public int getClickTime(String mem_id) {
		if (!mem_id.equals(ToolConstants.DOCNOTFOUND_STRING))
			return collection.find(mHelper.createQuery("mem_id", mem_id))
					.count();
		else
			return 0;
	}
    public int getClickTime(String mem_id,String mark_id){
    	if (!mem_id.equals(ToolConstants.DOCNOTFOUND_STRING))
			return collection.find(mHelper.createQuery("mem_id", mem_id,"mark_id",mark_id))
					.count();
		else
			return 0;
    }
	// sign 为排序方式，统计数升序或降序
	public AggregationOutput clickTime(String field, DBObject query,
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
	
	// 获取域名-----由图片地址得到
	public String getDomainName(String urlString) {
		String domain = "";
		int index = urlString.indexOf(".");
		String str = urlString.substring(index + 1);
		int index1 = str.indexOf(".");
		domain = str.substring(0, index1);
		return domain;
	}
	//获取当前系统总点击数
	public int getNumberOfClick() {
		return collection.find(
				mHelper.createQuery("status", ToolConstants.VALID_INT)).count();
	}
   //获取某月的增来那个
	public int getNumberOfClick(int year,int month) {
		DBObject query = mHelper.generateStartAndEndTime("date", year, month);
		return collection.find(query).count();
	}
   //获取某一标记最后点击时间
	public long lastClickDate(String mark_id){
		DBObject query=new BasicDBObject();
		query.put("mark_id", mark_id);
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortObject=new BasicDBObject();
		sortObject.put("date", -1);
		DBCursor cursor=collection.find(query).sort(sortObject);
		if(cursor.hasNext())
			return gson.fromJson(cursor.next().toString(), ClickInfo.class).getDate();
		else 
			return 0;
	}
	private DBCollection collection = mHelper.getConnection().getCollection(
			"clickinfo");
	private static Logger logger = Logger.getLogger(ClickInfoDao.class);

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}
	/*public static void main(String[] args){
		ClickInfoDao cDao=new ClickInfoDao();
		logger.info(cDao.lastClickDate("54042eb945ce82f691998b14"));
	}*/
}
