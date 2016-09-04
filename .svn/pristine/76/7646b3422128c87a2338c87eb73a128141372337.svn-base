/**
 * 
 */
package com.anyway.imagemark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Kario ������ת��Ϊ��Ӧ�ĸ�ʽ
 */
public class DateFormat {

	public SimpleDateFormat getsDateFormat() {
		return sDateFormat;
	}

	public void setsDateFormat(SimpleDateFormat sDateFormat) {
		this.sDateFormat = sDateFormat;
	}

	// ���ص�ǰʱ��ĸ�ʽ�ַ�
	public String generateTime(String format) {
		sDateFormat = new SimpleDateFormat(format);
		return sDateFormat.format(new Date(System.currentTimeMillis()));
	}

	public String generateTime(String format, Date date) {
		if (date != null) {
			sDateFormat = new SimpleDateFormat(format);
			return sDateFormat.format(date);
		} else {
			return null;
		}
	}

	public long generateTime(int day) {
		return System.currentTimeMillis() - (long) 86400000 * day;
	}

	public String generateTime(String format, long time) {
		sDateFormat = new SimpleDateFormat(format);
		return sDateFormat.format(time);
	}

	public long formatToLong(String date) {
		sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date timeDate = null;
		try {
			timeDate = sDateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeDate.getTime();
	}

	public String getTime(long time) {
		long temp = System.currentTimeMillis() - time;
		String reString = "";
		int days = (int) (temp / ToolConstants.ONEDAY_LONG);
		if (days != 0) {
			// days
			int year = days / 365;
			if (year != 0) {
				// years
				reString += year + "年前";
			} else {
				// month
				int month = days / 30;
				if (month != 0) {
					reString += month + "月前";
				} else {
					int week = days / 7;
					if (week != 0) {
						reString += week + "周前";
					} else {
						reString += days + "天前";
					}
				}
			}
		} else {
			int hours = (int) (temp / (long) (3600 * 1000));
			if (hours != 0) {
				reString += hours + "小时前";
			} else {
				int mins = (int) (temp / (60 * 1000));
				reString += mins + "分钟前";
			}
		}
		return reString;
	}

	public static void main(String[] args) {
		DateFormat myFormat = new DateFormat();
		long time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		String myString = "" + calendar.get(Calendar.YEAR) + "/0"
				+ (calendar.get(Calendar.MONTH) + 1) + "/01 00:00:00";
		System.out.println(time);
		String timeString = myFormat.generateTime("yyyy/MM/dd HH:mm:ss");
		System.out.println(System.currentTimeMillis());
		System.out.println(timeString);
		System.out.println(myFormat.generateTime("yyyy/MM/dd HH:mm:ss",
				1405409553381l));
		System.out.println(myFormat.formatToLong("2014-09-01"));
		System.out.println(myFormat.getTime(1423022053925l));
	}

	private SimpleDateFormat sDateFormat = null;
}
