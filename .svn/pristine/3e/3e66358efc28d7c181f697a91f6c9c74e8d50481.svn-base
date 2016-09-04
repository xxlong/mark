/**
 * 
 */
package com.anyway.imagemark.daoimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Administrator 新添加生命周期字段，拉取标记时应判断是否过期
 */
@Repository("markInfoDao")
public class MarkInfoDao implements BasicDao<MarkInfo> {
	public int save(MarkInfo markInfo) {
		int status = 0;
		logger.info(markInfo.getMer_id());
		if (isValid(markInfo.getUrl(), markInfo.getComponentLinkAddress())) {
			String url = markInfo.getUrl();
			String shortUrl;
			if (url.contains("?")) {
				shortUrl = url.substring(0, url.indexOf("?"));
			} else {
				shortUrl = url;
			}
			MerchantDao merchantDao = new MerchantDao();
			NodeDao nodeDao = new NodeDao();
			String node_id = markInfo.getNode_id();
			DBObject queryNode = new BasicDBObject();
			queryNode.put("nodeId", node_id);
			markInfo.set_id();
			markInfo.setUrl(shortUrl);
			// markInfo.setStatus(ToolConstants.VALID_INT);
			markInfo.setStatus(ToolConstants.VERIFY_INT);
			markInfo.setMarkDate(System.currentTimeMillis());
			markInfo.setValidDate(merchantDao.generateMarkValid(markInfo
					.getMer_id()));
			markInfo.setList(null);
			DBObject object = (DBObject) JSON.parse(gson.toJson(markInfo));
			collection.insert(object);
			// 设置相关节点的checkMark状态
			nodeDao.update(queryNode, new BasicDBObject("$set",
					new BasicDBObject("checkMark", true)));
			status = ToolConstants.ResultStatus_Success;
			logger.info("The markinfo inserted");
		} else {
			status = ToolConstants.ResultStatus_Fail_ExistedError;
			logger.info("The markinfo has existed");
		}
		return status;
	}

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
		return mHelper.findByCondition(collection, query);
	}

	public DBObject searchNotVerify(DBObject query) {
		query.put("status", ToolConstants.VERIFY_INT);
		return mHelper.findByCondition(collection, query);
	}

	public List<DBObject> search(int num) {
		// TODO Auto-generated method stub
		return mHelper.listFindDefault(collection, num);
	}

	public PageContent<DBObject> search(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		// query.put("status", ToolConstants.VALID_INT);
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

	// 删除标记，改变商家标记数、信用值等
	public void deleteByCondition(DBObject condition) {
		// TODO Auto-generated method stub
		logger.info("will delete Markinfo");
		if (collection.find(condition).hasNext()) {
			logger.info("Query has relative markinfo");
			MarkInfo markInfo = gson.fromJson(search(condition).toString(),
					MarkInfo.class);
			NotificationDao notificationDao = new NotificationDao();
			MerchantDao merchantDao = new MerchantDao();
			NodeDao nodeDao = new NodeDao();
			String mer_id = markInfo.getMer_id();
			String node_id = markInfo.getNode_id();
			update(condition, new BasicDBObject().append(
					"$set",
					new BasicDBObject().append("status",
							ToolConstants.DELETE_INT).append("deleteDate",
							System.currentTimeMillis())));
			// 改变node的状态
			DBObject nodeQuery = new BasicDBObject();
			nodeQuery.put("node_id", node_id);
			nodeQuery.put("status", ToolConstants.VALID_INT);
			if (collection.find(nodeQuery).hasNext()) {
				logger.info("node still has component");
			} else {
				nodeQuery.removeField("status");
				nodeQuery.put("status", ToolConstants.VERIFY_INT);
				DBObject query = new BasicDBObject();
				query.put("nodeId", node_id);
				if (collection.find(nodeQuery).hasNext()) {
					nodeDao.update(query, new BasicDBObject("$set",
							new BasicDBObject("status",
									ToolConstants.VERIFY_INT)));
				} else {
					nodeDao.update(query, new BasicDBObject("$set",
							new BasicDBObject("status",
									ToolConstants.DELETE_INT)));
				}

			}
			// 改变商家记录
			DBObject query = new BasicDBObject();
			query.put("_id", mer_id);
			query.put("status", ToolConstants.VALID_INT);
			merchantDao.update(query,
					new BasicDBObject("$inc", new BasicDBObject(
							"merchantTrust", -markInfo.getComponentTrust())));
			// 通知商家
			notificationDao.generateMessageForMerchant(
					"很遗憾，由于您的标记，不符合我们的要求，已被删除", mer_id);
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
			logger.info("query is not null");
		} else {
			query = new BasicDBObject();
			query.put("status", ToolConstants.DELETE_INT);
			logger.info("query is null");
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

	// 恢复标记，关联信息也应该恢复
	public void Restore(DBObject query) {
		// TODO Auto-generated method stub
		NotificationDao notificationDao = new NotificationDao();
		DateFormat format = new DateFormat();
		if (collection.find(query).hasNext()) {
			collection.update(query, new BasicDBObject().append("$set",
					new BasicDBObject().append("status",
							ToolConstants.VALID_INT)), false, true);
			String mer_id = search(query).get("mer_id").toString();
			notificationDao.generateMessageForMerchant(
					"恭喜您，您添加的商品标记已于"
							+ format.generateTime("yyyy/MM/dd HH:mm:ss")
							+ "被恢复了,谢谢支持！", mer_id);
		}
	}

	public void closeClient() {
		// TODO Auto-generated method stub
		mHelper.getConnection().getClient().close();
	}

	// 检查标记是否有效
	public boolean isValid(String url, String component) {
		boolean flag = false;
		DBObject query = mHelper.createQuery("Url", url,
				"componentLinkAddress", component);
		// query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (!cursor.hasNext())
			flag = true;
		return flag;
	}

	// 获取标记_id
	public String get_id(DBObject query) {
		query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext())
			return cursor.next().get("_id").toString();
		else
			return ToolConstants.DOCNOTFOUND_STRING;
	}

	public String get_id(String url, String link) {
		DBObject query = mHelper.createQuery("Url", url,
				"componentLinkAddress", link);
		query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext())
			return cursor.next().get("_id").toString();
		else
			return ToolConstants.DOCNOTFOUND_STRING;
	}

	// 审核商家标记---审核商家标记的同时，改变位置信息，当位置下的商品信息审核通过时，位置设置为有效；反之亦然
	public void setMarkStatus(DBObject query, int status) {
		logger.info("execute MarkInfoDao--setMarkStatus");
		BasicDBObject update = new BasicDBObject();
		NotificationDao notificationDao = new NotificationDao();
		NodeDao nodeDao = new NodeDao();
		DateFormat format = new DateFormat();
		query.put("status", ToolConstants.VERIFY_INT);
		update.append("$set", new BasicDBObject("status", status));
		DBCursor cursor = collection.find(query);
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			String node_id = object.get("node_id").toString();
			DBObject condition = new BasicDBObject();
			condition.put("nodeId", node_id);
			condition.put("status", ToolConstants.VERIFY_INT);
			String mer_id = search(query).get("mer_id").toString();
			if (status == ToolConstants.VALID_INT) {
				collection.update(query, update);
				notificationDao.generateMessageForMerchant("恭喜您，您添加的商品标记已于"
						+ format.generateTime("yyyy/MM/dd HH:mm:ss")
						+ "被审核通过了,谢谢支持！", mer_id);
				// 商品状态有效，设置相应的位置状态
				if (nodeDao.search(condition) != null) {
					nodeDao.update(condition,
							new BasicDBObject("$set", new BasicDBObject(
									"status", ToolConstants.VALID_INT)));
					logger.info("位置：" + node_id + " 被设置为有效");
				}
				// 商品状态改变后，查看该位置下是否还有未审核的商品;有则将位置信息 置为true
				/*
				 * if(collection.find(condition).hasNext()){
				 * logger.info("verify still");
				 * nodeDao.update(mHelper.createQuery("nodeId", node_id), new
				 * BasicDBObject("$set",new BasicDBObject("checkMark",true)));
				 * }else{ logger.info("verify  not still");
				 * nodeDao.update(mHelper.createQuery("nodeId", node_id), new
				 * BasicDBObject("$set",new BasicDBObject("checkMark",false)));
				 * }
				 */
			} else if (status == ToolConstants.DELETE_INT) {
				deleteByCondition(query);
				notificationDao.generateMessageForMerchant("很遗憾，您添加的商品标记已于"
						+ format.generateTime("yyyy/MM/dd HH:mm:ss")
						+ "未被审核通过了,谢谢支持！", mer_id);
				if (nodeDao.search(condition) != null) {
					nodeDao.update(condition, new BasicDBObject("$set",
							new BasicDBObject("status",
									ToolConstants.DELETE_INT)));
				}
			}
		}
	}

	public List<Integer> searchMarked(List<String> urlList) {
		List<Integer> myList = new ArrayList<Integer>();
		// 查询标记信息--------查询到的图片被标记可能数量>1，只取图片地址即可
		logger.info(urlList.size());
		for (int i = 0; i < urlList.size(); i++) {
			DBObject query = new BasicDBObject("Url", urlList.get(i));
			// query.put("status", new
			// BasicDBObject("$ne",ToolConstants.DELETE_INT));
			query.put("status", ToolConstants.VALID_INT);
			DBCursor cursor = collection.find(query);
			if (cursor.hasNext()) {
				logger.info("execute MarkInfoDao--searchMarked(urlList) Marked Url:"
						+ urlList.get(i));
				myList.add(1);
			} else {
				myList.add(0);
			}
		}
		logger.info("被标记的图片数：" + myList.size());
		return myList;
	}

	// 返回网址下的被标记的图片地址,返回为标记过的图片地址
	public List<Integer> searchMarked(String _id, List<String> urlList) {
		List<Integer> myList = new ArrayList<Integer>();
		DBObject queryMain = new BasicDBObject();
		// 查询标记信息--------查询到的图片被标记可能数量>1，只取图片地址即可
		for (int i = 0; i < urlList.size(); i++) {
			queryMain = mHelper.createQuery("Url", urlList.get(i));
			queryMain.put("status", ToolConstants.VALID_INT);
			DBCursor cursor = collection.find(queryMain);
			if (cursor.hasNext()) {
				DBObject query = new BasicDBObject();
				query.put("mer_id", _id);
				query.put("Url", urlList.get(i));
				// query.put("status", new
				// BasicDBObject("$ne",ToolConstants.DELETE_INT));
				query.put("status", ToolConstants.VALID_INT);
				DBCursor cursor1 = collection.find(query);
				if (cursor1.hasNext()) {
					logger.info("execute MarkInfoDao--searchMarked(mer_id,urlList) Marked By Myself Url:"
							+ urlList.get(i));
					myList.add(1);
				} else {
					myList.add(2);
				}
			} else {
				myList.add(0);
			}
		}
		logger.info("被标记的图片数：" + myList.size());
		return myList;
	}

	// 修改标记信息-----商家修改在某一图片的某一件商品，只能修改自己标记的商品---标记信息一天之内可以修改
	public String update(String ulrString, String linkString, MarkInfo newMark) {
		BasicDBObject queryObject = new BasicDBObject();
		String string = "";
		queryObject
				.append("Url", ulrString)
				.append("componentLinkAddress", linkString)
				.append("status",
						new BasicDBObject("$ne", ToolConstants.DELETE_INT));
		DBObject dbObject = (DBObject) JSON.parse(gson.toJson(newMark));
		DBObject object = search(queryObject);
		if (object != null) {
			MarkInfo markInfo = gson
					.fromJson(object.toString(), MarkInfo.class);
			long markDate = markInfo.getMarkDate();
			long validTime = markInfo.getValidDate();
			// 标记信息中的有效时间（x天）
			if (new Date().getTime() - markDate < validTime) {
				collection.update(queryObject, dbObject);
				logger.info("update success");
				string = ToolConstants.SUCCES_STRING;
			} else {
				string = "商品标记已超过一天，不能修改";
				logger.info(string);
			}

		} else {
			string = ToolConstants.DOCNOTFOUND_STRING;
		}
		return string;
	}

	// 对标记的商品信息排序。以图片的url为单位,对链接的商品排序---排列方式有？价格..商品链接条数..商品信用..商家信
	// 记录每条链接的被点击数量--点击记录保存后，改变----url为图片地址，link为商品链接
	public void addDirectCount(String mark_id) {
		BasicDBObject query = new BasicDBObject();
		query.append("_id", mark_id);
		query.append("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			MarkInfo markInfo = gson.fromJson(cursor.next().toString(),
					MarkInfo.class);
			int count = markInfo.getDirectCount() + 1;
			collection.update(
					query,
					new BasicDBObject().append("$set",
							new BasicDBObject().append("directCount", count)));
		} else
			logger.info(ToolConstants.DOCNOTFOUND_STRING);

	}

	// 修改标价信息中的标记被评价的次数--评论完成后修改-----url为图片地址，link为商品链接
	/*
	 * public void addCommentNum(String mark_id, int comment) { BasicDBObject
	 * query = new BasicDBObject(); query.append("_id", mark_id);
	 * query.append("status", ToolConstants.VALID_INT); DBCursor cursor =
	 * collection.find(query); if (cursor.hasNext()) { if (comment > 3) {
	 * logger.info("the good comment"); collection.update(query, new
	 * BasicDBObject().append("$inc", new
	 * BasicDBObject().append("goodCommentNum", 1))); } else if (comment < 3) {
	 * logger.info("the bad comment"); collection .update(query, new
	 * BasicDBObject().append("$inc", new
	 * BasicDBObject().append("badCommentNum", 1))); } else {
	 * logger.info("The comment is middle!"); } collection.update( query, new
	 * BasicDBObject().append("$inc", new BasicDBObject().append("commentNum",
	 * 1))); } else logger.info(ToolConstants.DOCNOTFOUND_STRING);
	 * 
	 * }
	 */

	// 计算平均
	/*
	 * public void addComponentTrust(String mark_id, int comment) {
	 * BasicDBObject query = new BasicDBObject(); query.append("_id", mark_id);
	 * query.append("status", ToolConstants.VALID_INT); DBCursor cursor =
	 * collection.find(query); if (cursor.hasNext()) { MarkInfo markInfo =
	 * gson.fromJson(cursor.next().toString(), MarkInfo.class); // int trust =
	 * markInfo.getComponentTrust() + comment; int commemtedNum =
	 * markInfo.getCommentNum(); float averageScore =
	 * markInfo.getComponentTrust(); float totalNum = averageScore *
	 * commemtedNum + (float) comment; float average = totalNum / (commemtedNum
	 * + 1); collection.update(query, new BasicDBObject().append("$set", new
	 * BasicDBObject().append("componentTrust", average))); String mer_id =
	 * markInfo.getMer_id(); MerchantDao merchantDao = new MerchantDao(); if
	 * (comment < 3) { merchantDao .update(new BasicDBObject("_id", mer_id), new
	 * BasicDBObject().append("$inc", new BasicDBObject().append(
	 * "merchantTrust", -1))); collection.update(query, new
	 * BasicDBObject().append("$inc", new
	 * BasicDBObject().append("merchantTrust", -1))); } else if (comment > 3) {
	 * merchantDao .update(new BasicDBObject("_id", mer_id), new
	 * BasicDBObject().append("$inc", new BasicDBObject().append(
	 * "merchantTrust", 1))); collection .update(query, new
	 * BasicDBObject().append("$inc", new
	 * BasicDBObject().append("merchantTrust", 1))); } else {
	 * logger.info("the comment is 3;do noting"); }
	 * 
	 * }
	 * 
	 * }
	 */

	public void addTextCount(String mark_id) {
		BasicDBObject query = new BasicDBObject();
		query.append("_id", mark_id);
		query.append("status", ToolConstants.VALID_INT);
		collection.update(
				query,
				new BasicDBObject().append("$inc",
						new BasicDBObject().append("textCount", 1)));
	}

	// 增加举报次数，删除标记，当举报次数达到阀值时，删除商家
	public void addInformTimes(String mark_id) {
		DBObject query = new BasicDBObject();
		query.put("_id", mark_id);
		query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			logger.info("增加标记举报次数");
			collection.update(query, new BasicDBObject("$inc",
					new BasicDBObject("informTimes", 1)));
			String mer_id = cursor.next().get("mer_id").toString();
			deleteByCondition(query);
			logger.info("删除标记");
			int total = getComplainTimes(mer_id);
			if (total >= 5) {
				MerchantDao merchantDao = new MerchantDao();
				merchantDao.deleteByCondition(mHelper
						.createQuery("_id", mer_id));
			}

		}
	}

	// 统计商家的信息
	public int getCommentTimes(String mer_id) {
		int count = 0;
		DBObject query = mHelper.createQuery("mer_id", mer_id);
		query.put("status", new BasicDBObject("$ne", ToolConstants.DELETE_INT));
		DBCursor cursor = collection.find(query);
		while (cursor.hasNext()) {
			MarkInfo markInfo = gson.fromJson(cursor.next().toString(),
					MarkInfo.class);
			count += markInfo.getTextCount();
		}
		return count;
	}

	public MarkInfo queryMarkInfoByMarkId(String markId) {
		DBObject query = mHelper.createQuery("_id", markId);
		// query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			MarkInfo markInfo = gson.fromJson(cursor.next().toString(),
					MarkInfo.class);
			return markInfo;
		} else
			return null;
	}

	public MarkInfo queryDeletedMarkInfoByMarkId(String markId) {
		DBObject query = mHelper.createQuery("_id", markId);
		query.put("status", ToolConstants.DELETE_INT);
		DBCursor cursor = collection.find(query);
		MarkInfo markInfo = gson.fromJson(cursor.next().toString(),
				MarkInfo.class);
		return markInfo;
	}

	public int getClickTimes(String mer_id) {
		int count = 0;
		DBCursor cursor = collection
				.find(mHelper.createQuery("mer_id", mer_id));
		while (cursor.hasNext()) {
			MarkInfo markInfo = gson.fromJson(cursor.next().toString(),
					MarkInfo.class);
			count += markInfo.getDirectCount();
		}
		return count;
	}

	public float getTotalTrust(String mer_id) {
		float count = 0;
		DBObject query = mHelper.createQuery("mer_id", mer_id);
		query.put("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		while (cursor.hasNext()) {
			MarkInfo markInfo = gson.fromJson(cursor.next().toString(),
					MarkInfo.class);
			count += markInfo.getComponentTrust();
		}
		return count;
	}

	/*
	 * public int getGoodComments(String mer_id) { int count = 0; DBObject query
	 * = mHelper.createQuery("mer_id", mer_id); query.put("status",
	 * ToolConstants.VALID_INT); DBCursor cursor = collection.find(query); while
	 * (cursor.hasNext()) { MarkInfo markInfo =
	 * gson.fromJson(cursor.next().toString(), MarkInfo.class); count +=
	 * markInfo.getGoodCommentNum(); } return count; }
	 */
	/*
	 * public int getBadComments(String mer_id) { int count = 0; DBObject query
	 * = mHelper.createQuery("mer_id", mer_id); query.put("status",
	 * ToolConstants.VALID_INT); DBCursor cursor = collection.find(query); while
	 * (cursor.hasNext()) { MarkInfo markInfo =
	 * gson.fromJson(cursor.next().toString(), MarkInfo.class); count +=
	 * markInfo.getBadCommentNum(); } return count; }
	 */

	// sign 为排序方式，统计数升序或降序
	public AggregationOutput markTimes(String field, DBObject query, int sign,
			int limit, int skip) {
		DBObject sort = new BasicDBObject();
		if (sign == 0) {
			sort.put("count", 1);
		} else if (sign == 1) {
			sort.put("count", -1);
		} else {
			sort = null;
		}
		return mHelper.aggregate(collection, field, query, sort, limit, skip);
	}

	// field 为统计的字段，排序有问题
	public AggregationOutput totalNum(String field, String field1,
			DBObject query, String sign, int limit, int skip) {
		DBObject sort = new BasicDBObject();
		// sign---"1111"形式,
		if (sign.equals("asc")) {
			sort.put(field1, 1);
		} else if (sign.equals("des")) {
			sort.put(field1, -1);
		} else {
			sort = null;
		}
		DBObject groupDbObject = new BasicDBObject();
		groupDbObject.put("count", new BasicDBObject("$sum", 1));
		groupDbObject.put("totalClick", new BasicDBObject("$sum",
				"$directCount"));
		groupDbObject.put("totalComment", new BasicDBObject("$sum",
				"$commentNum"));
		groupDbObject
				.put("trust", new BasicDBObject("$avg", "$componentTrust"));
		/*
		 * groupDbObject.put("totalGoodComment", new BasicDBObject("$sum",
		 * "$goodCommentNum")); groupDbObject.put("totalBadComment", new
		 * BasicDBObject("$sum", "$badCommentNum"));
		 */
		groupDbObject.put("informTimes", new BasicDBObject("$sum",
				"$informTimes"));
		return mHelper.aggregate(collection, field, groupDbObject, query, sort,
				limit, skip);
	}

	/**
	 * @author Kario 获取每个类别的标记数量,返回每个类别的标记数量，默认按数量倒序
	 * @param sign
	 *            排序字段
	 * @param query
	 *            查询条件
	 */
	public AggregationOutput countNumByMerchantType(String sign, DBObject query) {
		DBObject group = new BasicDBObject();
		group.put("count", new BasicDBObject("$sum", 1));
		DBObject sort = new BasicDBObject();
		// sign---"1111"形式,
		if (sign.equals("asc")) {
			sort.put("count", 1);
		} else if (sign.equals("des")) {
			sort.put("count", -1);
		} else {
			sort = null;
		}
		return mHelper.aggregate(collection, "componentType", group, query,
				sort, ToolConstants.BackGround_Default_PageSize, 0);
	}

	// field为限制返回字段值
	public PageContent<DBObject> searchSpecial(DBObject query,
			DBObject sortOrder, int currentPage, int num) {
		// TODO Auto-generated method stub
		return mHelper
				.listPager(collection, query, currentPage, sortOrder, num);
	}

	public int getMarkNum(String mer_id) {
		DBObject query = new BasicDBObject();
		query.put("mer_id", mer_id);
		query.put("status", ToolConstants.VALID_INT);
		return collection.find(query).count();
	}

	public int getComplainTimes(String mer_id) {
		DBObject query = new BasicDBObject("mer_id", mer_id);
		AggregationOutput output = totalNum("mer_id", "informTimes", query,
				"asc", 10, 0);
		int total = 0;
		for (DBObject obj : output.results()) {
			total = Integer.parseInt(obj.get("informTimes").toString());
		}
		return total;
	}

	private DBCollection collection = mHelper.getConnection().getCollection(
			"markinfo");
	private static Logger logger = Logger.getLogger(MarkInfoDao.class);

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}

	/**
	 * @author Kario 获取当前系统中有效标记总数
	 */
	public int getNumberOfMember() {
		return collection.find(
				mHelper.createQuery("status", ToolConstants.VALID_INT)).count();
	}

	/**
	 * @author Kario 获取每月标记增量，不管是否有效，只关注注册时间是否在该段时间内
	 * @param month
	 *            月份
	 * @param year
	 *            年份
	 */
	public int getNumberOfMark(int year, int month) {
		DBObject query = mHelper.generateStartAndEndTime("markDate", year,
				month);
		query.put("status", ToolConstants.VALID_INT);
		return collection.find(query).count();
	}

	/**
	 * @author Kario 返回前端会员登陆拉取的标记的相关评价
	 * @param memberName
	 *            会员名称
	 * @param query
	 *            查询条件
	 * @param sortOrder
	 *            排序方式
	 * @param currentPage
	 *            当前页号
	 * @param num
	 *            每页显示个数
	 */
	public PageContent<DBObject> SearchMarkByUrlAndCommented(String memberName,
			DBObject query, DBObject sortOrder, int currentPage, int num) {
		PageContent<DBObject> pageContent = mHelper.listPager(collection,
				query, currentPage, sortOrder, num);
		String memberID = null;
		if (pageContent != null) {
			if (pageContent.getList() != null) {
				MemberDao memberDao = new MemberDao();
				MerchantDao merchantDao = new MerchantDao();
				CommentDao commentDao = new CommentDao();
				for (int i = 0; i < pageContent.getList().size(); i++) {
					if (!memberName.equals("")) {
						// member
						memberID = memberDao.get_id(memberName);
						String markID = pageContent.getList().get(i).get("_id")
								.toString();
						DBObject condition = new BasicDBObject();
						condition.put("mem_id", memberID);
						condition.put("mark_id", markID);
						DBObject commentDbObject = commentDao.search(condition);
						if (commentDbObject != null) {
							pageContent
									.getList()
									.get(i)
									.put("isPraise",
											gson.fromJson(
													commentDbObject.toString(),
													Comment.class).isPhraise());
						} else {
							pageContent.getList().get(i).put("isPraise", false);
						}
					}
					DBObject queryMer = new BasicDBObject("_id", pageContent
							.getList().get(i).get("mer_id").toString());
					DBObject merchant = merchantDao.search(queryMer);
					if (merchant != null) {
						pageContent
								.getList()
								.get(i)
								.put("storeAddress",
										merchant.get("storeAddress").toString());
					}
				}
			}
		}
		return pageContent;
	}

	// 推荐
	public PageContent<DBObject> getRecommend() {
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		List<DBObject> list = new ArrayList<DBObject>();
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VALID_INT);
		int totalCount = collection.find(query).count();
		int skip = 0, temp = 0;
		for (int i = 0; i < ToolConstants.ForeGround_Default_PageSize; i++) {
			skip = (int) (Math.random() * totalCount) - 1;
			if (skip < 0)
				skip = 0;
			// if the random value is the same,recreate a random
			if (skip == temp) {
				i--;
				continue;
			} else {
				temp = skip;
			}
			DBObject object = collection.find(query).skip(skip).limit(1).next();
			list.add(object);
		}
		pageContent.setCurrentRecords(list.size());
		pageContent.setList(list);
		pageContent.setTotal(list.size());
		return pageContent;
	}

	public void addPraise(String mark_id) {
		BasicDBObject query = new BasicDBObject();
		query.append("_id", mark_id);
		query.append("status", ToolConstants.VALID_INT);
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			collection.update(
					query,
					new BasicDBObject().append("$inc",
							new BasicDBObject().append("praise", 1)));
			// 点赞，商家信用值加5；
			generateTrust(mark_id, 2);
		}
	}

	public void generateTrust(String id, int flag) {
		DBObject query = mHelper.createQuery("_id", id);
		DBObject object = search(query);
		if (flag == 1) {
			// 点击+2
			collection.update(query, new BasicDBObject().append("$inc",
					new BasicDBObject("componentTrust", 2)));
			if (object != null) {
				String mer_id = object.get("mer_id").toString();
				MerchantDao merchantDao = new MerchantDao();
				merchantDao.generateTrust(mer_id, 2);
			}
		}
		if (flag == 2) {
			// 点赞加5
			collection.update(query, new BasicDBObject().append("$inc",
					new BasicDBObject("componentTrust", 5)));
			if (object != null) {
				String mer_id = object.get("mer_id").toString();
				MerchantDao merchantDao = new MerchantDao();
				merchantDao.generateTrust(mer_id, 5);
			}
		}
	}

	// group---reduce 为处理函数，doc为当前文档，key为统计字段，finalize为对统计完的文档进行处理
	/*
	 * public String testGroup() { DBObject key = new BasicDBObject();
	 * key.put("mer_id", 1); DBObject cond = new BasicDBObject();
	 * cond.put("status", 2); DBObject initial = new BasicDBObject();
	 * initial.put("merchantId", 0); initial.put("markNum", 0);
	 * initial.put("totalTrust", 0); String reduce =
	 * "function(doc,out){out.merchantId=doc.merchantId;out.markNum+=1;out.totalTrust+=doc.componentTrust}"
	 * ; String finalize =
	 * "function(doc){doc.averageTrust=doc.totalTrust/doc.markNum;}";
	 * BasicDBList list = (BasicDBList) collection.group(key, cond, initial,
	 * reduce, finalize); return list.toString(); }
	 */

	public static void main(String[] args) {
		MarkInfoDao dao = new MarkInfoDao();
		PageContent<DBObject> pageContent = dao.getRecommend();
		logger.info(gson.toJson(pageContent));
	}

}
