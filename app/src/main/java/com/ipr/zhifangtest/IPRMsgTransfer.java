package com.ipr.zhifangtest;

/**
 * Created by fdd on 2017/07/04.
 */

public class IPRMsgTransfer extends MsgTransferInf {
    public static final String TAG = "IPRMsgTransfer";
    public IPRMsgTransfer() {
        init(new IPRMsgManage());
    }

    @Override
    public void doTask() {

//        Log.d(TAG, "doTask: running !!! ");

        String str = msgBuffer.toString().trim();
        int firstIndex =  str.indexOf("AA55");
        if (str.length()<=0 || firstIndex == -1){
            return;
        }
        str = str.substring(firstIndex);
        msgBuffer.delete(0,firstIndex);
//        Log.d(TAG, "doTask: "+str);
        String[] orders = str.split("AA55");

        for (int i = 0; i<orders.length; i++){
            String order = orders[i];
            if (order.length()>0 && order.length()!=22 && i!=orders.length-1){
                msgBuffer.delete(0,order.length()+4);
//                continue;
            }else if (order.length()==22){
                msgManage.parseOrder(order);
                msgBuffer.delete(0,order.length()+4);
            }
//            Log.d(TAG, "doTask: "+msgBuffer.toString());
        }


//        msgManage.parseOrder(msgBuffer.toString());
    }
}
