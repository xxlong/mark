package com.anyway.imagemark.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class VerifyUtil {
	//待审核商家队列，商家注册成功后，立即进入此队列
	public static ConcurrentLinkedDeque<String> verifiedMerchantNameList=
			new ConcurrentLinkedDeque<String>();
	//审核中的商家，由上述队列出队而来，目前一次出队一个商家
	//MerchantManage.getMerchantByNotVerify 进行出队并进入map操作;AdministratorController.VerifyMerchant 在map中查找自己分得的任务，
	// 存在则进行审核通过，并移除该项
	public static ConcurrentHashMap<String, Date> polledMerchantNameMap=
			new ConcurrentHashMap<String,Date>();
	public static ConcurrentLinkedDeque<String> verifiedMarkInfoIdList=
			new ConcurrentLinkedDeque<String>();

	public static void revertExpiredMerchantName(){
		for(Map.Entry<String, Date> entry:polledMerchantNameMap.entrySet()){
			Date accessTime=entry.getValue();
			Date currentTime=new Date();
			long diff=TimeUnit.MILLISECONDS.toMinutes(currentTime.getTime()-accessTime.getTime());
			System.out.println("VerifyUtil(26)"+diff);
			if(diff>ToolConstants.Verify_Merchant_TimeOut){
				polledMerchantNameMap.remove(String.valueOf(entry.getKey()));
				verifiedMerchantNameList.add(entry.getKey());
			}
		}
	}
}
