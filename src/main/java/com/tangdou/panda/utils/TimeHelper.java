package com.tangdou.panda.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class TimeHelper {

    // 分钟
    private static final long MINUTES_EXPIRE_MILLISECOND = (long) 1000 * 60;
    // 一小时
    private static final long HOUR_EXPIRE_MILLISECOND = (long) 1000 * 60 * 60;
    // 天
    private static final long DAY_EXPIRE_MILLISECOND = HOUR_EXPIRE_MILLISECOND * 24;
    // 周
    private static final long WEEK_EXPIRE_MILLISECOND = HOUR_EXPIRE_MILLISECOND * 24 * 7;

    /**
     * 根据时间获取编号
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static long GetIDByDate(Date dateTime) throws ParseException {
        long sortID = 0;
        Date BaseDate = new SimpleDateFormat("yyyy-mm-dd").parse("2010-1-1");
        sortID = (long) ((dateTime.getTime() - BaseDate.getTime()) / 1000);
        return sortID;

    }

    /**
     * 根据编号获取时间
     *
     * @param DateID
     * @return
     * @throws ParseException
     */
    public static Date GetDateByID(long DateID) throws ParseException {
        Date BaseDate = new SimpleDateFormat("yyyy-mm-dd").parse("2010-1-1");
        BaseDate.setTime(BaseDate.getTime() + DateID * 1000);
        Date SortDate = BaseDate;
        return SortDate;
    }

    /**
     * 时间添分钟数
     *
     * @param StartDate
     * @param intMinutes
     */
    public static Date DateAddMinutes(Date StartDate, int intMinutes) {
        Date LastDate = StartDate;
        try {
            LastDate = new Date(StartDate.getTime() + (MINUTES_EXPIRE_MILLISECOND * intMinutes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LastDate;
    }

    /**
     * 时间添加小时数
     *
     * @param StartDate
     * @param intHour
     */
    public static Date DateAddHour(Date StartDate, int intHour) {
        Date LastDate = StartDate;
        try {
            LastDate = new Date(StartDate.getTime() + (HOUR_EXPIRE_MILLISECOND * intHour));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LastDate;
    }

    /**
     * 时间添加天数
     *
     * @param StartDate
     * @param intDay
     */
    public static Date DateAddDay(Date StartDate, int intDay) {
        Date LastDate = StartDate;
        try {
            LastDate = new Date(StartDate.getTime() + (DAY_EXPIRE_MILLISECOND * intDay));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LastDate;
    }

    public static long GetCurrTimeID() throws ParseException {
        return GetIDByDate(new Date());
    }

    /**
     * 时间添加礼拜数
     *
     * @param StartDate
     * @param intWeek
     */
    public static Date DateAddWeek(Date StartDate, int intWeek) {
        Date LastDate = StartDate;
        try {
            LastDate = new Date(StartDate.getTime() + (WEEK_EXPIRE_MILLISECOND * intWeek));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LastDate;
    }

    public static String ConvertHour2Day(int intHour) {
        String str = "";
        if (intHour >= 24) {
            str = String.valueOf(intHour / 24) + "天";
        }
        if ((intHour % 24 != 0) && (intHour != 0)) {
            str += String.valueOf(intHour % 24) + "小时";
        }
        return str;
    }

    /**
     * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制)<br>
     * 如Sat May 11 17:24:21 CST 2002 to '2002-05-11 17:24:21'<br>
     *
     * @param time Date 日期<br>
     * @return String 字符串<br>
     */

    public static String DateTo24HourString(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss a'(12小时制)<br>
     * 如Sat May 11 17:23:22 CST 2002 to '2002-05-11 05:23:22 下午'<br>
     *
     * @param time Date 日期<br>
     * @return String 字符串<br>
     */
    public static String DateTo12String(Date time) {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     * 得到本月的第一天
     *
     * @return
     */
    public static String getMonthFirstDay(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        return dateFormat(format, calendar.getTime());
    }

    /**
     * 得到本月的最后一天
     *
     * @return
     */
    public static String getMonthLastDay(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormat(format, calendar.getTime());
    }

    public static int monthsOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int month1 = aCalendar.get(Calendar.MONTH);
        aCalendar.setTime(oDate);
        int month2 = aCalendar.get(Calendar.MONTH);
        int res = month2 - month1;
        return res == 0 ? 1 : Math.abs(res);
    }

    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    public static int hourOfTwo(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return 0;
        }
        long from = fDate.getTime();
        long to = oDate.getTime();
        return (int) ((to - from) / (1000 * 60 * 60));
    }

    public static int daysOfTwo(String from, String to) {
        try {
            Date fDate = null;
            if (null == from || from.trim().length() <= 0) {
                fDate = new Date();
            } else {
                fDate = dateParse("yyyy-MM-dd", from);
            }

            Date oDate = null;
            if (null == to || to.trim().length() <= 0) {
                oDate = new Date();
            } else {
                oDate = dateParse("yyyy-MM-dd", to);
            }
            return daysOfTwo(fDate, oDate);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return -1;
    }

    public static int monthsOfTwo(String from, String to) {
        try {
            Date fDate = null;
            if (null == from || from.trim().length() <= 0) {
                fDate = new Date();
            } else {
                fDate = dateParse("yyyy-mm-dd", from);
            }

            Date oDate = null;
            if (null == to || to.trim().length() <= 0) {
                oDate = new Date();
            } else {
                oDate = dateParse("yyyy-mm-dd", to);
            }
            return monthsOfTwo(fDate, oDate);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return -1;
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 格式化日期
     *
     * @param pattern 'yyyy-MM-dd'
     * @param date
     * @return
     */
    public static String dateFormat(String pattern, Date date) {
        if (null == date)
            return null;

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将日期字符串重新格式化
     *
     * @param timeStr
     * @param fromPartten
     * @param toPartten
     * @return
     * @throws Exception
     */
    public static String formatTimeStr(String timeStr, String fromPartten, String toPartten) throws Exception {
        if (null == timeStr || timeStr.trim().length() <= 0)
            return timeStr;
        Date date = TimeHelper.dateParse(fromPartten, timeStr);
        timeStr = TimeHelper.dateFormat(toPartten, date);
        return timeStr;
    }

    /**
     * 解析为日期格式
     *
     * @param pattern
     * @param source
     * @return
     * @throws ParseException
     */
    public static Date dateParse(String pattern, String source) throws ParseException {
        if (null == source || source.trim().length() <= 0)
            return null;

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(source);
    }

    /**
     * 得到上周一的时间
     *
     * @param format
     * @return
     */
    public static String getLastMonday(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(7, 1);
            return getBeforeDate(sdf.format(c.getTime()), -6, format);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 得到上周日的时间
     *
     * @param format
     * @return
     */
    public static String getLastSunday(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(7, 1);
            return sdf.format(c.getTime());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * @param date
     * @param amount
     * @param format
     * @return
     */
    public static String getBeforeDate(String date, int amount, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(date));
            c.add(5, amount);
            date = sdf.format(c.getTime());
        } catch (ParseException parseexception) {
        }
        return date;
    }

    /**
     * 得到上月第一天
     *
     * @param format
     * @return
     */
    public static String getLastMonth1(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(5, 1);
        c.set(2, c.get(2) - 1);
        return sdf.format(c.getTime());
    }

    /**
     * 得到上月最后一天
     *
     * @param format
     * @return
     */
    public static String getLastMonth30(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(2, c.get(2) - 1);
        c.set(5, c.getMaximum(5));
        return sdf.format(c.getTime());
    }

    /**
     * 判断两个时间是否相等
     *
     * @param day1
     * @param day2
     * @return
     */
    public boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean beforeDate(Date day1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = df.parse("2016-03-09");
            String dateStr = df.format(day1);
            Date date2 = df.parse(dateStr);
            if (date2.before(date1)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean beforeDate2(Date day1) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = df.parse("2016-03-01");
            String dateStr = df.format(day1);
            Date date2 = df.parse(dateStr);
            if (date2.before(date1)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
    /**
     * 获取前一天
     *
     * @param specifiedDay
     * @return
     */
    /*
     * public static String getSpecifiedDayBefore(String specifiedDay) { Calendar c
     * = Calendar.getInstance(); Date date = null; try { date = new
     * SimpleDateFormat("yy-MM-dd").parse(specifiedDay); } catch (ParseException e)
     * { e.printStackTrace(); } c.setTime(date); int day = c.get(Calendar.DATE);
     * c.set(Calendar.DATE, day - 1);
     *
     * String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c .getTime());
     * return dayBefore; }
     */

    /**
     * 获取某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 获取某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(int year, int week) {
        if (getYearWeek(year)) {
            week = week - 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);
        return getFirstDayOfWeekStr(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getLastDayOfWeek(int year, int week) {
        if (getYearWeek(year)) {
            week = week - 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);
        return getLastDayOfWeekStr(cal.getTime());
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfWeekStr(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfWeek = sdf.format(c.getTime());
        return firstDayOfWeek;
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfWeekStr(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfWeek = sdf.format(c.getTime());
        return lastDayOfWeek;
    }

    public static boolean getYearWeek(int year) {
        try {
            String pTime = year + "-01-01";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(pTime));
            int dayForWeek = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            if (dayForWeek < 5) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekOfYear(c.getTime());
    }

    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前时间的前一天时间
     *
     * @return
     */
    public static String getBeforeDay() {
        Calendar cl = Calendar.getInstance();
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day - 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataStr = sdf.format(cl.getTime());
        return dataStr;
    }

    /**
     * 获取当前时间的前一天时间
     *
     * @return
     */
    public static String getBeforeDay(String pattern) {
        Calendar cl = Calendar.getInstance();
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day - 1);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dataStr = sdf.format(cl.getTime());
        return dataStr;
    }

    /**
     * 获取当前时间的后一天时间
     *
     * @return
     */
    public static String getAfterDay() {
        Calendar cl = Calendar.getInstance();
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day + 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataStr = sdf.format(cl.getTime());
        return dataStr;
    }

    public static String beforeOneHourToNowDate(String dateHour) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyy-MM-dd HH").parse(dateHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 1);
        String dayHourBefore = new SimpleDateFormat("yyyy-MM-dd HH").format(c.getTime());
        return dayHourBefore;
    }

    public static String getCurrentTimestampForString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentTimestampForString(String pattern) {
        return new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime());
    }

    public static Timestamp getCurrentTimestamp() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Timestamp time = Timestamp.valueOf(date);
        return time;
    }

    public static String defaultFormatDate(Timestamp dt) {
        String newDate = "";
        if (dt == null) {
            return newDate;
        } else {
            try {
                SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                newDate = dateStyle.format(dt);
            } finally {
                return newDate;
            }
        }
    }

    public static String formatDate(Timestamp dt, String formatOption) {
        String newDate = "";
        if (dt == null) {
            return newDate;
        } else {
            if (formatOption == null)
                formatOption = "yyyy-MM-dd";
            try {
                SimpleDateFormat dateStyle = new SimpleDateFormat(formatOption);
                newDate = dateStyle.format(dt);
            } finally {
                return newDate;
            }
        }
    }

    /**
     * 获取指定日期区间的日期
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getDatesBetweenTwoDays(Date startDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        lDate.add(startDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (true) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (endDate.after(calendar.getTime())) {
                lDate.add(calendar.getTime());
            } else {
                break;
            }
        }

        if (endDate.compareTo(startDate) > 0) {
            lDate.add(endDate);
        }

        return lDate;
    }

    public static String[] getTimePiece(int day) {
        int len = day * 24;
        String[] arr = new String[len];
        for (int i = 0; i < len; i++) {
            Date date = TimeHelper.DateAddHour(new Date(), -i);
            long piece = getTimePiece(date);
            arr[i] = "" + piece;
        }
        return arr;
    }

    static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static long getTimePiece(Date date) {
        long timestamp;
        try {
            timestamp = date.getTime();
        } catch (Exception e) {
            // TODO log
            timestamp = Instant.now().toEpochMilli();
        }
        return ((timestamp % 259200000) / 3600000);
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (null == nowTime || null == startTime || null == endTime)
            return false;
        long nowTimestamp = nowTime.getTime();
        long startTimestamp = startTime.getTime();
        long endTimestamp = endTime.getTime();
        if (endTimestamp < startTimestamp)
            return false;
        if (startTimestamp > nowTimestamp || nowTimestamp > endTimestamp)
            return false;
        return true;

    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String dateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(dateStr);
        return date.getTime();
    }

    public static void main(String[] arg) {
//		try {
//			String nowTimeString = "2018-04-26 14:29:59";
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        Date nowTime = sdf.parse(nowTimeString);
//	        
//	        String startTimeString = "2018-04-26 14:30:00";
//	        Date startTime = sdf.parse(startTimeString);
//	        
//	        String endTimeString = "2018-04-26 14:31:00";
//	        Date endTime = sdf.parse(endTimeString);
//	        
//	        System.out.println(isEffectiveDate(nowTime, startTime, endTime));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        System.err.println(new Date().getTime() / 1000);
    }
}
