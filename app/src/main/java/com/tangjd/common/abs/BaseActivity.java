package com.tangjd.common.abs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tangjd.common.R;

/**
 * Created by tangjd on 2015/12/14.
 */
public class BaseActivity extends AppCompatActivity {
    // Permission
    // ------ Start ------
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
    // ------ End ------

    // Toolbar
    // ------ Start ------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private Toolbar mToolbar;

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

    public void showToast() {
        showToast("加载中...");
    }

    public void showToast(String message) {
        if (!isFinishing()) {
            if (mToast == null) {
                mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
            }
            mToast.setText(message);
            mToast.show();
        }
    }

    public void dismissToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void showShortToast(String message) {
        if (!isFinishing()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showLongToast(String message) {
        if (!isFinishing()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    // ------ End ------

    // Dialog
    // ------ Start ------
    public void showTipDialog(String message) {
        showTipDialog(message, true, null);
    }

    public void showTipDialog(String message, DialogInterface.OnClickListener onPositiveClick) {
        showTipDialog(message, false, onPositiveClick);
    }

    public void showTipDialog(String message, boolean cancelable, DialogInterface.OnClickListener onPositiveClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, onPositiveClick);
        builder.setCancelable(cancelable);
        if (!isFinishing())
            builder.create().show();
    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(positiveText, onPositiveClick);
        builder.setNegativeButton(negativeText, onNegativeClick);
        if (!isFinishing()) {
            builder.create().show();
        }
    }
    // ------ End ------

    // ProgressDialog
    // ------ Start ------
    private ProgressDialog mProgressDialog;

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
    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, R.drawable.ic_default);
    }

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
    // ------ End ------
}
