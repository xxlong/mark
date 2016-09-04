package com.image.sso;

import com.alibaba.fastjson.JSON;

public class LoginModel {

	// 用户名
	private String username;
	// 用户密码
	private String password;
	// 用户是否登陆标记，true代表已登录，false代表未登录
	private boolean logining;
	// 用户名和密码是否记住标记，true代表记住，false代表不记住
	private boolean remember;
	// 用户类型，商家和普通用户
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
		// 测试对象转为json字符串
		LoginModel model = new LoginModel();
		model.setUsername("username_tom");
		model.setPassword("pass_tom");
		model.setRemember(true);
		model.setLogining(false);
		model.setUsertype("customer");
		System.out.println(model.toJsonString());

		// 测试json字符串转为对象
		model = LoginModel.toLoginModel(model.toJsonString());
		model.setUsername(null);
		System.out.println(model.toJsonString());
	}
}
