package com.tangjd.common.rawsocket;

import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.tangjd.common.abs.BaseActivity;
import com.tangjd.common.utils.ByteUtil;

import java.net.InetSocketAddress;

/**
 * Author: Administrator
 * Date: 2017/10/18
 */

public abstract class RawSocketActivity extends BaseActivity {
    // 解析示例
//    private ByteArrayOutputStream mBaos = new ByteArrayOutputStream();
//    private static final int MIN_PACKET_LENGTH = 12;
//    private boolean mCanGetAnalasysResult = false;
//
//    /**
//     * 谱线数据
//     * AA 55 01 00 22 9F AF 44 [4096 data] 6E 0D 0A
//     * 分析结果返回
//     * AA 55 01 03 00 00 20 42 00 32 2C 43 6F 2D 36 30 20 B9 A4 D2 B5 20 30 2E 39 33 34 33 20 32 20 32 2C 54 68 2D 32 33 32 20 CC EC C8 BB 20 30 2E 37 36 35 38 20 31 20 32 2C 70 0D 0A
//     * AA 55 01 03 CD CC 0C 42 00 30 2C 51 0D 0A (没有识别出核素返回结果)
//     * AA 55 01 03 9A 99 C9 42 00 30 2C 96 0D 0A（真实数据）
//     * 状态返回
//     * AA 55 01 00 67 66 AE 42 00 EC 0D 0A
//     */
//    @Override
//    public synchronized void onDataReceived(byte[] data) {
//        if (data == null || data.length == 0) {
//            return;
//        }
//        mBaos.write(data, 0, data.length);
//        byte[] cachedData = mBaos.toByteArray();
//        if (cachedData.length < MIN_PACKET_LENGTH) {
//            return;
//        }
//        if (cachedData[0] != (byte) 0xAA || cachedData[1] != (byte) 0x55) {
//            throw new RuntimeException("数据出错");
//        }
//        int cachedDataLength = cachedData.length;
//        int startIndex = 0, endIndex = -1;
//        for (int i = 0; i < cachedDataLength; i++) {
//            if (cachedData[i] == (byte) 0x55 && cachedData[i - 1] == (byte) 0xAA) {
//                startIndex = i - 1;
//            }
//            if (cachedData[i] == (byte) 0x0A && cachedData[i - 1] == (byte) 0x0D) {
//                endIndex = i;
//                int singlePacketLength = endIndex - startIndex + 1;
//                final byte[] singlePacket = new byte[singlePacketLength];
//                System.arraycopy(cachedData, startIndex, singlePacket, 0, singlePacketLength);
//                byte deviceStatus = singlePacket[3];
//                switch (deviceStatus) {
//                    case 0x00:
//                        // TODO 空闲中
//                        break;
//                    case 0x01:
//                        // TODO 开始
//                        showLongSnackbar("开始测量……");
//                        mHandler.removeCallbacks(mGetPuxianRunnable);
//                        mHandler.post(mGetPuxianRunnable);
//                        mCanGetAnalasysResult = true;
//                        break;
//                    case 0x02:
//                        // TODO 测量中 a.发送测量指令返回
//                        break;
//                    case 0x03:
//                        // TODO 测量完毕
//                        mHandler.removeCallbacks(mGetPuxianRunnable);
//                        if (mCanGetAnalasysResult) {
//                            mCanGetAnalasysResult = false;
//                            sendMessage(RJCommand.ANALASYS_RESULT);
//                        }
//                        break;
//                    case 0x04:
//                        // TODO 非测量界面
//                        break;
//                    case (byte) 0x80:
//                        // TODO 错误指令
//                        showLongSnackbar("错误指令");
//                        break;
//                }
//                if (singlePacketLength == 12) {
//                    // 状态返回
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((FragmentSimple) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(0)).onGetData(FragmentSimple.byte2float(singlePacket, 4));
//                        }
//                    });
//                    Log.e("TTT", "Result1: " + ByteUtil.ByteArrayToHexString(singlePacket));
//                } else if (singlePacketLength > 4096) {
//                    // 谱线数据
//                    Log.e("TTT", "Result2: " + ByteUtil.ByteArrayToHexString(singlePacket));
//                } else if (singlePacketLength > 13 && singlePacket[10] == (byte) 0x2C) {
//                    // 分析结果，最少14位
//                    Log.e("TTT", "Result3: " + ByteUtil.ByteArrayToHexString(singlePacket));
//                } else {
//                    Log.e("TTT", "Result4: " + ByteUtil.ByteArrayToHexString(singlePacket));
//                }
//            }
//        }
//        mBaos.reset();
//        // endIndex初始值为-1，保证不漏字节
//        if (endIndex < cachedDataLength - 1) {
//            // 解析完多个Packet后剩余部分
//            mBaos.write(cachedData, endIndex + 1, cachedDataLength - (endIndex + 1));
//        }
//    }
    public static final String TAG = RawSocketActivity.class.getSimpleName();
    public boolean mConnected = false;
    private AsyncSocket mSocket;

    /**
     * @param address 如: 10.10.100.254
     * @param port    如: 8899
     */
    public void connect(String address, int port) {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(address, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if (ex != null) throw new RuntimeException(ex);
                mConnected = true;
                mSocket = socket;
                setCallBacks(socket);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onConnected();
                    }
                });
            }
        });
    }

    private void setCallBacks(final AsyncSocket socket) {
        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                final byte[] data = bb.getAllByteArray();
                Log.e(TAG, "Received: " + ByteUtil.ByteArrayToHexString(data));
                onDataReceived(data);
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                Log.e(TAG, "[Client] Successfully closed connection");
                mConnected = false;
                mSocket = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onConnectClosed();
                    }
                });
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                Log.e(TAG, "[Client] Successfully end connection");
                mConnected = false;
                mSocket = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onConnectEnd();
                    }
                });
            }
        });
    }

    public abstract void onConnected();

    public abstract void onDataReceived(byte[] data);

    public abstract void onConnectEnd();

    public abstract void onConnectClosed();

    public abstract void onMessageSent(byte[] message);

    public void sendMessage(String message) {
        sendMessage(message.getBytes());
    }

    public void sendMessage(final byte[] message) {
        Log.e("TTT", "Sending: " + ByteUtil.ByteArrayToHexString(message));
        Util.writeAll(mSocket, message, new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                Log.e("TTT", "Sent: " + ByteUtil.ByteArrayToHexString(message));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onMessageSent(message);
                    }
                });
            }
        });
    }
}
