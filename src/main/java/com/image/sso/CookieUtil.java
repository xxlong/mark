package com.image.sso;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	public static boolean save(HttpServletResponse response, String key,
			String value) {

		if (!isNull(response) && !isEmpty(key)) {
			Cookie cookie = new Cookie(key, value);
			// cookie.setDomain(".tom.com");
			// System.out.println(cookie.getDomain());
			cookie.setMaxAge(Integer.MAX_VALUE);
			cookie.setPath("/");
			response.addCookie(cookie);
			return true;
		}
		return false;
	}

	public static String get(HttpServletRequest request, String key) {
		Cookie cookie = getCookie(request, key);
		if (!isNull(cookie)) {
			return cookie.getValue();
		}
		return null;
	}

	public static Cookie getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (!isNull(cookies) && !isEmpty(key)) {
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static boolean isNull(Object obj) {
		return (obj == null);
	}

	public static boolean isEmpty(String str) {
		return ((str == null) || "".equals(str.trim()));
	}
}
