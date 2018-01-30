package com.tangjd.common.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tangjd.common.abs.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public abstract class BluetoothBaseActivity extends BaseActivity {

    private static final String TAG = "BluetoothBaseActivity";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;

    public static final String EXTRA_FILTER_DEVICE_NAME_CONTAINS = "extra_filter_device_name_contains";

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    protected BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    protected BluetoothChatService mChatService = null;

    public int mConnectStatus;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
//			finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Performing this check in onResume() covers the case in which BT was
//        // not enabled during onStart(), so we were paused to enable it...
//        // onResume() will be called when ACTION_REQUEST_ENABLE activity
//        // returns.
//        if (mChatService != null) {
//            // Only if the state is STATE_NONE, do we know that we haven't
//            // started
//            // already
//            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
//                // Start the Bluetooth chat services
//                mChatService.start();
//            }
//        }
//    }

    /**
     * Start bluetooth service manually, if want to start automatically, excute the code in {@link #onResume()}.
     * And it will restart all threads of bluetooth.
     */
    public void startNewBTListener() {
        Log.i(TAG, "startNewBTListener manually.");
        if (mChatService != null) {
            mChatService.start();
        }
    }

    public void stopBTListener() {
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                } else {
                    Toast.makeText(this, "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                } else {
                    Toast.makeText(this, "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_SHORT).show();
//				finish();
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBTListener();
    }

//	/**
//	 * Manually start bletooth service, if need to change to auto start, add in onRuseme
//	 */
//	public void startBluetoothAcceptService() {
//		if (mChatService != null) {
//			// Only if the state is STATE_NONE, do we know that we haven't started already
//			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
//				// Start the Bluetooth chat services
//				mChatService.start();
//			}
//		}
//	}

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    protected void connectDevice(BluetoothDevice device, boolean secure) {
        mChatService.connect(device, secure);
    }

    public void connectDevice(String deviceAddress, boolean secure) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        mChatService.connect(device, secure);
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String message) {
        if (message.length() > 0) {
            sendMessage(message.getBytes());
        }
    }

    public void sendMessage(byte[] data) {

        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(BluetoothBaseActivity.this, "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        // Check that there's actually something to send
        if (data.length > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            mChatService.write(data);

            // Reset out string buffer to zero
            mOutStringBuffer.setLength(0);
        }
        Log.e(TAG, "sendMessage " + new String(data));
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    onBtStateChanged(msg.arg1);
                    mConnectStatus = msg.arg1;
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus("已连接到 " + mConnectedDeviceName);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus("连接中……");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus("未连接");
                            break;
                        case BluetoothChatService.STATE_CONNECTION_FAILED:
                            setStatus("连接失败");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.e(TAG, "BtWrite: " + writeMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    // Toast.makeText(BluetoothBaseActivity.this, "蓝牙已连接到 " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_CONNECTION_LOST:
                    onBtConnectionLost();
                    break;
            }
        }
    };

    /**
     * Makes this device discoverable.
     */
    @SuppressWarnings("unused")
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // Get a set of currently paired devices
    protected Set<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        return pairedDevices;
    }

    protected void searchDeviceForSecureConnection() {
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    protected void searchDeviceForSecureConnection(String filterDeviceNameContains) {
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        serverIntent.putExtra(EXTRA_FILTER_DEVICE_NAME_CONTAINS, filterDeviceNameContains);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    public void searchDeviceForInsecureConnection() {
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }

    public void searchDeviceForInsecureConnection(String filterDeviceNameContains) {
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        serverIntent.putExtra(EXTRA_FILTER_DEVICE_NAME_CONTAINS, filterDeviceNameContains);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }

    protected synchronized void onBtDataReceive(int length, byte[] receiptData) {
        if (Constants.LOGABLE) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(receiptData, 0, length);
            byte[] data = baos.toByteArray();
            Log.e(TAG, "BtReceipt: " + ByteUtil.bytesToHexString(data));
            data = null;
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void onBtStateChanged(int btState) {
        // TODO onBtStateChanged
    }

    public void setStatus(CharSequence subTitle) {
        // TODO set status in sub class
    }

    protected synchronized void onBtConnectionLost() {
        // TODO onBtConnectionLost
    }

}
