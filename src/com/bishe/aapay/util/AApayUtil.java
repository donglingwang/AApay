package com.bishe.aapay.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AApayUtil {

	public static String getCurrentTime(String pattern) {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		Date date = new Date(System.currentTimeMillis());
		return sf.format(date);
	}
	
	/**
	  * 验证邮箱地址是否正确
	  * @param email
	  * @return
	  */
	 public static boolean checkEmail(String email) {
	  boolean flag = false;
	  try{
		   String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		   Pattern regex = Pattern.compile(check);
		   Matcher matcher = regex.matcher(email);
		   flag = matcher.matches();
	  } catch(Exception e) {
		  System.out.println("验证邮箱地址错误");
		  flag = false;
	  }
	  	return flag;
	 }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getCurrentTime("yyyy-MM-dd hh:mm:ss"));
	}

}
