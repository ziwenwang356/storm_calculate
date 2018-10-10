package com.tangdou.panda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Created by jiangtao on 2017/7/18.
 */
public class TimeUtils {
    private static final ZoneId TIMEZONE = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getDate(String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
            return dateTime.toLocalDate().format(DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // TODO log
            return ZonedDateTime.now(TIMEZONE).toLocalDate().format(DATE_FORMATTER);
        }
    }

    static long getTimeIndex(String timestamp, int interval) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nowTime = null != date ? date.getTime() : System.currentTimeMillis();
        format = new SimpleDateFormat("yyyy-MM-dd");
        String daybegin = format.format(nowTime);
        try {
            long offset = format.parse(daybegin).getTime();
            return (nowTime - offset) / interval;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static long getTimePiece(String timeStr) {
        long timestamp;
        try {
            timestamp = TIMESTAMP_FORMAT.parse(timeStr).getTime();
        } catch (ParseException | RuntimeException e) {
            // TODO log
            timestamp = Instant.now().toEpochMilli();
        }
        return ((timestamp % 259200000) / 3600000);
    }
}
