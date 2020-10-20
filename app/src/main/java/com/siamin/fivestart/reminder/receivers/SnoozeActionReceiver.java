package com.siamin.fivestart.reminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.siamin.fivestart.reminder.activities.SnoozeDialogActivity;
import com.siamin.fivestart.reminder.utils.AlarmUtil;


public class SnoozeActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra("NOTIFICATION_ID", 0);
//        Toast.makeText(context,"SnoozeActionReceiver", Toast.LENGTH_LONG).show();
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("checkBoxNagging", false)) {
            Intent alarmIntent = new Intent(context, NagReceiver.class);
            AlarmUtil.cancelAlarm(context, alarmIntent, reminderId);
        }

        // Close notification tray
        Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeIntent);

        Intent snoozeIntent = new Intent(context, SnoozeDialogActivity.class);
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        snoozeIntent.putExtra("NOTIFICATION_ID", reminderId);
        context.startActivity(snoozeIntent);
    }
}