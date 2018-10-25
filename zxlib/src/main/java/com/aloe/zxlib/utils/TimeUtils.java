package com.aloe.zxlib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils  {
    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;


    public static String getTimestampString(long time) {
        Date date = new Date();
        long splitTime =  date.getTime() - time;
        if (splitTime < 30 * ONE_DAY) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚";
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE);
            }
            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR);
            }

        }
        String result;
        result = "MM月dd日 HH:mm";

        return new SimpleDateFormat(result, Locale.CHINA).format(new Date(time));
    }
}
