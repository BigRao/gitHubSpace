package com.inspur.bjzx.city.util;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateStringUtil {


    public static String getTimeRange(String timeDim, String searchTime, int timeRange) {

        timeDim = timeDim.trim().toUpperCase();
        Date date = getDate(searchTime);

        if ("YEAR".equals(timeDim)) {
            return calcTimeYear(date, timeRange);
        } else if ("MONTH".equals(timeDim)) {
            return calcTimeMonth(date, timeRange);
        } else if ("WEEK".equals(timeDim)) {
            return calcTimeWeek(date, timeRange);
        } else {
            return calcTimeDay(date, timeRange);
        }
    }


    private static Date getDate(String strDate) {
        if (strDate == null || "".equals(strDate.trim())) return new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate = null;
        try {
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strtodate;
    }

    private static String calcTimeYear(Date date, int timeRange) {
        String startTime;
        String endTime;

        DateTime endDateTime = new DateTime(date);
        DateTime startDateTime = endDateTime.minusYears(timeRange);

        startTime = startDateTime.toString("yyyy-01-01 00:00:00");
        endTime = endDateTime.toString("yyyy-12-31 23:59:59");
        return startTime + ";" + endTime;

    }

    private static String calcTimeMonth(Date date, int timeRange) {
        String startTime;
        String endTime;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int d = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH);

        cal.add(Calendar.DAY_OF_MONTH, d);

        // 所在周结束日期
        endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());

        cal.add(Calendar.MONTH, -timeRange);
        // 所在周开始日期
        startTime = new SimpleDateFormat("yyyy-MM-01 00:00:00").format(cal.getTime());

        return startTime + ";" + endTime;
    }

    private static String calcTimeWeek(Date date, int timeRange) {

        String startTime;
        String endTime;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int d;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            d = 0;
        } else {
            d = 8 - cal.get(Calendar.DAY_OF_WEEK) ;
        }
        cal.add(Calendar.DAY_OF_WEEK, d);

        // 所在周结束日期
        endTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(cal.getTime());

        cal.add(Calendar.DAY_OF_WEEK, -6);
        cal.add(Calendar.WEEK_OF_YEAR, -timeRange);
        // 所在周开始日期
        startTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
        return startTime + ";" + endTime;

    }

    private static String calcTimeDay(Date date, int timeRange) {
        String startTime;
        String endTime;

        DateTime endDateTime = new DateTime(date);
        DateTime startDateTime = endDateTime.minusDays(timeRange);

        startTime = startDateTime.toString("yyyy-MM-dd 00:00:00");
        endTime = endDateTime.toString("yyyy-MM-dd 23:59:59");
        return startTime + ";" + endTime;
    }

    public static String convertStringDate(String timeStr) {
        System.out.println(timeStr+"|");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     //定义一个格式
        return sdf.format(timeStr);
    }
    public static void main(String[] args) {
        DateStringUtil.convertStringDate("2016-12-31 00:00:00");
        //System.out.println(DateStringUtil.getTimeRange("Week","",1));
    }
}
