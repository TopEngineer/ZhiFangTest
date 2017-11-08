package com.ipr.zhifangtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapDropDown;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;

import android_serialport_api.SerialPortFinder;

public class SystemTestActivity extends AppCompatActivity {
    private Application mApplication;
    private SerialPortFinder mSerialPortFinder;
    private BootstrapDropDown drop_dpj;
    private BootstrapDropDown drop_bra;
    private TextView tv_tem;
    private String[] devices;
    private String[] devicePaths;
    private String[] baudrates;
    private SharedPreferences spf;
    private SharedPreferences spf_dy;
    private SerialPortDevice dpjDevice;

    private SharedPreferences spf_ws;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_system_test);

        initData();

        EventBus.getDefault().register(this);
    }

    private void initData() {
        spf = getSharedPreferences(MainConstant.DEVICE_SPF, MODE_PRIVATE);
        spf_dy = getSharedPreferences(MainConstant.COMPANY_INFO, MODE_PRIVATE);
        spf_ws = getSharedPreferences(MainConstant.WAI_SHE,MODE_PRIVATE);
        mApplication = (Application) getApplication();
        mSerialPortFinder = mApplication.mSerialPortFinder;
//        mSerialPortFinder.getAllDevices();
        tv_tem = (TextView) findViewById(R.id.tv_tem);
        drop_dpj = (BootstrapDropDown) findViewById(R.id.drop_dpj);
        drop_bra = (BootstrapDropDown) findViewById(R.id.drop_bra);
        devices = mSerialPortFinder.getAllDevices();
        devicePaths = mSerialPortFinder.getAllDevicesPath();
        drop_dpj.setDropdownData(devices);
        baudrates = getResources().getStringArray(R.array.baudrates_name);
        drop_bra.setDropdownData(baudrates);
        drop_dpj.setText(spf.getString(MainConstant.DEVICE_DPJ, "选择串口"));
        drop_bra.setText(spf.getInt(MainConstant.DEVICE_BAU,9600)+"");
        drop_dpj.setOnDropDownItemClickListener(new BootstrapDropDown.OnDropDownItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int id) {
                drop_dpj.setText(devices[id]);
                spf.edit().putString(MainConstant.DEVICE_DPJ, devicePaths[id]).commit();
                Application.path1 = devicePaths[id];
                EventBus.getDefault().post(new TestEvent());
            }
        });
        drop_bra.setOnDropDownItemClickListener(new BootstrapDropDown.OnDropDownItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int id) {
                drop_bra.setText(baudrates[id]);
                spf.edit().putInt(MainConstant.DEVICE_BAU, Integer.parseInt(baudrates[id])).commit();
                Application.baudrate = Integer.parseInt(baudrates[id]);
                EventBus.getDefault().post(new TestEvent());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(HWMsgEvent event) {
        switch (event.getType()) {
            case 2:
                tv_tem.setText(event.getMsg()+"℃");
                break;
        }
    }


    private boolean istest = true;

    public void onTestClick(View v) {
        switch (v.getId()) {
            case R.id.bt_dpj_test:
               /* if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }*/
                startActivity(new Intent(this,MeasureTestDialogActivity.class).putExtra("test",0));
                break;
            case R.id.bt_sgtz_test:
                startActivity(new Intent(this,MeasureTestDialogActivity.class).putExtra("test",1));
                break;
            case R.id.tv_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-HH-mm");




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dpjDevice != null) {
            dpjDevice.closeSerialPort();
        }
        EventBus.getDefault().unregister(this);
    }
}
