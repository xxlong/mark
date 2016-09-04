package com.image.sso;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {

	private static final BASE64Encoder encoder = new BASE64Encoder();
	private static final BASE64Decoder decoder = new BASE64Decoder();

	// ����
	public static String getBase64(String s) {
		if (s == null) {
			return null;
		}
		String result = encoder.encode(s.getBytes());
		// ������з���cookie�в��ܴ洢���з�
		result = result.replace("\r\n", "");
		result = result.replace("\n", "");
		return result;
	}

	// ����
	public static String getFromBase64(String s) {
		if (s == null) {
			return null;
		}
		try {
			byte b[] = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
}
