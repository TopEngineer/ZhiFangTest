package com.ipr.zhifangtest;

/**
 * Created by fdd on 2017/4/7.
 */

public class ZhiFangEvent {
    private int type;
    private String msg;
    public ZhiFangEvent(int type,String msg){
        this.type = type;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getType() {
        return type;
    }
}
