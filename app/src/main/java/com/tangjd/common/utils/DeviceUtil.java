package com.tangjd.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tangjd on 2016/10/31.
 */

public class DeviceUtil {
    public static byte[] encoderByMd5(byte[] str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        }
        md5.update(str);
        return md5.digest();
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des = des + "0";
            }
            des = des + tmp;
        }
        return des;
    }

    public static String EncoderByMd5(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(str.getBytes());
        String strDes = bytes2Hex(md5.digest());
        return strDes;
    }

    private static String getMobileId(Context context) {
        boolean isZero = false;
        TelephonyManager telephonyManager = (TelephonyManager) getContext(context).getSystemService("phone");
        String deviceId = telephonyManager.getDeviceId();
        if ("unknown".equalsIgnoreCase(deviceId)) {
            deviceId = "0";
        }
        try {
            if (Integer.parseInt(deviceId) == 0) {
                isZero = true;
            }
        } catch (Exception e) {
            isZero = false;
        }
        if ((TextUtils.isEmpty(deviceId)) || (isZero) || (deviceId.length() < 11)) {
            deviceId = "";
        }
        return deviceId;
    }

    private static String getSerialNo() {
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("instance", new Class[]{String.class, String.class});
            serialnum = (String) get.invoke(c, new Object[]{"ro.serialno", "unknown"});
        } catch (Exception ignored) {
            serialnum = null;
        }
        if ((TextUtils.isEmpty(serialnum)) || ("unknown".equalsIgnoreCase(serialnum))) {
            serialnum = "";
        }
        String serialnum2 = null;
        try {
            Class myclass = Class.forName("android.os.SystemProperties");
            Method[] methods = myclass.getMethods();
            Object[] params = {new String("ro.serialno"),
                    new String("Unknown")};
            serialnum2 = (String) methods[2].invoke(myclass, params);
        } catch (Exception ignored) {
            serialnum2 = null;
        }
        if ((TextUtils.isEmpty(serialnum2)) || ("unknown".equalsIgnoreCase(serialnum2)) || (serialnum2.equalsIgnoreCase(serialnum))) {
            serialnum2 = "";
        }
        String data = serialnum + serialnum2;
        return data;
    }

    private static String getMobileHardInfo() {
        String m_szDevIDShort = Build.BOARD +
                Build.CPU_ABI + Build.DEVICE +
                Build.DISPLAY + Build.HOST +
                Build.MANUFACTURER + Build.MODEL +
                Build.PRODUCT + Build.TYPE +
                Build.USER;

        return m_szDevIDShort;
    }

    public static String getDeviceUUID(Context context) {
        Context appContext = getContext(context);
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("sp_device", 0);
        if ((sharedPreferences != null) && (sharedPreferences.contains("UNION_ZD_DEVICE_ID"))) {
            String deviceId = sharedPreferences.getString("UNION_ZD_DEVICE_ID", "");
            if (deviceId.length() > 0) {
                return deviceId;
            }
        }
        String did = getUUIDFromSDFile(appContext);
        if (TextUtils.isEmpty(did)) {
            String deviceId = getMobileId(appContext) + getSerialNo() + getMobileHardInfo();
            did = EncoderByMd5(deviceId);
        }
        sharedPreferences.edit().putString("UNION_ZD_DEVICE_ID", did).commit();
        return did;
    }

    private static String getUUIDFromSDFile(Context context) {
        String deviceId;
        try {
            String INSTALLATION = "INSTALLATION";
            File installation = new File(Environment.getExternalStorageDirectory() + "/Android/" + INSTALLATION);
            if (!installation.exists()) {
                writeInstallationFile(installation, context);
            }
            deviceId = readInstallationFile(installation);
        } catch (Exception e) {
            return "";
        }
        return deviceId;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation, Context ct) throws IOException {
        File pFile = installation.getParentFile();
        if ((pFile == null) || (!pFile.exists())) {
            pFile.mkdirs();
        }
        if (!installation.exists()) {
            installation.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(installation);
        String deviceId = getMobileId(getContext(ct)) + getSerialNo() + getMobileHardInfo();
        String did = EncoderByMd5(deviceId);
        out.write(did.getBytes());
        out.close();
    }

    private static Context getContext(Context context) {
        if ((context != null) && (context.getApplicationContext() != null)) {
            return context.getApplicationContext();
        }
        return context;
    }
}
