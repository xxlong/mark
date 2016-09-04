/**
 * 
 */
package com.anyway.imagemark.webDomain;

/**
 * @author Kario
 * 
 */
public class MerchantMark {
	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getComplainId() {
		return complainId;
	}

	public void setComplainId(String complainId) {
		this.complainId = complainId;
	}

	public String getComplainReason() {
		return complainReason;
	}

	public void setComplainReason(String complainReason) {
		this.complainReason = complainReason;
	}

	public String getComplainTime() {
		return complainTime;
	}

	public void setComplainTime(String complainTime) {
		this.complainTime = complainTime;
	}

	public Float getTrust() {
		return trust;
	}

	public void setTrust(Float trust) {
		this.trust = trust;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public float getComponentPrice() {
		return componentPrice;
	}

	public void setComponentPrice(float componentPrice) {
		this.componentPrice = componentPrice;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public int getUnstatisfyComment() {
		return unstatisfyComment;
	}

	public void setUnstatisfyComment(int unstatisfyComment) {
		this.unstatisfyComment = unstatisfyComment;
	}

	public int getStatisfyComment() {
		return statisfyComment;
	}

	public void setStatisfyComment(int statisfyComment) {
		this.statisfyComment = statisfyComment;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMarkDate() {
		return markDate;
	}

	public void setMarkDate(String markDate) {
		this.markDate = markDate;
	}

	public int getClickTimes() {
		return clickTimes;
	}

	public void setClickTimes(int clickTimes) {
		this.clickTimes = clickTimes;
	}

	public int getCommentTimes() {
		return commentTimes;
	}

	public void setCommentTimes(int commentTimes) {
		this.commentTimes = commentTimes;
	}
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	private String _id;
	private String url;
	private String link;
	private String componentName;
	private float componentPrice;
	private String componentType;
	private String markDate;
	private String deleteDate;
	private String node_id;
	private int clickTimes;
	private int commentTimes;
	private int unstatisfyComment;
	private int statisfyComment;
	private String  merchantName;
	private Float  trust;
	private String complainReason;
	private String complainTime;
	private String complainId;
}
