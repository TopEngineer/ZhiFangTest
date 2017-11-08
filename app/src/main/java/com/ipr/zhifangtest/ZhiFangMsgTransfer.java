package com.ipr.zhifangtest;

/**
 * Created by fdd on 2017/4/7.
 */

public class ZhiFangMsgTransfer extends MsgTransferInf {
    private final static String orderHead = "AA55";
    private final static int orderLength = 22;
    public ZhiFangMsgTransfer() {
        init(new ZhiFangMsgManage());
    }
    @Override
    public void doTask() {
        String str = msgBuffer.toString().trim();
        int firstIndex =  str.indexOf(orderHead);
        if (str.length()<=0 || firstIndex == -1){
            return;
        }
        str = str.substring(firstIndex);
        msgBuffer.delete(0,firstIndex);
//        Log.d(TAG, "doTask: "+str);
        String[] orders = str.split(orderHead);

        for (int i = 0; i<orders.length; i++){
            String order = orders[i];
            if (order.length()>0 && order.length()!=orderLength && i!=orders.length-1){
                msgBuffer.delete(0,order.length()+4);
//                continue;
            }else if (order.length()==orderLength){
                msgManage.parseOrder(order);
                msgBuffer.delete(0,order.length()+4);
            }
//            Log.d(TAG, "doTask: "+msgBuffer.toString());
        }
    }
}
