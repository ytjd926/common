package com.tangjd.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangjd.common.R;

import java.util.List;

/**
 * Created by tangjd on 2016/9/23.
 */
public abstract class RvBase<T> extends RecyclerView {
    public View mLoadingView, mEmptyView, mErrorView;

    public RvBase(Context context) {
        this(context, null);
    }

    public RvBase(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RvBase(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        setLayoutManager(customSetLayoutManager(context));
        mAdapter = instanceCustomAdapter();
        setAdapter(mAdapter);

        mLoadingView = LayoutInflater.from(context).inflate(R.layout.rv_loading_layout, this, false);
        mEmptyView = LayoutInflater.from(context).inflate(R.layout.rv_empty_data_layout, this, false);
        mErrorView = LayoutInflater.from(context).inflate(R.layout.rv_error_layout, this, false);
    }

    public BaseQuickAdapter<T, BaseViewHolder> instanceCustomAdapter() {
        return new CustomAdapter(customSetItemLayoutId());
    }

    public void showLoadingView() {
        mAdapter.setEmptyView(mLoadingView);
    }

    public void showEmptyView() {
        mAdapter.setEmptyView(mEmptyView);
    }

    public void showErrorView() {
        mAdapter.setEmptyView(mErrorView);
    }

    public void setOnEmptyViewClickListener(OnClickListener listener) {
        mEmptyView.setOnClickListener(listener);
        mErrorView.setOnClickListener(listener);
    }

    public void clearEmptyView() {
        mAdapter.setEmptyView(new View(getContext()));
    }

    public abstract LayoutManager customSetLayoutManager(Context context);

    public abstract int customSetItemLayoutId();

    public abstract void customConvert(BaseViewHolder holder, T t);

    public void setData(List<T> data) {
        mAdapter.setNewData(data);
    }

    public BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    public class CustomAdapter extends BaseQuickAdapter<T, BaseViewHolder> {

        public CustomAdapter(int resId) {
            super(resId, null);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, T t) {
            customConvert(baseViewHolder, t);
        }
    }
}
