/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javassist.expr.NewArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Kario
 *
 */
@Repository("notificationDao")
public class NotificationDao implements BasicDao <Notification>{
	public int save(Notification notification){
		notification.set_id();
		notification.setStatus(ToolConstants.VALID_INT);
		notification.setSendTime(System.currentTimeMillis());
		DBObject object=(DBObject) JSON.parse(gson.toJson(notification));
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
		if (collection.find(query).hasNext())
			collection.update(query, update);
		else {
			log.info(ToolConstants.DOCNOTFOUND_STRING);
		}
	}
	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub
		if(collection.find(condition).hasNext()){
		update(condition, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.DELETE_INT)));
		}else{
			log.info("no result fit the condition");
		}
	}
	public PageContent<DBObject> Deleted(DBObject query,DBObject sortOrder,int currentPage,
			int num) {
		// TODO Auto-generated method stub
		if(query!=null){
			query.put("status", ToolConstants.DELETE_INT);
			log.info("query in not null");
		}else {
			query=new BasicDBObject();
			query.put("status", ToolConstants.DELETE_INT);
			log.info("query in null");
		}
		return mHelper.listPager(collection, query, currentPage, sortOrder, num);
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

	//记录消息----信息入库时，通知信息状态为save，删除时标记状态为delete
	public void recordnotification(Notification notification){
			notification.setStatus(ToolConstants.VALID_INT);
			DBObject object=(DBObject) JSON.parse(gson.toJson(notification));
			collection.insert(object);
	}
	public List<Notification> forMemberList(){
		List<Notification> myList=new ArrayList<Notification>();
		BasicDBObject queryObject=new BasicDBObject();
		BasicDBList list=new BasicDBList();
		list.add(new BasicDBObject("notifier",ToolConstants.NOTIFIYALL_STRING));
		list.add(new BasicDBObject("notifier",ToolConstants.NOTIFIYMEMBER_STRING));
		queryObject.append("notifier", new BasicDBObject("$or",list));
		DBCursor  cursor=collection.find(queryObject);
		while(cursor.hasNext()){
			Notification notification=gson.fromJson(cursor.next().toString(), Notification.class);
			myList.add(notification);
		}
		return myList;
	}
	public List<Notification> forMerchant(){
		List<Notification> myList=new ArrayList<Notification>();
		BasicDBObject queryObject=new BasicDBObject();
		BasicDBList list=new BasicDBList();
		list.add(new BasicDBObject("notifier",ToolConstants.NOTIFIYALL_STRING));
		list.add(new BasicDBObject("notifier",ToolConstants.NOTIFIYMERCHANT_STRING));
		queryObject.append("notifier", new BasicDBObject("$or",list));
		DBCursor  cursor=collection.find(queryObject);
		while(cursor.hasNext()){
			Notification notification=gson.fromJson(cursor.next().toString(), Notification.class);
			myList.add(notification);
		}
		return myList;
	}
	//获取公告信息----公告信息不含类别，面向全部
	public List<Notification> getNotice(){
		List<Notification> myList=new ArrayList<Notification>();
		BasicDBObject queryBasicDBObject=new BasicDBObject();
		queryBasicDBObject.append("type", 2);
		DBCursor cursor=collection.find(queryBasicDBObject);
		while(cursor.hasNext()){
			Notification notification=gson.fromJson(cursor.next().toString(), Notification.class);
			myList.add(notification);
		}
		return myList;
	}
	//查询通知信息 ---按关键字查询,内容和标题？
	public List<Notification> searchByKey(String keyWord){
		List<Notification> myList=new ArrayList<Notification>();
		BasicDBObject queryBasicDBObject=new BasicDBObject();
		Pattern pattern=Pattern.compile(keyWord);
		queryBasicDBObject.append("content", pattern);
		return myList;
	}
	/**
	 * @author Administrator 产生相应的消息给会员，并保存
	 *@param message 消息内容
	 */
	public void generateMessageForMember(String message,String noticeId){
		Notification notification=new Notification();
		notification.set_id();
		notification.setNotifier(ToolConstants.NOTIFIYMEMBER_STRING);
		notification.setSendTime(System.currentTimeMillis());
		notification.setStatus(ToolConstants.VALID_INT);
		notification.setTitle("系统消息");
		notification.setType(3);
		notification.setContent(message);
		notification.setNoticeID(noticeId);
		collection.insert((DBObject)JSON.parse(gson.toJson(notification)));
	}
	public void generateMessageForMerchant(String message,String noticeId){
		Notification notification=new Notification();
		notification.set_id();
		notification.setNotifier(ToolConstants.NOTIFIYMERCHANT_STRING);
		notification.setSendTime(System.currentTimeMillis());
		notification.setStatus(ToolConstants.VALID_INT);
		notification.setTitle("系统消息");
		notification.setType(3);
		notification.setContent(message);
		notification.setNoticeID(noticeId);
		collection.insert((DBObject)JSON.parse(gson.toJson(notification)));
	}
	public void delete(Notification notification){
		long time=notification.getSendTime();
		BasicDBObject query=new BasicDBObject();
		query.append("sendTime", time);
		notification.setStatus(ToolConstants.DELETE_INT);
		DBObject freshObject=(DBObject) JSON.parse(gson.toJson(notification));
		collection.update(query, freshObject);
	}
	public void delete(String id){
		BasicDBObject query=new BasicDBObject();
		query.put("_id", id);
		DBObject update=new BasicDBObject();
		update.put("$set", new BasicDBObject().put("status", ToolConstants.DELETE_INT));
		if(collection.find(query).hasNext()){
			collection.update(query, update);
		}
	}
	private DBCollection collection = mHelper.getConnection().getCollection("notification");
	private static Logger log = Logger.getLogger(NotificationDao.class);
	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return null;
	}
}
