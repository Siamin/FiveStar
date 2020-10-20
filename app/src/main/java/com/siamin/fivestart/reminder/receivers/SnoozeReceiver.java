package com.siamin.fivestart.reminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.models.Reminder;
import com.siamin.fivestart.reminder.utils.NotificationUtil;


public class SnoozeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper database = DatabaseHelper.getInstance(context);
        int reminderId = intent.getIntExtra("NOTIFICATION_ID", 0);
//        Toast.makeText(context,"SnoozeReceiver", Toast.LENGTH_LONG).show();
        if (reminderId != 0 && database.isNotificationPresent(reminderId)) {
            Reminder reminder = database.getNotification(reminderId);
            NotificationUtil.createNotification(context, reminder);
        }
        database.close();
    }
}