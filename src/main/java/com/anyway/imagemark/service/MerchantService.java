package com.anyway.imagemark.service;

import java.util.List;

import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.domain.Node;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.mail.SendMail;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.mongodb.DBObject;

public interface MerchantService {

	int login(String merchantName, String merchantPassword, int type,
			String ipInfo);

	MerchantInfo queryMerchantInfoByMerchantName(String merchantName);
	DBObject getMerchantInfo(String idString);

	//Merchant queryMerchantByNameOrMail(String merchantNameOrMail);

	Merchant queryNotVerifyMerchantByName(String merchantName);

	
	//******************************KG*****************************
	int addMerchant(Merchant merchant);
	int addMerchant(Merchant merchant,String active_url,SendMail sendmail,String randomCode);
	//******************************KG*****************************
	int addNode(Node node);
    PageContent<DBObject> queryNode(String url,int status);
    PageContent<DBObject>  queryMarkByNode(String node,int sortType,int currentPage,int status,int pageSize);
	void updateMerchant(DBObject query, DBObject update);

	PageContent<Notification> queryNotificationsByType(String merchantName,int type, int sortType,
			int pageNumber, int pageSize);

	Notification queryNotificationById(String _id);

	int saveMarkInfo(MarkInfo markInfo);
	void deleteMarkInfo(DBObject condition);
	void updateMarkInfo(MarkInfo markInfo);
	MarkInfo queryMarkInfoById(String mark_id);

	PageContent<DBObject> queryByMechantIdAndUrl(DBObject query,
			DBObject sortType, int pageNumber, int pageSize);

	String getIdByMerchantName(String merchantName);

	List<Integer> queryMarkedByMerchantNameAndOther(String merchantName,
			List<String> images);

	PageContent<MemberComment> getMarkCommentsByMarkId(String mark_id,
			int filter, int sortType, int pageNumber, int pageSize);

	PageContent<MerchantMark> getStatisticalMarksByMerchantName(
			String merchantName, int sortType, int pageNumber, int pageSize);

	PageContent<MerchantMark> getStatisticalTopicByMerchantName(
			String merchantName, int sortType, int pageNumber, int pageSize);
	List<List<DBObject>> getAllNode(String[] urlList);
	PageContent<MerchantMark> getMarkInfoByMerchant(String idString,int page,int pagesize);

	
	//*************************************KG********************************************
	
	public boolean changepasswd(String name,String newPwd);
	public Merchant findMerchantByName(String username);
	public void activeMerchant(String username) ;
	public Merchant queryMerchantByNameOrMail(String merchantNameOrMail,int status);
	//*************************************KG********************************************

}
