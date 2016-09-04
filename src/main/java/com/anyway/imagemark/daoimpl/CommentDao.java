/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.List;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.Comment;
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
@Repository("commentDao")
public class CommentDao implements BasicDao<Comment> {

	public int save(Comment comment) {
		// TODO Auto-generated method stub
		int status = 0;
		// 存评分
		/*if (comment.getComment() != 0) {
			// 判断是否评价过
			if (isCommented(comment.getMark_id(), comment.getMem_id(), 1)) {
				if (recordExist(comment.getMark_id(), comment.getMem_id())) {
					comment.set_id();
					comment.setStatus(ToolConstants.VALID_INT);
					DBObject object = (DBObject) JSON.parse(gson
							.toJson(comment));
					collection.insert(object);
					logger.info("评论信息保存成功");
				} else {
					String idString = search("mem_id", comment.getMem_id(),
							"mark_id", comment.getMark_id()).get("_id")
							.toString();
					DBObject queryDbObject = new BasicDBObject();
					queryDbObject.put("_id", idString);
					collection
							.update(queryDbObject,
									new BasicDBObject("$set",
											new BasicDBObject("comment",
													comment.getComment())));
				}
				MemberDao memberDao = new MemberDao();
				BasicDBObject query = new BasicDBObject();
				query.append("_id", comment.getMem_id());
				DBObject obj = memberDao.search(query);
				logger.info(obj.toString());
				String name = obj.get("memberName").toString();
				memberDao.generateScore(name);
				// memberDao.closeClient();
				logger.info("修改会员信息成功");
				MarkInfoDao markInfoDao = new MarkInfoDao();
				markInfoDao.addComponentTrust(comment.getMark_id(),
						comment.getComment());
				markInfoDao.addCommentNum(comment.getMark_id(),
						comment.getComment());
				// markInfoDao.closeClient();
				logger.info("修改标记信息成功");
			} else {
				logger.info("You have commented the markinfo");
				status = ToolConstants.ResultStatus_Fail_ExistedError;
			}
		}*/
		// 存点赞
		if (comment.isPhraise() != false) {
			if (isCommented(comment.getMark_id(), comment.getMem_id(), 2)) {
				if (recordExist(comment.getMark_id(), comment.getMem_id())) {
					comment.set_id();
					comment.setStatus(ToolConstants.VALID_INT);
					DBObject object = (DBObject) JSON.parse(gson
							.toJson(comment));
					collection.insert(object);
					logger.info("评论信息保存成功");
				} else {
					String idString = search("mem_id", comment.getMem_id(),
							"mark_id", comment.getMark_id()).get("_id")
							.toString();
					DBObject queryDbObject = new BasicDBObject();
					queryDbObject.put("_id", idString);
					collection.update(queryDbObject, new BasicDBObject("$set",
							new BasicDBObject("phraise", comment.isPhraise())));
				}
				MemberDao memberDao=new MemberDao();
				MarkInfoDao dao = new MarkInfoDao();
				memberDao.generateScore(comment.getMem_id(), 2);
				dao.addPraise(comment.getMark_id());
			} else {
				logger.info("You have commented the markinfo");
				status = ToolConstants.ResultStatus_Fail_ExistedError;
			}
		}
		if(comment.isCollected()!=false){
			//collect
			if(isCommented(comment.getMark_id(), comment.getMem_id(), 3)){
				if (recordExist(comment.getMark_id(), comment.getMem_id())) {
					comment.set_id();
					comment.setStatus(ToolConstants.VALID_INT);
					DBObject object = (DBObject) JSON.parse(gson
							.toJson(comment));
					collection.insert(object);
					logger.info("评论信息保存成功");
				} else {
					String idString = search("mem_id", comment.getMem_id(),
							"mark_id", comment.getMark_id()).get("_id")
							.toString();
					DBObject queryDbObject = new BasicDBObject();
					queryDbObject.put("_id", idString);
					collection.update(queryDbObject, new BasicDBObject("$set",
							new BasicDBObject("collected", comment.isCollected())));
				}
				
			}
		}
		//text评论
		if (comment.getTime() != 0) {
			if (isCommented(comment.getMark_id(), comment.getMem_id(), 4)) {
				if (recordExist(comment.getMark_id(), comment.getMem_id())) {
					comment.set_id();
					comment.setStatus(ToolConstants.VALID_INT);
					DBObject object = (DBObject) JSON.parse(gson
							.toJson(comment));
					collection.insert(object);
					logger.info("评论信息保存成功");
				} else {
					String idString = search("mem_id", comment.getMem_id(),
							"mark_id", comment.getMark_id()).get("_id")
							.toString();
					DBObject queryDbObject = new BasicDBObject();
					queryDbObject.put("_id", idString);
					collection.update(
							queryDbObject,
							new BasicDBObject("$set", new BasicDBObject()
									.append("time", comment.getTime())
									.append("textComment",
											comment.getTextComment())));
				}
				MemberDao memberDao=new MemberDao();
				MarkInfoDao dao = new MarkInfoDao();
				dao.addTextCount(comment.getMark_id());
				memberDao.generateScore(comment.getMem_id(), 3);
			} else {
				logger.info("You have commented the markinfo");
				status = ToolConstants.ResultStatus_Fail_ExistedError;
			}
		}

		return status;
	}

	public Comment queryDeletedCommentById(String _id) {
		DBObject query = mHelper.createQuery("_id", _id);
		query.put("status", ToolConstants.DELETE_INT);
		DBCursor cursor = collection.find(query);
		Comment comment = gson
				.fromJson(cursor.next().toString(), Comment.class);
		return comment;
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

	public PageContent<DBObject> search(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		query.put("status", ToolConstants.VALID_INT);
		return mHelper
				.listPager(collection, query, currentPage, sortOrder, num);
	}

	public void update(DBObject query, DBObject update) {
		// TODO Auto-generated method stub
		if (collection.find(query).hasNext())
			collection.update(query, update);
		else {
			logger.info(ToolConstants.DOCNOTFOUND_STRING);
		}
	}

	// 删除评论信息
	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub
		if (collection.find(condition).hasNext()) {
			List<DBObject> list = searchList(condition);
			MarkInfoDao markInfoDao = new MarkInfoDao();
			for (int i = 0; i < list.size(); i++) {
				Comment comment = gson.fromJson(list.get(i).toString(),
						Comment.class);
				String mark_id = comment.getMark_id();
				DBObject query = new BasicDBObject("_id", new ObjectId(mark_id));
				markInfoDao.update(
						query,
						new BasicDBObject("$inc", new BasicDBObject().append(
								"textCount", -1)));

				update(mHelper.createQuery("mem_id", comment.getMem_id()),
						new BasicDBObject().append("$set", new BasicDBObject()
								.append("textStatus", ToolConstants.DELETE_INT)));
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
	// sign 为排序方式，统计数升序或降序
	public AggregationOutput commentTimes(String field, DBObject query,
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

	// 检查会员是否已经评价，对一件商品只能评价一次,评价过返回false
	public boolean isCommented(String mark_id, String mem_id, int cal) {
		boolean flag = true;
		DBObject queryDbObject = new BasicDBObject();
		queryDbObject.put("mem_id", mem_id);
		queryDbObject.put("mark_id", mark_id);
		if (cal == 1) {
			// 评分
			queryDbObject.put("comment", new BasicDBObject("$ne", 0));
		} else if (cal == 2) {
			// d点赞
			queryDbObject.put("phraise", new BasicDBObject("$ne", false));
		} else if(cal==3){
			queryDbObject.put("collected", new BasicDBObject("$ne", false));
		}else{
			// 评论
			queryDbObject.put("time", new BasicDBObject("$ne", 0));
		}
		DBCursor cursor = collection.find(queryDbObject);
		if (cursor.hasNext()) {
			flag = false;
		}
		return flag;
	}

	public boolean recordExist(String mark_id, String mem_id) {
		boolean flag = true;
		DBObject queryDbObject = new BasicDBObject();
		queryDbObject.put("mem_id", mem_id);
		queryDbObject.put("mark_id", mark_id);
		DBCursor cursor = collection.find(queryDbObject);
		if (cursor.hasNext())
			flag = false;
		return flag;
	}

	// 统计用户评论次数
	public int getCommentTimes(String mem_id) {
		if (!mem_id.equals(ToolConstants.DOCNOTFOUND_STRING))
			return collection.find(new BasicDBObject("mem_id",mem_id).append("time",new BasicDBObject("$exists",true).append("$ne", 0))).count();
		else
			return 0;
	}
	//获取商品的评论信息
	public PageContent<DBObject> getTextComment(String mark_id,int page,int pageSize){
		DBObject queryDbObject=new BasicDBObject();
		queryDbObject.put("mark_id", mark_id);
		queryDbObject.put("status", ToolConstants.VALID_INT);
		queryDbObject.put("time", new BasicDBObject().append("$exists", true).append("$ne", 0));
		DBObject sortOrder=new BasicDBObject();
		sortOrder.put("time", -1);
		return mHelper.listPager(collection, queryDbObject, page, sortOrder, pageSize);
	}

	private DBCollection collection = mHelper.getConnection().getCollection(
			"comment");
	private static Logger logger = Logger.getLogger(CommentDao.class);

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}

	public List<DBObject> searchListByConditonAndSort(DBObject query,
			DBObject sort) {
		// TODO Auto-generated method stub
		return mHelper.listFindByConditionAndSort(collection, query, sort);
	}

	/**
	 * @author Kario 获取当前系统中有效评论总数
	 */
	public int getNumberOfMember() {
		return collection.find(
				mHelper.createQuery("status", ToolConstants.VALID_INT)).count();
	}

	/**
	 * @author Kario 获取每月评论增量，不管是否有效，只关注注册时间是否在该段时间内
	 * @param month
	 *            月份
	 * @param year
	 *            年份
	 * @return int 该时间段记录的增量
	 */
	public int getNumberOfComment(int year, int month) {
		DBObject query = mHelper.generateStartAndEndTime("time", year,
				month);
		return collection.find(query).count();
	}

	
}
