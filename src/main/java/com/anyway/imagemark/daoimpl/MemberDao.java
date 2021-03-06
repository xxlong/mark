/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.domain.Administrator;
import com.anyway.imagemark.domain.LogInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.mail.SendMail;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Repository("memberDao")
public class MemberDao implements BasicDao<Member> {
	/**
	 * @author Kario 使用值名对查询时，返回的数据为状态为有效的文档
	 */
	/*public int save(Member member) {
		int str = 0;
		if (mailValid(member.getMemberMail())
				&& nameValid(member.getMemberName())) {
			member.setMemberScore(0);
			member.setMemberLevel(0);
			member.setDate(System.currentTimeMillis());
			member.setStatus(ToolConstants.VALID_INT);
			member.setGoodComments(0);
			member.setBadComments(0);
			DBObject object = (DBObject) JSON.parse(gson.toJson(member));
			collection.insert(object);
			log.info("save member into Member successfully!");
			Administrator administrator = new Administrator();
			administrator.set_id();
			administrator.setAdminName(member.getMemberName());
			administrator.setAdminPassword(member.getMemberPassword());
			administrator.setAdminMail(member.getMemberMail());
			administrator.setRole(ToolConstants.Role_Member);
			administrator.setStatus(ToolConstants.VALID_INT);
			collectionAdmin.insert((DBObject) JSON.parse(gson
					.toJson(administrator)));
			log.info("save member into Administrator successfully!");
		} else if (!mailValid(member.getMemberMail())) {
			str = ToolConstants.ResultStatus_Fail_MailUsed;
		} else if (!nameValid(member.getMemberName())) {
			str = ToolConstants.ResultStatus_Fail_NameUsed;
		} else {
			str = ToolConstants.ResultStatus_Fail_MailAndNameUsed;
		}
		return str;
	}*/

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

	// 使用条件查询时，条件查询时需要对状态
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
		return mHelper
				.listPager(collection, query, currentPage, sortOrder, num);
	}

	public void update(DBObject query, DBObject update) {
		// TODO Auto-generated method stub
		if (collection.find(query).hasNext())
			collection.update(query, update);
		else {
			log.info(ToolConstants.DOCNOTFOUND_STRING);
		}
	}

	// 删除会员时，关联的评论、点击信息也删除---单个删除
	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub
		if (collection.find(condition).hasNext()) {
			update(condition, new BasicDBObject().append("$set",
					new BasicDBObject().append("status",
							ToolConstants.DELETE_INT).append("deleteDate", System.currentTimeMillis())));
			String idString = get_id(condition);
			CommentDao commentDao = new CommentDao();
			ClickInfoDao clickInfoDao = new ClickInfoDao();
			NotificationDao notificationDao=new NotificationDao();
			commentDao.deleteByCondition(mHelper
					.createQuery("mem_id", idString));
			clickInfoDao.deleteByCondition(mHelper.createQuery("mem_id",
					idString));
		   notificationDao.generateMessageForMember("很遗憾，您的操作失常，已被管理员删除", idString);
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
		collection.update(query, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)));
	}

	public void closeClient() {
		// TODO Auto-generated method stub
		mHelper.getConnection().getClient().close();
	}

	public String get_id(DBObject query) {
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext())
			return cursor.next().get("_id").toString();
		else
			return ToolConstants.DOCNOTFOUND_STRING;

	}
	public String get_id(String name) {
		DBCursor cursor = collection.find(mHelper.createQuery("memberName",
				name));
		if (cursor.hasNext())
			return cursor.next().get("_id").toString();
		else
			return ToolConstants.DOCNOTFOUND_STRING;
	}

	// 对会员的审核,查看会员是否有效--主要针对邮箱，一个邮箱只能注册一次。
	public boolean mailValid(String mail) {
		if (collection.find(mHelper.createQuery("memberMail", mail)).hasNext()
				|| collectionMerchant.find(
						mHelper.createQuery("merchantMail", mail)).hasNext())
			return false;
		else
			return true;
	}

	// 对会员的用户名验证，用户名唯一。
	public boolean nameValid(String name) {
		if (collection.find(mHelper.createQuery("memberName", name)).hasNext()
				|| collectionMerchant.find(
						mHelper.createQuery("merchantName", name)).hasNext()
				|| collectionAdmin.find(mHelper.createQuery("adminName", name))
						.hasNext())
			return false;
		else
			return true;
	}

	// 登陆时，可能使用邮箱和用户名登陆,浏览器前端需要指明登陆者使用的登陆类型type，登陆成功返回登陆者信息。
	public int login(String userName, String pwd, int type, String ipInfo) {
		int string = 0;
		DBCursor cursor = null;
		String mem_id = "";
		if (type == 1) {
			DBObject query = mHelper.createQuery("memberMail", userName);
			query.put("status", ToolConstants.VALID_INT);
			cursor = collection.find(query);
			mem_id = get_id(query);
			log.info("login use mail");
		} else if (type == 0) {
			DBObject query = mHelper.createQuery("memberName", userName);
			query.put("status", ToolConstants.VALID_INT);
			cursor = collection.find(query);
			mem_id = get_id(query);
			log.info("login use name");
		} else {
			log.info("the value of type error");
		}
		if (cursor.hasNext()) {
			DBObject obj = cursor.next();
			Member member = gson.fromJson(obj.toString(), Member.class);
			String password = member.getMemberPassword();
			// 查看会员状态
			if (member.getStatus() == 0) {
				log.info("数据异常");
			} else if (member.getStatus() == ToolConstants.DELETE_INT) {
				return ToolConstants.LOGIN_NOTEXIST;
			} else {
				if (password.equals(pwd)) {
					string = ToolConstants.LOGIN_SUCCESS;
					LogInfoDao lDao = new LogInfoDao();
					LogInfo logInfo = new LogInfo();
					logInfo.set_id();
					logInfo.setUser_id(mem_id);
					logInfo.setIpInfo(ipInfo);
					logInfo.setLoginTime(System.currentTimeMillis());
					logInfo.setLoginType("member");
					logInfo.setStatus(ToolConstants.VALID_INT);
					lDao.save(logInfo);
					// lDao.closeClient();
				} else {
					string = ToolConstants.LOGIN_INCORRECT;
				}
			}
		} else {
			string = ToolConstants.LOGIN_NOTEXIST;
		}
		return string;
	}

	// 找回密码-返回为对象，可以取回密码，邮箱等
	public Member getPwd(String name) {
		DBObject object = search("memberName", name);
		if (object != null)
			return gson.fromJson(object.toString(), Member.class);
		else {
			log.info(ToolConstants.DOCNOTFOUND_STRING);
			return null;
		}
	}

	public Member getMemberByNameOrMail(String memberNameOrMail) {
		DBCursor cursor = null;
		cursor = collection.find(mHelper.createQuery("memberName",
				memberNameOrMail));
		if (cursor.hasNext()) {
			log.info("login use name");
			return gson.fromJson(cursor.next().toString(), Member.class);
		} else {
			log.info("login use mail");
			cursor = collection.find(mHelper.createQuery("memberMail",
					memberNameOrMail));
			return gson.fromJson(cursor.next().toString(), Member.class);
		}
	}

	public void generateScore(String id,int flag) {
		DBObject query = mHelper.createQuery("_id", id);
		MemberDao memberDao = new MemberDao();
		DBObject object = search(query);
		Member member = gson.fromJson(object.toString(), Member.class);
		// 评价一次积分加5,点赞加2，点击加1，举报成功加10
		if(flag==1){
			//点击
			member.setMemberScore(member.getMemberScore() + 1);
		}else if(flag==2){
			//点赞
			member.setMemberScore(member.getMemberScore() + 2);
		}else if(flag==3){
			//评论
			member.setMemberScore(member.getMemberScore() + 5);
		}else if(flag==4){
			//举报
			member.setMemberScore(member.getMemberScore() + 10);
		}
		// 等级
		member.setMemberLevel(generateLeval(id, member.getMemberScore(),
				member.getMemberLevel()));
		DBObject obj = (DBObject) JSON.parse(gson.toJson(member));
		memberDao.update(query, obj);
	}

	/**
	 * @author Kario
	 * @param score
	 *            当前会员的积分
	 * @param level
	 *            当前会员的等级 当会员等级提升后，生成通知给用户
	 */
	private int generateLeval(String id, int score, int level) {
		int levelScore = 20 + 20 * level;
		int upScore = ((20 + levelScore) * (level + 1)) / 2;
		String name=collection.find(new BasicDBObject("_id",id)).next().get("memberName").toString();
		if (score >= upScore) {
			level = level + 1;
			NotificationDao notificationDao = new NotificationDao();
			notificationDao.generateMessageForMember("" + name + "好样的"
					+ ",您的等级提升了，现在会员等级是" + level + "。",id);
		}
		return level;
	}

	public Member queryDeletedMemberByName(String memberName) {
		DBObject query = mHelper.createQuery("memberName", memberName);
		query.put("status", ToolConstants.DELETE_INT);
		DBCursor cursor = collection.find(query);
		Member member = gson.fromJson(cursor.next().toString(), Member.class);
		return member;
	}

	// 以用户名作为查询条件修改密码，用户名唯一。前后密码一致时，修改失败
	public boolean updatePwd(String name, String freshPwd) {
		boolean flag = false;
		DBObject query = mHelper.createQuery("memberName", name);
		DBObject object = mHelper.findByCondition(collection, query);
		Member preMember = gson.fromJson(object.toString(), Member.class);
		String prePwd = preMember.getMemberPassword();
		if (freshPwd.equals(prePwd)) {
			String message = ToolConstants.PASSWORDCONSISTENCE_STRING;
			log.info(message);
		} else {
			BasicDBObject obj = new BasicDBObject();
			obj.append("$set", mHelper.createQuery("memberPassword", freshPwd));
			collection.update(query, object);
			flag = true;
		}
		return flag;
	}

	/**
	 * @author Kario 获取当前系统中有效会员总数
	 */
	public int getNumberOfMember() {
		return collection.find(
				mHelper.createQuery("status", ToolConstants.VALID_INT)).count();
	}

	/**
	 * @author Kario 获取每月会员增量，不管是否有效，只关注注册时间是否在该段时间内
	 * @param month  月份
	 * @param year     年份
	 * @return   int       该时间段的记录增量
	 */
	public int getNumberOfMemberByMonth(int year,int month) {
		DBObject query = mHelper.generateStartAndEndTime("date",year, month);
		query.put("status", ToolConstants.VALID_INT);
		return collection.find(query).count();
	}

	private DBCollection collection = mHelper.getConnection().getCollection(
			"member");
	private DBCollection collectionMerchant = mHelper.getConnection()
			.getCollection("merchant");
	private DBCollection collectionAdmin = mHelper.getConnection()
			.getCollection("administrator");
	private static Logger log = Logger.getLogger(MemberDao.class);

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}

	//***********************************************KG********************************************************

	//激活用户
	public void activeMember(String username) {
		DBObject object = new BasicDBObject();
		object.put("memberName", username);
		collection.update(object, new BasicDBObject().append("$set",
				new BasicDBObject().append("status", ToolConstants.VALID_INT)));
	}
	
	
//根据username查找数据库中的普通用户
	public Member findMerchantByName(String username){
		BasicDBObject query = new BasicDBObject();
		query.append("memberName", username);
		DBCursor cursor = collection.find(query);
		Member member=null;
		if (cursor.hasNext()){
			DBObject obj = cursor.next();
			member = gson.fromJson(obj.toString(),Member.class);
		}
		return member;
	}
	
	
	// 更改密码--------name字段唯一
	public boolean changePwd_AM(String name, String newPwd) {
		boolean flag = false;
		DBObject query = mHelper.createQuery("memberName", name);
		//query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			log.info("update");
			BasicDBObject obj = new BasicDBObject();
			obj.append("$set",mHelper.createQuery("memberPassword", newPwd));
			collection.update(query, obj);
			log.info("update success");
			flag = true;
		} else
			log.info(ToolConstants.DOCNOTFOUND_STRING);
		return flag;
	}
	
	public int save(Member member) {
		int str = 0;
		if (mailValid(member.getMemberMail())
				&& nameValid(member.getMemberName())) {
			member.setMemberScore(0);
			member.setMemberLevel(0);
			member.setDate(System.currentTimeMillis());
			//member.setStatus(ToolConstants.VALID_INT);
			member.setStatus(ToolConstants.NO_ACTIVE_INT);
			String randomCode = RandomStringUtils.randomAlphanumeric(30);
			member.setRandomCode(randomCode);
			/*member.setGoodComments(0);
			member.setBadComments(0);*/
			DBObject object = (DBObject) JSON.parse(gson.toJson(member));
			collection.insert(object);
			log.info("save member into Member successfully!");
			Administrator administrator = new Administrator();
			administrator.set_id();
			administrator.setAdminName(member.getMemberName());
			administrator.setAdminPassword(member.getMemberPassword());
			administrator.setAdminMail(member.getMemberMail());
			administrator.setRole(ToolConstants.Role_Member);
			administrator.setStatus(ToolConstants.NO_ACTIVE_INT);
			administrator.setRandomCode(randomCode);
			collectionAdmin.insert((DBObject) JSON.parse(gson
					.toJson(administrator)));
			log.info("save member into Administrator successfully!");
		} else if (!mailValid(member.getMemberMail())) {
			str = ToolConstants.ResultStatus_Fail_MailUsed;
		} else if (!nameValid(member.getMemberName())) {
			str = ToolConstants.ResultStatus_Fail_NameUsed;
		} else {
			str = ToolConstants.ResultStatus_Fail_MailAndNameUsed;
		}
		return str;
	}
	
	public int save(Member member,String active_url,SendMail sendMail,String randomCode) {
		int str = 0;
		boolean flag = sendMail.sendModelMail(member.getMemberMail(), "imagemark 邮件激活", "register.vm",active_url, member.getMemberName());
		if(flag==true){
			if (mailValid(member.getMemberMail())&& nameValid(member.getMemberName())) {
				member.setMemberScore(0);
				member.setMemberLevel(0);
				member.setDate(System.currentTimeMillis());
				//member.setStatus(ToolConstants.VALID_INT);
				member.setStatus(ToolConstants.NO_ACTIVE_INT);
				//String randomCode = RandomStringUtils.randomAlphanumeric(30);
				member.setRandomCode(randomCode);
				/*member.setGoodComments(0);
				member.setBadComments(0);*/
				DBObject object = (DBObject) JSON.parse(gson.toJson(member));
				collection.insert(object);
				log.info("save member into Member successfully!");
				Administrator administrator = new Administrator();
				administrator.set_id();
				administrator.setAdminName(member.getMemberName());
				administrator.setAdminPassword(member.getMemberPassword());
				administrator.setAdminMail(member.getMemberMail());
				administrator.setRole(ToolConstants.Role_Member);
				administrator.setStatus(ToolConstants.NO_ACTIVE_INT);
				administrator.setRandomCode(randomCode);
				collectionAdmin.insert((DBObject) JSON.parse(gson.toJson(administrator)));
				log.info("save member into Administrator successfully!");
			} else if (!mailValid(member.getMemberMail())) {
				str = ToolConstants.ResultStatus_Fail_MailUsed;
			} else if (!nameValid(member.getMemberName())) {
				str = ToolConstants.ResultStatus_Fail_NameUsed;
			} else {
				str = ToolConstants.ResultStatus_Fail_MailAndNameUsed;
			}
		}else{
			str = ToolConstants.ResultStatus_Fail_MailNOTEXIST;
		}	
		return str;
	}
	//***********************************************KG********************************************************
}
