package com.image.sso;

import com.alibaba.fastjson.JSON;

public class LoginModel {

	// �û���
	private String username;
	// �û�����
	private String password;
	// �û��Ƿ��½��ǣ�true�����ѵ�¼��false����δ��¼
	private boolean logining;
	// �û����������Ƿ��ס��ǣ�true�����ס��false������ס
	private boolean remember;
	// �û����ͣ��̼Һ���ͨ�û�
	private String usertype;

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public LoginModel() {
		username = "none";
		password = "none";
		logining = false;
		remember = false;
		usertype = "none";
	}

	public LoginModel(String username, String password, String usertype,
			boolean logining, boolean remember) {
		this.username = username;
		this.password = password;
		this.usertype = usertype;
		this.logining = logining;
		this.remember = remember;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogining() {
		return logining;
	}

	public void setLogining(boolean logining) {
		this.logining = logining;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	public static LoginModel toLoginModel(String jsonString) {
		return (LoginModel) (JSON.parseObject(jsonString, LoginModel.class));
	}

	public static void main(String[] args) {
		// ���Զ���תΪjson�ַ���
		LoginModel model = new LoginModel();
		model.setUsername("username_tom");
		model.setPassword("pass_tom");
		model.setRemember(true);
		model.setLogining(false);
		model.setUsertype("customer");
		System.out.println(model.toJsonString());

		// ����json�ַ���תΪ����
		model = LoginModel.toLoginModel(model.toJsonString());
		model.setUsername(null);
		System.out.println(model.toJsonString());
	}
}
