package com.tangjd.rs485;

import android.os.HandlerThread;
import android.serialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class SerialPortManager {
    private static final String TAG = "SerialPortManager";
    private SerialReadThread mReadThread;
    private OutputStream mOutputStream;
    private HandlerThread mWriteThread;
    private static SerialPortManager sManager = new SerialPortManager();

    public static SerialPortManager instance() {
        return sManager;
    }

    private SerialPort mSerialPort;

    private SerialPortManager() {
    }

    public SerialPort open(Device device) {
        return open(device.getPath(), device.getBaudrate());
    }

    public SerialPort open(String devicePath, String baudrateString) {
        if (mSerialPort != null) {
            close();
        }
        try {
            File device = new File(devicePath);
            int baurate = Integer.parseInt(baudrateString);
            mSerialPort = new SerialPort(device, baurate, 0);
            mReadThread = new SerialReadThread(mSerialPort.getInputStream());
            mReadThread.start();
            mOutputStream = mSerialPort.getOutputStream();
            mWriteThread = new HandlerThread("write-thread");
            mWriteThread.start();
            return mSerialPort;
        } catch (Throwable tr) {
            tr.printStackTrace();
            close();
            return null;
        }
    }

    public void close() {
        if (mReadThread != null) {
            mReadThread.close();
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mWriteThread != null) {
            mWriteThread.quit();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    public void sendCommand(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
