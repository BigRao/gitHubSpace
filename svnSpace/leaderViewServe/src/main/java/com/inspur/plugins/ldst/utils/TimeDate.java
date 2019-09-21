package com.inspur.plugins.ldst.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeDate {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置时间格式1
    private final Calendar cal = Calendar.getInstance();

    public String monday(String time) throws ParseException {

        Date date = sdf.parse(time);
        getDay(sdf, date);


        return sdf.format(cal.getTime());
    }

    public String sunday(String time) throws ParseException {
        //设置时间格式

        Date date = sdf.parse(time);
        getDay(sdf, date);
        cal.add(Calendar.DATE, 6);
        sdf.format(cal.getTime());

        return sdf.format(cal.getTime());
    }

    public List<String> getBetweenDate(String begin, String end) throws ParseException {
        List<String> betweenList = new ArrayList<>();
        Date startDate = sdf.parse(begin);
        Date endDate = sdf.parse(end);
        cal.setTime(startDate);
        while (cal.getTimeInMillis() <= endDate.getTime()) {
            String str = sdf.format(cal.getTime());
            betweenList.add(str);
            cal.add(Calendar.DAY_OF_MONTH, 1);//进行当前日期月份加1
        }

        return betweenList;
    }

    private void getDay(SimpleDateFormat sdf, Date date) {
        cal.setTime(date);
        sdf.format(cal.getTime());//输出要计算日期
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        sdf.format(cal.getTime());
    }
}
