
package com.anyway.imagemark.daoimpl;

import java.util.List;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.Complain;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Kario
 *
 */
public class ComplainDao implements BasicDao<Complain> {

	public int save(Complain object) {
		// TODO Auto-generated method stub
		int status = 0;
		object.set_id((new ObjectId()).toString());
		object.setComplainTime();
		object.setStatus(ToolConstants.VERIFY_INT);
		DBObject resultDbObject = (DBObject) JSON.parse(gson.toJson(object));
		int tempLength = collection.find().count();
		collection.insert(resultDbObject);
		int currLength = collection.find().count();
		if (tempLength >= currLength)
			status = 1;
		return status;
	}

	public DBObject search(String field, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public DBObject search(String field1, String value1, String field2,
			String value2) {
		// TODO Auto-generated method stub
		return null;
	}

	public DBObject search(DBObject query) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DBObject> search(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return null;
	}

	public PageContent<DBObject> search(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		return mHelper
				.listPager(collection, query, currentPage, sortOrder, num);
	}
	/**
	 * @author Kario 改变投诉状态，如果投诉属实，删除该条标记
	 * @param 
	 */
	public void changeStatus(String _id, int status) {
		BasicDBObject condition = new BasicDBObject();
		logger.info("改变投诉状态");
		condition.put("_id", _id);
		DBCursor cursor = collection.find(condition);
		if (cursor.hasNext()) {
			collection.update(condition, new BasicDBObject("$set",
					new BasicDBObject("status", status)));
			if(status==2){
				//当举报被通过，即举报属实,删除标记
				DBObject object=cursor.next();
				logger.info("举报属实,举报信息为："+gson.toJson(object));
				String mark_id=object.get("mark_id").toString();
				String mem_id=object.get("mem_id").toString();
				MemberDao memberDao=new MemberDao();
				MarkInfoDao dao=new MarkInfoDao();
				dao.addInformTimes(mark_id);
				memberDao.generateScore(mem_id, 4);
			}
		}
	}

	public void update(DBObject query, DBObject update) {
		// TODO Auto-generated method stub

	}

	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub

	}

	public PageContent<DBObject> Deleted(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	public void Restore() {
		// TODO Auto-generated method stub

	}

	public void Restore(DBObject query) {
		// TODO Auto-generated method stub

	}

	public void closeClient() {
		// TODO Auto-generated method stub

	}
    public static void main(String[] args){
    	ComplainDao dao=new ComplainDao();
    	Complain complain=new Complain();
    	complain.setComplainer("member1");
    	complain.setComplainReason(4);
    	complain.setMark_id("5434fffe45cef0e376e40760");
    	dao.save(complain);
        dao.changeStatus("5551aceea310a0a5451aecdf", 2);
    }
	private DBCollection collection = mHelper.getConnection().getCollection(
			"complain");
	private static Logger logger = Logger.getLogger(ComplainDao.class);
}
