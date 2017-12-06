package com.tangjd.common.helper;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tangjd.common.R;
import com.tangjd.common.utils.InvokeUtil;
import com.tangjd.common.widget.HintSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Administrator
 * Date: 2017/12/6
 */

public class HintSpinnerAdapterHelper {
    public static void set(Spinner spinner, String hintMsg, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_dropdown_item, data);
        TextView hintView = (TextView) LayoutInflater.from(spinner.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, spinner, false);
        hintView.setText(hintMsg);
        spinner.setAdapter(new HintSpinnerAdapter(adapter, hintView, spinner.getContext()));
    }

    public static void set(Spinner spinner, String hintMsg, List<?> data, String fieldName) {
        List<String> strData = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                strData.add((String) InvokeUtil.getValueOfField(data.get(i), fieldName));
            }
        }
        set(spinner, hintMsg, strData);
    }
}
