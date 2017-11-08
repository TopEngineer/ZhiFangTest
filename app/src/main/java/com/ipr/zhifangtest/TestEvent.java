package com.ipr.zhifangtest;

/**
 * Created by fdd on 2016/12/21.
 */

public class TestEvent {
    private boolean isTest;
    private byte[] bys;
    private int size;

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getBys() {
        return bys;
    }

    public void setBys(byte[] bys) {
        this.bys = bys;
    }
}
