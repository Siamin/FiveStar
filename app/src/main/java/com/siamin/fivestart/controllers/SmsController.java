package com.siamin.fivestart.controllers;

import android.content.Context;
import android.telephony.SmsManager;

import com.siamin.fivestart.R;
import com.siamin.fivestart.helpers.MessageHelper;

public class SmsController {

    private Context context;
    private MessageHelper messageHelper;
    public SmsController(Context context,MessageHelper messageHelper){
        this.context = context;
        this.messageHelper = messageHelper;
    }

    public void sendSMSMessage(String phoneNo, String message ) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        messageHelper.Snackbar(context.getString(R.string.succssesSend),null, R.color.colorGreen,R.drawable.messagestylesuccess);
    }

    public void sendSMSMessageNotToast(String phoneNo, String message ) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
    }
}
