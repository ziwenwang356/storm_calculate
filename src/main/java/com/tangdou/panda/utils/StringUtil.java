package com.tangdou.panda.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiangtao on 2017/6/7.
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    static final long threeDay = 3 * 24 * 60 * 60 * 1000;
    static final long fourDay = 4 * 24 * 60 * 60 * 1000;

    public static int safeGetInt(JSONObject json, String key) {
        try {
            return json.getInteger(key);
        } catch (Exception e) {
            return 0;
        }
    }

    public static final String getOne(String value) {
    	String[] arr = getArray(value);
    	if (null != arr && arr.length >0) {
    		return arr[0];
    	}
    	return value;
    }
    public static final String[] getArray(String value) {
    	if (StringUtils.isBlank(value)) {
    		return new String[] {};
    	}
    	return value.split("\\||\\,");
    }
    public static final String[] getArray(String value, String regex) {
    	if (StringUtils.isBlank(value)) {
    		return new String[] {};
    	}
    	return value.split(regex);
    }
    public static final String getOrDefault(String value, String defaultVal) {
    	if (null == value) {
    		return defaultVal;
    	}
    	return value;
    }
    public static final String getOrDefault(String[] arr, int index, String defaultVal) {
    	if (null == arr || arr.length <= index) {
    		return defaultVal;
    	}
    	String value = arr[index]; 
    	return StringUtils.isBlank(value) ? defaultVal : value;
    }


    public static void main(String[] args) throws Exception {
        String str = "GET /api.php?mode=empty HTTP/1.1";
        String str2 = "GET /api.php?mod=emptylog&ac=video_display&vid=6904596,8146331,8540285,8058853,8302067," +
                "8563198,7399495,8794641&source=搜索&client_module=框搜&suid=&netop=联通&city=昌吉回族自治州&channel=huawei&lon" +
                "=86.931952&diu3=219d3602285649af9adc38b3fde80b4a&uuid=f3ebb99e22e1b526d9f57fb4eca386af&diu2" +
                "=24df6a75cfae&div=5.7.1&diu=866657026905927&sdkversion=5.0.2&province=新疆维吾尔自治区&startid=33696073" +
                "&device_s=hwALE-H&client=2&model=ALE-UL00&abtag=B&lat=44.192861&height=1184&ver=v2&package=com" +
                ".bokecc.dance&stepid=3&version=5.7.1&manufacture=HUAWEI&xinge=Amr3RipxUW" +
                "-RAo00ER0Xi7jl8QoDRcOxO6Qgben41koq&nettype=WIFI&width=720&time=1485273718864&device=ALE-UL00-Android" +
                ":5.0.2&dic=huawei&hash=ee347f548786e55841c3d25294c1fa44 HTTP/1.1";

        String regEx = "(GET|POST)\\s(/.*)\\?(.*)\\s(HTTP/1\\.1)";
        Pattern pattern = Pattern.compile(regEx);

        Matcher matcher = pattern.matcher(str2);

        System.out.println(matcher.find());
        System.out.println(matcher.matches());
        System.out.println(matcher.groupCount());
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
        System.out.println(matcher.group(3));
        System.out.println(matcher.group(4));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        System.out.println(today);

        long hoho = System.currentTimeMillis() - threeDay;
        System.out.println(hoho);
        long hoho2 = System.currentTimeMillis() - fourDay;
        System.out.println(hoho2);
    }
}
