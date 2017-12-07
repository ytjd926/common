package com.tangjd.common.abs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tangjd.common.R;

import java.io.Serializable;

/**
 * Created by tangjd on 2015/12/14.
 */
public class BaseActivity extends AppCompatActivity {

    public static String EXTRA_COMMON_DATA_BEAN = "extra_data_bean";
    public static int REQUEST_CODE_COMMON = 9999;
    public View mContentView;
    public Serializable mCommonBean;

    private Toolbar mToolbar;
    private Toast mToast;
    private Snackbar mSnackbar;
    private ProgressDialog mProgressDialog;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null, false));
    }

    @Override
    public void setContentView(View view) {
        mContentView = view;
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.REQUEST_READ_EXTERNAL_STORAGE:
            case PermissionManager.REQUEST_ACCESS_FINE_LOCATION:
            default:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showTipDialog("获取权限成功!");
                } else {
                    showTipDialog("获取权限已被阻止，请在应用权限设置中开启!");
                }
                break;
        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return mToolbar;
    }

    public void enableMenu(Menu menu, String menuTitle, MenuItem.OnMenuItemClickListener listener) {
        enableMenu(menu, new String[]{menuTitle}, new MenuItem.OnMenuItemClickListener[]{listener});
    }

    public void enableMenu(Menu menu, String[] menuTitles, MenuItem.OnMenuItemClickListener[] listeners) {
        getToolbar();
        for (int i = 0; i < menuTitles.length; i++) {
            MenuItem menuItem = menu.add(menuTitles[i]);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            menuItem.setOnMenuItemClickListener(listeners[i]);
        }
    }

    public void enableMenu(Menu menu, int menuIconResId, MenuItem.OnMenuItemClickListener listener) {
        enableMenu(menu, new int[]{menuIconResId}, new MenuItem.OnMenuItemClickListener[]{listener});
    }

    public void enableMenu(Menu menu, int[] menuIconResIds, MenuItem.OnMenuItemClickListener[] listeners) {
        getToolbar();
        for (int i = 0; i < menuIconResIds.length; i++) {
            MenuItem menuItem = menu.add("");
            menuItem.setIcon(menuIconResIds[i]);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setOnMenuItemClickListener(listeners[i]);
        }
    }

    public void enableMenu(Menu menu, String[] menuTitles, int[] menuIconResIds, MenuItem.OnMenuItemClickListener[] listeners) {
        getToolbar();
        for (int i = 0; i < menuIconResIds.length; i++) {
            MenuItem menuItem = menu.add(menuTitles[i]);
            menuItem.setIcon(menuIconResIds[i]);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setOnMenuItemClickListener(listeners[i]);
        }
    }

    public void enableCollapseMenu(Menu menu, String[] menuTitles, MenuItem.OnMenuItemClickListener[] listeners) {
        getToolbar();
        for (int i = 0; i < menuTitles.length; i++) {
            MenuItem menuItem = menu.add(menuTitles[i]);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menuItem.setOnMenuItemClickListener(listeners[i]);
        }
    }

    public void enableLeftMenu(String menuTitle, View.OnClickListener listener) {
        enableLeftMenu(menuTitle, 0, listener);
    }

    public void enableLeftMenu(String menuTitle, int resId, View.OnClickListener listener) {
        TextView tvLeftMenu = (TextView) findViewById(R.id.toolbar_left_menu);
        tvLeftMenu.setText(menuTitle);
        tvLeftMenu.setVisibility(View.VISIBLE);
        tvLeftMenu.setOnClickListener(listener);
        if (resId != 0) {
            tvLeftMenu.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
            tvLeftMenu.setCompoundDrawablePadding(10);
        }
    }

    public void setToolbarTitle(String title) {
        getToolbar();
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
    }

    public void enableBackFinish() {
        getToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getToolbar().setNavigationIcon(R.drawable.ic_menu_back);
    }

    // ------ End ------


    private Toast getToast() {
        if (mToast == null) {
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    public void showToast() {
        showToast("加载中...");
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getToast().setText(message);
                    getToast().setDuration(Toast.LENGTH_LONG);
                    getToast().show();
                }
            }
        });
    }

    public void dismissToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
            }
        });
    }

    public void showShortToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getToast().setText(message);
                    getToast().setDuration(Toast.LENGTH_SHORT);
                    getToast().show();
                }
            }
        });
    }

    public void showLongToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getToast().setText(message);
                    getToast().setDuration(Toast.LENGTH_LONG);
                    getToast().show();
                }
            }
        });
    }

    // Dialog
    // ------ Start ------
    public void showTipDialog(String message) {
        showTipDialog(message, true, null);
    }

    public void showTipDialog(String message, DialogInterface.OnClickListener onPositiveClick) {
        showTipDialog(message, false, onPositiveClick);
    }

    public void showTipDialog(String message, boolean cancelable, DialogInterface.OnClickListener onPositiveClick) {
        showTipDialog(message, cancelable, getString(android.R.string.ok), onPositiveClick);
    }

    public void showTipDialog(String message, boolean cancelable, String positiveText, DialogInterface.OnClickListener onPositiveClick) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton(positiveText, onPositiveClick);
            builder.setCancelable(cancelable);
            if (!isFinishing())
                builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ------ End ------

    public void showAlertDialog(String message, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        showAlertDialog(message, getString(android.R.string.ok), true, onPositiveClick, onNegativeClick);
    }
    // ------ End ------

    public void showAlertDialog(String message, boolean cancelable, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        showAlertDialog(message, getString(android.R.string.ok), cancelable, onPositiveClick, onNegativeClick);
    }

    public void showAlertDialog(String message, String positiveText, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        showAlertDialog(message, positiveText, true, onPositiveClick, onNegativeClick);
    }

    public void showAlertDialog(String message, String positiveText, boolean cancelable, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        showAlertDialog(message, positiveText, getString(android.R.string.cancel), cancelable, onPositiveClick, onNegativeClick);
    }

    public void showAlertDialog(String message, String positiveText, String negativeText, boolean cancelable, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setCancelable(cancelable);
            builder.setPositiveButton(positiveText, onPositiveClick);
            builder.setNegativeButton(negativeText, onNegativeClick);
            if (!isFinishing()) {
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SingleChoiceDialog
    // ------ Start ------
    public void showSingleChoiceDialog(String[] items, DialogInterface.OnClickListener listener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(items, listener);
            if (!isFinishing()) {
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Snackbar getSnackbar() {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(mContentView, "", Snackbar.LENGTH_SHORT);
        }
        return mSnackbar;
    }

    public void showShortSnackbar(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getSnackbar().setText(content);
                    getSnackbar().setDuration(Snackbar.LENGTH_SHORT);
                    getSnackbar().show();
                }
            }
        });
    }
    // ------ End ------

    public void showLongSnackbar(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getSnackbar().setText(content);
                    getSnackbar().setDuration(Snackbar.LENGTH_LONG);
                    getSnackbar().show();
                }
            }
        });
    }

    public void showActionLongSnackbar(final String content, final String actionText, final View.OnClickListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getSnackbar().setText(content);
                    getSnackbar().setDuration(Snackbar.LENGTH_LONG);
                    getSnackbar().setAction(actionText, listener);
                    getSnackbar().show();
                }
            }
        });
    }

    public void showActionIndefiniteSnackbar(final String content, final String actionText, final View.OnClickListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    getSnackbar().setText(content);
                    getSnackbar().setDuration(Snackbar.LENGTH_INDEFINITE);
                    getSnackbar().setAction(actionText, listener);
                    getSnackbar().show();
                }
            }
        });
    }

    public void dismissSnackbar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && mSnackbar != null && mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                }
            }
        });
    }

    public void showProgressDialog(String title, CharSequence message, boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setMessage(message);
        if (!isFinishing()) {
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // ------ End ------

    public void showProgressDialog() {
        showProgressDialog("", "加载中...", true);
    }

    public void showProgressDialog(boolean cancelable) {
        showProgressDialog("", "加载中...", cancelable);
    }

    public void showProgressDialog(CharSequence message) {
        showProgressDialog("", message, true);
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

    // Glide
    // ------ Start ------
    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, R.drawable.ic_default);
    }
    // ------ End ------

    public void loadImage(String url, ImageView imageView, int loadingRes) {
        builderDrawableRequest(url, loadingRes)
                .into(imageView);
    }

    public DrawableRequestBuilder builderDrawableRequest(String url) {
        return builderDrawableRequest(url, R.drawable.ic_default);
    }

    public DrawableRequestBuilder builderDrawableRequest(String url, int loadingRes) {
        return Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(loadingRes)
                .error(loadingRes)
                .fallback(loadingRes)
                .crossFade();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommonBean = getIntent().getSerializableExtra(EXTRA_COMMON_DATA_BEAN);
    }

    public void startAct(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void startAct(Class<?> cls, Serializable bean) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(EXTRA_COMMON_DATA_BEAN, bean);
        startActivity(intent);
    }

    public void startActForResult(Class<?> cls) {
        startActivityForResult(new Intent(this, cls), REQUEST_CODE_COMMON);
    }

    public void startActForResult(Class<?> cls, Serializable bean) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(EXTRA_COMMON_DATA_BEAN, bean);
        startActivityForResult(intent, REQUEST_CODE_COMMON);
    }
    // ------ End ------
}
