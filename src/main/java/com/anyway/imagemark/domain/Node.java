package com.anyway.imagemark.domain;

//标记位置信息
public class Node {
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isCheckMark() {
		return checkMark;
	}

	public void setCheckMark(boolean checkMark) {
		this.checkMark = checkMark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getxPosition() {
		return xPosition;
	}

	public void setxPosition(float xPosition) {
		this.xPosition = xPosition;
	}

	public float getyPosition() {
		return yPosition;
	}

	public void setyPosition(float yPosition) {
		this.yPosition = yPosition;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	
	public String getUrl() {
		return Url;
	}

	public void setUrl(String Url) {
		this.Url = Url;
	}
	private String nodeId;
	private String Url;
	private float xPosition;
	private float yPosition;
	//标记商品类型
	private String Type;
	private int status;
	//表示该点下面是否有待审核的标记；true表示有，false表示无；
	private boolean checkMark;
}
