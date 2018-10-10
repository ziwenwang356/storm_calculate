package com.tangdou.panda.utils;

import org.apache.commons.lang.StringUtils;

public class Convert {

	public static Integer toInt(Object obj) {
		try {
			String str = toString(obj);
			if (!StringUtils.isNumeric(str)) {
				return null;
			}
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return null;
	}

	public static Long toLong(Object obj) {
		try {
			String str = toString(obj);
			if (!StringUtils.isNumeric(str)) {
				return null;
			}
			return Long.parseLong(str);
		} catch (Exception e) {
		}
		return null;
	}

	public static Double toDouble(Object obj) {
		try {
			String str = toString(obj);
			if (StringUtils.isBlank(str)) {
				return null;
			}
			return Double.parseDouble(str);
		} catch (Exception e) {
		}
		return null;
	}

	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		return String.valueOf(obj);
	}

	public static int toInt(Object obj, int def) {
		try {
			String str = toString(obj);
			if (StringUtils.isBlank(str)) {
				return def;
			}
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return def;
	}
	
	public static double toDouble(Object obj,Double def) {
		try {
			String str = toString(obj);
			if (StringUtils.isBlank(str)) {
				return def;
			}
			return Double.parseDouble(str);
		} catch (Exception e) {
		}
		return def;
	}
}
