package com.siamin.fivestart.controllers;

import android.content.Context;
import android.util.Log;

import com.siamin.fivestart.helpers.SharedPreferencesHelper;

public class SmsReceiverConterller {

    private Context context;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private String keyPhoneReceive="ReceivePhone",keyMessage="ReceiveMessage";
    private String TAG = "TAG_";

    public SmsReceiverConterller(Context context){
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
        this.context = context;
    }

    public void removeReceivePhone(){
        sharedPreferencesHelper.SetCode(keyPhoneReceive,"null");
    }

    public void setReceivePhone(String phone){
        Log.i(TAG,"setReceivePhone");
        sharedPreferencesHelper.SetCode(keyPhoneReceive,phone);
    }

    public String getReceivePhone(){
        return sharedPreferencesHelper.get_Data(keyPhoneReceive,"null").replace("+98","0");
    }


    public void removeReceiveMessage(){
        sharedPreferencesHelper.SetCode(keyMessage,"null");
    }

    public void setReceiveMessage(String message){
        Log.i(TAG,"setReceiveMessage");
        sharedPreferencesHelper.SetCode(keyMessage,message);
    }

    public String getReceiveMessage(String phone){
        if (!getReceivePhone().equals("null") && getReceivePhone().equals(phone) ){
            return sharedPreferencesHelper.get_Data(keyMessage,"null");
        }else{
            return "null";
        }

    }


}
