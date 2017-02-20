package com.tangjd.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangjd.common.utils.DisplayUtils;

import java.util.List;

/**
 * Created by tangjd on 2016/9/23.
 */
public abstract class RvBaseWithSlideDelete<T> extends RecyclerView {

    public RvBaseWithSlideDelete(Context context) {
        this(context, null);
    }

    public RvBaseWithSlideDelete(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RvBaseWithSlideDelete(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(customSetLayoutManager(context));
        mAdapter = new CustomAdapter(customSetItemLayoutId());
        setAdapter(mAdapter);
    }

    public abstract LayoutManager customSetLayoutManager(Context context);

    public abstract int customSetItemLayoutId();

    public abstract void customConvert(BaseViewHolder holder, T t);

    public void setData(List<T> data) {
        mAdapter.setNewData(data);
    }

    public CustomAdapter mAdapter;

    public class CustomAdapter extends BaseQuickAdapter<T, BaseViewHolder> implements ItemSlideHelper.Callback {

        public CustomAdapter(int resId) {
            super(resId, null);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, T t) {
            customConvert(baseViewHolder, t);
        }

        @Override
        public int getHorizontalRange(ViewHolder holder) {
            return DisplayUtils.Dp2px(getContext(), 80);
        }

        @Override
        public ViewHolder getChildViewHolder(View childView) {
            return RvBaseWithSlideDelete.this.getChildViewHolder(childView);
        }

        @Override
        public View findTargetView(float x, float y) {
            return findChildViewUnder(x, y);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            addOnItemTouchListener(new ItemSlideHelper(getContext(), this));
        }
    }
}
