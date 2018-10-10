package com.tangdou.panda.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;

/**
 * Created by jiangtao on 2017/7/10.
 */
public class LogDecoder {


    //将传入的ISO8601 时间字符串，转成成指定格式的Local时区字(UTC+8)符串
    public static String isoStr2utc8Str(String isoStr) throws ParseException {

        DateTimeFormatter isoformat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime dt = isoformat.parseDateTime(isoStr);

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return dt.toString(format);
    }

}
