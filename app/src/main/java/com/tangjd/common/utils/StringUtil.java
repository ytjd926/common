package com.tangjd.common.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tangjd on 2015/12/15.
 */
public class StringUtil {
    public static boolean isEmpty(String content) {
        return TextUtils.isEmpty(content) || content.equalsIgnoreCase("null");
    }

    public static boolean isEmpty(EditText editText) {
        String content = editText.getText().toString();
        return isEmpty(content);
    }

    public static boolean isEmpty(TextView textView) {
        String content = textView.getText().toString();
        return isEmpty(content);
    }
}
