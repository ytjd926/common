package com.tangjd.rs485;

import android.os.SystemClock;
import android.util.Log;

import com.tangjd.rs485.util.ByteUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SerialReadThread extends Thread {
    private static final String TAG = "SerialReadThread";
    private BufferedInputStream mInputStream;

    public SerialReadThread(InputStream is) {
        mInputStream = new BufferedInputStream(is);
    }

    @Override
    public void run() {
        byte[] received = new byte[1024];
        int size;
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {
                int available = mInputStream.available();
                if (available > 0) {
                    size = mInputStream.read(received);
                    if (size > 0) {
                        onDataReceive(received, size);
                    }
                } else {
                    SystemClock.sleep(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Thread.yield();
        }
    }

    private void onDataReceive(byte[] received, int size) {
        String hexStr = ByteUtil.bytes2HexStr(received, 0, size);
        Log.e("TTTTTT", hexStr);
    }

    public void close() {
        try {
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            super.interrupt();
        }
    }
}
