package com.anyway.imagemark.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.NewBSONDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.daoimpl.ClickInfoDao;
import com.anyway.imagemark.daoimpl.CommentDao;
import com.anyway.imagemark.daoimpl.ComplainDao;
import com.anyway.imagemark.daoimpl.LogInfoDao;
import com.anyway.imagemark.daoimpl.MarkInfoDao;
import com.anyway.imagemark.daoimpl.MemberDao;
import com.anyway.imagemark.daoimpl.NotificationDao;
import com.anyway.imagemark.domain.ClickInfo;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.Complain;
import com.anyway.imagemark.domain.LogInfo;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.mail.SendMail;
import com.anyway.imagemark.manage.MarkManage;
import com.anyway.imagemark.manage.MemberManage;
import com.anyway.imagemark.service.MemberService;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberFoot;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	@Qualifier("memberDao")
	private BasicDao basicDao;
	@Autowired
	private MemberManage memberManage;
	private MemberDao memberDao=new MemberDao();
	private MarkInfoDao markInfoDao=new MarkInfoDao();
	private ClickInfoDao clickInfoDao=new ClickInfoDao();
	private CommentDao commentDao=new CommentDao();
	private LogInfoDao logInfoDao=new LogInfoDao();
	private NotificationDao notificationDao=new NotificationDao();
	private MarkManage markManage=new MarkManage();
	private static Gson gson = new Gson();
	public int addMember(Member member) {
		// TODO Auto-generated method stub
		return memberDao.save(member);
	}


	/*public void deleteMember(Member member) {
		// TODO Auto-generated method stub

	}*/

	public void updateMember(DBObject condition,Member member) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		memberDao.update(condition, (DBObject)JSON.parse(gson.toJson(member)));
		
	}

	public int saveComment(Comment comment) {
		// TODO Auto-generated method stub
		return commentDao.save(comment);
	}
	public int saveComplain(Complain complain){
		ComplainDao dao=new ComplainDao();
		return dao.save(complain);
	}

	public void deleteComment(DBObject condition) {
		// TODO Auto-generated method stub
		commentDao.deleteByCondition(condition);
	}

	public void updateComment(DBObject condition,Comment comment) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		commentDao.update(condition, (DBObject)JSON.parse(gson.toJson(comment)));
	}

	public void saveClickInfo(ClickInfo clickInfo) {
		// TODO Auto-generated method stub
		clickInfoDao.save(clickInfo);
	}

	public PageContent<DBObject> queryMarkInfoByUrl(DBObject query, DBObject sortType, int pageNumber,
			 int pageSize) {
		// TODO Auto-generated method stub
		return markInfoDao.search(query, sortType, pageNumber, pageSize);
	}
    public PageContent< DBObject> SearchMarkByUrlAndCommented(String memberName,DBObject query, DBObject sortType, int pageNumber,
			 int pageSize){
    	return markInfoDao.SearchMarkByUrlAndCommented(memberName, query, sortType, pageNumber, pageSize);
    }
	public int login(String merchantName, String merchantPassword,
			int type, String ipInfo) {
		// TODO Auto-generated method stub
		return memberDao.login(merchantName, merchantPassword, type, ipInfo);
	}

	public List<Integer> queryMarked(List<String> images) {
		// TODO Auto-generated method stub
		return markInfoDao.searchMarked(images);
	}

	public String getIdByMemberName(String memberName) {
		// TODO Auto-generated method stub
		return memberDao.get_id(memberName);
	}

	public String getMarkIdByUrlAndComponentLinkAddress(String url,
			String componentLinkAddress) {
		// TODO Auto-generated method stub
		return markInfoDao.get_id(url, componentLinkAddress);
	}

	public PageContent<MemberFoot> getMemberStatisticalClicks(String memberName,int
			sortField,int sortType, int pageNumber,int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMemberStatisticalClicks(memberName,sortField, sortType, pageNumber,pageSize);
	}

	public PageContent<MemberComment> getMemberStatisticalComments(String memberName,
			int filter, int sortType, int pageNumber,int pageSize) {
		// TODO Auto-generated method stub
		return memberManage.getMemberStatisticalComments(memberName, filter, sortType, pageNumber,pageSize);
	}

	public Member queryMemberByNameOrMail(String memberNameOrMail) {
		// TODO Auto-generated method stub
		return memberDao.getMemberByNameOrMail(memberNameOrMail);
	}

	public MemberInfo queryMemberInfoByMemberName(String memberName) {
		// TODO Auto-generated method stub
		return memberManage.getInfo(memberName);
	}

	public MarkInfo queryMarkInfoByMarkId(String markId) {
		// TODO Auto-generated method stub
		return markInfoDao.queryMarkInfoByMarkId(markId);
	}

	public PageContent<Notification> queryNotificationsByType(String memberName,int type,int sortType,
			int pageNumber,int pageSize) {
		// TODO Auto-generated method stub
		DBObject sortOrder=new BasicDBObject();
		DBObject query=new BasicDBObject();
		BasicDBList values = new BasicDBList();  
		String mem_id=memberDao.get_id(memberName);
		values.add("all");  
		values.add("member");  
		query.put("notifier", new BasicDBObject("$in", values));
		//query.put("noticeID", mem_id);
		System.out.println(query.toString());
		//condition.put("notifier", "all");
		if (type != 0) {
			query.put("type", type);
		}
		if(sortType==1){
			sortOrder.put("sendTime", 1);
		}else{
			sortOrder.put("sendTime", -1);
		}
		List<Notification> notificationList=new ArrayList<Notification>();
		PageContent<DBObject> list=notificationDao.search(query,sortOrder,pageNumber,pageSize);
		PageContent<Notification> pageContent = new PageContent<Notification>();
		if(list!=null){
		for (int i = 0; i < list.getList().size(); i++) {
			notificationList.add(gson.fromJson(list.getList().get(i).toString(), Notification.class));
		}
		pageContent.setCurrentRecords(notificationList.size());
		pageContent.setList(notificationList);
		pageContent.setPageNumber(pageNumber);
		pageContent.setPageSize(pageSize);
		pageContent.setTotalPages(list.getTotalPages());
		pageContent.setTotal(list.getTotal());
		}
		return pageContent;
	}

	public Notification queryNotificationById(String _id) {
		// TODO Auto-generated method stub
		DBObject dbObject= notificationDao.search("_id", _id);
		return gson.fromJson(dbObject.toString(), Notification.class);
	}
    public static void main(String[] args){
    	MemberServiceImpl impl=new MemberServiceImpl();
    	PageContent<Notification> pageContent=impl.queryNotificationsByType("member1", 3, 1, 1, 20);
    	List<Notification> list=pageContent.getList();
    	for(int i=0;i<list.size();i++){
    		System.out.println(JSON.parse(gson.toJson(list.get(i))));
    	}
    }
    public PageContent<DBObject> getRecommend(){
    	return markInfoDao.getRecommend();
    }
    public  PageContent<DBObject> getUserPrased(String userName, int page,
			int pageSize,int flag){
    	return markManage.getUserPrased(userName, page, pageSize,flag);
    }
    
    
    
	 
	 //********************************************KG***************************************************
	 public Member findMemberByName(String username){
		 return memberDao.findMerchantByName(username);
	 }

		public boolean changepasswd(String username,String newPwd){
			return memberDao.changePwd_AM(username, newPwd);
		}
		//激活用户
		public void activeMember(String username) {
			memberDao.activeMember(username);
		}
		
		//添加用户
		public int addMember(Member member,String active_url,SendMail sendMail,String randomCode) {
			return memberDao.save(member,active_url,sendMail,randomCode);
		}
	//********************************************KG***************************************************
    
    
    
}
