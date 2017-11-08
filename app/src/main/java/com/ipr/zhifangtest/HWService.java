package com.ipr.zhifangtest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class HWService extends Service {
    private SerialPortDevice mSerialPortDevice;
    private DPJReceiver receiver;

    public HWService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        mSerialPortDevice = new SerialPortDevice(Application.path1, Application.baudrate);
        mSerialPortDevice.setMsgTransfer(new IPRMsgTransfer());

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ipr.send");
        receiver = new DPJReceiver();
        registerReceiver(receiver, filter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSerialPortDevice != null) {
            mSerialPortDevice.closeSerialPort();
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }


    class DPJReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Log.d(TAG, "onReceiver: " + msg);
            Log.d(TAG, "jiaoyan" + jiaoYan(msg));
            if (mSerialPortDevice != null) {
                mSerialPortDevice.sendBuffer(DataUtil.hexStringToBytes(msg));
            }
        }
    }

    private String jiaoYan(String order) {
        byte[] bys = DataUtil.hexStringToBytes(order);
        byte b = 0;
        for (int i = 0; i < 33; i++) {
            b += bys[i];
        }
        b = (byte) (255 - b + 1);
        return DataUtil.toHex(b);
//        Log.d(TAG, "jiaoYan: "+DataUtil.toHex(b));
    }

}
