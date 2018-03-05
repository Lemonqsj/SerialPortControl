package eyesight.kc.com.serialportcontroller;

/**
 * Describe the function of the class
 *
 * @author lemon
 * @date 2017/11/1
 * @time 11:30
 * @Email lemonqsj@163.com
 * @description Describe the place where the class needs to pay attention.
 */

public class HexDump {



    /**
     * 将字符串形式表示的十六进制数转换为byte数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        String[] hexStrings = hexString.split(" ");
        byte[]   bytes      = new byte[hexStrings.length];
        for (int i = 0; i < hexStrings.length; i++) {
            char[] hexChars = hexStrings[i].toCharArray();
            bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
//        return (byte) "0123456789abcdef".indexOf(c);
        // 个人喜好,我喜欢用小写的
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    static public String byteArrToHex(byte[] inBytArr, int size) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            strBuilder.append(byte2Hex(inBytArr[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    /**
     * 1字节转2个Hex字符
     *
     * @param inByte
     * @return
     */
    static public String byte2Hex(Byte inByte) {
        return String.format("%02x", inByte).toUpperCase();
    }



    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] getBitArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }
    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }


}
