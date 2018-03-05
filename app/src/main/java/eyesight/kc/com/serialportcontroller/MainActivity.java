package eyesight.kc.com.serialportcontroller;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tw.com.prolific.driver.pl2303.PL2303Driver;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "eyesight.kc.com.serialportcontroller.USB_PERMISSION";
    private static final String TAG = "==controller===";
    private static PL2303Driver mSerial;
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;


    //    private Context mContext;
    //    private final InstructionIOThreadPool mInstructionIOThreadPool;
    private ExecutorService mExecutorService;
    String test="FF FF 0A 00 00 00 00 00 01 01 4D 01 5A";
//    String data = "FF FF 0C 40 00 00 00 00 00 01 5D 01 00 01 AC 56 47";
    String opoen = "FF FF 0A 00 00 00 00 00 01 01 4D 02 5B";
    String device = "FF FF 0A 00 00 00 00 00 01 01 4D 01 5A";
    String close = "FF FF 0A 00 00 00 00 00 01 01 4D 03 5C";
    String highFan = "FF FF 0C 00 00 00 00 00 01 01 5D 07 00 00 72 ";
    String query = "FF FF 0A 00 00 00 00 00 01 01 4D 01 5A";
    public static volatile boolean isWrite = false;
    public static volatile boolean isRead = false;

    private DataParse dataParse;
//    private Handler handler;
    private Thread selectTherad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        handler = new Handler(getMainLooper());

        mExecutorService = Executors.newSingleThreadExecutor();
        selectTherad = new Thread(new selectTask());

        mSerial = new PL2303Driver((UsbManager) this.getSystemService
                (Context.USB_SERVICE), this, ACTION_USB_PERMISSION);

        dataParse = new DataParse();

        //open serial
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUsbSerial();
                if (!selectTherad.isAlive())
                selectTherad.start();
                writeDataToSerialPort(test);

            }
        });


        //open air conditioner
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeDataToSerialPort(opoen);

            }
        });
        //query data every 5s

        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDataToSerialPort(device);

            }
        });

        //set highFan
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                writeDataToSerialPort(highFan);
            }


        });

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDataToSerialPort(test);

            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                writeDataToSerialPort(close);
//                selectTherad.interrupt();

            }


        });

    }




    private void checkUsbSerialPortFunction() {


        if (!mSerial.isConnected()) {
            Log.d(TAG, "New instance : " + mSerial);
            if (!mSerial.enumerate()) {
                mSerial.InitByBaudRate(mBaudrate, 700);
//                Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d(TAG, "onResume:enumerate succeeded!");
            }
        }//if isConnected

        if (!mSerial.PL2303Device_IsHasPermission()) {
            Log.d(TAG, "serial no permission to connect");
        }
        // check USB host function.
        if (!mSerial.PL2303USBFeatureSupported()) {
//            Toast.makeText(this, "无支持USB主机", Toast.LENGTH_SHORT)
//                    .show();
            Log.d(TAG, "无支持USB主机");
            mSerial = null;
        }


    }

    synchronized private String readData() {




        if (null == mSerial) {
            return "serial is null";
        }
//            mSerial = getSerialSingleInstance(mContext);


        if (!mSerial.isConnected()) {
            checkUsbSerialPortFunction();
//            return "serial have't connected";

        }

        final byte[] rbuf = new byte[1024];
        final StringBuffer sbHex = new StringBuffer();

        Runnable runnable = new Runnable() {


            int len = 0;

            @Override
            public void run() {
                isRead=true;

                len = mSerial.read(rbuf);
                if (len < 0) {
                    Log.d(TAG, "Fail to bulkTransfer(read data)");
                    return;
                }
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                if (len>37){
                    Log.d(TAG, "Fail to bulkTransfer(读取丢失数据)读取到的数据长度为：" + len);
                }

//                if (len>0&&len<37){
//                    len1 = mSerial.read(rbuf2);
//                }

//                if (len+len1==37){
//                    for (int j = 0; j < len; j++) {
//
//                        sbHex.append(HexDump.byte2Hex(rbuf[j]) + " ");
//                    }
//                    for (int j = 0; j < len1; j++) {
//
//                        sbHex.append(HexDump.byte2Hex(rbuf2[j]) + " ");
//                    }
//                }

                while (len > 0 && len < 37 ) {
                    Log.d(TAG, "Fail to bulkTransfer(读取丢失数据)读取到的数据长度为：" + len);
                    len= mSerial.read(rbuf);
                }

                if (len ==37) {
                    Log.d(TAG, "read len : " + len);
                    for (int j = 0; j < len; j++) {

                        sbHex.append(HexDump.byte2Hex(rbuf[j]) + " ");
                    }
                    Log.d(TAG, "len=" + len + "....receive data " + sbHex.toString());
                    isRead=false;
//                    Log.d(TAG, "--------------------------------------read data end  -----------------------");
                }


                else if (len == 0) {
//                    Log.d(TAG, "read len : 0 ");

//                    this.run();
//                    Log.d(TAG, "--------------------------------------read data end  -----------------------");
                }

            }
        };

        while (true) {
            if (!isWrite||!isRead) {
                mExecutorService.execute(runnable);
                break;
            }
        }


        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dataParse.parseReceivedData(sbHex.toString());



        return sbHex.toString();

    }


     synchronized private void writeDataToSerialPort(final String HexString) {


        if (!checkSerialPortState()) {
            Log.d(TAG, "serial port state is not active");
            return;
        }

        final byte []buffer=new byte[1024];

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isWrite = true;
                byte[] bytes = HexDump.hexStringToBytes(HexString);
                int res = mSerial.write(bytes, bytes.length);
                Log.d(TAG, " test " + res);
                if (res < 0) {
                    mSerial.read(buffer);
                    res = mSerial.write(bytes, bytes.length);
                }

                if (res < 0) {
                    Log.d(TAG, "setup2: fail to controlTransfer: " + res);
                    return;
                }

                isWrite = false;

                Log.d(TAG, "Write length: " + bytes.length + " bytes" + "...." + HexDump.byteArrToHex(bytes, bytes.length));
                Log.d(TAG, "Leave writeDataToSerial");
                Log.d(TAG, "--------------------------------------write data end  -----------------------");
            }
        };

        mExecutorService.execute(runnable);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void openUsbSerial() {

        // get service

        // check USB host function.
        if (!mSerial.PL2303USBFeatureSupported()) {
            Toast.makeText(this, "无支持USB主机", Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "无支持USB主机");
            mSerial = null;
        }
        if (null == mSerial)
            return;

        if (mSerial.isConnected()) {

            mBaudrate = PL2303Driver.BaudRate.B9600;

//             if (!mSerial.InitByBaudRate(mBaudrate)) {
            if (!mSerial.InitByBaudRate(mBaudrate, 700)) {

                if (!mSerial.PL2303Device_IsHasPermission()) {
                    Toast.makeText(this, "打开失败，可能权限不够！", Toast.LENGTH_SHORT).show();
                }

                if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    Toast.makeText(this, "打开失败，可能芯片不支持, 请选择PL2303HXD/RA/EA系列的芯片.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "connected :open serial successfully ", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connected :open serial successfully ");

            }
        } else {
            Log.d(TAG, "serial is not connected ");
        }
        Log.d(TAG, "离开 openUsbSerial");
        Log.d(TAG, "--------------------------------------opnen sucessfully -----------------------");

    }


    @Override
    protected void onResume() {
        super.onResume();

        String action = getIntent().getAction();
        Log.d(TAG, "onResume:" + action);
        if (!mSerial.isConnected()) {
            if (!mSerial.enumerate()) {
                Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d(TAG, "onResume:enumerate succeeded!");
            }
        }//如果连接
        Toast.makeText(this, "attached", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Enter onDestroy");
        if (mSerial != null) {
            mSerial.end();
            mSerial = null;
//            isOpenThread = true;
        }

//        SerialPortController.GetInstance(mContext).closeUsbSerialPortConnection();
//        SerialPortController.GetInstance(MainActivity.this).closeUsbSerialPortConnection();
        super.onDestroy();
        Log.d(TAG, "Leave onDestroy");
    }


    public boolean checkSerialPortState() {

        if (mSerial != null && mSerial.isConnected()) {
            return true;
        } else {
            if (mSerial == null) {
                mSerial = new PL2303Driver((UsbManager) this.getSystemService
                        (Context.USB_SERVICE), this, ACTION_USB_PERMISSION);
            }
            if (!mSerial.isConnected()) {
                checkUsbSerialPortFunction();
            }
        }

        if (mSerial != null && mSerial.isConnected()) {
            return true;
        } else {
            if (mSerial == null) {
                Log.d(TAG, "Check serial port state serial is null");
                return false;
            }
            if (!mSerial.isConnected()) {
                Log.d(TAG, "Check serial port state serial not connected");
                return false;
            }
        }
        return false;
    }

    public class selectTask implements Runnable{

        @Override
        public void run() {
            while (true){

                try {
//                    selectTherad.sleep(100);
                    Thread.sleep(200);// 线程暂停10秒，单位毫秒
                    readData();
                    Thread.sleep(100);
                    writeDataToSerialPort(query);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
