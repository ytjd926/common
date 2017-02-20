package com.tangjd.common.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 文件操作工具
 *
 * @author Chris
 */
public class FileKit {
    private static final String TAG = "FileKit";

    /**
     * 保存数据
     *
     * @param context
     * @param data
     * @param filename
     * @return
     */
    public static boolean save(Context context, String data, String filename) {
        if (StringKit.isEmpty(data)) {
            return false;
        }
        return save(context, data.getBytes(), filename);
    }

    /**
     * 保存数据
     *
     * @param context
     * @param data
     * @param filename
     * @return
     */
    public static boolean save(Context context, byte[] data, String filename) {
        boolean result = false;
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data);
            fos.flush();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 读取文件, 获取文本数据
     *
     * @param context
     * @param filename
     * @return
     */
    public static String getString(Context context, String filename) {
        String result = null;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
            result = StringKit.stream2String(fis);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 保存数据对象
     *
     * @param context
     * @param object
     * @param filename
     * @return
     */
    public static boolean save(Context context, Object object, String filename) {
        boolean result = false;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            result = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 读取数据对象
     *
     * @param context
     * @param filename
     * @return
     */
    public static Object getObject(Context context, String filename) {
        Object object = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(filename);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return object;
    }

    /**
     * 删除数据对象
     *
     * @param context
     * @param filename
     */
    public static boolean remove(Context context, String filename) {
        boolean flag = false;
        if (context == null || filename == null) {
            return flag;
        }
        File file = context.getFileStreamPath(filename);
        if (file != null && file.exists() && file.isFile()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 判断文件是否存在（应用内部目录）
     *
     * @param context
     * @param filename
     * @return
     */
    public static boolean exists(Context context, String filename) {
        String path = context.getFilesDir().getPath() + "/" + filename;
        File file = new File(path);
        return file.exists();
    }
}
