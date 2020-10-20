package com.siamin.fivestart.reminder.receivers;

import android.annotation.SuppressLint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


@SuppressLint("NewApi")
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    public static String smsBody, address;


    public void onReceive(Context context, Intent intent) {
        Log.i("TAG_","SmsReceiver");
        try {
            Bundle intentExtras = intent.getExtras();

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);

                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    smsBody = smsMessage.getMessageBody().toString();
                    address = smsMessage.getOriginatingAddress();
                    address = address.substring(3, address.length());
                }

                Toast.makeText(context,"(" + smsBody + "-----" + address + ")",Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            Log.i("TAG_",e.toString());
        }

    }

}