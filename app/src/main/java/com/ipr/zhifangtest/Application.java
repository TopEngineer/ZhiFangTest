/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.ipr.zhifangtest;

import android.content.Intent;
import android.content.SharedPreferences;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class Application extends android.app.Application {
    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public static String path1 = "/dev/ttyS2";//连接单片机串口
    public static String path2 = "/dev/ttyS1";//连接打印机串口
    public static String path3 = "/dev/ttyS2";//连接体温枪
    public static String path4 = "/dev/ttyS2";//连接血压计
    public static String path5 = "/dev/ttyS2";//连接脂肪仪
    public static String path6 = "/dev/ttyS2";//连接身份证读卡器
    public static int baudrate = 9600;
    private Intent hwService;

    @Override
    public void onCreate() {
        super.onCreate();



        TypefaceProvider.registerDefaultIconSets();
        SharedPreferences sp = getSharedPreferences(MainConstant.DEVICE_SPF, MODE_PRIVATE);
        path1 = sp.getString(MainConstant.DEVICE_DPJ, "");
        path2 = sp.getString(MainConstant.DEVICE_PRINTER, "");
        path3 = sp.getString(MainConstant.DEVICE_TW, "");
        path4 = sp.getString(MainConstant.DEVICE_XY, "");
        path5 = sp.getString(MainConstant.DEVICE_ZF, "");
        path6 = sp.getString(MainConstant.DEVICE_SF, "");

        baudrate = sp.getInt(MainConstant.DEVICE_BAU,9600);



        hwService = new Intent(this, HWService.class);
        startService(hwService);

        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent(final TestEvent event) {
        if (hwService != null) {
            stopService(hwService);
            startService(hwService);
        }
    }

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences("com.ipr.babybalance_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

			/* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
//			path = "/dev/ttyS2";
//			baudrate = 50;
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

}
