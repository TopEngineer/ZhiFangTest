package com.ipr.zhifangtest;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fdd on 2016/11/16.
 */

public abstract class MsgTransferInf {
    private static final String TAG = "MsgTransferInf";

    public StringBuffer msgBuffer; //接收数据缓冲区
    public Timer timer;
    public TimerTask timerTask;
    public MsgManageInf msgManage;

    public void init(MsgManageInf msgManage){
        msgBuffer = new StringBuffer();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                doTask();
            }
        };
        timer.schedule(timerTask,100,100);
        this.msgManage = msgManage;
    }

    public abstract void doTask();

    public void addMsgBuffer(String msg){
        this.msgBuffer.append(msg.trim());
        Log.d(TAG, "addMsgBuffer: "+msg);
    }

    public void clearBuffer(){
        this.msgBuffer.delete(0,msgBuffer.length());
    }
    public void stopTimer(){
        timer.cancel();
        msgBuffer.delete(0,msgBuffer.length());
//        timer = null;
    }
}
