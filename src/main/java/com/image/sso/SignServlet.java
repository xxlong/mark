package com.image.sso;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setHeader(request, response);
		String method = request.getParameter("method");
		if (method != null) {
			if (method.equals("save")) {
				doSaveLoginInfor(request, response);
			} else if (method.equals("get")) {
				doGetLoginInfor(request, response);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void setHeader(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/javascript;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader(
				"P3P",
				"CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
	}

	private void doGetLoginInfor(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String sso = Base64Util.getFromBase64(CookieUtil.get(request, "sso"));
		if (sso == null) {
			sso = new LoginModel().toJsonString();
		}
		// System.out.println("before encode sso: " + sso);
		PrintWriter out = response.getWriter();
		out.print("sso = " + sso + ";");
		out.flush();
		out.close();
	}

	private void doSaveLoginInfor(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean flag = false;
		String loginInfo = getLoginInfo(request);
		if (loginInfo != null) {
			String encodedLoginInfo = Base64Util.getBase64(loginInfo);
			if (encodedLoginInfo != null){
				flag = CookieUtil.save(response, "sso", encodedLoginInfo);
			}
		}
		PrintWriter out = response.getWriter();
		if (flag) {
			// System.out.println("save success!");
			out.print("save_sso_result = {result:'true'};");
		} else {
			out.print("save_sso_result = {result:'false'};");
		}
		out.flush();
		out.close();
	}

	private String getLoginInfo(HttpServletRequest request) {
		// String username = request.getParameter("username");
		// String password = request.getParameter("password");
		// boolean remember = getBoolean(request.getParameter("remember"));
		// boolean logining = getBoolean(request.getParameter("logining"));
		// String usertype = request.getParameter("usertype");
		String sso = request.getParameter("sso");
		System.out.println(sso);
		return sso;
	}

	private boolean getBoolean(String s) {
		boolean result = false;
		try {
			result = Boolean.valueOf(s);
		} catch (Exception e) {
			return false;
		}
		return result;
	}

}
