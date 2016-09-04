/**
 * 
 */
package com.anyway.imagemark.utils;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;

/**
 * @author Kario
 *
 */
@Repository
public class PageContent<E> {
	 public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrentRecords() {
		return currentRecords;
	}

	public void setCurrentRecords(int currentRecords) {
		this.currentRecords = currentRecords;
	}

	public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public void setTotalPages(int totalPages) {
			this.totalPages = totalPages;
		}
		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public List<E> getList() {
			return rows;
		}

		public void setList(List<E> list) {
			this.rows = list;
		}
		
		//第一页记录数
	    private int               pageSize         = 20;
		//总页数
	    private int               totalPages        = 1;
	    //本次返回的总记录数
	    private int               currentRecords      = 0;
	    //当前页码
	    private int               pageNumber      = 1;
	    
	    //返回的数据集
	    private List<E> rows = null;
	    private int total;
}
