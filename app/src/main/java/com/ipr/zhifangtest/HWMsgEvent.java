package com.ipr.zhifangtest;

/**
 * Created by fdd on 2016/12/9.
 */

public class HWMsgEvent {
    private int type;
    private String msg;
    public HWMsgEvent(int type, String msg){
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
