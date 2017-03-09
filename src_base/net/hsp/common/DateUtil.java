package net.hsp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 将date对象转换为"yyyy-MM-dd hh:mm:ss"型字符串。
	 * 
	 * @param
	 * @return
	 */
	public static String getDateTime(Date time) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return dateTimeFormat.format(time);
	}

	/**
	 * 将"yyyy-MM-dd hh:mm:ss"型字符串转换为date对象。
	 * 
	 * @param
	 * @return
	 */
	public static Date getDateTime(String dateTimeString) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			return dateTimeFormat.parse(dateTimeString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取Date对象中 年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance(java.util.Locale.CHINA);

		calendar.clear();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

}
