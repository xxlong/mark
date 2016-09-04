import java.util.List;

import com.anyway.imagemark.daoimpl.MerchantDao;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.manage.CommentManage;
import com.anyway.imagemark.manage.MarkManage;
import com.anyway.imagemark.manage.MemberManage;
import com.anyway.imagemark.manage.MerchantManage;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class test {
public static void main(String[] args){
	MarkManage markManage=new MarkManage();
	CommentManage commentManage=new CommentManage();
	MemberManage memberManage=new MemberManage();
	MerchantManage merchantManage=new MerchantManage();
	PageContent<MerchantMark> pageContent=markManage.getStatisticalMarksByMerchant("5462d23e45ce085e8ff21b77", 1, 3);
	//PageContent<MemberComment> pageContent=commentManage.deleted(null, 1, 1, 20);
	//PageContent<MerchantInfo> pageContent=merchantManage.getMerchantsBehavioursByMarkAggregate("2014/06/11 00:00:00", "2014/07/24 00:00:00","count", 1, 1, 20);
	//PageContent<MerchantInfo> pageContent=merchantManage.getMerchantsBehavioursByRegisterSpan("2014/06/11 00:00:00", "2014/07/19 00:00:00", 1, 1, 20);
	//PageContent<MerchantInfo> pageContent=merchantManage.getMerchantsBehavioursByLoginSpan("2014/06/11 00:00:00", "2014/07/24 00:00:00", 1, 1, 20);
	//PageContent<Member> pageContent=memberManage.getMemberByNewAdd("2014/06/11 00:00:00", "2014/07/24 00:00:00",2, 1, 1, 20);
	//PageContent<MemberInfo> pageContent=memberManage.getMembersBehavioursByRegisterSpan("2014/06/11 00:00:00", "2014/07/24 00:00:00", 1, 1, 20);
	////PageContent<MemberInfo> pageContent=memberManage.getMembersBehavioursByClickSpan("2014/06/11 00:00:00", "2014/07/24 00:00:00", 1, 1, 20);
	//PageContent<MemberInfo> pageContent=memberManage.getMembersBehavioursByLoginSpan("2014/06/11 00:00:00", "2014/07/24 00:00:00", 1, 1, 20);
	//PageContent<MemberInfo> pageContent=memberManage.getMembersBehavioursByCommentSpan("2014/06/11 00:00:00", "2014/07/24 00:00:00", 1, 1, 20);
	////PageContent<MerchantMark> pageContent=markManage.getMarkByMerchant("2014/06/11 00:00:00", "2014/07/19 00:00:00", 1, 1, 10);
	//PageContent<MerchantMark> pageContent=markManage.getStatisticalMarksByFilter("2014/06/11 00:00:00", "2014/07/22 00:00:00", "commentNum", 1, 1, 1, 10);
	//PageContent<MerchantMark> pageContent=markManage.getStatisticalMarksByMerchantName("taobao", 1, 1, 20);
	//PageContent<MerchantMark> pageContent=markManage.getStatisticalMarksByNewAdd("2014/06/11 00:00:00", "2014/07/22 00:00:00", 1, 1, 20);
	//PageContent<MerchantMark> pageContent=markManage.getStatisticalMarksByComponentType("2014/06/11 00:00:00", "2014/07/22 00:00:00", 1,1, 1, 20);
	//PageContent<Merchant> pageContent=merchantManage.getMerchantByTrust(1, 1, 1, 10);
	//PageContent<MemberComment> pageContent=memberManage.getMemberStatisticalComments("baidu", 1, 1, 1);
	//PageContent<MemberComment> pageContent=commentManage.getComments("2014/06/11 00:00:00", "2014/07/22 00:00:00", 1, 1, 10);
	//PageContent<MerchantMark> pageContent=markManage.getStatisticalMarksByMerchantName("2014/06/11 00:00:00", "2014/07/17 00:00:00", 1,1,1,10);
	/*List<MemberComment> pageContent=commentManage.getComment("53cc816de4b0deb86d823e63");
	for(int i=0;i<pageContent.size();i++){
		System.out.println(pageContent.get(i).toString());
	}*/
	//PageContent<MemberComment> pageContent=memberManage.getMemberStatisticalComments("sina", 1, 1, 1);
	if(pageContent!=null){
	for(int i=0;i<pageContent.getList().size();i++){
		System.out.println(pageContent.getList().get(i).toString());
	}}
	else {
		System.out.println("the result is null");
	}
}
}
