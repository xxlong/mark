/**
 * 
 */
package com.anyway.imagemark.domain;

import org.bson.types.ObjectId;

/**
 * @author Kario
 * 
 */
public class ClickInfo {

	


	public String getMer_id() {
		return mer_id;
	}

	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
	}

	public String get_id() {
		return _id;
	}

	public void set_id() {
		this._id = new ObjectId().toString();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMark_id() {
		return mark_id;
	}

	public void setMark_id(String mark_id) {
		this.mark_id = mark_id;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/*public String getViewPhoto() {
		return viewPhoto;
	}

	public void setViewPhoto(String viewPhoto) {
		this.viewPhoto = viewPhoto;
	}

	public String getViewComponent() {
		return viewComponent;
	}

	public void setViewComponent(String viewComponent) {
		this.viewComponent = viewComponent;
	}
*/
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	//_id
		private String _id;
	// 点击信息会员_id，
	private String mem_id;
	// 图片所在域名----可以把域名取出来，单独放在一个聚集？保留该字段，统计时查询即可
	private String domain;
	//该条标记信息的_id字段值
	private String mark_id;
	private String mer_id;
	// 点击时间
	private long date;
	//信息状态
	private int status;
}
