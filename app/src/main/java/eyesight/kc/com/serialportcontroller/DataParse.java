package eyesight.kc.com.serialportcontroller;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 *  2018/1/31.
 */

public class DataParse {

    int i = 0;


    public synchronized ControlleBean parseReceivedData(String data) {

//         if(null==data)
//             return null;
//         if (data.length()<37)
//             return null;


        if (data != null && data.length() > 36) {


            Log.d(TAG, "parseReceivedData: receivedData[10]" + data);
            Log.d(TAG, "parseReceivedData: receivedData[10]" + data.length());
            Log.d(TAG, "parseReceivedData: receivedData[10]" + data.trim().length());
            byte[] receivedData = HexDump.hexStringToBytes(data.trim());
            if (receivedData.length != 37)
                return null;
            Log.d(TAG, "parseReceivedData: 记录次数" + (++i));
            Log.d(TAG, "parseReceivedData: 接受到数据的长度" + receivedData.length);

            Log.d(TAG, "parseReceivedData: receivedData[10]" + HexDump.byte2Hex(receivedData[10]));
            Log.d(TAG, "parseReceivedData: receivedData[11]" + HexDump.byte2Hex(receivedData[11]));

            ControlleBean controlleBean = new ControlleBean();

            if ("6D".equals(HexDump.byte2Hex(receivedData[10])) && "01".equals(HexDump.byte2Hex(receivedData[11]))) {

                //00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36
                //FF FF 22 00 00 00 00 00 01 02 6D 01 00 00 00 00 00 7F 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 02 15
                //第十三位代表温度
                controlleBean.setTem(Integer.parseInt(HexDump.byte2Hex(receivedData[13]), 16) + 16 + "");
//            Log.d(TAG, "parseReceivedData: " + HexDump.byte2Hex(receivedData[12]));
//            Log.d(TAG, Integer.parseInt(HexDump.byte2Hex(receivedData[12]), 16) + 16 + "");
                int tem = Integer.parseInt(HexDump.byte2Hex(receivedData[35]), 16) + 16;
                Log.d(TAG, "parseReceivedData:当前温度为 " + tem);

                //第二十二和二十三位代表的是模式，00 00自动模式，00 01制冷模式，00 02制热模式，00 03送风模式，00 04除湿模式
                switch (receivedData[23]) {
                    case 00:
                        Log.d(TAG, "parseReceivedData:当前模式 " + "自动模式");
                        break;
                    case 01:
                        Log.d(TAG, "parseReceivedData:当前模式 " + "制冷模式");
                        break;
                    case 02:
                        Log.d(TAG, "parseReceivedData:当前模式 " + "制热模式");
                        break;
                    case 03:
                        Log.d(TAG, "parseReceivedData:当前模式 " + "送风模式");
                        break;
                    case 04:
                        Log.d(TAG, "parseReceivedData:当前模式 " + "除湿模式");
                        break;
                    default:
                        break;
                }

                //第二十四和二十五位代表的是风速，00 03自动风，00 02低风，00 01为中风，00 00为高风；

                switch (receivedData[25]) {
                    case 03:
                        Log.d(TAG, "parseReceivedData:当前风速 " + "自动风");
                        break;
                    case 02:
                        Log.d(TAG, "parseReceivedData:当前风速 " + "低风");
                        break;
                    case 01:
                        Log.d(TAG, "parseReceivedData:当前风速 " + "中风");
                        break;
                    case 00:
                        Log.d(TAG, "parseReceivedData:当前风速 " + "高风");
                        break;
                    default:
                        break;
                }
                //第二十九位代表wordA代表开关机
                switch (HexDump.getBitArray(receivedData[29])[7]) {
                    case 0:
                        Log.d(TAG, "parseReceivedData:当前空调的开关机状态为 " + "关机");
                        break;
                    case 1:
                        Log.d(TAG, "parseReceivedData:当前空调的开关机状态为 " + "开机");
                        break;
                    default:
                        break;
                }


            }

            Map<String, String> map = new HashMap<>();
            map.put("TEM", "24");
            sendControlleCommond(map);

            return controlleBean;
        }

        return null;
    }

    String Tem = "FF FF 0C 00 00 00 00 00 01 01 5D 01 00 00 6F";
    String Mode="FF FF 0C 00 00 00 00 00 01 01 5D 08 00 00 73";

    /**
     * 中包含了用户想改变的值，如模式，风速等等
     * 中包含了当前的获取到的十六进制的数
     * 若是用户只想单纯的改变某一项数据，则ControlleBean的其他属性为空，仅仅只有要改变的属性不为空
     */
    public String sendControlleCommond(Map<String, String> map) {

        //设定温度
        if (map != null && map.containsKey("TEM")) {
            String tem = map.get("TEM");
            int i = Integer.valueOf(tem) - 16;
            String value = Integer.toHexString(i);
            StringBuilder sb = new StringBuilder(Tem);
            sb.replace(40, 41, value);
            if (i <= 2) {
                switch (i) {
                    case 0:
                        sb.replace(44, 45, "C");
                        break;
                    case 1:
                        sb.replace(44, 45, "D");
                        break;
                    case 2:
                        sb.replace(44, 45, "E");
                        break;
                    default:
                        break;
                }
            }
            Log.d(TAG, "sendControlleCommond: " + sb.toString());
            return sb.toString();
        }

        if (map.containsKey("MODE")){
            String mode = map.get("MODE");
            switch (mode){
                case "DRYMODE":
                    return ControleCommond.DRYMODE;
                case "AUTOMODE":
                    return ControleCommond.AUTOMODE;
                case "HEATMODE":
                    return ControleCommond.HEATMODE;
                case "CODEMODE":
                    return ControleCommond.CODEMODE;
                case "FANMODE":
                    return ControleCommond.FANMODE;
                default:
                    break;
            }

        }


        if (map.containsKey("FAN")){
            String fan = map.get("FAN");
            switch (fan){
                case "MINFAN":
                    return ControleCommond.MINFAN;
                case "MIDFAN":
                    return ControleCommond.MIDFAN;
                case "HIGHFAN":
                    return ControleCommond.HIGHFAN;
                case "AUTOFAN":
                    return ControleCommond.AUTOFAN;
                default:
                    break;
            }
        }
        return null;
    }

}
