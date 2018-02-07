/**
 * Copyright (c) 2011-2012 浙江超仪电子股分有限公司 版权所有 Ping An Insurance Company of China.
 * All rights reserved. This software is the confidential and proprietary
 * information of Ping An Insurance Company of China. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the contract agreement you
 * entered into with Ping An.
 */
package com.joymeter.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ZhongFuqiang
 */
public class MyDatetime {

    public static String FORMAT_DATE_1 = "yyyyMMdd";
    public static String FORMAT_DATE_2 = "yyyy-MM-dd";
    public static String FORMAT_DATE_3 = "MMdd";
    public static String FORMAT_DATE_4 = "yyyy年MM月dd日";

    public static String FORMAT_DATETIME_1 = "yyyyMMdd HH:mm:ss";
    public static String FORMAT_DATETIME_2 = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_DATETIME_3 = "yyyyMMddHHmmss";
    public static String FORMAT_DATETIME_4 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String FORMAT_DATETIME_401 = "yyyy-MM-dd HH:mm:ss.S";
    public static String FORMAT_DATETIME_5 = "HH:mm:ss";
    public static String FORMAT_DATETIME_6 = "yyyyMMddHHmmss";

    /**
     * 在指定时间，加上指定的小时
     *
     * @param dt
     * @param day
     * @return
     */
    public static Date addDay(Date dt, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 指定的时间，加上指定的月数
     *
     * @param dt
     * @param month return
     */
    public static Date addMonth(Date dt, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();

    }

    /**
     * 在当前时间，加上指定的小时
     *
     * @param day
     * @return
     */
    public static Date addDay(int day) {
        return MyDatetime.addDay(new Date(), day);
    }

    /**
     * 在指定时间，加上指定的小时
     *
     * @param dt
     * @param hour
     * @return
     */
    public static Date addHour(Date dt, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * 在当前时间，加上指定的小时
     *
     * @param hour
     * @return
     */
    public static Date addHour(int hour) {
        return MyDatetime.addHour(new Date(), hour);
    }

    /**
     * 在指定时间，加上指定的分钟
     *
     * @param dt
     * @param minute
     * @return
     */
    public static Date addMinute(Date dt, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 在当前时间，加上指定的分钟
     *
     * @param minute
     * @return
     */
    public static Date addMinute(int minute) {
        return MyDatetime.addMinute(new Date(), minute);
    }

    /**
     * 两个时间之间相差的天数
     *
     * @param dt1
     * @param dt2
     * @return
     * @throws java.text.ParseException
     */
    public static int getDays(Date dt1, Date dt2) throws ParseException {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        dt1 = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        dt2 = calendar2.getTime();
        long day = (dt2.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000);
        return (int) day;
    }

    /**
     * 获取几天前的日期
     *
     * @param value
     * @return
     */
    public static Date getFirstDateOfDayBefore(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 5, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        return calendar.getTime();
    }

    /**
     *
     * @param value
     * @return
     */
    public static Date getEndDateOfDayBefore(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 5, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        return calendar.getTime();
    }

    /**
     * 几天前的时间
     *
     * @param value
     * @return 时间
     */
    public static Date getDateOfBeforeDay(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 5, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        return calendar.getTime();
    }

    /**
     * 几天后的时间
     *
     * @param value
     * @return 时间
     */
    public static Date getDateOfAfterDay(int value) {
        return MyDatetime.getDateOfBeforeDay(-value);
    }

    /**
     * 获取几天前的日期
     *
     * @param dt
     * @param value
     * @return
     */
    public static Date getDateOfBeforeDay(Date dt, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        return calendar.getTime();
    }

    /**
     * 两个时间之间相差的周数
     *
     * @param dt1
     * @param dt2
     * @return
     * @throws java.text.ParseException
     */
    public static int getWeeks(Date dt1, Date dt2) throws ParseException {
        int weeks = 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        while (calendar1.before(calendar2)) {
            calendar1.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar1.get(Calendar.DAY_OF_WEEK) == calendar1.getFirstDayOfWeek()) {
                weeks++;
            }
        }
        return weeks;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Date getFirstDateOfWeek(int value) {
        value = value * 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        switch (w) {
            case Calendar.SUNDAY:
                value = 7 - 7;
                break;
            case Calendar.SATURDAY:
                value = 7 - 1;
                break;
            case Calendar.FRIDAY:
                value = 7 - 2;
                break;
            case Calendar.THURSDAY:
                value = 7 - 3;
                break;
            case Calendar.WEDNESDAY:
                value = 7 - 4;
                break;
            case Calendar.TUESDAY:
                value = 7 - 5;
                break;
            case Calendar.MONDAY:
                value = 7 - 6;
                break;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     *
     * @param dt
     * @param value
     * @return
     */
    public static Date getDateOfWeek(Date dt, int value) {
        value = value * 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        switch (w) {
            case Calendar.SUNDAY:
                value = 7 - 7;
                break;
            case Calendar.SATURDAY:
                value = 7 - 1;
                break;
            case Calendar.FRIDAY:
                value = 7 - 2;
                break;
            case Calendar.THURSDAY:
                value = 7 - 3;
                break;
            case Calendar.WEDNESDAY:
                value = 7 - 4;
                break;
            case Calendar.TUESDAY:
                value = 7 - 5;
                break;
            case Calendar.MONDAY:
                value = 7 - 6;
                break;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取一星期中的第几天
     *
     * @return
     */
    public static int getDateOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        switch (w) {
            case Calendar.SUNDAY:
                return 0X008;
            case Calendar.SATURDAY:
                return 0X010;
            case Calendar.FRIDAY:
                return 0X020;
            case Calendar.THURSDAY:
                return 0X040;
            case Calendar.WEDNESDAY:
                return 0X080;
            case Calendar.TUESDAY:
                return 0X100;
            case Calendar.MONDAY:
                return 0X200;
        }
        return 0;
    }

    /**
     * 获取一天中的上午，中午，晚上
     *
     * @return
     */
    public static int getDateOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        if (h > 0 && h <= 9) {
            return 0X004;
        }
        if (h > 9 && h <= 16) {
            return 0X002;
        }
        if (h > 16 && h <= 24) {
            return 0X001;
        }
        return 0;
    }

    /**
     * 两个时间之间相差的月数
     *
     * @param dt1
     * @param dt2
     * @return
     * @throws java.text.ParseException
     */
    public static int getMonths(Date dt1, Date dt2) throws ParseException {
        int months = 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        while (calendar1.before(calendar2)) {
            if (calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                months++;
            }
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return months;
    }

    /**
     * 几个月前的第一天
     *
     * @param value
     * @return
     */
    public static Date getFirstDateOfMonth(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 几个月前的第一天
     *
     * @param dt
     * @param value
     * @return
     */
    public static Date getDateOfMonth(Date dt, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 两个时间之间相差的季数
     *
     * @param dt1
     * @param dt2
     * @return
     * @throws java.text.ParseException
     */
    public static int getSeasons(Date dt1, Date dt2) throws ParseException {
        int seasons = 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        while (calendar1.before(calendar2)) {
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
            if (calendar1.get(Calendar.MONTH) + 1 == 1
                    && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
            if (calendar1.get(Calendar.MONTH) + 1 == 4
                    && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
            if (calendar1.get(Calendar.MONTH) + 1 == 7
                    && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
            if (calendar1.get(Calendar.MONTH) + 1 == 10
                    && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
        }
        return seasons;
    }

    /**
     * 几个Seasons前的第一天
     *
     * @param value
     * @return
     */
    public static Date getFirstDateOfSeasons(int value) {
        int seasons;
        value = value * 3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -value);
        switch (calendar.get(Calendar.MONTH) + 1) {
            case 1:
                seasons = 1;
                break;
            case 2:
                seasons = 1;
                break;
            case 3:
                seasons = 1;
                break;
            case 4:
                seasons = 4;
                break;
            case 5:
                seasons = 4;
                break;
            case 6:
                seasons = 4;
                break;
            case 7:
                seasons = 7;
                break;
            case 8:
                seasons = 7;
                break;
            case 9:
                seasons = 7;
                break;
            case 10:
                seasons = 10;
                break;
            case 11:
                seasons = 10;
                break;
            case 12:
                seasons = 10;
                break;
            default:
                seasons = 0;
                break;
        }
        if (seasons > 0) {
            seasons--;
        }
        calendar.set(calendar.get(Calendar.YEAR), seasons, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 几个Seasons前的第一天
     *
     * @param dt
     * @param value
     * @return
     */
    public static Date getDateOfSeasons(Date dt, int value) {
        int seasons;
        value = value * 3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH, -value);
        switch (calendar.get(Calendar.MONTH) + 1) {
            case 1:
                seasons = 1;
                break;
            case 2:
                seasons = 1;
                break;
            case 3:
                seasons = 1;
                break;
            case 4:
                seasons = 4;
                break;
            case 5:
                seasons = 4;
                break;
            case 6:
                seasons = 4;
                break;
            case 7:
                seasons = 7;
                break;
            case 8:
                seasons = 7;
                break;
            case 9:
                seasons = 7;
                break;
            case 10:
                seasons = 10;
                break;
            case 11:
                seasons = 10;
                break;
            case 12:
                seasons = 10;
                break;
            default:
                seasons = 0;
                break;
        }

        if (seasons > 0) {
            seasons--;
        }
        calendar.set(calendar.get(Calendar.YEAR), seasons, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /*
     * 两个时间之间相差的年数 @param dt1 @param dt2 @return
     */
    public static int getYears(Date dt1, Date dt2) throws ParseException {
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(dt1);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(dt2);
        return calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
    }

    /**
     * 几年前的第一天
     *
     * @param value
     * @return
     */
    public static Date getFirstDateOfBeforeYear(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -value);
        calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 几年前的第一天
     *
     * @param dt
     * @param value
     * @par
     * @return
     */
    public static Date getFirstDateOfBeforeYear(Date dt, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.YEAR, -value);
        calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return calendar.getTime();
    }
    /**
     * 获取TokenID,如果当前TokenID 小于 前一个Token ID， 则当前TokenID 为前一个TokenID + 1
     *
     * @return
     */
    private static long TokenID = 0;

    public static long getTokenID() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        long res = (getTicks(calendar) - 628614432000000000L) / 600000000;

        if (res <= TokenID) {
            TokenID++;
            res = TokenID;
        }

        TokenID = res;

        return res;
    }

    /**
     * 开始时间
     *
     * @param date
     * @return 时间
     */
    public static Date getStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar1.getTime();
    }

    /**
     * 几天后的开始时间
     *
     * @param days
     * @return 时间
     */
    public static Date getStartDateAfterDay(int days) {
        return MyDatetime.getStartDate(MyDatetime.getDateOfAfterDay(days));
    }

    /**
     * 结束时间
     *
     * @param date
     * @return 时间
     */
    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calendar1.getTime();
    }

    /**
     * 几天后的结束时间
     *
     * @param days
     * @return 时间
     */
    public static Date getEndDateAfterDay(int days) {
        return MyDatetime.getEndDate(MyDatetime.getDateOfAfterDay(days));
    }

    /**
     * 格式化时间
     *
     * @param format
     * @param date
     * @return
     */
    public static String dateFormat(Date date, String format) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        }
        return null;
    }

    /**
     * 返回一个ID
     *
     * @return id
     */
    public static long getID() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar1.getTimeInMillis() - 728496000000L;
    }

    /**
     *
     * @param day
     * @return
     */
    public static String getBirthday(int day) {
        Date dt = MyDatetime.getEndDateAfterDay(day);
        return MyDatetime.dateFormat(dt, MyDatetime.FORMAT_DATE_3);
    }

    /**
     *
     * @param calEnd
     * @return
     */
    public static long getTicks(Calendar calEnd) {
        //start of the ticks time
        Calendar calStart = Calendar.getInstance();
        calStart.set(1, 1, 3, 0, 0, 0);

        //epoch time of the ticks-start time
        long epochStart = calStart.getTime().getTime();
        //epoch time of the target time
        long epochEnd = calEnd.getTime().getTime();

        //get the sum of epoch time, from the target time to the ticks-start time
        long all = epochEnd - epochStart;
        //convert epoch time to ticks time
        long ticks = ((all / 1000) * 1000000) * 10;

        return ticks;
    }

    /**
     * 在指定时间中加分钟
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date getDatetimeAddMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 当前时间中加分钟
     *
     * @param minute
     * @return
     */
    public static Date getDatetimeAddMinute(int minute) {
        return getDatetimeAddMinute(MyDatetime.getStartDate(new Date()), minute);
    }

    /**
     * 字符型转成日期型
     *
     * @param value
     * @return
     */
    public static Date strToDate(final String value) {
        return strToDate(value, MyDatetime.FORMAT_DATETIME_2);
    }

    /**
     * 字符型转成日期型
     *
     * @param value
     * @param format
     * @return
     */
    public static Date strToDate(final String value, final String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(value);
        } catch (ParseException ex) {
            Logger.getLogger(MyDatetime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }

    /**
     * 时间戳转成日期
     *
     * @param value
     * @return
     */
    public static Date timestampToDate(final String value) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(value));
            return calendar.getTime();
        } catch (NumberFormatException ex) {
            Logger.getLogger(MyDatetime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }

    /**
     * 时间戳转成日期
     *
     * @param date
     * @return
     */
    public static Timestamp dateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 设置操作系统的时间
     *
     * @param date
     * @param time
     * @return 当前操作系统的时间
     */
    public static Date setOSDateTime(final String date, final String time) {
        try {
            String cmd;
            String osName = System.getProperty("os.name");
            Logger.getLogger(MyDatetime.class.getName()).info(osName);
            if (osName.matches("^(?i)Windows.*$")) { // Window 系统  
                cmd = " cmd /c time %s";
                cmd = String.format(cmd, time);
                Runtime.getRuntime().exec(cmd);
                cmd = " cmd /c date %s";
                cmd = String.format(cmd, date);
                Runtime.getRuntime().exec(cmd);
            } else { // Linux 系统  
                cmd = String.format("/home/tc/setDateTime.sh %s", date);
                String[] cmds = new String[]{"/bin/sh", "-c", cmd};
                Runtime.getRuntime().exec(cmds);
                cmd = String.format("/home/tc/setDateTime.sh %s", time);
                cmds = new String[]{"/bin/sh", "-c", cmd};
                Runtime.getRuntime().exec(cmds);
            }
            Thread.sleep(500);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MyDatetime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }

    /**
     * 同步系统时间
     *
     * @param datetime
     * @return 当前系统时间
     */
    public static Date setOSDateTime(String datetime) {
        if (datetime == null || datetime.isEmpty()) {
            return new Date();
        }
        long dif = Math.abs(MyDatetime.strToDate(datetime).getTime() - new Date().getTime());
        if ((dif / 1000) > 60) {
            datetime = String.format("\"%s\"", datetime);
            return MyDatetime.setOSDateTime(datetime, datetime);
        }
        return new Date();
    }

    /**
     *
     * @param timestamp
     * @return 10位时间戳
     */
    public static Long tenTimeStamp(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.getTime() / 1000;
        }
        return null;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Date timestampToDate(Long value) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar.getTime();
        } catch (NumberFormatException ex) {
            Logger.getLogger(MyDatetime.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }

    /**
     *
     * @return 将分秒设置为00:00
     */
    public static Long TimeEndHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(calendar.HOUR_OF_DAY), 0, 0);
        return calendar.getTimeInMillis();
    }
}
