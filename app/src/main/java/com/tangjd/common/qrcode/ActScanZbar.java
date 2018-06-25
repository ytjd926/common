package com.tangjd.common.qrcode;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;

import com.tangjd.common.R;
import com.tangjd.common.abs.BaseActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public abstract class ActScanZbar extends BaseActivity implements QRCodeView.Delegate {
    private static final String TAG = "TTTTTT";

    private QRCodeView mQRCodeView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scan_zbar_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQRCodeView.setDelegate(this);
        setToolbarTitle("扫描");
        enableBackFinish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpotDelay(500);
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showLongToast("打开相机出错");
        finish();
    }
}