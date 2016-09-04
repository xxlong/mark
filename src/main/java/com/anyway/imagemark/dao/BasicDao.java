/**
 * 
 */
package com.anyway.imagemark.dao;

import java.util.List;

import com.anyway.imagemark.utils.MongoHelper;
import com.anyway.imagemark.utils.PageContent;
import com.google.gson.Gson;
import com.mongodb.*;

/**
 * @author Administrator
 * 
 */
public interface BasicDao<E> {
	//save
	public int save(E object);
	// 单个字段查询
	public DBObject search(String field, String value);
	// 两个字段组合查询
	public DBObject search(String field1, String value1, String field2,
			String value2);
	// query条件查询
	public DBObject search(DBObject query);
	// 默认，无查询语句，不排序，返回num个，
	public List<DBObject> search(int num);
    public List<DBObject> searchList(DBObject query);
	// flag为pre或next,currentPage为当前页，分页处理
	public PageContent search(DBObject query,DBObject sortOrder,int currentPage,
			int num);
	// 提供给其他操作类
	public void update(DBObject query, DBObject update);
	// 按条件删除
	public void deleteByCondition(DBObject condition);
	// 获取标记为删除的文档
	public PageContent Deleted(DBObject query,DBObject sortOrder,int currentPage,
			int num);
	// 恢复全部
	public void Restore();
	// 条件恢复
	public void Restore(DBObject query);
	// 关闭连接
	public void closeClient();
	Gson gson = new Gson();
	MongoHelper mHelper = new MongoHelper("222.214.218.140",27017);
	//MongoHelper mHelper = new MongoHelper("127.0.0.1",27017);
}
