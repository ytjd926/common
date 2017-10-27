package com.tangjd.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by tangjd on 2016/7/12.
 */
public class MeasuredGridView extends GridView {
    public MeasuredGridView(Context context) {
        this(context, null);
    }

    public MeasuredGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasuredGridView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
