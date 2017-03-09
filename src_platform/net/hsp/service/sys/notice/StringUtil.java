package net.hsp.service.sys.notice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class StringUtil {

	public static String getString(Object obj) {
		if (null == obj)
			return "";
		else
			return obj.toString().replaceAll("'", "\"").trim();
	}

	public static String[] getStringSplit(Object obj) {
		return getString(obj).split(",");
	}

	public static String getJsonString(Object obj) {
		String s = getString(obj);
		if (isJsonNull(s))
			return "";
		else
			return s;
	}

	public static Date getNow() {
		return new Date();
	}

	public static Date getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(getDateString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public static Date getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(getDateTimeString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public static String getDateString(Object obj) {
		String dt = getString(obj);
		if (dt.indexOf(" ") > 0)
			return dt.substring(0, dt.indexOf(" "));
		return dt;
	}

	public static String getDateHMString(Object obj) {
		if (isEmpty(obj))
			return "";
		else {
			String dateHM = getString(obj);
			dateHM = dateHM.substring(0, dateHM.lastIndexOf(":"));
			return dateHM;
		}
	}

	public static String getDateString() {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		return sdformat.format(new Date());
	}

	public static String getDateTimeString() {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdformat.format(new Date());
	}

	public static String getDateString(Date date) {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		return sdformat.format(date);
	}

	public static String getDateTimeString(Date date) {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdformat.format(date);
	}

	public static boolean isJsonNull(Object obj) {
		if (isEmpty(obj))
			return true;
		else if ("null".equals(getString(obj)))
			return true;
		else
			return false;
	}

	public static boolean isEmpty(Object obj) {
		try {
			if (obj == null)
				return true;
			else if (obj.toString().trim().length() == 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	public static String getWhereInNumber(List<Map<String, Object>> list, String key) {
		StringBuffer sb = new StringBuffer(" (");
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).get(key).toString());
			if (i < list.size() - 1)
				sb.append(",");
		}
		sb.append(") ");
		return sb.toString();
	}

	public static List<Map<String, Object>> getMergeList(List<Map<String, Object>> list, List<Map<String, Object>> list1) {
		for (Map map : list1) {
			if (Collections.frequency(list, map) < 1)
				list.add(map);
		}
		return list;
	}

	public static Map<String, Object> getDefaultRootMap() {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("id", 0);
		rootMap.put("name", "全部");
		rootMap.put("pId", -1);
		rootMap.put("pId", -1);
		return rootMap;
	}

	public static Map<String, Object> getNoticeRoot() {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("id", 0);
		rootMap.put("name", "通知公告");
		rootMap.put("parentId", -1);
		rootMap.put("pId", -1);
		return rootMap;
	}

}
