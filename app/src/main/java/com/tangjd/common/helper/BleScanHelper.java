/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tangjd.common.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tangjd.common.abs.BaseActivity;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BleScanHelper extends BaseActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;

    private static final int REQUEST_ENABLE_BT = 1;
//    // Stops scanning after 10 seconds.
//    private static final long SCAN_PERIOD = 10000;

    private ConnectState mState = ConnectState.STATE_NONE;

    public enum ConnectState {
        STATE_NONE("无连接"), // we're doing nothing
        STATE_LISTEN("监听中"), // now listening for incoming connections
        STATE_CONNECTING("连接中"), // now initiating an outgoing connection
        STATE_CONNECTED("已连接"), // now connected to a remote device
        STATE_DISCONNECTED("已断开"),
        STATE_CONNECTION_FAILED("连接失败"); // now connected to a remote device
        public String mDesc;

        ConnectState(String desc) {
            mDesc = desc;
        }
    }

    public void setState(ConnectState state) {
        mState = state;
        if (state == ConnectState.STATE_NONE || state == ConnectState.STATE_LISTEN
                || state == ConnectState.STATE_CONNECTION_FAILED || state == ConnectState.STATE_DISCONNECTED) {
            scanLeDevice(true);
        } else if (state == ConnectState.STATE_CONNECTING || state == ConnectState.STATE_CONNECTED) {
            scanLeDevice(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        if (bluetoothManager == null) {
            showTipDialog("此设备不支持蓝牙", false, null);
            return;
        }
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            showTipDialog("此设备不支持蓝牙", false, null);
            return;
        }
        mFilterDeviceNameContains = getFilterDeviceName();
        mFilterRssi = getFilterRssi();

        mayRequestPermission(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        }, new PermissionRequestCallback() {
            @Override
            public void onSuccess() {
                if (mBluetoothAdapter.enable()) {
                    scanLeDevice(true);
                } else {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_scan:
//                mLeDeviceListAdapter.clear();
//                scanLeDevice(true);
//                break;
//            case R.id.menu_stop:
//                scanLeDevice(false);
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_OK) {
                scanLeDevice(true);
            }
        }
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position).mLeDevices;
//        if (device == null) return;
//        final Intent intent = new Intent(this, DeviceControlActivity.class);
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//        if (mScanning) {
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            mScanning = false;
//        }
//        startActivity(intent);
//    }

    public void scanLeDevice(final boolean enable) {
        if (mScanning && enable) {
            return;
        }
        if (!mScanning && !enable) {
            return;
        }
        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            boolean filterRssi = (mFilterRssi != 0);
            boolean filterDeviceName = (!TextUtils.isEmpty(mFilterDeviceNameContains));

            boolean filterRssiMatch = false;
            if (filterRssi) {
                filterRssiMatch = (rssi > mFilterRssi);
            }
            boolean filterDeviceNameMatch = false;
            if (filterDeviceName) {
                filterDeviceNameMatch = ((!TextUtils.isEmpty(device.getName())) && (device.getName().toLowerCase().contains(mFilterDeviceNameContains.toLowerCase())));
            }

            if ((filterRssi && (!filterRssiMatch)) || (filterDeviceName && (!filterDeviceNameMatch))) {
                return;
            }
            parse(device, rssi, scanRecord);
        }
    };

    private void parse(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TTTTTT", "----------Rssi:" + rssi + "      " + device.getAddress() + "      " + device.getName() + "      " + ((scanRecord[27] << 8 & 65280) + (scanRecord[28] & 255)));
                processLogin(device, rssi, scanRecord,
                        (scanRecord[25] << 8 & 65280) + (scanRecord[26] & 255),
                        (scanRecord[27] << 8 & 65280) + (scanRecord[28] & 255));
            }
        });
    }

    public String mFilterDeviceNameContains;
    public int mFilterRssi;

    public abstract String getFilterDeviceName();

    public abstract int getFilterRssi();

    public abstract void processLogin(BluetoothDevice device, int rssi, byte[] scanRecord, int major, int minor);
}