package com.tangjd.common.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by tangjd on 2015/12/18.
 */
public class IOUtil {
    public static void close(Closeable... items) {
        if (items == null) {
            return;
        }
        for (Closeable item : items) {
            try {
                if (null != item) {
                    item.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
