package com.anyway.imagemark.utils;

import org.springframework.security.authentication.encoding.PasswordEncoder;

public class MD5Encoder implements PasswordEncoder {

	public String encodePassword(String origPwd, Object salt) {
		// TODO Auto-generated method stub
		/*return MD5.getMD5ofStr(origPwd);*/
		return CipherUtil.generatePassword(origPwd);
	}

	public boolean isPasswordValid(String encPwd, String origPwd, Object salt) {
		// TODO Auto-generated method stub
		return encPwd.equals(encodePassword(origPwd, salt));
	}

}
