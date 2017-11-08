package com.ipr.zhifangtest;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by fdd on 2017/07/04.
 */

public class IPRMsgManage implements MsgManageInf {
    private static final String TAG = "HWMsgManage";

    @Override
    public void parseOrder(String order) {
        Log.d(TAG, "AA55" + order);

        if (order.startsWith("B5") && order.endsWith(jiaoYan(order))) {
            Log.d(TAG, "身高" + parseValue(order));
            EventBus.getDefault().post(new HWMsgEvent(0, "" + Double.parseDouble(parseValue(order))));
        } else if (order.startsWith("B4") && order.endsWith(jiaoYan(order))) {
            Log.d(TAG, "启动菜单");
            EventBus.getDefault().post(new HWMsgEvent(1, "启动菜单"));
        } else if (order.startsWith("B6") && order.endsWith(jiaoYan(order))) {
            Log.d(TAG, "温度" + Double.parseDouble(parseTemValue(order)));
            EventBus.getDefault().post(new HWMsgEvent(2, "" + Double.parseDouble(parseTemValue(order))));
        } else if (order.startsWith("B700") && order.endsWith(jiaoYan(order))) {
          /*  Log.d(TAG, "去皮失败");
           String value = parsePeelValue(order);
            if ("000.0".equals(value)){
                EventBus.getDefault().post(new HWMsgEvent(3,"去皮值重置为"+ Double.parseDouble(value)+"千克"));
            }else{
                EventBus.getDefault().post(new HWMsgEvent(3,"当前去皮值为"+ Double.parseDouble(value)+"千克，与实际皮重不符"));
            }*/

//            EventBus.getDefault().postSticky();
        } else if (order.startsWith("B701") && order.endsWith(jiaoYan(order))) {
            Log.d(TAG, "去皮成功");
//            EventBus.getDefault().post(new HWMsgEvent(4,"去皮成功，去皮值为"+ Double.parseDouble(parsePeelValue(order))+"千克"));
        } else if (order.startsWith("C0") && order.endsWith(jiaoYan(order))) {
//            EventBus.getDefault().post(new HWMsgEvent(5,"测量完成"));
            EventBus.getDefault().post(new HWMsgEvent(5, parseValue1(order)));
        } else if (order.startsWith("A0") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(6, parseValue1(order)));
        } else if (order.startsWith("A600") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(7, "体重校准初始化"));
        } else if (order.startsWith("A601") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(8, "体重校准成功"));
        } else if (order.startsWith("A700") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(9, "身高校准初始化"));
        } else if (order.startsWith("A701") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(10, "身高校准成功"));
        } else if (order.startsWith("FE00") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(11, "关闭彩灯"));
        } else if (order.startsWith("FE01") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new HWMsgEvent(12, "打开彩灯"));
        }else if (order.startsWith("C1") && order.endsWith(jiaoYan(order))){//脂肪解析
//            if (order.startsWith("C100")){}
            parseZhiFangOrder(order);
        }else if (order.startsWith("E8") || order.startsWith("E9") || order.startsWith("EA") && order.endsWith(jiaoYan(order))){
            //血压解析
            parseXueYaOrder(order);
        }
    }




    private String jiaoYan(String order) {
        byte[] bys = DataUtil.hexStringToBytes("AA55" + order);
        byte b = 0;
        for (int i = 0; i < 12; i++) {
            b += bys[i];
        }
        b = (byte) (255 - b + 1);
        Log.d(TAG, "jiaoYan: " + DataUtil.toHex(b));
        return DataUtil.toHex(b);
    }

    //解析身高，体重--- Bx协议
    private String parseValue(String order) {
        return "" + order.charAt(3) + order.charAt(5) + order.charAt(7) + "." + order.charAt(9);
    }

    private String parseTemValue(String order) {
        if (order.startsWith("B601")) {
            return "-" + order.charAt(5) + order.charAt(7) + order.charAt(9) + "." + order.charAt(11);
        } else {
            return "" + order.charAt(5) + order.charAt(7) + order.charAt(9) + "." + order.charAt(11);
        }
    }

    //解析身高，体重---A0协议
    private String parseValue1(String order) {
        int a = Integer.valueOf("" + order.charAt(2) + order.charAt(3), 16);
        Double b = Integer.valueOf("" + order.charAt(4) + order.charAt(5), 16) / 10.0;
        int c = Integer.valueOf("" + order.charAt(6) + order.charAt(7), 16);
        Double d = Integer.valueOf("" + order.charAt(8) + order.charAt(9), 16) / 10.0;
        String str1 = a == 0 ? b + "" : a + "" + b;
        String str2 = c == 0 ? d + "" : c + "" + d;
        return str1 + "," + str2;
    }

    //解析去皮
    private String parsePeelValue(String order) {
        return "" + order.charAt(7) + order.charAt(9) + order.charAt(11) + "." + order.charAt(13);
    }

    public void parseZhiFangOrder(String order) {
        Log.d(TAG, "parseOrder: "+order);
        if (order.startsWith("C177")){
            String zhifang = (Integer.parseInt(order.substring(4,6),16)+Integer.parseInt(order.substring(6,8),16)*256)/10.0+"";
            String bmi = (Integer.parseInt(order.substring(8,10),16)+Integer.parseInt(order.substring(10,12),16)*256)/10.0+"";
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

    public void parseXueYaOrder(String order) {
        if (order.startsWith("E8") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new XueYaEvent(0,"响应测量血压"));
        } else if (order.startsWith("E9") && order.endsWith(jiaoYan(order))) {
            String xy = Integer.parseInt(order.substring(2,4),16)+"_"+(Integer.parseInt(order.substring(4,6),16)+25)+"_"+Integer.parseInt(order.substring(8,10),16);
            EventBus.getDefault().post(new XueYaEvent(1,xy));
        } else if (order.startsWith("EA") && order.endsWith(jiaoYan(order))) {
            EventBus.getDefault().post(new XueYaEvent(2,"血压测量失败"));
        }
    }
}
