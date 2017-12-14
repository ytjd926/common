package com.tangjd.common.widget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tangjd on 2016/8/23.
 */
public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface OnLoadStateChangeListener {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

        void onJsAlert(WebView view, String url, String message, JsResult result);
    }

    private OnLoadStateChangeListener mListener;

    public void setOnLoadStateChangeListener(OnLoadStateChangeListener listener) {
        mListener = listener;
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(true);

        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (mListener != null) {
                    mListener.onJsAlert(view, url, message, result);
                }
                return false;
            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog();
                if (mListener != null) {
                    mListener.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                    }
                }, 1000);
                if (mListener != null) {
                    mListener.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showTipDialog("加载异常: \n" + failingUrl + "\n" + description);
                if (mListener != null) {
                    mListener.onReceivedError(view, errorCode, description, failingUrl);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView paramWebView, final String paramString) {
                if (TextUtils.isEmpty(paramString)) {
                    return false;
                }
                post(new Runnable() {

                    @Override
                    public void run() {
                        loadUrl(paramString);
                    }
                });
                return true;
            }
        });

        WebSettings settings = getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setAllowContentAccess(true);
        }
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(false);
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setGeolocationEnabled(true);

        settings.setSupportZoom(true);

        CookieManager.getInstance().setAcceptCookie(true);
    }


    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getContext());
            }
            mProgressDialog.setTitle("");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTipDialog(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setCancelable(true);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
