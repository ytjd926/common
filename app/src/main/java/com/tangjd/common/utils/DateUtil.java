package com.tangjd.common.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tangjd on 2018/5/9.
 */

public class DateUtil {
    public static String simpleFormat(String pattern, long timeInMillis) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(timeInMillis);
    }
}
