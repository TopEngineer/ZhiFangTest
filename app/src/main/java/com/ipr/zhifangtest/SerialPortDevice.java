package com.ipr.zhifangtest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 * 串口操作类
 *
 * @author Jerome
 */
public class SerialPortDevice {
    private String TAG = SerialPortDevice.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;
    private MsgTransferInf msgTransferInf;

    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public SerialPortDevice(String path, int baudrate) {
        onCreate(path, baudrate);
    }

    public void setMsgTransfer(MsgTransferInf msgTransferInf){
        if (this.msgTransferInf!=null){
            this.msgTransferInf.stopTimer();
            this.msgTransferInf = null;
        }
        this.msgTransferInf = msgTransferInf;
    }
    /**
     * 初始化串口信息
     */
    public void onCreate(String path, int baudrate) {
        try {
            mSerialPort = getSerialPort(path, baudrate);
//            mSerialPort.
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            isStop = false;
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        try {
            byte[] mBuffer = (cmd + "\r\n").getBytes("gbk");
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
//        String tail = "\r\n";
//        byte[] tailBuffer = tail.getBytes();
//        byte[] mBufferTemp = new byte[mBuffer.length + tailBuffer.length];
//        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
//        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mInputStream == null)
                        return;
                    byte[] buffer = new byte[512];
//                    mInputStream.available();
                    size = mInputStream.read(buffer);
                    if (size > 0) {
//                        if(MyLog.isDyeLevel()){
//                            MyLog.log(TAG, MyLog.DYE_LOG_LEVEL, "length is:"+size+",data is:"+new String(buffer, 0, size));
//                        }
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);
                        }
                        if (null != msgTransferInf){
                            msgTransferInf.addMsgBuffer(DataUtil.bytesToHexString(buffer,size).toUpperCase());
                        }
                    }
//                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
//        sendShellCommond1();
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
        if (msgTransferInf != null){
            msgTransferInf.stopTimer();
        }
    }

    public SerialPort getSerialPort(String path, int baudrate) throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }


}