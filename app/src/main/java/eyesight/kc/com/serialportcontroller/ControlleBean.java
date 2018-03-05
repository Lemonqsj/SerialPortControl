package eyesight.kc.com.serialportcontroller;

/**
 * Created by shangji.qi on 2018/1/31.
 */

public class ControlleBean {

    private String mode;
    private String tem;
    private String fan;
    private boolean isPowerOn;
    private boolean isMute;


    public ControlleBean() {
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public boolean isPowerOn() {
        return isPowerOn;
    }

    public void setPowerOn(boolean powerOn) {
        isPowerOn = powerOn;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    @Override
    public String toString() {
        return "ControlleBean{" +
                "mode='" + mode + '\'' +
                ", tem='" + tem + '\'' +
                ", fan='" + fan + '\'' +
                ", isPowerOn=" + isPowerOn +
                ", isMute=" + isMute +
                '}';
    }
}
