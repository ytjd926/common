package com.tangjd.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by tangjd on 2015/12/30.
 */
public class DisplayUtils {
    public static int Dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int spToPx(int sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static Point getScreenSize(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point screenPoint = new Point();
        if (BuildUtil.hasHoneycombMR2()) {
            display.getSize(screenPoint);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            screenPoint.x = width;
            screenPoint.y = height;
        }
        return screenPoint;
    }


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> statusBarClass = null;
        Object statusBarObj = null;
        java.lang.reflect.Field statusBarField = null;
        int id = 0;
        int statusBarHeight = 0;
        try {
            statusBarClass = Class.forName("com.android.internal.R$dimen");
            statusBarObj = statusBarClass.newInstance();
            statusBarField = statusBarClass.getField("status_bar_height");
            id = Integer.parseInt(statusBarField.get(statusBarObj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(id);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
            statusBarHeight = 0;
        }
        return statusBarHeight;
    }

    public static int getActionBarHeight(Context context) {
        // Calculate ActionBar height
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
