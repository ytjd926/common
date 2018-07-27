//package com.tangjd.rs485.activity;
//
//import android.os.Bundle;
//import android.serialport.SerialPortFinder;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.tangjd.rs485.Device;
//import com.tangjd.rs485.R;
//import com.tangjd.rs485.SerialPortManager;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//    @BindView(R.id.spinner_devices)
//    Spinner mSpinnerDevices;
//    @BindView(R.id.btn_open_device)
//    Button mBtnOpenDevice;
//    @BindView(R.id.btn_send_data)
//    Button mBtnSendData;
//
//    private Device mDevice;
//
//    private int mDeviceIndex;
//
//    private String[] mDevices;
//
//    private boolean mOpened = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        initDevice();
//        initSpinners();
//        updateViewState(mOpened);
//    }
//
//    @Override
//    protected void onDestroy() {
//        SerialPortManager.instance().close();
//        super.onDestroy();
//    }
//
//    private void initDevice() {
//        SerialPortFinder serialPortFinder = new SerialPortFinder();
//        mDevices = serialPortFinder.getAllDevicesPath();
//        if (mDevices.length == 0) {
//            mDevices = new String[]{"找不到串口设备"};
//        }
//        mDevice = new Device(mDevices[mDeviceIndex], "9600");
//    }
//
//    private void initSpinners() {
//        ArrayAdapter<String> deviceAdapter = new ArrayAdapter<>(this, R.layout.spinner_default_item, mDevices);
//        deviceAdapter.setDropDownViewResource(R.layout.spinner_item);
//        mSpinnerDevices.setAdapter(deviceAdapter);
//        mSpinnerDevices.setOnItemSelectedListener(this);
//    }
//
//    @OnClick({R.id.btn_open_device, R.id.btn_send_data})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_open_device:
//                switchSerialPort();
//                break;
//            case R.id.btn_send_data:
//                sendData();
//                break;
//        }
//    }
//
//    private void sendData() {
//        SerialPortManager.instance().sendCommand(new byte[]{0x01, 0x03, 0x00, 0x10, 0x00, 0x10, 0x45, (byte) 0xC3});
//    }
//
//    private void switchSerialPort() {
//        if (mOpened) {
//            SerialPortManager.instance().close();
//            mOpened = false;
//        } else {
//            mOpened = SerialPortManager.instance().open(mDevice) != null;
//            if (mOpened) {
//                Toast.makeText(this, "成功打开串口", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "打开串口失败", Toast.LENGTH_LONG).show();
//            }
//        }
//        updateViewState(mOpened);
//    }
//
//    private void updateViewState(boolean isSerialPortOpened) {
//        mBtnOpenDevice.setText(isSerialPortOpened ? "关闭串口" : "打开串口");
//        mSpinnerDevices.setEnabled(!isSerialPortOpened);
//        mBtnSendData.setEnabled(isSerialPortOpened);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch (parent.getId()) {
//            case R.id.spinner_devices:
//                mDeviceIndex = position;
//                mDevice.setPath(mDevices[mDeviceIndex]);
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//    }
//}
