package com.siamin.fivestart.controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.siamin.fivestart.R;
import com.siamin.fivestart.helpers.IndicatorHelper;
import com.siamin.fivestart.helpers.MessageHelper;
import com.siamin.fivestart.interfaces.BluetoothInterface;
import com.siamin.fivestart.models.BluetoothModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class DataController {
    private Context context;
    private BluetoothAdapter myblu;
    private BluetoothSocket myso;
    private BluetoothDevice mydi;
    private OutputStream myout;
    private InputStream myin;
    private Thread Thread;
    volatile private boolean stop;
    private byte[] rb;
    private String data;
    private int red;
    private MessageHelper messageHelper;
    private String TAG = "TAG_BluetoothDataController";
    private IndicatorHelper indicatorHelper;

    public DataController(Context context, MessageHelper messageHelper) {
        this.context = context;
        this.messageHelper = messageHelper;
        this.indicatorHelper = new IndicatorHelper(context);
    }

    public void Connection(BluetoothModel model) {
        try {
            indicatorHelper.show();

            findbt(model.getIp());
            onbt();
        } catch (IOException e) {
            ((BluetoothInterface) context).notSelectBlutooth(true);
            e.printStackTrace();
        }
    }

    void onbt() throws IOException {
        UUID uu = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        myso = mydi.createRfcommSocketToServiceRecord(uu);
        myso.connect();
        myout = myso.getOutputStream();
        myin = myso.getInputStream();
        beginlisenfordata();

        indicatorHelper.dismiss();
        messageHelper.SuccssesMessage(context.getString(R.string.BluetoothConnected));
    }

    public void beginlisenfordata() {
        final Handler handler = new Handler();
        final byte delimtire = 84;
        red = 0;
        rb = new byte[1024];
        Thread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && !stop)
                {

                    try {
                        int bytesavailable = myin.available();
                        if(bytesavailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesavailable];
                            myin.read(packetBytes);
                            for(int i=0;i<bytesavailable;i++)
                            {
                                byte b = packetBytes[i];
                                Log.i(TAG," b = > "+ b);
                                if(b == delimtire)
                                {
                                    byte[] encodeBytes = new byte[red];
                                    System.arraycopy(rb, 0, encodeBytes, 0, encodeBytes.length);
                                    data += new String(encodeBytes,"UTF-8");
                                    red = 0;
                                    handler.post(new Runnable() {
                                        public void run() {
                                            Log.i(TAG,"Data = > "+data);
                                            ShowMessage(data);
                                        }
                                    });
                                }
                                else{
                                    rb[red++] =b;
                                }
                            }
                        }

                    } catch (Exception e) {
                        stop=true;
                    }
                }

            }
        });
        Thread.start();
    }

    public void findbt(String ip) {
        indicatorHelper.dismiss();
        myblu = BluetoothAdapter.getDefaultAdapter();
        if (myblu == null) {
            ((BluetoothInterface) context).notSelectBlutooth(true);

            return;
        }

        Set<BluetoothDevice> pairDevice = myblu.getBondedDevices();
        mydi = myblu.getRemoteDevice(ip);
        if (pairDevice.contains(mydi)) {

        }
    }

    public void sender(String code) {
        Log.i(TAG,"Data is =>  "+data);
        data = "";
        try {
            byte[] msga = code.getBytes();
            myout.write(msga);
            messageHelper.SuccssesMessage(context.getResources().getString(R.string.BluetoothMessageIsSend));
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    public boolean socketIsEnabled() {
        return stop;// myso.isConnected();
    }

    public void socketClose() {
        try {
            myso.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ShowMessage(String text){

        switch (text){
            case "HE SYSEM IS PARSE":
                //The System Is PartSet
                data = "";
                messageHelper.SuccssesMessage(context.getResources().getString(R.string.The_System_Is_PartSet));
                break;
            case "HE SYSEM IS SILEN":
                //The System Is Silent
                data = "";
                messageHelper.SuccssesMessage(context.getResources().getString(R.string.The_System_Is_Silent));
                break;

        }

    }
}




