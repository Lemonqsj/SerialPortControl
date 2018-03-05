package eyesight.kc.com.serialportcontroller;

/**
 * Created by shangji.qi on 2018/2/23.
 */

public interface ControleCommond {
    String OPEN="FF FF 0A 00 00 00 00 00 01 01 4D 02 5B";
    String CLOSE="FF FF 0A 00 00 00 00 00 01 01 4D 03 5C";
    String AUTOMODE="FF FF 0C 00 00 00 00 00 01 01 5D 08 00 00 73";
    String CODEMODE="FF FF 0C 00 00 00 00 00 01 01 5D 08 00 01 74";
    String HEATMODE="FF FF 0C 00 00 00 00 00 01 01 5D 08 00 02 75";
    String FANMODE="FF FF 0C 00 00 00 00 00 01 01 5D 08 00 03 76";
    String DRYMODE="FF FF 0C 00 00 00 00 00 01 01 5D 08 00 04 77";
    String AUTOFAN="FF FF 0C 00 00 00 00 00 01 01 5D 07 00 03 75";
    String MINFAN="FF FF 0C 00 00 00 00 00 01 01 5D 07 00 02 74";
    String MIDFAN="FF FF 0C 00 00 00 00 00 01 01 5D 07 00 01 73";
    String HIGHFAN="FF FF 0C 00 00 00 00 00 01 01 5D 07 00 00 72";



}
