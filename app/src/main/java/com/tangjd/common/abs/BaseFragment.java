package com.tangjd.common.abs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Author: tangjd
 * Date: 2017/8/3
 */

public abstract class BaseFragment extends Fragment {
    protected View mLayoutView;
    protected boolean mDataLoaded = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutView == null) {
            mLayoutView = setContentView(inflater, container);
            ButterKnife.bind(this, mLayoutView);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (parent != null) {
                parent.removeView(mLayoutView);
            }
        }
        getDataOnCreateView();
        return mLayoutView;
    }

    protected abstract View setContentView(LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void initView();

    public void getDataOnCreateView() {
        if (!mDataLoaded) {
            getDataJustOnce();
            mDataLoaded = true;
        }
    }

    protected abstract void getDataJustOnce();
}
