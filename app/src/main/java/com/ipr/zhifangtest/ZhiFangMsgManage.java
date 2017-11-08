package com.ipr.zhifangtest;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by fdd on 2017/4/7.
 */

public class ZhiFangMsgManage implements MsgManageInf {
    private static final String TAG = "ZhiFangMsgManage";
    @Override
    public void parseOrder(String order) {
        Log.d(TAG, "parseOrder: "+order);
        if (order.startsWith("C177")){
            String zhifang = (Integer.parseInt(order.substring(4,6),16)+Integer.parseInt(order.substring(6,8),16)*256)/10.0+"";
            String bmi = (Integer.parseInt(order.substring(8,10),16)+Integer.parseInt(order.substring(10,12),16)*256)/10.0+"";
//            String daixie = new BigDecimal((Integer.parseInt(order.substring(12,14),16)+Integer.parseInt(order.substring(14,16),16)*256)/10.0).setScale(0, BigDecimal.ROUND_HALF_UP)+"";
            String daixie = Integer.parseInt(order.substring(12,14),16)+Integer.parseInt(order.substring(14,16),16)*256+"";
            String tizhi = "";
            String tixing = "";
            switch (Integer.parseInt(order.substring(16,18),16)){
                case 1:
                    tizhi="偏低";
                    break;
                case 2:
                    tizhi="标准";
                    break;
                case 3:
                    tizhi="偏高";
                    break;
                case 4:
                    tizhi="高";
                    break;
            }
            switch (Integer.parseInt(order.substring(18,20),16)){
                case 1:
                    tixing = "消瘦";
                    break;
                case 2:
                    tixing = "标准";
                    break;
                case 3:
                    tixing = "隐性肥胖";
                    break;
                case 4:
                    tixing = "健壮";
                    break;
                case 5:
                    tixing = "肥胖";
                    break;
            }
            Log.d(TAG, "parseOrder: "+zhifang+"_"+bmi+"_"+daixie+"_"+tizhi+"_"+tixing);
            EventBus.getDefault().post(new ZhiFangEvent(0,zhifang+"_"+bmi+"_"+daixie+"_"+tizhi+"_"+tixing));
        }else if (order.startsWith("C100")){
            String msg = order.substring(4,6);
            if ("00".equals(msg)){
                EventBus.getDefault().post(new ZhiFangEvent(1,"未知原因"));
            }else if ("02".equals(msg)){
                EventBus.getDefault().post(new ZhiFangEvent(1,"设备准备开始测量，但是测量者长时间没有手握设备。（建议重新开始依次测量）"));
            }else if ("03".equals(msg)){
                EventBus.getDefault().post(new ZhiFangEvent(1,"手握姿势不正确或者测量时手有晃动（建议以正确姿势测量、测量是请保持正确的姿势不变）"));
            }else if ("04".equals(msg)){
                EventBus.getDefault().post(new ZhiFangEvent(1,"参数不正确或者超过设备支持的范围。（建议检查输入的参数）"));
            }else if ("05".equals(msg)){
                EventBus.getDefault().post(new ZhiFangEvent(1,"测量结果超过系统支持范围。（建议检查输入的参数）"));
            }
        }

    }
}
