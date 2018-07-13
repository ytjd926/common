package com.tangjd.common.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tangjd.common.abs.BaseActivity;
import com.tangjd.common.utils.ByteUtil;
import com.tangjd.common.utils.Log;

import java.lang.reflect.Method;

/**
 * Created by tangjd on 2017/2/15.
 */

public abstract class NfcSignInHelper extends BaseActivity {

    public boolean mSignIning = false;
    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(final Tag tag) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSignIning) {
                        return;
                    }
                    mSignIning = true;
                    // 获取卡id
                    byte[] id = tag.getId();
                    String tagId = ByteUtil.ByteArrayToHexString(id);
                    Log.e("TTT", "发现设备：" + tagId);
                    processLogin(tagId);
                }
            });
        }
    };

    public abstract void processLogin(String tagId);

    @Override
    protected void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableReaderMode();
    }

    private NfcAdapter nfc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfc = NfcAdapter.getDefaultAdapter(this);
        Log.e("TTTTTT", "0" + (nfc == null ? " null" : " nfc initial success"));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void enableReaderMode() {
        if (nfc != null) {
            Log.e("TTTTTT", "1");
            if (!nfc.isEnabled()) {
                showTipDialog("请在设置页面打开NFC功能");
                return;
            }
            nfc.enableReaderMode(this, mReaderCallback, READER_FLAGS, null);
        } else {
            Log.e("TTTTTT", "2");
            showAlertDialog("设备NFC功能异常", "重试", "取消", false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Log.e("TTTTTT", "21");
                        Method methodGetNfcAdapter = NfcAdapter.class.getMethod("getNfcAdapter", Context.class);
                        Log.e("TTTTTT", "22");
                        nfc = (NfcAdapter) methodGetNfcAdapter.invoke(null, getApplicationContext());
                        Log.e("TTTTTT", "23" + (nfc == null ? " null" : " nfc invoke success"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLongToast("NFC初始化异常，请重启程序");
                        throw new NullPointerException("NFC adapter null");
                    }
                    enableReaderMode();
                }
            }, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void disableReaderMode() {
        Log.e("TTTTTT", 333 + "");
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }
}
