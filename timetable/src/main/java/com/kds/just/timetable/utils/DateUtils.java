package com.kds.just.timetable.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    /**
     * 오늘 0시 0분 0초 Ms를 가져온다.
     */
    public static long getTodayStartTimeMs() {
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 현재 날짜를 보여준다. (yyyy-MM-dd)
     */
    public static String getTodayDateFormat() {
        return getTodayDateFormat("yyyy-MM-dd");
    }

    /**
     * 기본 날짜 포멧을 특정 포멧으로 변경
     * @param date
     * @param targetFormat
     * @return
     */
    public static String changeDateFormat(String date, String targetFormat) {
        return changeDateFormat(date,"yyyy-MM-dd",targetFormat);
    }
    /**
     * 현재 날짜 포멧을 targetFormat형태로 변경
     * @param date
     * @param format
     * @param targetFormat
     * @return
     */
    public static String changeDateFormat(String date, String format, String targetFormat) {
        long ms = getDateToMs(date,format);
        return getStringForDate(ms,targetFormat);
    }

    /**
     * 기본 시간 포멧을 특정 포멧으로 변경
     * @param time
     * @param targetFormat
     * @return
     */
    public static String basicChangeTimeFormat(String time, String targetFormat) {
        time = time.replace(":","");
        long ms = getDateToMs(time,"HHmm");
        return getStringForDate(ms,targetFormat);
    }

    /**
     * 현재 시간를 보여준다. (HH:mm)
     */
    public static String getTodayTimeFormat() {
        return getTodayDateFormat("HH:mm");
    }

    /**
     * 현재 날짜 시간을 보여준다. (yyyyMMddHHmmssSS)
     */
    public static String getTodayDateTimeFormat() {
        return getTodayDateFormat("yyyyMMddHHmmssSS");
    }

    /**
     * 현재 날짜를 보여준다.
     *
     * @param format string format (yyyyMMddHHmmssSS)
     */
    public static String getTodayDateFormat(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdfNow = new SimpleDateFormat(format);
        return sdfNow.format(date);
    }

    /**
     * 인자로 넘겨준 ms 값을 기준으로 날짜와 시간값을 반환한다.
     *
     * @param timeMs 시간 ms
     * @param format string format (yyyyMMddHHmmssSS)
     */
    public static String getStringForDate(long timeMs, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dd = new Date(timeMs);
        return sdf.format(dd);
    }

    /**
     * 날짜를 ms로 변경
     *
     * @param format string format (yyyyMMddHHmmssSS)
     */
    public static long getDateToMs(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = simpleDateFormat.parse(date);
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 날짜를 ms로 변경
     *
     * @param format string format (yyyyMMddHHmmssSS)
     */
    public static Date getStringToDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
//            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = simpleDateFormat.parse(date);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 현재시간 대비 경과시간
     *
     */
    public static String timeAgoString(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        if(date != null){
            c.setTime(date);
        }

        long curTime = now.getTimeInMillis() / 1000;
        long targetTime = c.getTimeInMillis() / 1000;
        long diff = curTime - targetTime;

        if(targetTime > curTime) {
            return "방금";
        } else {
            if(diff < 10) {
                return "방금";
            } else if(diff < 60) {
                return diff + "초 전";
            } else if(diff < 3600) {
                return diff / 60 + "분 전";
            } else if(diff < 3600 * 24) {
                return diff / 60 / 60 + "시간 전";
            } else if(diff < 3600 * 24 * 30) {
                long diffDay = diff / 3600 / 24;
                if(diffDay == 1) {
                    return "하루 전";
                } else {
                    return diffDay + "일 전";
                }
            } else if(diff < 3600 * 24 * 365) {
                long diffMonth = diff / 3600 / 24 / 30;
                if(diffMonth == 1) {
                    return "한달 전";
                } else {
                    return diffMonth + "달 전";
                }
            } else {
                long diffYear = diff / 3600 / 24 / 365;
                if(diffYear == 1) {
                    return "작년";
                } else {
                    return diffYear + "년 전";
                }
            }
        }
    }

    public static String getWeekDay(String date, String dateType) {
        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = null;
        try {
            nDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        return day;
    }

    // -------------- SendBird --------------------------
    // This class should not be initialized
    private DateUtils() {

    }


    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public static String formatTimeWithMarker(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    public static int getHourOfDay(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("H", Locale.getDefault());
        return Integer.valueOf(dateFormat.format(timeInMillis));
    }

    public static int getMinute(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("m", Locale.getDefault());
        return Integer.valueOf(dateFormat.format(timeInMillis));
    }

    /**
     * If the given time is of a different date, display the date.
     * If it is of the same date, display the time.
     * @param timeInMillis  The time to convert, in milliseconds.
     * @return  The time or date.
     */
    public static String formatDateTime(long timeInMillis) {
        if(isToday(timeInMillis)) {
            return formatTime(timeInMillis);
        } else {
            return formatDate(timeInMillis);
        }
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    public static String formatDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(timeInMillis);
    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    public static boolean isToday(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = dateFormat.format(timeInMillis);
        return date.equals(dateFormat.format(System.currentTimeMillis()));
    }

    /**
     * Checks if two dates are of the same day.
     * @param millisFirst   The time in milliseconds of the first date.
     * @param millisSecond  The time in milliseconds of the second date.
     * @return  Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    public static boolean hasSameDate(long millisFirst, long millisSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(millisFirst).equals(dateFormat.format(millisSecond));
    }
}
