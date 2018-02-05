package com.tangjd.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by tangjd on 2018/2/5.
 */

public class DecimalUtil {
    public static DecimalFormat get2Format() {
        return new DecimalFormat("###.00");
    }

    public static float strToFloat2(String value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
