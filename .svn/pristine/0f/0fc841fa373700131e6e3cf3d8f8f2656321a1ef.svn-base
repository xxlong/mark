/**
 * 
 */
package com.anyway.imagemark.domain;

/**
 * @author Kario
 *
 */
public class Complain {

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getMark_id() {
		return mark_id;
	}

	public void setMark_id(String mark_id) {
		this.mark_id = mark_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComplainer() {
		return complainer;
	}

	public void setComplainer(String complainer) {
		this.complainer = complainer;
	}

	public String getComplainReason() {
		return complainReason;
	}

	public void setComplainReason(int complainReason) {
		if (complainReason == 1) {
			this.complainReason = "商品图片与标记图片不符";
		} else if (complainReason == 2) {
			this.complainReason = "商品虚假连接";
		} else if (complainReason == 3) {
			this.complainReason = "商品内容涉嫌违规";
		} else if (complainReason == 4) {
			this.complainReason = "商品类别不符";
		} else {
			this.complainReason = "其他原因";
		}

	}

	public long getComplainTime() {
		return complainTime;
	}

	public void setComplainTime() {
		this.complainTime = System.currentTimeMillis();
	}

	//
	private String _id;
	// 标记id
	private String mark_id;
	// 会员名称
	private String complainer;
	private String mem_id;
	// 举报原因
	private String complainReason;
	// 举报时间
	private long complainTime;
	// 信息状态
	private int status;
}
