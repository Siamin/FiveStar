package com.siamin.fivestart;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.siamin.fivestart.controllers.BluetoothController;
import com.siamin.fivestart.controllers.DataController;
import com.siamin.fivestart.controllers.SmsController;
import com.siamin.fivestart.controllers.SmsReceiverConterller;
import com.siamin.fivestart.controllers.SystemController;
import com.siamin.fivestart.controllers.PasswordController;
import com.siamin.fivestart.helpers.ConvertJsonHelper;
import com.siamin.fivestart.helpers.DialogHelper;
import com.siamin.fivestart.helpers.IndicatorHelper;
import com.siamin.fivestart.helpers.LanguageHelper;
import com.siamin.fivestart.helpers.MessageHelper;
import com.siamin.fivestart.helpers.PersianCalendarHelper;
import com.siamin.fivestart.helpers.SharedPreferencesHelper;

public abstract class MyActivity extends AppCompatActivity {

    public Intent ActivityPrevious;
    public SharedPreferencesHelper sp ;
    public SystemController systemController;
    public DialogHelper dialog;
    public MessageHelper message;
    public PasswordController passwordController;
    public BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    public BluetoothController bluetoothController;
    public DataController dataController;
    public SmsController smsController;
    public ConvertJsonHelper convertJsonHelper;
    public SmsReceiverConterller smsReceiverConterller;
    public IndicatorHelper indicatorHelper;
    public LanguageHelper languageHelper;
    public PersianCalendarHelper persianCalendarHelper;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        languageHelper = new LanguageHelper(this);
        languageHelper.loadLanguage();

        persianCalendarHelper = new PersianCalendarHelper();

        sp = new SharedPreferencesHelper(getApplicationContext());

        smsReceiverConterller = new SmsReceiverConterller(this);
        systemController = new SystemController(this,sp);
        passwordController = new PasswordController(this,sp);
        message = new MessageHelper(this);

        bluetoothController = new BluetoothController(this);
        dataController = new DataController(this,message);
        smsController = new SmsController(this,message);
        dialog = new DialogHelper(this,systemController,message);
        convertJsonHelper = new ConvertJsonHelper(this);
        indicatorHelper = new IndicatorHelper(this);
    }

    public boolean sendSMS(String number, String dataSend){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
            return false;
        } else {
            smsController.sendSMSMessage(number, dataSend);
            return  true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //send message
                    Log.i("TAG_","if onRequestPermissionsResult");
                } else {
                    Log.i("TAG_","else onRequestPermissionsResult");
                    return;
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ActivityPrevious!=null){
            startActivity(ActivityPrevious);
            finish();
        }else {
            finish();
        }
    }
}
