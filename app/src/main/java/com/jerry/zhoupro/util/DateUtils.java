package com.jerry.zhoupro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wzl-pc on 2016/3/27.
 * DateUtil工具类：日期计算器
 */
public class DateUtils {
    /**
     * 接受yyyy年MM月dd日的日期字符串参数,返回两个日期相差的天数
     *
     * @param start 开始日期
     * @param end 结束日期
     * @return
     */
    public static long getDistDates(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date startDate;
        Date endDate;
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
            return getDistDates(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 返回两个日期相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static long getDistDates(Date startDate, Date endDate) {
        long totalDate = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long timestart = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long timeend = calendar.getTimeInMillis();
        totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
        return totalDate;
    }

    /**
     * 比较第一个日期是否在第二个日期之前
     *
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static int compareTo(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startDate;
        Date endDate;
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
            return startDate.compareTo(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
