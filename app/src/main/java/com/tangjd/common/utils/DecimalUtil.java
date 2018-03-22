package com.tangjd.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by tangjd on 2018/2/5.
 */

public class DecimalUtil {
    private static DecimalFormat sDecimalFormat;

    public static DecimalFormat getSimpleFormat() {
        if (sDecimalFormat == null) {
            sDecimalFormat = new DecimalFormat("0.0");
        }
        return sDecimalFormat;
    }

    public static String simpleFormat(Object data) {
        return getSimpleFormat().format(data);
    }


    public static float strToFloat2(String value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
