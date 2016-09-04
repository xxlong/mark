/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.Administrator;
import com.anyway.imagemark.domain.LogInfo;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.mail.SendMail;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Administrator
 * 
 */
@Repository("merchantDao")
public class MerchantDao implements BasicDao<Merchant> {
	/*public int save(Merchant merchant) {
		int str = 0;
		if (mailValid(merchant.getMerchantMail())
				&& nameValid(merchant.getMerchantName())) {
			merchant.set_id();
			merchant.setDate(System.currentTimeMillis());
			merchant.setStatus(ToolConstants.VERIFY_INT);
			// merchant.setStatus(ToolConstants.VALID_INT);
			DBObject object = (DBObject) JSON.parse(gson.toJson(merchant));
			collection.insert(object);
			Administrator administrator = new Administrator();
			administrator.set_id();
			administrator.setAdminName(merchant.getMerchantName());
			administrator.setAdminPassword(merchant.getMerchantPassword());
			administrator.setAdminMail(merchant.getMerchantMail());
			administrator.setRole(ToolConstants.Role_Merchant);
			administrator.setStatus(ToolConstants.VALID_INT);
			collectionAdmin.insert((DBObject) JSON.parse(gson
					.toJson(administrator)));
		} else if (!mailValid(merchant.getMerchantMail())) {
			str = ToolConstants.ResultStatus_Fail_MailUsed;
		} else if (!nameValid(merchant.getMerchantName())) {
			str = ToolConstants.ResultStatus_Fail_NameUsed;
		} else {
			str = ToolConstants.ResultStatus_Fail_MailAndNameUsed;
		}
		return str;
	}*/

	public DBObject search(String field, String value) {
		// TODO Auto-generated method stub
		DBObject query = mHelper.createQuery(field, value);
		query.put("status", ToolConstants.VALID_INT);
		return mHelper.findByCondition(collection, query);
	}

	public DBObject search(String field1, String value1, String field2,
			String value2) {
		// TODO Auto-generated method stub
		DBObject query = mHelper.createQuery(field1, value1, field2, value2);
		query.put("status", ToolConstants.VALID_INT);
		return mHelper.findByCondition(collection, query);
	}

	public DBObject search(DBObject query) {
		// TODO Auto-generated method stub
		// query.put("status", ToolConstants.VALID_INT);
		return mHelper.findByCondition(collection, query);
	}

	public List<DBObject> search(int num) {
		// TODO Auto-generated method stub
		return mHelper.listFindDefault(collection, num);
	}

	public PageContent<DBObject> search(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		return mHelper
				.listPager(collection, query, currentPage, sortOrder, num);
	}

	public void update(DBObject query, DBObject update) {
		// TODO Auto-generated method stub
		query.put("status", ToolConstants.VALID_INT);
		if (collection.find(query).hasNext())
			collection.update(query, update);
		else {
			log.info(ToolConstants.DOCNOTFOUND_STRING);
		}
	}

	// 商家删除时单个删除--相应标记也应删除
	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub
		List<DBObject> list = searchList(condition);
		log.info(list.toString());
		MarkInfoDao markInfoDao = new MarkInfoDao();
		LogInfoDao logInfoDao = new LogInfoDao();
		for (int i = 0; i < list.size(); i++) {
			update(condition, new BasicDBObject().append("$set",
					new BasicDBObject("status", ToolConstants.DELETE_INT).append("deleteDate",System.currentTimeMillis())));
			log.info("商家修改标记成功");
			String mer_id = list.get(i).get("_id").toString();
			DBObject query = new BasicDBObject();
			query.put("mer_id", mer_id);
			query.put("status", ToolConstants.VALID_INT);
			markInfoDao.deleteByCondition(query);
			logInfoDao
					.deleteByCondition(mHelper.createQuery("user_id", mer_id));
		}
	}

	public PageContent<DBObject> Deleted(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		if (query != null) {
			query.put("status", ToolConstants.DELETE_INT);
			log.info("query in not null");
		} else {
			query = new BasicDBObject();
			query.put("status", ToolConstants.DELETE_INT);
			log.info("query in null");
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
		if (search(query) != null)
			log.info("恢复商家");
		collection.update(query, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)));
	}

	public void closeClient() {
		// TODO Auto-generated method stub
		mHelper.getConnection().getClient().close();
	}

	public String get_id(DBObject query) {
		query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext())
			return cursor.next().get("_id").toString();
		else
			return ToolConstants.DOCNOTFOUND_STRING;
	}

	public String get_id(String name) {
		DBObject query = mHelper.createQuery("merchantName", name);
		//query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext())
			return cursor.next().get("_id").toString();
		else
			return ToolConstants.DOCNOTFOUND_STRING;
	}

	// 读取商家状态,获取生命周期
	public long generateMarkValid(String mer_id) {
		return ToolConstants.ONEDAY_LONG;
	}

	public boolean mailValid(String mail) {
		DBObject query = mHelper.createQuery("merchantMail", mail);
		if (collection.find(query).hasNext()
				|| collectionMember.find(
						mHelper.createQuery("memberMail", mail)).hasNext())
			return false;
		else
			return true;
	}

	public boolean nameValid(String name) {
		DBObject query = mHelper.createQuery("merchantName", name);
		// query.put("status", new
		// BasicDBObject("$ne",ToolConstants.DELETE_INT));
		if (collection.find(query).hasNext()
				|| collectionMember.find(
						mHelper.createQuery("memberName", name)).hasNext()
				|| collectionAdmin.find(mHelper.createQuery("adminName", name))
						.hasNext())
			return false;
		else
			return true;
	}

	// 登陆成功后记录登陆信息----type为name或mail
	public int login(String userName, String pwd, int type, String ipInfo) {
		int login_status = 0;
		DBCursor cursor = null;
		String mer_id = "";
		DBObject query = new BasicDBObject();
		if (type == 1) {
			query = mHelper.createQuery("merchantMail", userName);
			query.put("status", ToolConstants.VALID_INT);
			cursor = collection.find(query);
			mer_id = get_id(mHelper.createQuery("merchantMail", userName));
			log.info("login use mail");
		} else if (type == 0) {
			query = mHelper.createQuery("merchantName", userName);
			query.put("status", ToolConstants.VALID_INT);
			cursor = collection.find(query);
			mer_id = get_id(mHelper.createQuery("merchantName", userName));
			log.info("login use name");
		} else {
			log.info("The value of type error");
		}
		// status
		if (cursor.hasNext()) {
			DBObject obj = cursor.next();
			Merchant mer = gson.fromJson(obj.toString(), Merchant.class);
			String password = mer.getMerchantPassword();
			log.info(password);
			log.info(pwd);
			if (mer.getStatus() == ToolConstants.VALID_INT) {
				if (password.equals(pwd)) {
					login_status = ToolConstants.LOGIN_SUCCESS;
					LogInfoDao lDao = new LogInfoDao();
					LogInfo logInfo = new LogInfo();
					logInfo.set_id();
					logInfo.setUser_id(mer_id);
					logInfo.setIpInfo(ipInfo);
					logInfo.setLoginTime(System.currentTimeMillis());
					logInfo.setLoginType("merchant");
					lDao.save(logInfo);
					// lDao.closeClient();
				} else {
					login_status = ToolConstants.LOGIN_INCORRECT;
					log.info("The password and name not correct");
				}
			} else {
				login_status = ToolConstants.ResultStatus_Fail_NoAuthority;
				log.info("The documnent status  error");
			}

		} else {
			log.info("The documnent status  not exists");
			login_status = ToolConstants.LOGIN_NOTEXIST;
		}
		return login_status;
	}

	// 找回密码------返回密码和邮箱,
	public Merchant getPwd(String name) {
		DBObject query = mHelper.createQuery("merchantName", name);
		query.put("status", new BasicDBObject("$ne", ToolConstants.DELETE_INT));
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext())
			return gson.fromJson(cursor.next().toString(), Merchant.class);
		else
			return null;
	}

/*	public Merchant getMerchantByNameOrMail(String merchantNameOrMail) {
		DBObject query = mHelper
				.createQuery("merchantName", merchantNameOrMail);
		 query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			log.info("login use name:" + merchantNameOrMail);
			return gson.fromJson(cursor.next().toString(), Merchant.class);
		} else {
			log.info("login use mail:" + merchantNameOrMail);
			DBObject otherQuery = mHelper.createQuery("merchantMail",
					merchantNameOrMail);
			otherQuery.put("status", ToolConstants.VALID_INT);
			cursor = collection.find(otherQuery);
			if (cursor.hasNext()) {
				return gson.fromJson(cursor.next().toString(), Merchant.class);
			} else {
				return null;
			}
		}
	}*/

	public Merchant queryDeletedMerchantByName(String memberName) {
		DBObject query = mHelper.createQuery("merchantName", memberName);
		query.put("status", ToolConstants.DELETE_INT);
		DBCursor cursor = collection.find(query);
		Merchant merchant = gson.fromJson(cursor.next().toString(),
				Merchant.class);
		return merchant;
	}

	public Merchant queryNotVerrifyMerchantByName(String memberName) {
		DBObject query = mHelper.createQuery("merchantName", memberName);
		query.put("status", ToolConstants.VERIFY_INT);
		DBCursor cursor = collection.find(query);
		Merchant merchant = gson.fromJson(cursor.next().toString(),
				Merchant.class);
		return merchant;
	}

	// 更改密码--------name字段唯一
	public boolean changePwd(String name, String newPwd) {
		boolean flag = false;
		DBObject query = mHelper.createQuery("merchantName", name);
		query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			log.info("update");
			BasicDBObject obj = new BasicDBObject();
			obj.append("$set",mHelper.createQuery("merchantPassword", newPwd));
			collection.update(query, obj);
			log.info("update success");
			flag = true;
		} else
			log.info(ToolConstants.DOCNOTFOUND_STRING);
		return flag;
	}

/*	*//**
	 * @author Kario 审核商家，审核通过生成系统消息
	 *//*
	public void setStatusMerchant(String merchantName) {
		NotificationDao notificationDao = new NotificationDao();
		String mer_id = get_id(merchantName);
		DBObject object = new BasicDBObject();
		object.put("merchantName", merchantName);
		collection.update(object, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)));
		notificationDao.generateMessageForMerchant("恭喜您，" + merchantName
				+ ",您已被审核通过，谢谢支持", mer_id);
	}*/

	/**
	 * @author Kario 获取当前系统中有效商家总数
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
	public int getNumberOfMerchant(int year, int month) {
		DBObject query = mHelper.generateStartAndEndTime("date", year, month);
		query.put("status", ToolConstants.VALID_INT);
		return collection.find(query).count();
	}
	
	/**
	 * @author Kario 增加商家的总点赞数
	 * 
	 */
    public void generateTrust(String mer_id,int trust){
    	BasicDBObject query = new BasicDBObject();
		query.append("_id", mer_id);
		query.append("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if(cursor.hasNext()){
			collection.update(
					query,
					new BasicDBObject().append("$inc",
							new BasicDBObject().append("merchantTrust", trust)));
		}
    }
    
	private DBCollection collection = mHelper.getConnection().getCollection(
			"merchant");
	private DBCollection collectionMember = mHelper.getConnection()
			.getCollection("member");
	private DBCollection collectionAdmin = mHelper.getConnection()
			.getCollection("administrator");
	private static Logger log = Logger.getLogger(MerchantDao.class);

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}
	
	
	
	
	//********************************************************KG**********************************************	
	//
	public Merchant getMerchantByNameOrMail(String merchantNameOrMail,int status) {
		DBObject query = mHelper
				.createQuery("merchantName", merchantNameOrMail);
		 query.put("status", status);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			log.info("login use name:" + merchantNameOrMail);
			return gson.fromJson(cursor.next().toString(), Merchant.class);
		} else {
			log.info("login use mail:" + merchantNameOrMail);
			DBObject otherQuery = mHelper.createQuery("merchantMail",
					merchantNameOrMail);
			otherQuery.put("status", status);
			cursor = collection.find(otherQuery);
			if (cursor.hasNext()) {
				return gson.fromJson(cursor.next().toString(), Merchant.class);
			} else {
				return null;
			}
		}
	}
	
	
	/**
	 * @author Kario 审核商家，审核通过生成系统消息
	 */
	public void setStatusMerchant(String merchantName) {
		NotificationDao notificationDao = new NotificationDao();
		String mer_id = get_id(merchantName);
		DBObject object = new BasicDBObject();
		object.put("merchantName", merchantName);
		collection.update(object, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)));
		
		DBObject object2 = new BasicDBObject();
		object2.put("adminName", merchantName);
		collectionAdmin.update(object2, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)));
		
		notificationDao.generateMessageForMerchant("恭喜您，" + merchantName
				+ ",您已被审核通过，谢谢支持", mer_id);
	}
	
	
	//激活用户
	public void activeMerchant(String username) {
		DBObject object = new BasicDBObject();
		object.put("merchantName", username);
		collection.update(object, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VERIFY_INT)));
	}
	

	/*	// 更改商家密码
	public boolean changePwd_AM(String name, String newPwd) {
		boolean flag = false;
		BasicDBObject query = new BasicDBObject();
		query.append("merchantName", name);
		DBCursor cursor = collection.find(query);
		Merchant merchant = new Merchant();
		if (cursor.hasNext()) {
			merchant = gson.fromJson(cursor.next().toString(), Merchant.class);
			merchant.setMerchantPassword(newPwd);
			DBObject object = (DBObject) JSON.parse(gson.toJson(merchant));
			collection.remove(query);
			collection.insert(object);
			flag = true;
		} else {
			log.info("该商家不存在");
		}
		// 数据丢失?
		return flag;
	}*/
	

	
	// 更改密码--------name字段唯一
	public boolean changePwd_AM(String name, String newPwd) {
		boolean flag = false;
		System.out.println(name);
		System.out.println(newPwd);
		DBObject query = mHelper.createQuery("merchantName", name);
		//query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			log.info("update");
			BasicDBObject obj = new BasicDBObject();
			obj.append("$set",mHelper.createQuery("merchantPassword", newPwd));
			collection.update(query, obj);
			log.info("update success");
			flag = true;
		} else
			log.info(ToolConstants.DOCNOTFOUND_STRING);
		return flag;
	}
	
//************************************************KG************************************************	
	//增加商家
	public int save(Merchant merchant) {
		int str = 0;
		if (mailValid(merchant.getMerchantMail())
				&& nameValid(merchant.getMerchantName())) {
			merchant.set_id();
			merchant.setDate(System.currentTimeMillis());
			//merchant.setStatus(ToolConstants.VERIFY_INT);
			merchant.setStatus(ToolConstants.NO_ACTIVE_INT);
			String randomCode = RandomStringUtils.randomAlphanumeric(30);
			merchant.setRandomCode(randomCode);
			// merchant.setStatus(ToolConstants.VALID_INT);
			DBObject object = (DBObject) JSON.parse(gson.toJson(merchant));
			collection.insert(object);
			Administrator administrator = new Administrator();
			administrator.set_id();
			administrator.setAdminName(merchant.getMerchantName());
			administrator.setAdminPassword(merchant.getMerchantPassword());
			administrator.setAdminMail(merchant.getMerchantMail());
			administrator.setRole(ToolConstants.Role_Merchant);
			//administrator.setStatus(ToolConstants.VALID_INT);
			administrator.setStatus(ToolConstants.NO_ACTIVE_INT);
			//增加随机码
			administrator.setRandomCode(randomCode);
			collectionAdmin.insert((DBObject) JSON.parse(gson.toJson(administrator)));
		} else if (!mailValid(merchant.getMerchantMail())) {
			str = ToolConstants.ResultStatus_Fail_MailUsed;
		} else if (!nameValid(merchant.getMerchantName())) {
			str = ToolConstants.ResultStatus_Fail_NameUsed;
		} else {
			str = ToolConstants.ResultStatus_Fail_MailAndNameUsed;
		}
		return str;
	}
	//增加商家
	public int save(Merchant merchant,String active_url,SendMail sendMail,String randomCode) {
		int str = 0;
		boolean flag = sendMail.sendModelMail(merchant.getMerchantMail(), "imagemark 邮件激活", "register.vm",active_url, merchant.getMerchantName());
		System.out.println("**************************MerchantDao539 **************flag="+flag);
		if(flag==true){
			if (mailValid(merchant.getMerchantMail())&& nameValid(merchant.getMerchantName())) {
				merchant.set_id();
				merchant.setDate(System.currentTimeMillis());
				//merchant.setStatus(ToolConstants.VERIFY_INT);
				merchant.setStatus(ToolConstants.NO_ACTIVE_INT);
//				String randomCode = RandomStringUtils.randomAlphanumeric(30);
				merchant.setRandomCode(randomCode);
				// merchant.setStatus(ToolConstants.VALID_INT);
				DBObject object = (DBObject) JSON.parse(gson.toJson(merchant));
				collection.insert(object);
				Administrator administrator = new Administrator();
				administrator.set_id();
				administrator.setAdminName(merchant.getMerchantName());
				administrator.setAdminPassword(merchant.getMerchantPassword());
				administrator.setAdminMail(merchant.getMerchantMail());
				administrator.setRole(ToolConstants.Role_Merchant);
				//administrator.setStatus(ToolConstants.VALID_INT);
				administrator.setStatus(ToolConstants.NO_ACTIVE_INT);
				//增加随机码
				administrator.setRandomCode(randomCode);
				collectionAdmin.insert((DBObject) JSON.parse(gson.toJson(administrator)));
			} else if (!mailValid(merchant.getMerchantMail())) {
				str = ToolConstants.ResultStatus_Fail_MailUsed;
			} else if (!nameValid(merchant.getMerchantName())) {
				str = ToolConstants.ResultStatus_Fail_NameUsed;
			} else {
				str = ToolConstants.ResultStatus_Fail_MailAndNameUsed;
			}
		}else{
			str = ToolConstants.ResultStatus_Fail_MailNOTEXIST;
		}
		System.out.println("**************************MerchantDao572 **************str="+str);
		return str;
	}
//**********************************************KG*************************************
	
	//根据username查找数据库中的商家
	public Merchant findMerchantByName(String username){
		BasicDBObject query = new BasicDBObject();
		query.append("merchantName", username);
		DBCursor cursor = collection.find(query);
		Merchant merchant=null;
		if (cursor.hasNext()){
			DBObject obj = cursor.next();
			merchant = gson.fromJson(obj.toString(),Merchant.class);
		}
		return merchant;
	}	
	
	//********************************************************KG**********************************************
}
