package com.anyway.imagemark.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.springframework.stereotype.Repository;

import com.anyway.imagemark.dao.BasicDao;
import com.anyway.imagemark.domain.Node;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ToolConstants;
import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Repository("nodeDao")
public class NodeDao implements BasicDao<Node> {

	public int save(Node object) {
		// 添加位置信息，初始设置状态为待审核;当达到上限时，提示不能添加
		int status = ToolConstants.ResultStatus_Success;
		object.setStatus(ToolConstants.VERIFY_INT);
		// 对url进行处理
		if (object.getUrl() != null && object.getUrl() != "") {
			String prefix = object.getUrl().substring(0,
					object.getUrl().indexOf('&'));
			String fixedUrl="";
			//网易
			if(prefix.contains("cache.netease.com")){
			fixedUrl = getOriginalUrl(prefix)
					+ object.getUrl().substring(object.getUrl().indexOf('&'),
							object.getUrl().length());
			}else{
				fixedUrl=prefix+object.getUrl().substring(object.getUrl().indexOf('&'),
						object.getUrl().length());
			}
			DBObject query = mHelper.createQuery("Url", fixedUrl);
			query.put("status", ToolConstants.VALID_INT);
			int currentNode = collection.find(query).count();
			if (currentNode < ToolConstants.NodeUPLIMIT) {
				object.setUrl(fixedUrl);
				DBObject resultDbObject = (DBObject) JSON.parse(gson
						.toJson(object));
				collection.insert(resultDbObject);
			} else {
				status = ToolConstants.ResultStatus_Fail_InsertError;
			}
		}
		return status;
	}

	public DBObject search(String field, String value) {
		// TODO Auto-generated method stub
		DBObject query = mHelper.createQuery(field, value);
		return mHelper.findByCondition(collection, query);
	}

	public DBObject search(String field1, String value1, String field2,
			String value2) {
		// TODO Auto-generated method stub
		return null;
	}

	public DBObject search(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.findByCondition(collection, query);
	}

	public List<DBObject> search(int num) {
		// TODO Auto-generated method stub
		return mHelper.listFindDefault(collection, num);
	}

	public List<DBObject> searchList(DBObject query) {
		// TODO Auto-generated method stub
		return mHelper.listFindByCondition(collection, query);
	}

	public List<DBObject> searchNode(String url) {
		DBObject queryDbObject = new BasicDBObject();
		logger.info("the url is :" + url);
		if (!url.equals("")) {
			// if the picture crop like '...../900+600_...jpg
			String regex="";
			if(url.contains("cache.netease.com")){
				String tempString=url.substring(url.indexOf('.')+1, url.length());
				regex = ".*?" + getOriginalUrl(tempString) + ".*";
			}else{
				regex=".*?"+url+".*";
			}
			logger.info(regex);
			Pattern pattern = Pattern.compile(regex);
			queryDbObject.put("Url", pattern);
			queryDbObject.put("status", ToolConstants.VALID_INT);
			// DBObject filterDbObject=new BasicDBObject();
			// int total=collection.find(queryDbObject).count();
			DBCursor cursor = collection.find(queryDbObject);
			List<DBObject> list = new ArrayList<DBObject>();
			// filterDbObject.put("total", total);
			// list.add(filterDbObject);
			while (cursor.hasNext()) {
				list.add(cursor.next());
			}
			return list;
		} else {
			return null;
		}
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
			logger.info(ToolConstants.DOCNOTFOUND_STRING);
		}
	}

	// 设置位置信息有效
	public int validateNode(String id, int status) {
		DBObject query = new BasicDBObject();
		query.put("nodeId", id);
		query.put("status", ToolConstants.VERIFY_INT);
		DBObject temp = search(query);
		if (temp != null) {
			String url = temp.get("Url").toString();
			DBObject condition = new BasicDBObject("Url", url);
			condition.put("status", ToolConstants.VALID_INT);
			int count = collection.find(condition).count();
			if (count < ToolConstants.NodeUPLIMIT) {
				update(query, new BasicDBObject("$set", new BasicDBObject(
						"status", ToolConstants.VALID_INT)));
				return ToolConstants.ResultStatus_Success;
			} else {
				// 当位置信息达到上限时，删除其他未审核的位置信息
				collection.update(query, new BasicDBObject("$set",
						new BasicDBObject("status", ToolConstants.DELETE_INT)),
						true, true);
				return ToolConstants.ResultStatus_Fail_InsertError;
			}
		} else {
			return ToolConstants.ResultStatus_NoData;
		}
	}

	public void deleteByCondition(DBObject condition) {
		// 删除位置信息，则关联的标记信息也被删除
		DBObject result = search(condition);
		if (result != null) {
			String _id = result.get("nodeId").toString();
			DBObject query = new BasicDBObject("node_id", _id);
			MarkInfoDao markInfoDao = new MarkInfoDao();
			markInfoDao.deleteByCondition(query);
			update(mHelper.createQuery("nodeId", _id), new BasicDBObject(
					"$set", new BasicDBObject("status",
							ToolConstants.DELETE_INT)));
		} else {
			logger.info("the search result is null");
		}
	}

	public PageContent Deleted(DBObject query, DBObject sortOrder,
			int currentPage, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	public void Restore() {
		// TODO Auto-generated method stub

	}

	public void Restore(DBObject query) {
		// TODO Auto-generated method stub

	}

	public void closeClient() {
		// TODO Auto-generated method stub

	}

	// 统计url
	public AggregationOutput getAggregationOutput(DBObject sort, int pageSize,
			int page) {
		DBObject query = new BasicDBObject("status", new BasicDBObject("$ne",
				ToolConstants.DELETE_INT));
		query.put("checkMark", true);
		return mHelper.aggregate(collection, "Url", query, sort, pageSize,
				(page - 1) * pageSize);
	}

	private String getOriginalUrl(String url) {
		String temp = "";
		int len = url.length();
		int index1 = url.indexOf('_');
		if (index1 > 0) {
			temp = url.substring(0, index1 - 7)
					+ url.subSequence(index1 + 1, len);
		} else {
			temp = url;
		}
		return temp;
	}

	public static void main(String[] args) {
		String regex = "";
		NodeDao nodeDao = new NodeDao();
		logger.info(gson.toJson(nodeDao.searchNode(regex)));
		AggregationOutput output = nodeDao.getAggregationOutput(
				mHelper.createQuery("Url", 1), 10, 1);
	}

	private DBCollection collection = mHelper.getConnection().getCollection(
			"node");
	private static Logger logger = Logger.getLogger(NodeDao.class);
	private static Gson gson = new Gson();
}
