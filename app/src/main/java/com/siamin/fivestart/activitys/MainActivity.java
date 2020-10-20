package com.siamin.fivestart.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.MySmsReceiver;
import com.siamin.fivestart.R;
import com.siamin.fivestart.interfaces.BluetoothInterface;
import com.siamin.fivestart.interfaces.SendSmsInterface;
import com.siamin.fivestart.interfaces.SwitchInterface;
import com.siamin.fivestart.models.BluetoothModel;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.reminder.activities.ReminderActivity;
import com.siamin.fivestart.views.SwitchButtonView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyActivity implements SwitchInterface,SendSmsInterface, NavigationView.OnNavigationItemSelectedListener, BluetoothInterface {

    Toolbar toolbar;
    DrawerLayout drawer;
    Spinner spinner;
    NavigationView navigationView;
    private String TAG = "TAG_MainActivity";
    private boolean statusSwitchs = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        spinner = findViewById(R.id.SpinnerSystem);

        systemController.setNamesToSpinnerById(spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView titleZone = findViewById(R.id.mainZone);
                systemController.setDefualtSystem(position);
                if (position > 0) {
                    SystemModel systemModel = systemController.getModelByIndex(position - 1);

                    if (systemModel.getArmCode().equals("5")){
                        titleZone.setText(getResources().getText(R.string.All));
                    }else{
                        titleZone.setText(getResources().getText(R.string.Zone)+(systemModel.getArmCode().replace("Z","").replace("A","")));
                    }

                } else {
                    titleZone.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, HelpActivity.class));
            finish();
        } else if (id == R.id.nav_reminder) {
            startActivity(new Intent(this, ReminderActivity.class));
            finish();
        } else if (id == R.id.nav_outputs) {
            try{
                dataController.socketClose();
            }catch (Exception e){
                Log.i(TAG,"Error nav_outputs => "+e.toString());
            }finally {
                startActivity(new Intent(this, OutputActivity.class));
                finish();
            }

        } else if (id == R.id.nav_zoons) {
            startActivity(new Intent(this, ZoneActivity.class));
            finish();
        } else if (id == R.id.nav_inquiry) {
            dialog.dialogReports(this);
        } else if (id == R.id.nav_exit) {
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnMainActive(View view) {
        sendData("5");
    }

    public void btnMainDeactive(View view) {
        sendData("6");
    }

    public void btnMainSemiActive(View view) {
        sendData("7");
    }

    public void btnMainSilent(View view) {

        sendData("8");
    }


    public void btnMainRefresh(View view) {
        sendData("F");
    }

    public void btnMainOpener(View view) {
        sendData("D");
    }

    private void sendData(String dataSend) {
        int selected = spinner.getSelectedItemPosition();
        if (selected > 0) {
            SystemModel model = systemController.getModelByIndex(selected - 1);

            if (dataSend.equals("5"))
                dataSend = model.getArmCode();
            else if (dataSend.equals("6"))
                dataSend = model.getDisArmCode();

            dataSend += model.pinCode;

            if (statusSwitchs) {
                dataController.sender(dataSend);
            } else {
                sendSMS(model.phoneNumber, dataSend);
            }
        } else {
            message.ErrorMessage(getString(R.string.pleaseSelectDevice));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bluetoothController.bluetoothIsEnabled()) {
            try {
                if (dataController.socketIsEnabled()) {
                    statusSwitchs = true;
                } else {
                    statusSwitchs = false;
                }
            } catch (Exception e) {
                statusSwitchs = false;
            }

        } else {
            statusSwitchs = false;
        }
    }

    @Override
    public void selectBluetooth(BluetoothModel model) {
        statusSwitchs = true;
        dataController.Connection(model);
    }

    @Override
    public void notSelectBlutooth(boolean status) {
        statusSwitchs = false;
        if (status)
        message.ErrorMessage(getString(R.string.NoDevicesFound));
        //set switch buttom to sms...
        SwitchButtonView sbv = findViewById(R.id.SwitchButtonView);
        sbv.deselectedBluetooth();
    }

    @Override
    public void sendMessage(String message) {
        sendData(message);
    }

    @Override
    public void cancle() {

    }


    @Override
    public void Sms() {
        statusSwitchs = false;
    }

    @Override
    public void Bluetooth() {
        statusSwitchs = true;
        bAdapter.enable();
        bluetoothController.bluetoothConnection(dialog, this);

    }
}