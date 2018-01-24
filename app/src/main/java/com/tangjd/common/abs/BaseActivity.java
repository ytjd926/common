package com.tangjd.common.abs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tangjd.common.R;
import com.werb.permissionschecker.PermissionChecker;

import java.io.Serializable;

/**
 * Created by tangjd on 2015/12/14.
 */
public class BaseActivity extends AppCompatActivity {
    // StatusBar
    // ------ Start ------
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    // ------ End ------

    // Toolbar
    // ------ Start ------
    private Toolbar mToolbar;

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

    // Toast
    // ------ Start ------
    private Toast mToast;

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
    // ------ End ------

    // TipDialog
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

    // AlertDialog
    // ------ Start ------
    public void showAlertDialog(String message, DialogInterface.OnClickListener onPositiveClick, DialogInterface.OnClickListener onNegativeClick) {
        showAlertDialog(message, getString(android.R.string.ok), true, onPositiveClick, onNegativeClick);
    }

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
    // ------ End ------

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
    // ------ End ------

    // Snackbar
    // ------ Start ------
    private Snackbar mSnackbar;
    public View mContentView;

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

    public void showLongSnackbarWithAction(final String content, final String actionText, final View.OnClickListener listener) {
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

    public void showSnackbarWithActionIndefinite(final String content, final String actionText, final View.OnClickListener listener) {
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
    // ------ End ------

    // ProgressDialog
    // ------ Start ------
    public ProgressDialog mProgressDialog;

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
    // ------ End ------

    // Glide
    // ------ Start ------
    public RequestOptions getDefaultRequestOption(int loadingRes) {
        return RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(loadingRes)
                .error(loadingRes)
                .fallback(loadingRes);
    }

    public void loadImageCenterCrop(Object urlOrFileOrPath, ImageView imageView) {
        loadImageCenterCrop(urlOrFileOrPath, imageView, R.drawable.ic_default);
    }

    public void loadImageCenterCrop(Object urlOrFileOrPath, ImageView imageView, int loadingRes) {
        Glide.with(this)
                .load(urlOrFileOrPath)
                .apply(
                        getDefaultRequestOption(loadingRes).centerCropTransform()
                ).into(imageView);
    }

    public void loadImageFitCenter(Object urlOrFileOrPath, ImageView imageView) {
        loadImageFitCenter(urlOrFileOrPath, imageView, R.drawable.ic_default);
    }

    public void loadImageFitCenter(Object urlOrFileOrPath, ImageView imageView, int loadingRes) {
        Glide.with(this)
                .load(urlOrFileOrPath)
                .apply(
                        getDefaultRequestOption(loadingRes).fitCenterTransform()
                ).into(imageView);
    }

    public void loadImageCenterInside(Object urlOrFileOrPath, ImageView imageView) {
        loadImageCenterInside(urlOrFileOrPath, imageView, R.drawable.ic_default);
    }

    public void loadImageCenterInside(Object urlOrFileOrPath, ImageView imageView, int loadingRes) {
        Glide.with(this)
                .load(urlOrFileOrPath)
                .apply(
                        getDefaultRequestOption(loadingRes).centerInsideTransform()
                ).into(imageView);
    }

    public void loadImageRound(Object urlOrFileOrPath, ImageView imageView) {
        loadImageRound(urlOrFileOrPath, imageView, R.drawable.ic_default);
    }

    public void loadImageRound(Object urlOrFileOrPath, ImageView imageView, int loadingRes) {
        Glide.with(this)
                .load(urlOrFileOrPath)
                .apply(
                        getDefaultRequestOption(loadingRes).circleCropTransform()
                ).into(imageView);
    }
    // ------ End ------

    // startActivity & Intent
    // ------ Start ------
    public static String EXTRA_COMMON_DATA_BEAN = "extra_data_bean";
    public static int REQUEST_CODE_COMMON = 9999;
    public Serializable mCommonBean;

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

    public void startAct(Class<?> cls, Serializable... bean) {
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

    public void startActForResult(Class<?> cls, Serializable... bean) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(EXTRA_COMMON_DATA_BEAN, bean);
        startActivityForResult(intent, REQUEST_CODE_COMMON);
    }

    public void setResult(int resultCode, Serializable bean) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_COMMON_DATA_BEAN, bean);
        super.setResult(resultCode, intent);
    }
    // ------ End ------

    // Permission
    // ------ Start ------
    PermissionChecker mPermissionChecker;
    PermissionRequestCallback mPermissionCallback;

    public void mayRequestPermission(String[] permissionArr, PermissionRequestCallback callback) {
        if (mPermissionChecker == null) {
            mPermissionChecker = new PermissionChecker(this);
        }
        mPermissionCallback = callback;
        if (mPermissionChecker.isLackPermissions(permissionArr)) {
            mPermissionChecker.requestPermissions();
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (mPermissionChecker.hasAllPermissionsGranted(grantResults)) {
                    // 执行你的相关操作
                    mPermissionCallback.onSuccess();
                } else {
                    // 权限拒绝后的提示
                    mPermissionChecker.showDialog();
                }
                break;
        }
    }

    public interface PermissionRequestCallback {
        void onSuccess();
    }
    // ------ End ------
}
