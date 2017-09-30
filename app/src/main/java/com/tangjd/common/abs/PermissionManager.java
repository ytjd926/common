package com.tangjd.common.abs;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by Administrator on 2016/7/8.
 */
public class PermissionManager {

    public static final int REQUEST_READ_EXTERNAL_STORAGE = 9001;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 9002;

    public static boolean mayRequestReadExternalStorage(final BaseActivity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity.checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (activity.shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            activity.showTipDialog("请允许程序使用存储空间权限", new DialogInterface.OnClickListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public void onClick(DialogInterface dialog, int which) {
                    activity.requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                }
            });
        } else {
            activity.requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    public static boolean mayRequestAccessFineLocation(final BaseActivity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity.checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (activity.shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            activity.showTipDialog("请允许程序获取定位权限", false, new DialogInterface.OnClickListener() {
                @Override
                @TargetApi(Build.VERSION_CODES.M)
                public void onClick(DialogInterface dialog, int which) {
                    activity.requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
                }
            });
        } else {
            activity.requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        }
        return false;
    }
}
