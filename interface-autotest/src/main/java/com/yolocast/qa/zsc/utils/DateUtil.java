package com.yolocast.qa.zsc.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangshichao
 * @ClassName DateUtil
 * @Description 日期工具类
 * @Date 2022-04-24
 */
public class DateUtil {

    // ==格式到年==
    /**
     * 日期格式，年份，例如：2004，2008
     */
    public static final String DATE_FORMAT_YYYY = "yyyy";


    // ==格式到年月 ==
    /**
     * 日期格式，年份和月份，例如：200707，200808
     */
    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

    /**
     * 日期格式，年份和月份，例如：200707，2008-08
     */
    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";


    // ==格式到年月日==
    /**
     * 日期格式，年月日，例如：050630，080808
     */
    public static final String DATE_FORMAT_YYMMDD = "yyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
     */
    public static final String DATE_FORMAT_YY_MM_DD = "yy-MM-dd";

    /**
     * 日期格式，年月日，例如：20050630，20080808
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式，年月日，例如：2016.10.05
     */
    public static final String DATE_FORMAT_POINTYYYYMMDD = "yyyy.MM.dd";

    /**
     * 日期格式，年月日，例如：2016年10月05日
     */
    public static final String DATE_TIME_FORMAT_YYYY年MM月DD日 = "yyyy年MM月dd日";


    // ==格式到年月日 时分 ==

    /**
     * 日期格式，年月日时分，例如：200506301210，200808081210
     */
    public static final String DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm";

    /**
     * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

    /**
     * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";


    // ==格式到年月日 时分秒==
    /**
     * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
     * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";


    // ==格式到年月日 时分秒 毫秒==
    /**
     * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";


    // ==特殊格式==
    /**
     * 日期格式，月日时分，例如：10-05 12:00
     */
    public static final String DATE_FORMAT_MMDDHHMI = "MM-dd HH:mm";

    /**
     * 日期格式，月日，例如：06月25号
     */
    public static final String DATE_FORMAT_SIMPLE = "MM月dd日";


    /* ************工具方法***************   */

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
     * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     *
     */
    public static String getSysdateStr() {
        Date sysdate = new Date();
        return String.format("%tF %tT", sysdate, sysdate);
    }


    /**
     * 返回日期yyyymmdd样式
     * @param n 前/后n天
     */
    public static String getYMD(int n) {
        return LocalDateFormatStr(getPreviousDate(n), DATE_FORMAT_YYYYMMDD);//yyyymmdd
    }

    /**
     * 获取今日时间戳 00点00分00秒开始
     *
     * @return
     */
    public static long getTodayCurrent() {
        long current = System.currentTimeMillis();
        //return current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        return current;
    }


    /**
     * 获取某日期的年份
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取某日期的月份
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某日期的日数
     *
     * @param date
     * @return
     */
    public static Integer getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);//获取日
        return day;
    }

    /**
     * 格式化Date时间
     *
     * @param time       Date类型时间
     * @param timeFormat String类型格式
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFormat) {
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        return dateFormat.format(time);
    }

    /**
     * 格式化Timestamp时间
     *
     * @param timestamp  Timestamp类型时间
     * @param timeFormat
     * @return 格式化后的字符串
     */
    public static String parseTimestampToStr(Timestamp timestamp, String timeFormat) {
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        return df.format(timestamp);
    }

    /**
     * 格式化Date时间
     *
     * @param time         Date类型时间
     * @param timeFormat   String类型格式
     * @param defaultValue 默认值为当前时间Date
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFormat, final Date defaultValue) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFormat);
            return dateFormat.format(time);
        } catch (Exception e) {
            if (defaultValue != null)
                return parseDateToStr(defaultValue, timeFormat);
            else
                return parseDateToStr(new Date(), timeFormat);
        }
    }

    /**
     * 格式化Date时间
     *
     * @param time         Date类型时间
     * @param timeFormat   String类型格式
     * @param defaultValue 默认时间值String类型
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFormat, final String defaultValue) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFormat);
            return dateFormat.format(time);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 格式化String时间
     *
     * @param time       String类型时间
     * @param timeFormat String类型格式
     * @return 格式化后的Date日期
     */
    public static Date parseStrToDate(String time, String timeFormat) {
        if (time == null || time.equals("")) {
            return null;
        }

        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFormat);
            date = dateFormat.parse(time);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 格式化String时间
     *
     * @param strTime      String类型时间
     * @param timeFormat   String类型格式
     * @param defaultValue 异常时返回的默认值
     * @return
     */
    public static Date parseStrToDate(String strTime, String timeFormat,
                                      Date defaultValue) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFormat);
            return dateFormat.parse(strTime);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 当strTime为2008-9时返回为2008-9-1 00:00格式日期时间，无法转换返回null.
     *
     * @param strTime
     * @return
     */
    public static Date strToDate(String strTime) {
        if (strTime == null || strTime.trim().length() <= 0)
            return null;

        Date date = null;
        List<String> list = new ArrayList<String>(0);

        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
        list.add(DATE_FORMAT_YYYY_MM_DD);
        //list.add(DATE_FORMAT_YY_MM_DD);
        list.add(DATE_FORMAT_YYYYMMDD);
        list.add(DATE_FORMAT_YYYY_MM);
        list.add(DATE_FORMAT_YYYYMM);
        list.add(DATE_FORMAT_YYYY);


        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            String format = (String) iter.next();
            if (strTime.indexOf("-") > 0 && format.indexOf("-") < 0)
                continue;
            if (strTime.indexOf("-") < 0 && format.indexOf("-") > 0)
                continue;
            if (strTime.length() > format.length())
                continue;
            date = parseStrToDate(strTime, format);
            if (date != null)
                break;
        }

        return date;
    }

    /**
     * 解析两个日期之间的所有月份
     *
     * @param beginDateStr 开始日期，至少精确到yyyy-MM
     * @param endDateStr   结束日期，至少精确到yyyy-MM
     * @return yyyy-MM日期集合
     */
    public static List<String> getMonthListOfDate(String beginDateStr, String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        // 返回的月份列表
        String sRet = "";

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        GregorianCalendar beginGC = null;
        GregorianCalendar endGC = null;
        List<String> list = new ArrayList<String>();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = new GregorianCalendar();
            beginGC.setTime(beginDate);

            endGC = new GregorianCalendar();
            endGC.setTime(endDate);

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
                sRet = beginGC.get(Calendar.YEAR) + "-"
                        + (beginGC.get(Calendar.MONTH) + 1);
                list.add(sRet);
                // 以月为单位，增加时间
                beginGC.add(Calendar.MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析两个日期段之间的所有日期
     *
     * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
     * @param endDateStr   结束日期  ，至少精确到yyyy-MM-dd
     * @return yyyy-MM-dd日期集合
     */
    public static List<String> getDayListOfDate(String beginDateStr, String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        Calendar beginGC = null;
        Calendar endGC = null;
        List<String> list = new ArrayList<String>();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

                list.add(sdf.format(beginGC.getTime()));
                // 以日为单位，增加时间
                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当下年份指定前后数量的年份集合
     *
     * @param before 当下年份前年数
     * @param behind 当下年份后年数
     * @return 集合
     */
    public static List<Integer> getYearListOfYears(int before, int behind) {
        if (before < 0 || behind < 0) {
            return null;
        }
        List<Integer> list = new ArrayList<Integer>();
        Calendar c = null;
        c = Calendar.getInstance();
        c.setTime(new Date());
        int currYear = Calendar.getInstance().get(Calendar.YEAR);

        int startYear = currYear - before;
        int endYear = currYear + behind;
        for (int i = startYear; i < endYear; i++) {
            list.add(Integer.valueOf(i));
        }
        return list;
    }

    /**
     * 获取当前日期是一年中第几周
     *
     * @param date
     * @return
     */
    public static Integer getWeekthOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取某一年各星期的始终时间
     * 实例：getWeekList(2016)，第52周(从2016-12-26至2017-01-01)
     *
     * @param year 年份
     * @return
     */
    public static HashMap<Integer, String> getWeekTimeOfYear(int year) {
        HashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekthOfYear(c.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dayOfWeekStart = "";
        String dayOfWeekEnd = "";
        for (int i = 1; i <= count; i++) {
            dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));
            dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));
            map.put(Integer.valueOf(i), "第" + i + "周(从" + dayOfWeekStart + "至" + dayOfWeekEnd + ")");
        }
        return map;

    }

    /**
     * 获取某一年的总周数
     *
     * @param year
     * @return
     */
    public static Integer getWeekCountOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekthOfYear(c.getTime());
        return count;
    }

    /**
     * 获取指定日期所在周的第一天
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
     * 获取指定日期所在周的最后一天
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
     * 获取某年某周的第一天
     *
     * @param year 目标年份
     * @param week 目标周数
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年某周的最后一天
     *
     * @param year 目标年份
     * @param week 目标周数
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        int day = c.getActualMinimum(c.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        int day = c.getActualMaximum(c.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取某个日期为星期几
     *
     * @param date
     * @return String "星期*"
     */
    public static String getDayWeekOfDate1(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 获得指定日期的星期几数
     *
     * @param date
     * @return int
     */
    public static Integer getDayWeekOfDate2(Date date) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int weekDay = aCalendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }

    /**
     * 验证字符串是否为日期
     * 验证格式:YYYYMMDD、YYYY_MM_DD、YYYYMMDDHHMISS、YYYYMMDD_HH_MI、YYYY_MM_DD_HH_MI、YYYYMMDDHHMISSSSS、YYYY_MM_DD_HH_MI_SS
     *
     * @param strTime
     * @return null时返回false;true为日期，false不为日期
     */
    public static boolean validateIsDate(String strTime) {
        if (strTime == null || strTime.trim().length() <= 0)
            return false;

        Date date = null;
        List<String> list = new ArrayList<String>(0);

        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
        list.add(DATE_FORMAT_YYYY_MM_DD);
        //list.add(DATE_FORMAT_YY_MM_DD);
        list.add(DATE_FORMAT_YYYYMMDD);
        //list.add(DATE_FORMAT_YYYY_MM);
        //list.add(DATE_FORMAT_YYYYMM);
        //list.add(DATE_FORMAT_YYYY);

        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            String format = (String) iter.next();
            if (strTime.indexOf("-") > 0 && format.indexOf("-") < 0)
                continue;
            if (strTime.indexOf("-") < 0 && format.indexOf("-") > 0)
                continue;
            if (strTime.length() > format.length())
                continue;
            date = parseStrToDate(strTime.trim(), format);
            if (date != null)
                break;
        }

        if (date != null) {
            System.out.println("生成的日期:" + DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS, "--null--"));
            return true;
        }
        return false;
    }

    /**
     * 将指定日期的时分秒格式为零
     *
     * @param date
     * @return
     */
    public static Date formatHhMmSsOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得指定时间加减参数后的日期(不计算则输入0)
     *
     * @param date        指定日期
     * @param year        年数，可正可负
     * @param month       月数，可正可负
     * @param day         天数，可正可负
     * @param hour        小时数，可正可负
     * @param minute      分钟数，可正可负
     * @param second      秒数，可正可负
     * @param millisecond 毫秒数，可正可负
     * @return 计算后的日期
     */
    public static Date addDate(Date date, int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);//加减年数
        c.add(Calendar.MONTH, month);//加减月数
        c.add(Calendar.DATE, day);//加减天数
        c.add(Calendar.HOUR, hour);//加减小时数
        c.add(Calendar.MINUTE, minute);//加减分钟数
        c.add(Calendar.SECOND, second);//加减秒
        c.add(Calendar.MILLISECOND, millisecond);//加减毫秒数

        return c.getTime();
    }

    /**
     * 获得两个日期的时间戳之差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDistanceTimestamp(Date startDate, Date endDate) {
        long daysBetween = (endDate.getTime() - startDate.getTime() + 1000000) / (3600 * 24 * 1000);
        return daysBetween;
    }

    /**
     * 判断二个时间是否为同年同月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean compareIsSameMonth(Date date1, Date date2) {
        boolean flag = false;
        int year1 = getYear(date1);
        int year2 = getYear(date2);
        if (year1 == year2) {
            int month1 = getMonth(date1);
            int month2 = getMonth(date2);
            if (month1 == month2) flag = true;
        }
        return flag;
    }

    /**
     * 获得两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param one 时间参数 1 格式：1990-01-01 12:00:00
     * @param two 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTime(Date one, Date two) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }


    /**
     * 时间差计算（秒、分钟、小时、天数、月份、年）
     * @author zhangsc
     * @date 2022/7/13 下午12:20
     * @param startDate
     * @param endDate
     * @return java.lang.String
     */
    public static String convert(Date startDate,Date endDate) {
        long startTime = startDate.getTime();//获取毫秒数
        long endTime = endDate.getTime();	 //获取毫秒数
        long timeDifference = endTime-startTime;
        long second = timeDifference/1000;	//计算秒
        return Long.toString(second);

    }


    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static Long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date getDayBeginTime(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date getDayEndTime(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取几天前的时间戳
     *
     * @return
     */
    public static Date getSubDaysTime(int days) {
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, -days);
        return c.getTime();
    }

    /**
     * 获取几天前的时间戳
     *
     * @return
     */
    public static Date getSubDaysTime(double days) {
        Calendar c = Calendar.getInstance();
        int day = (int) days;
        double hours = (days - (int) days) * 24;
        //过去七天
        c.setTime(new Date());
        if (day > 0) {
            c.add(Calendar.DATE, -day);
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, -(int) hours);
        }
        return c.getTime();
    }

    /**
     * 获取几个月前的时间戳
     *
     * @param months
     * @return
     */
    public static Date getSubMonthsTime(int months) {
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.MONTH, -months);
        return c.getTime();
    }

    /**
     * 获得当前日期的前/后n天（不含时间）
     * 2016-04-18
     */
    public static LocalDate getPreviousDate(long n) {
        LocalDate today = LocalDate.now();
        return today.plusDays(n);
    }

    /**
     * 获得当前日期的前/后n天（不含时间）
     * 2016-04-18
     */
    public static LocalDateTime getPreviousDateTime(long n) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusDays(n);
    }

    /**
     * 获得当前日期的前/后7天（不含时间）
     *
     * @param n 前7天n=-1，后七天n=1
     */
    public static LocalDate getPreviousWeekDate(long n) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(n, ChronoUnit.WEEKS);
        return nextWeek;
    }

    /**
     * 获得当前日期的前/后一月（不含时间）
     *
     * @param n 前一月n=-1，后一月n=1
     */
    public static LocalDate getPreviousMonthDate(long n) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(n, ChronoUnit.MONTHS);
        return nextWeek;
    }

    /**
     * 获得当前日期的前/后一年（不含时间）
     *
     * @param n 前一月n=-1，后一月n=1
     */
    public static LocalDate getPreviousYearDate(long n) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(n, ChronoUnit.YEARS);
        return nextWeek;
    }

    //自定义时间
    public static LocalDateTime SetLocalDateTime(LocalDate day, int hour, int minute, int second) {
        LocalDateTime endTime = LocalDateTime.of(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), hour, minute, second);
        return endTime;

    }

    public static long TimeToTimeStamp(LocalDate day, int hour, int minute, int second, boolean flag) {
        LocalDateTime localDateTime = SetLocalDateTime(day, hour, minute, second);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        if (flag) {
            return (instant.getEpochSecond());  // 秒级时间戳
        } else {
            return (instant.toEpochMilli());    // 毫秒级时间戳
        }
    }

    /**
     * 获取该天0点时间戳
     *
     * @param day  日期年月日
     * @param hour 具体小时
     * @param flag 是否返回秒级时间戳
     */
    public static long dateTimeStamp(LocalDate day, int hour, boolean flag) {
        return (LocalDateToTimeStamp(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), hour, flag));

    }


    public static long LocalDateToTimeStamp(int year, int month, int dayOfMonth, int hour, boolean flag) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        if (flag) {
            return (instant.getEpochSecond());  // 秒级时间戳
        } else {
            return (instant.toEpochMilli());    // 毫秒级时间戳
        }
    }

    public static String LocalDateFormatStr(LocalDate date, String format) {
        String str = "";
        if (date != null) {
            str = date.format(DateTimeFormatter.ofPattern(format));
        }
        return str;
    }

    public static LocalDateTime TimestampToLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDate TimestampToLocalDate(long timestamp, String format) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDate localDate = instant.atZone(ZoneOffset.ofHours(8)).toLocalDate();
        return localDate;
    }

    public static boolean CompareTimestampAll(long timestamp1, long timestamp2, long timestamp3) {
        LocalDateTime firstDate = TimestampToLocalDateTime(timestamp1);
        LocalDateTime secondDate = TimestampToLocalDateTime(timestamp2);
        LocalDateTime Date = TimestampToLocalDateTime(timestamp3);
        return Date.isBefore(secondDate) && (Date.isAfter(firstDate) || Date.isEqual(firstDate));
    }


    public static LocalDate StrFormatLocalDate(String str, String format) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(str, fmt);
        return date;
    }

    /**
     * 比较第一个日期是否小于第二个日期
     *
     * @param time1 第一个日期
     * @param time2 第二个日期
     * @return true-小于;false-大于
     */
    public static boolean CompareLocalDate(String time1, String time2, String format) {
        LocalDate firstDate = StrFormatLocalDate(time1, format);
        LocalDate secondDate = StrFormatLocalDate(time2, format);
        return firstDate.isBefore(secondDate);
    }

    /**
     * 比较第一，二个日期间隔天数
     *
     * @param time1 第一个日期
     * @param time2 第二个日期
     * @return
     */
    public static int periodDay(String time1, String time2, String format) {
        LocalDate firstDate = StrFormatLocalDate(time1, format);
        LocalDate secondDate = StrFormatLocalDate(time2, format);
        Period next = Period.between(firstDate, secondDate);
        return next.getDays();
    }

    /**
     * 比较第三个日期是否在第一，二个日期间
     *
     * @param time1 第一个日期
     * @param time2 第二个日期
     * @return true-小于;false-大于
     */
    public static boolean CompareLocalDateAll(String time1, String time2, String time3, String format) {
        LocalDate firstDate = StrFormatLocalDate(time1, format);
        LocalDate secondDate = StrFormatLocalDate(time2, format);
        LocalDate Date = StrFormatLocalDate(time3, format);
        return Date.isBefore(secondDate) && (Date.isAfter(firstDate) || Date.isEqual(firstDate));
    }

    /**
     * 获取日期的上周首日和末日
     *
     * @param n       日期（前/后n天）
     * @param isFirst true为首日，false为末日

     */
    public static LocalDate getStartOrEndDayOfWeek(int n, Boolean isFirst) {
        LocalDate resDate = LocalDate.now().minusWeeks(n * 1l);
        DayOfWeek week = resDate.getDayOfWeek();
        int value = week.getValue();
        if (isFirst) {
            resDate = resDate.minusDays(value - 1);
        } else {
            resDate = resDate.plusDays(7 - value);
        }
        return resDate;
    }


    /**
     * 获取日期的上月首日和末日
     *
     * @param n       日期（前/后n月）
     * @param isFirst true为首日，false为末日
     */
    public static LocalDate getStartOrEndDayOfMonth(int n, Boolean isFirst) {
        LocalDate resDate = LocalDate.now().minusMonths(n * 1l);
        Month month = resDate.getMonth();
        int length = month.length(resDate.isLeapYear());
        if (isFirst) {
            resDate = LocalDate.of(resDate.getYear(), month, 1);
        } else {
            resDate = LocalDate.of(resDate.getYear(), month, length);
        }
        return resDate;
    }


    /**
     * 获取日期的上季度首日和末日
     *
     * @param n       日期（前/后n月）
     * @param isFirst true为首日，false为末日
     */
    public static LocalDate getStartOrEndDayOfQuarter(int n, Boolean isFirst) {
        LocalDate resDate = LocalDate.now().minusMonths(n * 3l);

        Month month = resDate.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        int year = resDate.getYear();
//        if (firstMonthOfQuarter.getValue() == 10) {
//            year = year - 1;
//        }
        if (isFirst) {
            resDate = LocalDate.of(year, firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(year, endMonthOfQuarter, endMonthOfQuarter.length(resDate.isLeapYear()));
        }
        return resDate;
    }

    /**
     * 获取日期的上年首日和末日
     *
     * @param n       日期（前/后n年）
     * @param isFirst true为首日，false为末日
     */
    public static LocalDate getStartOrEndDayOfYear(int n, Boolean isFirst) {
        LocalDate resDate = LocalDate.now().minusYears(n * 1l);

        if (isFirst) {
            resDate = LocalDate.of(resDate.getYear(), Month.JANUARY, 1);
        } else {
            resDate = LocalDate.of(resDate.getYear(), Month.DECEMBER, Month.DECEMBER.length(resDate.isLeapYear()));
        }
        return resDate;
    }


    /**
     * 获取当前时间
     */
    public static String getDateNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        return simpleDateFormat.format(new Date());
    }

    /**
     * @param num 前num天
     *            获取当前时间-num
     */
    public static String getDatePast(int num) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -num);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前小时
     */
    public static int getHourNow() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当天凌晨时间戳
     */
    public static long getTimestampToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = getDateNow();
        try {
            return simpleDateFormat.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }

    /**
     * 获取前num天当天凌晨时间戳
     *
     * @param num 前num天
     * @return
     */
    public static long getTimestampPast(int num) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = getDatePast(num);
        try {
            return simpleDateFormat.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() - num * 60 * 60 * 24 * 1000;
        }
    }

    /**
     * 将int类型数字转换成时分秒毫秒的格式数据
     *
     * @param time long类型的数据
     * @return HH:mm:ss.SSS
     * @author zero 2019/04/11
     */
    public static String msecToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;
        if (time <= 0) {
            return "00:00:00.000";
        } else {
            second = time / 1000;
            minute = second / 60;
            millisecond = time % 1000;
            if (second < 60) {
                timeStr = "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else if (minute < 60) {
                second = second % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else {// 数字>=3600 000的时候
                hour = minute / 60;
                minute = minute % 60;
                second = second - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "."
                        + unitFormat2(millisecond);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {// 时分秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    public static String unitFormat2(int i) {// 毫秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "00" + Integer.toString(i);
        } else if (i >= 10 && i < 100) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    public static void main(String[] args) {
        try {
            Date date = DateUtil.getSubDaysTime(1.5);
            System.out.println(System.currentTimeMillis());
            System.out.println(getPreviousWeekDate(-1));
            System.out.println(date.getTime());
            System.out.println(getPreviousDateTime(0));
            LocalDateTime time = SetLocalDateTime(LocalDate.now(), 16, 59, 40);
            System.out.println(time);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
