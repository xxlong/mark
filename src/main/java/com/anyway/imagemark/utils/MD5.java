package com.anyway.imagemark.utils;
import java.security.MessageDigest; 

public class MD5 {
	public static String getMD5ofStr(String origString) {  
        String origMD5 = null;  
        try {  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] result = md5.digest(origString.getBytes());  
            origMD5 = byteArrayToHexStr(result);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return origMD5;  
    }  
    /** 
     * 处理字节数组得到MD5密码的方法 
     */  
    private static String byteArrayToHexStr(byte[] bs) {  
        StringBuffer sb = new StringBuffer();  
        for (byte b : bs) {  
            sb.append(byteToHexStr(b));  
        }  
        return sb.toString();  
    }  
    /** 
     * 字节标准移位转十六进制方法 
     */  
    private static String byteToHexStr(byte b) {  
        String hexStr = null;  
        int n = b;  
        if (n < 0) {  
            // 若需要自定义加密,请修改这个移位算法即可  
            n = b & 0x7F + 128;  
        }  
        hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);  
        return hexStr.toUpperCase();  
    }  
    /** 
     * 提供一个MD5多次加密的方法 
     */  
    public static String getMD5ofStr(String origString, int times) {  
        String md5 = getMD5ofStr(origString);  
        for (int i = 0; i < times - 1; i++) {  
            md5 = getMD5ofStr(md5);  
        }  
        return getMD5ofStr(md5);  
    }  
    /** 
     * 密码验证方法 
     */  
    public static boolean verifyPassword(String inputStr, String MD5Code) {  
        return getMD5ofStr(inputStr).equals(MD5Code);  
    }  
    /** 
     * 多次加密时的密码验证方法 
     */  
    public static boolean verifyPassword(String inputStr, String MD5Code,  
            int times) {  
        return getMD5ofStr(inputStr, times).equals(MD5Code);  
    }  
    
    
    public static void main(String[] args) {    
        System.out.println("123456:" + getMD5ofStr("123456"));  
        System.out.println("pioneer:" + getMD5ofStr("pioneer"));  
        System.out.println("123:" + getMD5ofStr("123", 4));  
    }  
    
    
    //****************************************************KG***************************************************
    

    //****************************************************KG***************************************************
}