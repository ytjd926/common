package com.tangjd.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {
    public static void installApk(Context context, File file, String fileProviderAuthority) {
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // com.tangjd.common.fileprovider是common中的authorities
                data = FileUtil.getFileProviderUri(context, file, fileProviderAuthority);
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(file);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    public static Uri getFileProviderUri(Context context, File file, String fileProviderAuthority) {
//        return FileProvider.getUriForFile(context, "com.tangjd.common.fileprovider", file);
        return FileProvider.getUriForFile(context, fileProviderAuthority, file);
    }

    public static String readFromAssets(Context context, String fileName) {
        String ret = "";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            ret = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String readFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String ret = "";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            ret = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(bufferedReader, inputStreamReader, inputStream);
        }

        return ret;
    }

    public static String readFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        String ret = "";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            ret = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(bufferedReader, inputStreamReader, inputStream);
        }

        return ret;
    }

    public static boolean writeToFile(String filePath, String content, boolean append) {
        boolean success = false;
        FileWriter fileWriter = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            fileWriter.flush();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        } finally {
            IOUtil.close(fileWriter);
        }
        return success;
    }


    public static File getCustomDir(Context context, String customDirName) {
        File file = getFileDir(context, customDirName);
        return file;
    }

    public static File getDownloadDir(Context context) {
        File file = getFileDir(context, "download");
        return file;
    }

    private static File getFileDir(Context context, String parentDirName) {
        if (externalStorageWritable()) {
            File cacheDirFile = null;
            if (BuildUtil.hasFroyo()) {
                cacheDirFile = context.getExternalFilesDir(parentDirName);
            }
            if (cacheDirFile != null) {
                return cacheDirFile;
            }
            // Before Froyo we need to construct the external cache dir ourselves
            final String cacheDir = "/Android/data/" + context.getPackageName() + File.separator + parentDirName + File.separator;
            return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        } else {
            return new File(context.getFilesDir().getAbsolutePath() + File.separator + parentDirName + File.separator);
        }
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     */
    private static boolean externalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !isExternalStorageRemovable();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(VERSION_CODES.GINGERBREAD)
    private static boolean isExternalStorageRemovable() {
        if (BuildUtil.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static long getExternalCacheSize(Context context) {
        return folderSize(context.getExternalCacheDir());
    }

    public static void deleteExternalCache(Context context) {
        deleteDirectory(context.getExternalCacheDir());
    }

    public static void deleteDirectory(File path) {
        if (path == null) {
            return;
        }
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        if (directory == null || !directory.exists()) {
            return 0;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file == null) {
                continue;
            }
            if (file.isFile()) {
                length += file.length();
            } else {
                length += folderSize(file);
            }
        }
        return length;
    }
}
