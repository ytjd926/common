package com.tangjd.common.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tangjd.common.abs.BaseActivity;
import com.tangjd.common.abs.PermissionManager;
import com.tangjd.common.utils.StringUtil;

/**
 * Created by tangjd on 2016/9/26.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleSignInHelper extends BaseActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mSigningIn;
    private Handler mHandler = new Handler();
    public static int RSSI_ULTIMATE_VALUE = -40;
    public static final int REQUEST_CODE_ENABLE_BT = 1000;

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] data) {
            if (rssi < RSSI_ULTIMATE_VALUE) {
                return;
            }
            Log.e("TTT", "----------Rssi:" + rssi + "      " + device.getAddress() + "      " + device.getName());
            String filterMac = getFilterMac();
            if (!StringUtil.isEmpty(filterMac) && !device.getAddress().equalsIgnoreCase(filterMac)) {
                return;
            }
            if (!isSigningIn()) {
                setSigningIn(true);
                BtBean bean = new BtBean();
                bean.mDeviceName = device.getName();
                bean.mMajor = (data[25] << 8 & 65280) + (data[26] & 255);
                bean.mMinor = (data[27] << 8 & 65280) + (data[28] & 255);
                bean.mMac = device.getAddress();
                bean.mRssi = rssi;
                Log.e("TTT", "Rssi:" + rssi + " | " + bean.mDeviceName + " | " + bean.mMac + " | " + bean.mMajor + " | " + bean.mMinor);
                mBluetoothAdapter.stopLeScan(this);
                processLogin(device, bean);
            }
        }
    };

    public String getFilterMac() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showTipDialog("此设备不支持BLE", false, null);
            return;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            showTipDialog("此设备不支持蓝牙", false, null);
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PermissionManager.mayRequestAccessFineLocation(BleSignInHelper.this);
                // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
                // fire an intent to display a dialog asking the user to grant permission to enable it.
                if (!mBluetoothAdapter.isEnabled()) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BT);
                    }
                }
                if (!isSigningIn()) {
                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                }
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_CODE_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            showTipDialog("蓝牙已被禁止打开");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void processLogin(BluetoothDevice bluetoothDevice, BtBean bean) {
    }

    public void setSigningIn(boolean signingIn) {
        mSigningIn = signingIn;
    }

    private boolean isSigningIn() {
        return mSigningIn;
    }

    public void restartScan() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSigningIn(false);
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        }, 500);
    }

    public class BtBean {
        public String mDeviceName;
        public int mMajor;
        public int mMinor;
        public String mMac;
        public int mRssi;
    }
}
