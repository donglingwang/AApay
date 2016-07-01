package com.bishe.aapay.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AApayDateUtil {

	private static GregorianCalendar gregorianCalendar = new GregorianCalendar();
	private static Calendar calendar = Calendar.getInstance();
	private static Date givenDate = new Date();
	
	public static void main(String[] args) {
		SimpleDateFormat format = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );  
	     Date date = new Date();
	    try {
			 date = format.parse("2015-04-21 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	    setGivenDate(date);
		System.out.println(getMondayDate());
		System.out.println(getDateByMonday(6));
		System.out.println(getWeek());
		System.out.println(getCurrentDate());
		System.out.println(getBeginTimeOfDate(getBeginTimeOfCurrentMonth()));
		System.out.println(getEndTimeOfDate(getEndTimeOfCurrenntMonth()));
	}
	
	/**
	 * ��ȡ��ǰ���������ڼ�
	 * @return
	 */
	 public static String getWeek() {
		 calendar.setTime(givenDate);
		 String[] weekDays = {"������", "����һ", "���ڶ�", "������", "������", "������", "������"};
			int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if( w < 0) {
				w = 0;
			}
			return weekDays[w];
	 }
	 
	public static String getDateByInterval(int interval) {
		gregorianCalendar.setTime(givenDate);
	    gregorianCalendar.add(GregorianCalendar.DATE, interval);
	    calendar.setTime(gregorianCalendar.getTime());
	    return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));
	}
	
	public static String getMondayByInterval(int interval) {
		return getDateByMonday(interval*7);
	}
	public static String getSundayByInterval(int interval) {
		return getDateByMonday(6+interval*7);
	}
	
	public static String getMonthBeginByInterval(int interval) {
		gregorianCalendar.setTime(givenDate);
	    gregorianCalendar.add(GregorianCalendar.MONTH, interval);
	    calendar.setTime(gregorianCalendar.getTime());
		return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, 1);
	}
	
	public static String getMonthEndByInterval(int interval) {
		gregorianCalendar.setTime(givenDate);
	    gregorianCalendar.add(GregorianCalendar.MONTH, interval);
	    calendar.setTime(gregorianCalendar.getTime());
		 return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.getActualMaximum(Calendar.DATE));
	}
	
	 public static String getWeekByDate(String strDate) {
		 SimpleDateFormat format = new  SimpleDateFormat("yyyy-MM-dd HH:mm" );  
	     Date date = new Date();
	    try {
			 date = format.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    calendar.setTime(date);
	    String[] weekDays = {"������", "����һ", "���ڶ�", "������", "������", "������", "������"};
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if( w < 0) {
			w = 0;
		}
		return weekDays[w];
	 }
	 /**
	  * ��ȡ��ǰ�ܵ���������
	  * @return
	  */
	 public static String getWeekInterval() {
		 return getMondayDate()+"~"+getDateByMonday(6);
	 }
	 
	/**
	 * ��ȡ��ǰ���ڣ�����һ������
	 * @return
	 */
	public static String getMondayDate() {
		setGregorianCalendarByMonday();
        Date monday = gregorianCalendar.getTime();
        calendar.setTime(monday);
        return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));
	}
	
	private static void setGregorianCalendarByMonday() {
		gregorianCalendar.setTime(givenDate);
		calendar.setTime(givenDate);
	    int mondayPlus = getMondayPlus();
	    gregorianCalendar.add(GregorianCalendar.DATE, mondayPlus);
	}
	/**
	 * ��ȡ��ǰ����
	 * @return
	 */
	public static String getCurrentDate() {
		calendar.setTime(givenDate);
		//calendar.set(Calendar.MONTH,3);
		return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));
	}
	
	 public static String getCurrentDate(String pattern) {
		 SimpleDateFormat df = new SimpleDateFormat(pattern);
		 return df.format(givenDate);
	 }
	 /**
	  * ��������һ�����ڻ�ȡ�������ڼ�������
	  * @param mondayCalendar
	  * @param day
	  * @return
	  */
	 public static String getDateByMonday(int day) {
		 setGregorianCalendarByMonday();
		 gregorianCalendar.add(GregorianCalendar.DATE, day);
		 Date date = gregorianCalendar.getTime();
		 calendar.setTime(date);
		 return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));
	 }
	 
	 /**
	  * ��ȡ��ǰ���ں�����һ��������
	  * String[] weekDays = {"������", "����һ", "���ڶ�", "������", "������", "������", "������"};
	  * @param calendar
	  * @return
	  */
	private static int getMondayPlus() {
		int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
		if(dayOfweek == 1) {
			return -6;
		}
		return 2 - dayOfweek;
	}
	
	 /**
	  * ��ʽ��
	  * @param year
	  * @param month
	  * @param day
	  * @return
	  */
	 private static String formatDate(int year,int month, int day) {
		 String m = month < 10 ? "0"+month : ""+month;
		 String d = day < 10 ? "0"+day : ""+day;
		 return year +"-"+ m +"-"+ d;
	 }
	 /**
	  * ��ȡ��ǰ�¿�ʼ����
	  * @return
	  */
	 public static String getBeginTimeOfCurrentMonth() {
		 calendar.setTime(givenDate);
		 //calendar.set(Calendar.MONTH, 3);
		 return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, 1);
	 }
	 /**
	  * ��ȡ��ǰ�½�������
	  * @return
	  */
	 public static String getEndTimeOfCurrenntMonth() {
		 calendar.setTime(givenDate);
		// calendar.set(Calendar.MONTH, 3);
		 return formatDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.getActualMaximum(Calendar.DATE));
	 }
	 

	 /**
	  * ��ȡĳ�յĿ�ʼʱ��
	  * @param date
	  * @return
	  */
	 public static String getBeginTimeOfDate(String date) {
		 return date + " 00:00:00";
	 }
	 /**
	  * ��ȡĳ�յĽ���ʱ��
	  * @param date
	  * @return
	  */
	 public static String getEndTimeOfDate(String date) {
		 return date +" 23:59:59";
	 }



	public static Date getGivenDate() {
		return givenDate;
	}



	public static void setGivenDate(Date givenDate) {
		AApayDateUtil.givenDate = givenDate;
	}
}
