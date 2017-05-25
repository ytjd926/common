package com.tangjd.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangjd.common.R;

import java.util.ArrayList;
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
        setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    public void init(Context context) {
        setLayoutManager(customSetLayoutManager(context));
        mAdapter = instanceCustomAdapter();
        setAdapter(mAdapter);

        mLoadingView = LayoutInflater.from(context).inflate(R.layout.rv_loading_layout, this, false);
        mEmptyView = LayoutInflater.from(context).inflate(R.layout.rv_empty_data_layout, this, false);
        mErrorView = LayoutInflater.from(context).inflate(R.layout.rv_error_layout, this, false);
    }

    public void setEmptyViewText(String content) {
        if (!TextUtils.isEmpty(content)) {
            TextView textView = ((TextView) mEmptyView.findViewById(R.id.tv_msg));
            if (textView != null) {
                textView.setText(content);
            }
        }
    }

    public void setLoadingViewText(String content) {
        if (!TextUtils.isEmpty(content)) {
            TextView textView = ((TextView) mLoadingView.findViewById(R.id.tv_msg));
            if (textView != null) {
                textView.setText(content);
            }
        }
    }

    public void setErrorViewText(String content) {
        if (!TextUtils.isEmpty(content)) {
            TextView textView = ((TextView) mErrorView.findViewById(R.id.tv_msg));
            if (textView != null) {
                textView.setText(content);
            }
        }
    }

    public BaseQuickAdapter<T, BaseViewHolder> instanceCustomAdapter() {
        return new CustomAdapter(customSetItemLayoutId());
    }

    public abstract LayoutManager customSetLayoutManager(Context context);

    public abstract int customSetItemLayoutId();

    public abstract void customConvert(BaseViewHolder holder, T bean);

    public void setData(List<T> data) {
        mAdapter.setNewData(data);
    }

    public void addData(T bean) {
        List<T> newData = new ArrayList<>();
        newData.add(bean);
        addData(newData);
    }

    public void addData(List<T> data) {
        mAdapter.addData(data);
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

    public void showLoadingView() {
        mAdapter.setEmptyView(mLoadingView);
    }

    public void showEmptyView() {
        mAdapter.setEmptyView(mEmptyView);
    }

    public void showErrorView() {
        mAdapter.setEmptyView(mErrorView);
    }

    public void clearEmptyView() {
        mAdapter.setEmptyView(new View(getContext()));
    }

    public void setOnEmptyViewClickListener(OnClickListener listener) {
        mEmptyView.setOnClickListener(listener);
        mErrorView.setOnClickListener(listener);
    }

    public void onGetDataSuccess(List<T> beans) {
        if (beans == null) {
            showErrorView();
        } else if (beans.size() == 0) {
            showEmptyView();
        } else {
            setData(beans);
            clearEmptyView();
        }
    }

    public void onGetDataSuccess(boolean hasMore, List<T> beans) {
        setHasMore(hasMore);
        onGetDataSuccess(beans);
    }

    public void onGetDataFail() {
        showErrorView();
    }

    public void onLoadMoreSuccess(List<T> beans) {
        if (beans == null || beans.size() == 0) {
            // onError("没有更多数据");
            mAdapter.loadMoreFail();
        } else {
            addData(beans);
            mAdapter.loadMoreComplete();
        }
    }

    public void onLoadMoreSuccess(boolean hasMore, List<T> beans) {
        setHasMore(hasMore);
        onLoadMoreSuccess(beans);
    }

    public void onLoadMoreFail() {
        mAdapter.loadMoreFail();
    }

    public void setHasMore(boolean hasMore) {
        if (hasMore) {
            mAdapter.loadMoreEnd(false);
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }
}
