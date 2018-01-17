package com.tangjd.common.abs;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tangjd.common.R;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SFBaseActivity extends BaseActivity implements SlidingPaneLayout.PanelSlideListener {
    private static final int DEFAULT_TRANSLATION_X = 300;
    private static final int DEFAULT_SHADOW_WIDTH = 50;
    private static View sDecorView;
    private static Map<SFBaseActivity, View> mViewMap = new HashMap<>();
    private View mPreDecorView;
    private SlidingPaneLayout mSlidingPaneLayout;
    private FrameLayout mContentView;
    private View mShadowView;
    /**
     * Flag of whether SlidingPaneLayout initialize success.
     * If success, use SlidingPaneLayout as contentView,
     * otherwise use the contentView which set in Activity#setContentView().
     */
    private boolean mInitSuccess;

    public static void startActSF(BaseActivity activity, Class<?> cls) {
        sDecorView = activity.getWindow().getDecorView();
        activity.startAct(cls);
    }

    public static void startActSF(BaseActivity activity, Class<?> cls, Serializable bean) {
        sDecorView = activity.getWindow().getDecorView();
        activity.startAct(cls, bean);
    }

    public static void startActSF(BaseActivity activity, Class<?> cls, Serializable... bean) {
        sDecorView = activity.getWindow().getDecorView();
        activity.startAct(cls, bean);
    }

    public static void startActSFForResult(BaseActivity activity, Class<?> cls) {
        sDecorView = activity.getWindow().getDecorView();
        activity.startActForResult(cls);
    }

    public static void startActSFForResult(BaseActivity activity, Class<?> cls, Serializable bean) {
        sDecorView = activity.getWindow().getDecorView();
        activity.startActForResult(cls, bean);
    }

    public static void startActSFForResult(BaseActivity activity, Class<?> cls, Serializable... bean) {
        sDecorView = activity.getWindow().getDecorView();
        activity.startActForResult(cls, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            mSlidingPaneLayout = new SlidingPaneLayout(this);
            Field mOverhangSize = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            mOverhangSize.setAccessible(true);
            mOverhangSize.set(mSlidingPaneLayout, 0);
            mSlidingPaneLayout.setPanelSlideListener(this);
            mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
            mInitSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            mInitSuccess = false;
        }
        super.onCreate(savedInstanceState);
        if (!mInitSuccess) {
            return;
        }
        // frontContainer
        LinearLayout frontContainer = new LinearLayout(this);
        frontContainer.setOrientation(LinearLayout.HORIZONTAL);
        frontContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        frontContainer.setLayoutParams(new ViewGroup.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() + DEFAULT_SHADOW_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT));
        // contentView
        mContentView = new FrameLayout(this);
        mContentView.setBackgroundColor(getResources().getColor(android.R.color.white));
        mContentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // shadowView
        mShadowView = new ImageView(this);
        mShadowView.setBackgroundResource(R.drawable.sliding_finish_shadow);
        mShadowView.setLayoutParams(new LinearLayout.LayoutParams(DEFAULT_SHADOW_WIDTH, LinearLayout.LayoutParams.MATCH_PARENT));
        // add views to frontContainer
        frontContainer.addView(mShadowView);
        frontContainer.addView(mContentView);
        frontContainer.setTranslationX(-DEFAULT_SHADOW_WIDTH);
        // add views to SlidingPaneLayout
        mSlidingPaneLayout.addView(new View(this), 0);
        mSlidingPaneLayout.addView(frontContainer, 1);
        mViewMap.put(this, sDecorView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreDecorView = mViewMap.get(this);
    }

    @Override
    protected void onDestroy() {
        if (mInitSuccess) {
            if (mPreDecorView != null) {
                mPreDecorView.setTranslationX(0);
            }
            mViewMap.remove(this);
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        if (mInitSuccess) {
            super.setContentView(mSlidingPaneLayout, params);
            mContentView.removeAllViews();
            mContentView.addView(v, params);
        } else {
            super.setContentView(v, params);
        }
    }

    @Override
    public void onPanelClosed(View panel) {
        if (mPreDecorView != null) {
            mPreDecorView.setTranslationX(0);
        }
    }

    @Override
    public void onPanelOpened(View panel) {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        // Add any effect here
        if (mPreDecorView != null) {
            mPreDecorView.setTranslationX(slideOffset * (float) DEFAULT_TRANSLATION_X - (float) DEFAULT_TRANSLATION_X);
        }
        mShadowView.setAlpha(1 - slideOffset);
    }
}
