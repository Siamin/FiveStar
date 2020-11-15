package com.siamin.fivestart.reminder.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.google.android.material.snackbar.Snackbar;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.MainActivity;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.dialogs.AdvancedRepeatSelector;
import com.siamin.fivestart.reminder.dialogs.DaysOfWeekSelector;
import com.siamin.fivestart.reminder.dialogs.IconPicker;
import com.siamin.fivestart.reminder.dialogs.RepeatSelector;
import com.siamin.fivestart.reminder.models.Colour;
import com.siamin.fivestart.reminder.models.Reminder;
import com.siamin.fivestart.reminder.receivers.AlarmReceiver;
import com.siamin.fivestart.reminder.utils.AlarmUtil;
import com.siamin.fivestart.reminder.utils.AnimationUtil;
import com.siamin.fivestart.reminder.utils.DateAndTimeUtil;
import com.siamin.fivestart.reminder.utils.TextFormatUtil;

import java.util.Calendar;


public class CreateEditActivity extends MyActivity implements ColorChooserDialog.ColorCallback,
        IconPicker.IconSelectionListener, AdvancedRepeatSelector.AdvancedRepeatSelectionListener,
        DaysOfWeekSelector.DaysOfWeekSelectionListener, RepeatSelector.RepeatSelectionListener,
        com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener {


    CoordinatorLayout coordinatorLayout;
    EditText titleEditText;
    EditText contentEditText;
    TextView timeText;
    TextView dateText;
    TextView repeatText;
    SwitchCompat foreverSwitch;
    EditText timesEditText;
    LinearLayout foreverRow;
    LinearLayout bottomRow;
    View bottomView;
    TextView showText;
    TextView timesText;
    TextView iconText;
    TextView colourText;
    ImageView imageColourSelect;
    ImageView imageIconSelect;
    ImageView imageWarningTime;
    ImageView imageWarningDate;
    ImageView imageWarningShow;
    Toolbar toolbar;
    private Spinner spinnerSystem, spinnerCode;
    private String TIMEPICKER = "TimePickerDialog", DATEPICKER = "DatePickerDialog", MULTIDATEPICKER = "MultiDatePickerDialog";


    private boolean createdPage = false;
    private String icon;
    private String colour;
    private Calendar calendar;
    private boolean[] daysOfWeek = new boolean[7];
    private int timesShown = 0;
    private int timesToShow = 1;
    private int repeatType;
    private int id;
    private int interval = 1;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initView();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }



        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(null);

        calendar = Calendar.getInstance();

        icon = getString(R.string.default_icon_value);
        colour = getString(R.string.default_colour_value);
        repeatType = Reminder.DOES_NOT_REPEAT;
        id = getIntent().getIntExtra("NOTIFICATION_ID", 0);

        spinnerSystem = findViewById(R.id.SpinnerSystem);
        spinnerCode = findViewById(R.id.SpinnerCode);

        systemController.setNamesToSpinnerById(spinnerSystem);
        systemController.setCodeToSpinnerById(spinnerCode, false);


        // Check whether to edit or create a new notification
        if (id == 0) {
            DatabaseHelper database = DatabaseHelper.getInstance(this);
            id = database.getLastNotificationId() + 1;
            database.close();
        } else {
            assignReminderValues();
        }


        spinnerSystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SystemModel model = systemController.getModelByIndex(position - 1);
                    if (model.Model.equals(getResources().getString(R.string.Other)))
                        systemController.setCodeToSpinnerById(spinnerCode, true);

                    titleEditText.setText(model.systemName + "=>" + model.phoneNumber);

                } else {
                    systemController.setCodeToSpinnerById(spinnerCode, false);
                    titleEditText.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedSystem = spinnerSystem.getSelectedItemPosition();
                if (selectedSystem > 0) {
                    if (position > 0) {
                        SystemModel model = systemController.getModelByIndex(selectedSystem - 1);
                        String codeSend = systemController.getCodeByIndex(position, model.pinCode);
                        contentEditText.setText(codeSend);
                    } else {
                        contentEditText.setText("");
                    }
                } else {
                    contentEditText.setText("");
                    if (createdPage)
                        message.ErrorMessage(getResources().getString(R.string.pleaseSelectedModelSystem));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        createdPage = true;
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imageWarningShow = findViewById(R.id.error_show);
        imageWarningDate = findViewById(R.id.error_date);
        imageWarningTime = findViewById(R.id.error_time);
        imageIconSelect = findViewById(R.id.selected_icon);
        imageColourSelect = findViewById(R.id.colour_icon);
        colourText = findViewById(R.id.select_colour_text);
        iconText = findViewById(R.id.select_icon_text);
        timesText = findViewById(R.id.times);
        showText = findViewById(R.id.show);
        bottomView = findViewById(R.id.bottom_view);
        bottomRow = findViewById(R.id.bottom_row);
        foreverRow = findViewById(R.id.forever_row);
        timesEditText = findViewById(R.id.show_times_number);
        foreverSwitch = findViewById(R.id.switch_toggle);
        repeatText = findViewById(R.id.repeat_day);
        dateText = findViewById(R.id.date);
        timeText = findViewById(R.id.time);
        contentEditText = findViewById(R.id.notification_content);
        titleEditText = findViewById(R.id.notification_title);
        coordinatorLayout = findViewById(R.id.create_coordinator);

    }


    public void assignReminderValues() {
        // Prevent keyboard from opening automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Reminder reminder = database.getNotification(id);
        database.close();

        timesShown = reminder.getNumberShown();
        repeatType = reminder.getRepeatType();
        interval = reminder.getInterval();
        icon = reminder.getIcon();
        colour = reminder.getColour();

        calendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());

        showText.setText(getString(R.string.times_shown_edit, reminder.getNumberShown()));

        int indexSpinnrSystem = systemController.findIndexSystemByPhone(reminder.getTitle().split("=>")[1]);
        spinnerSystem.setSelection(indexSpinnrSystem);
        titleEditText.setText(reminder.getTitle());

        SystemModel model = systemController.getModelByIndex(indexSpinnrSystem - 1);
        int indexSpinnerCode = systemController.findIndexCodeByName(reminder.getContent().replace(model.pinCode, ""));
        spinnerCode.setSelection(indexSpinnerCode);
        contentEditText.setText(reminder.getContent());

        if (languageHelper.getLanguage().equals(languageHelper.KeyEn)) {
            dateText.setText(DateAndTimeUtil.toStringReadableDate(calendar));
        } else {

            String appropriateDate = persianCalendarHelper.ConvertDateMiladiToJalaliByMonthNames(reminder.getDateAndTime());
            dateText.setText(appropriateDate);
        }

        timeText.setText(DateAndTimeUtil.toStringReadableTime(calendar, this));
        timesEditText.setText(String.valueOf(reminder.getNumberToShow()));
        colourText.setText(colour);
        imageColourSelect.setColorFilter(Color.parseColor(colour));
        timesText.setVisibility(View.VISIBLE);

        if (!getString(R.string.default_icon).equals(icon)) {
            imageIconSelect.setImageResource(getResources().getIdentifier(reminder.getIcon(), "drawable", getPackageName()));
            iconText.setText(R.string.custom_icon);
        }

        if (reminder.getRepeatType() != Reminder.DOES_NOT_REPEAT) {
            if (reminder.getInterval() > 1) {
                repeatText.setText(TextFormatUtil.formatAdvancedRepeatText(this, repeatType, interval));
            } else {
                repeatText.setText(getResources().getStringArray(R.array.repeat_array)[reminder.getRepeatType()]);
            }
            showFrequency(true);
        }

        if (reminder.getRepeatType() == Reminder.SPECIFIC_DAYS) {
            daysOfWeek = reminder.getDaysOfWeek();
            repeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, daysOfWeek));
        }

        if (Boolean.parseBoolean(reminder.getForeverState())) {
            foreverSwitch.setChecked(true);
            bottomRow.setVisibility(View.GONE);
        }
    }

    public void showFrequency(boolean show) {
        if (show) {
            foreverRow.setVisibility(View.VISIBLE);
            bottomRow.setVisibility(View.VISIBLE);
            bottomView.setVisibility(View.VISIBLE);
        } else {
            foreverSwitch.setChecked(false);
            foreverRow.setVisibility(View.GONE);
            bottomRow.setVisibility(View.GONE);
            bottomView.setVisibility(View.GONE);
        }
    }

    public void timePicker(View view) {
        TimePickerDialog TimePicker = new TimePickerDialog(CreateEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                timeText.setText(DateAndTimeUtil.toStringReadableTime(calendar, getApplicationContext()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        TimePicker.show();
    }

    public void datePicker(View view) {

        if (languageHelper.getLanguage().equals("en")) {
            DatePickerDialog DatePicker = new DatePickerDialog(CreateEditActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(android.widget.DatePicker DatePicker, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateText.setText(DateAndTimeUtil.toStringReadableDate(calendar));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            DatePicker.show();
        } else {
            getDataJalali();
        }

    }

    public void iconSelector(View view) {
        DialogFragment dialog = new IconPicker();
        dialog.show(getSupportFragmentManager(), "IconPicker");
    }

    void getDataJalali() {

        PersianCalendar persianCalendar = new PersianCalendar();
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog datePickerDialog
                = com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay());

        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onIconSelection(DialogFragment dialog, String iconName, String iconType, int iconResId) {
        icon = iconName;
        iconText.setText(iconType);
        imageIconSelect.setImageResource(iconResId);
        dialog.dismiss();
    }

    public void colourSelector(View view) {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        int[] colours = database.getColoursArray();
        database.close();

        new ColorChooserDialog.Builder(this, R.string.select_colour)
                .allowUserColorInputAlpha(false)
                .customColors(colours, null)
                .preselect(Color.parseColor(colour))
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColour) {
        colour = String.format("#%06X", (0xFFFFFF & selectedColour));
        imageColourSelect.setColorFilter(selectedColour);
        colourText.setText(colour);
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        database.addColour(new Colour(selectedColour, DateAndTimeUtil.toStringDateTimeWithSeconds(Calendar.getInstance())));
        database.close();
    }

    public void repeatSelector(View view) {
        DialogFragment dialog = new RepeatSelector();
        dialog.show(getSupportFragmentManager(), "RepeatSelector");
    }

    @Override
    public void onRepeatSelection(DialogFragment dialog, int which, String repeatText) {
        interval = 1;
        repeatType = which;
        this.repeatText.setText(repeatText);
        if (which == Reminder.DOES_NOT_REPEAT) {
            showFrequency(false);
        } else {
            showFrequency(true);
        }
    }

    @Override
    public void onDaysOfWeekSelected(boolean[] days) {
        repeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, days));
        daysOfWeek = days;
        repeatType = Reminder.SPECIFIC_DAYS;
        showFrequency(true);
    }

    @Override
    public void onAdvancedRepeatSelection(int type, int interval, String repeatText) {
        repeatType = type;
        this.interval = interval;
        this.repeatText.setText(repeatText);
        showFrequency(true);
    }

    public void saveNotification() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Reminder reminder = new Reminder()
                .setId(id)
                .setTitle(titleEditText.getText().toString())
                .setContent(contentEditText.getText().toString())
                .setDateAndTime(DateAndTimeUtil.toStringDateAndTime(calendar))
                .setRepeatType(repeatType)
                .setForeverState(Boolean.toString(foreverSwitch.isChecked()))
                .setNumberToShow(timesToShow)
                .setNumberShown(timesShown)
                .setIcon(icon)
                .setColour(colour)
                .setInterval(interval);

        database.addNotification(reminder);

        if (repeatType == Reminder.SPECIFIC_DAYS) {
            reminder.setDaysOfWeek(daysOfWeek);
            database.addDaysOfWeek(reminder);
        }

        database.close();
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        calendar.set(Calendar.SECOND, 0);
        AlarmUtil.setAlarm(this, alarmIntent, reminder.getId(), calendar);
        finish();
    }

    public void toggleSwitch(View view) {
        foreverSwitch.toggle();
        if (foreverSwitch.isChecked()) {
            bottomRow.setVisibility(View.GONE);
        } else {
            bottomRow.setVisibility(View.VISIBLE);
        }
    }

    public void switchClicked(View view) {
        if (foreverSwitch.isChecked()) {
            bottomRow.setVisibility(View.GONE);
        } else {
            bottomRow.setVisibility(View.VISIBLE);
        }
    }

    public void validateInput() {
        imageWarningShow.setVisibility(View.GONE);
        imageWarningTime.setVisibility(View.GONE);
        imageWarningDate.setVisibility(View.GONE);
        Calendar nowCalendar = Calendar.getInstance();

        if (timeText.getText().equals(getString(R.string.time_now))) {
            calendar.set(Calendar.HOUR_OF_DAY, nowCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, nowCalendar.get(Calendar.MINUTE));
        }

        if (dateText.getText().equals(getString(R.string.date_today))) {
            calendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, nowCalendar.get(Calendar.DAY_OF_MONTH));
        }

        // Check if the number of times to show notification is empty
        if (timesEditText.getText().toString().isEmpty()) {
            timesEditText.setText("1");
        }

        timesToShow = Integer.parseInt(timesEditText.getText().toString());
        if (repeatType == Reminder.DOES_NOT_REPEAT) {
            timesToShow = timesShown + 1;
        }

        // Check if selected date is before today's date
        if (DateAndTimeUtil.toLongDateAndTime(calendar) < DateAndTimeUtil.toLongDateAndTime(nowCalendar)) {
            Snackbar.make(coordinatorLayout, R.string.toast_past_date, Snackbar.LENGTH_SHORT).show();
            imageWarningTime.setVisibility(View.VISIBLE);
            imageWarningDate.setVisibility(View.VISIBLE);

            // Check if title is empty
        } else if (titleEditText.getText().toString().trim().isEmpty()) {
            Snackbar.make(coordinatorLayout, R.string.toast_title_empty, Snackbar.LENGTH_SHORT).show();
            AnimationUtil.shakeView(titleEditText, this);

            // Check if times to show notification is too low
        } else if (timesToShow <= timesShown && !foreverSwitch.isChecked()) {
            Snackbar.make(coordinatorLayout, R.string.toast_higher_number, Snackbar.LENGTH_SHORT).show();
            imageWarningShow.setVisibility(View.VISIBLE);
        } else {
            saveNotification();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                validateInput();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int[] miladiDate = persianCalendarHelper.JalaliToMiladi(year, monthOfYear, dayOfMonth-1);

        calendar.set(Calendar.YEAR, miladiDate[0]);
        calendar.set(Calendar.MONTH, miladiDate[1]);
        calendar.set(Calendar.DAY_OF_MONTH, miladiDate[2]);


        dateText.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }
}