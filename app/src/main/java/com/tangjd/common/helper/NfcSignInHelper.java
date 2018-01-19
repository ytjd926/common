package com.tangjd.common.helper;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;

import com.tangjd.common.abs.BaseActivity;
import com.tangjd.common.utils.ByteUtil;
import com.tangjd.common.utils.Log;

/**
 * Created by tangjd on 2017/2/15.
 */

public abstract class NfcSignInHelper extends BaseActivity {

    public boolean mSignIning = false;
    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void enableReaderMode() {
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            if (!nfc.isEnabled()) {
                showTipDialog("请在设置页面打开NFC功能");
                return;
            }
            nfc.enableReaderMode(this, mReaderCallback, READER_FLAGS, null);
        } else {
            showTipDialog("设备NFC功能异常");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void disableReaderMode() {
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }
}
