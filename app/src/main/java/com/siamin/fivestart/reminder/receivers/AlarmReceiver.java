package com.siamin.fivestart.reminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.siamin.fivestart.activitys.MainActivity;
import com.siamin.fivestart.controllers.SmsController;
import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.models.Reminder;
import com.siamin.fivestart.reminder.utils.AlarmUtil;
import com.siamin.fivestart.reminder.utils.NotificationUtil;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper database = DatabaseHelper.getInstance(context);
        Reminder reminder = database.getNotification(intent.getIntExtra("NOTIFICATION_ID", 0));
        reminder.setNumberShown(reminder.getNumberShown() + 1);
        database.addNotification(reminder);

        SmsController sms = new SmsController(context,null);
        sms.sendSMSMessageNotToast(reminder.getTitle().split("=>")[1],reminder.getContent().split("=> ")[1]);

        NotificationUtil.newCreateNotifation(context, reminder);

        if (reminder.getNumberToShow() > reminder.getNumberShown() || Boolean.parseBoolean(reminder.getForeverState())) {
            AlarmUtil.setNextAlarm(context, reminder, database);
        }

        Intent updateIntent = new Intent("BROADCAST_REFRESH");
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
        database.close();
        Log.i("TAG_","AlarmReceiver");


    }
}