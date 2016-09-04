/**
 * 
 */
package com.anyway.imagemark.utils;

/**
 * @author Kario
 *���峣��
 */
public class ToolConstants {
	public static final String  DATEFORMAT_STRING="yyyy-MM-dd";
	public static final int  NodeUPLIMIT=6;
	//result.statu的值
	public static final int ResultStatus_Success=0;
	public static final int ResultStatus_NoData=8;
	public static final int ResultStatus_Fail_Password=1;
	public static final int ResultStatus_Fail_NameNotExist=2;
	public static final int ResultStatus_Fail_DataIllegal=4;
	public static final int ResultStatus_Fail_MailUsed=3;
	public static final int ResultStatus_Fail_NameUsed=5;
	public static final int ResultStatus_Fail_MailAndNameUsed=6;
	public static final int ResultStatus_Fail_ExistedError=7;
	public static final int ResultStatus_Fail_NoAuthority=9;
	public static final int ResultStatus_Fail_ParameterError=10;
	public static final int ResultStatus_Fail_NoImageMark=11;
	public static final int  ResultStatus_Fail_InsertError=12;
	public static final int  ResultStatus_Fail_MailNOTEXIST=13;//注册的邮箱不存在
	public static final String SUCCES_STRING="Success";
	public static final String SUCCES_WITHOUT_IMAGE_STRING="添加无图片标记失败";
	public static final String DOCNOTFOUND_STRING="内容为空";
	public static final String ResultDesc_NoData="内容为空";

	public static final String MAILUSED_STRING="邮箱已使用";
	public static final String MAILNOTEXIST_STRING="邮箱不存在";
	public static final String NAMEUSED_STRING="用户名已使用";
	public static final String PASSWORDFAILED_STRING="账户名和密码不匹配";
	public static final String PASSWORDCONSISTENCE_STRING="密码不一致";
	public static final String REGISTERNOTDONE_STRING="您输入的账户名不存在";
	public static final String  ResultDesc_Fail_ExistedError="该内容已经存在";
	public static final String ResultDesc_Fail_DataIllegal="数据不合法";
	public static final String ResultDesc_Fail_NoAuthority="没有权限进行此操作";
	public static final String ResultDesc_Fail_ParameterError="请求参数错误！";
	public static final String ResultDesc_Fail_InsertError="插入数据失败";

	//componentType
	public static final int   COMPONENT_DEFAULT=0;
	public static final int   COMPONENT_COAT=1;
	public static final int   COMPONENT_PANTS=2;
	public static final int   COMPONENT_SHOE=3;
	public static final int   COMPONENT_HAT=4;
	public static final int   COMPONENT_OTHER=5;

	//login status---use for login(merchant and member)
	public static final int   LOGIN_SUCCESS=0;
	public static final int   LOGIN_INCORRECT=1;
	public static final int   LOGIN_NOTEXIST=2;
	//sort oder
	public static final int   PRICELOWTOHIGH_STRING=1;
	public static final int   PRICEHIGHTOLOW_STRING=2;
	public static final int   COMMENTLOWTOHIGH_STRING=3;
	public static final int   COMMENTHIGHTOLOW_STRING=4;
	public static final int   TRUSTLOWTOHIGH_STRING=5;
	public static final int   TRUSTHIGHTOLOW_STRING=6;

	//manage sortType
	public static final int   DEFAULT=0;
	public static final int   TIMEASC=1;
	public static final int   TIMEDES=2;
	public static final int   NUMASC=3;
	public static final int   NUMDES=4;

	//the status of the document
	public static final int VERIFY_INT=1;
	public static final int VALID_INT=2;
	public static final int PASS_INT=3;
	public static final int PASSFAIL_INT=4;
	public static final int MOREDETAIL_INT=5;
	public static final int DELETE_INT=6;
	public static final int INVALID_INT=7; 
	
	//********************KG************************
	public static final int NO_ACTIVE_INT=8;
	//********************KG************************
	
	

	//the number to show 
	public static final int SHOWNUM_INT=3;
	public static final int TABLENUM_INT=3;
	public static final int ForeGround_Default_PageSize=3;
	public static final int BackGround_Default_PageSize=20;

	//第一页
	public static final int PageNumber_first=1;

	//one day----use for validate
	public static final long ONEDAY_LONG=1*24*3600*1000;

	//通知对象
	public static final String NOTIFIYMEMBER_STRING="member";
	public static final String NOTIFIYMERCHANT_STRING="merchant";
	public static final String NOTIFIYALL_STRING="all";

	//通知类型整数值
	public static final int   Notification_Type_Notice=1;
	public static final int   Notification_Type_Announcement=2;
	public static final int   Notification_Type_SystemInfo=3;

	//角色权限值
	public static final int   Role_Admin_All=10;
	public static final int   Role_Admin_Verifier=11;
	public static final int   Role_Merchant=2;
	public static final int   Role_Member=3;
	//商品类型
	public static final String[] COMPONENTTYPE_STRINGS=new String[]{"商品类型","服饰鞋帽","手机数码","电脑办公","家用电器","食品饮料","个护化妆","家居家装","其他类型"};
	
	//审核员审核超时时间（分钟）
	public static final int Verify_Merchant_TimeOut=2;
} 
