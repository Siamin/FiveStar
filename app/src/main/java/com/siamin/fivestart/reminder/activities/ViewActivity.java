package com.siamin.fivestart.reminder.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.models.Reminder;
import com.siamin.fivestart.reminder.receivers.AlarmReceiver;
import com.siamin.fivestart.reminder.receivers.DismissReceiver;
import com.siamin.fivestart.reminder.receivers.SnoozeReceiver;
import com.siamin.fivestart.reminder.utils.AlarmUtil;
import com.siamin.fivestart.reminder.utils.DateAndTimeUtil;
import com.siamin.fivestart.reminder.utils.NotificationUtil;
import com.siamin.fivestart.reminder.utils.TextFormatUtil;

import java.util.Calendar;


public class ViewActivity extends MyActivity {

    TextView notificationTitleText;
    TextView notificationTimeText;
    TextView contentText;
    ImageView iconImage;
    ImageView circleImage;
    TextView timeText;
    TextView dateText;
    TextView repeatText;
    TextView shownText;
    Toolbar toolbar;
    LinearLayout linearLayout;
    ScrollView scrollView;
    View headerView;
    CoordinatorLayout coordinatorLayout;

    private Reminder reminder;
    private boolean hideMarkAsDone;
    private boolean reminderChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initView();
        setupTransitions();


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(null);

        // Add drawable shadow and adjust layout if build version is before lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            linearLayout.setPadding(0, 0, 0, 0);
        } else {
            ViewCompat.setElevation(headerView, getResources().getDimension(R.dimen.toolbar_elevation));
        }

        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        int mReminderId = intent.getIntExtra("NOTIFICATION_ID", 0);

        // Arrived to activity from notification on click
        // Cancel notification and nag alarm
        if (intent.getBooleanExtra("NOTIFICATION_DISMISS", false)) {
            Intent dismissIntent = new Intent().setClass(this, DismissReceiver.class);
            dismissIntent.putExtra("NOTIFICATION_ID", mReminderId);
            sendBroadcast(dismissIntent);
        }

        // Check if notification has been deleted
        if (database.isNotificationPresent(mReminderId)) {
            reminder = database.getNotification(mReminderId);
            database.close();
        } else {
            database.close();
            returnHome();
        }
    }

    private void initView(){
        notificationTitleText = findViewById(R.id.notification_title);
        notificationTimeText = findViewById(R.id.notification_time);
        contentText = findViewById(R.id.notification_content);
        iconImage = findViewById(R.id.notification_icon);
        circleImage = findViewById(R.id.notification_circle);
        timeText = findViewById(R.id.time);
        dateText = findViewById(R.id.date);
        repeatText = findViewById(R.id.repeat);
        shownText = findViewById(R.id.shown);
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.detail_layout);
        scrollView = findViewById(R.id.scroll);
        headerView = findViewById(R.id.header);
        coordinatorLayout = findViewById(R.id.view_coordinator);

    }

    public void assignReminderValues() {
        Calendar calendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());
        notificationTitleText.setText(reminder.getTitle().split("=>")[0]);
        contentText.setText(reminder.getContent().split("=>")[0]);
        if (languageHelper.getLanguage().equals(languageHelper.KeyEn)){
            dateText.setText(DateAndTimeUtil.toStringReadableDate(calendar));
        }else{
            String appropriateDate = persianCalendarHelper.ConvertDateMiladiToJalaliByMonthNames(reminder.getDateAndTime());
            dateText.setText(appropriateDate);
        }

        iconImage.setImageResource(getResources().getIdentifier(reminder.getIcon(), "drawable", getPackageName()));
        circleImage.setColorFilter(Color.parseColor(reminder.getColour()));
        String readableTime = DateAndTimeUtil.toStringReadableTime(calendar, this);
        timeText.setText(readableTime);
        notificationTimeText.setText(readableTime);

        if (reminder.getRepeatType() == Reminder.SPECIFIC_DAYS) {
            repeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, reminder.getDaysOfWeek()));
        } else {
            if (reminder.getInterval() > 1) {
                repeatText.setText(TextFormatUtil.formatAdvancedRepeatText(this, reminder.getRepeatType(), reminder.getInterval()));
            } else {
                repeatText.setText(getResources().getStringArray(R.array.repeat_array)[reminder.getRepeatType()]);
            }
        }

        if (Boolean.parseBoolean(reminder.getForeverState())) {
            shownText.setText(R.string.forever);
        } else {
            shownText.setText(getString(R.string.times_shown, reminder.getNumberShown(), reminder.getNumberToShow()));
        }

        // Hide "Mark as done" action if reminder is inactive
        hideMarkAsDone = reminder.getNumberToShow() <= reminder.getNumberShown() && !Boolean.parseBoolean(reminder.getForeverState());
        invalidateOptionsMenu();
    }

    public void setupTransitions() {
        // Add shared element transition animation if on Lollipop or later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Enter transitions
            TransitionSet setEnter = new TransitionSet();

            Transition slideDown = new Explode();
            slideDown.addTarget(headerView);
            slideDown.excludeTarget(scrollView, true);
            slideDown.setDuration(500);
            setEnter.addTransition(slideDown);

            Transition fadeOut = new Slide(Gravity.BOTTOM);
            fadeOut.addTarget(scrollView);
            fadeOut.setDuration(500);
            setEnter.addTransition(fadeOut);

            // Exit transitions
            TransitionSet setExit = new TransitionSet();

            Transition slideDown2 = new Explode();
            slideDown2.addTarget(headerView);
            slideDown2.setDuration(570);
            setExit.addTransition(slideDown2);

            Transition fadeOut2 = new Slide(Gravity.BOTTOM);
            fadeOut2.addTarget(scrollView);
            fadeOut2.setDuration(280);
            setExit.addTransition(fadeOut2);

            getWindow().setEnterTransition(setEnter);
            getWindow().setReturnTransition(setExit);
        }
    }

    public void confirmDelete() {
        new AlertDialog.Builder(this, R.style.Dialog)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        actionDelete();
                    }
                })
                .setNegativeButton(R.string.no, null).show();
    }

    public void actionShowNow() {
        NotificationUtil.createNotification(this, reminder);
    }

    public void actionDelete() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        database.deleteNotification(reminder);
        database.close();
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        AlarmUtil.cancelAlarm(this, alarmIntent, reminder.getId());
        Intent snoozeIntent = new Intent(this, SnoozeReceiver.class);
        AlarmUtil.cancelAlarm(this, snoozeIntent, reminder.getId());
        finish();
    }

    public void actionEdit() {
        Intent intent = new Intent(this, CreateEditActivity.class);
        intent.putExtra("NOTIFICATION_ID", reminder.getId());
        startActivity(intent);
        finish();
    }

    public void actionMarkAsDone() {
        reminderChanged = true;
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        // Check whether next alarm needs to be set
        if (reminder.getNumberShown() + 1 != reminder.getNumberToShow() || Boolean.parseBoolean(reminder.getForeverState())) {
            AlarmUtil.setNextAlarm(this, reminder, database);
        } else {
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            AlarmUtil.cancelAlarm(this, alarmIntent, reminder.getId());
            reminder.setDateAndTime(DateAndTimeUtil.toStringDateAndTime(Calendar.getInstance()));
        }
        reminder.setNumberShown(reminder.getNumberShown() + 1);
        database.addNotification(reminder);
        assignReminderValues();
        database.close();
        Snackbar.make(coordinatorLayout, R.string.toast_mark_as_done, Snackbar.LENGTH_SHORT).show();
    }

    public void actionShareText() {
        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, reminder.getTitle() + "\n" + reminder.getContent());
        startActivity(Intent.createChooser(intent, getString(R.string.action_share)));
    }

    public void returnHome() {
        Intent intent = new Intent(this, ReminderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void updateReminder() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        reminder = database.getNotification(reminder.getId());
        database.close();
        assignReminderValues();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("BROADCAST_REFRESH"));
        updateReminder();
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onPause();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            reminderChanged = true;
            updateReminder();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_viewer, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (reminderChanged) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                confirmDelete();
                return true;
            case R.id.action_edit:
                actionEdit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}