/**
 * 
 */
package com.anyway.imagemark.domain;

import org.bson.types.ObjectId;

/**
 * @author Kario
 * 
 */
public class Notification {

	public String getNoticeID() {
		return noticeID;
	}

	public void setNoticeID(String noticeID) {
		this.noticeID = noticeID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendID() {
		return sendID;
	}

	public void setSendID(String sendID) {
		this.sendID = sendID;
	}

	public String getNotifier() {
		return notifier;
	}

	public void setNotifier(String notifier) {
		this.notifier = notifier;
	}
	// objectId
	private String  _id;
	// 通知对象---商家、会员、全部
	private String notifier;
	// 通知类别---通知、公告(通知1，公告2,系统消息3）
	private int type;
	// 消息标题
	private String title;
	// 消息内容
	private String content;
	// 发送日期
	private long sendTime;
	// 发送者id
	private String sendID;
	//通知对象ID
	private String noticeID;
	// 信息状态
	private int status;
}
