package com.siamin.fivestart.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.adapters.OutputAdapter;
import com.siamin.fivestart.interfaces.BluetoothInterface;
import com.siamin.fivestart.interfaces.SwitchInterface;
import com.siamin.fivestart.models.BluetoothModel;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.views.SwitchButtonView;

import java.util.ArrayList;
import java.util.List;

public class OutputActivity extends MyActivity implements SwitchInterface,BluetoothInterface {

    private Spinner spinner;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private OutputAdapter outputAdapter;
    private CountDownTimer CDT;
    private int Count = 0,timer_=0;
    private boolean statusSwitchs = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        ActivityPrevious = new Intent(this, MainActivity.class);
        initView();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                systemController.setDefualtSystem(position);
                if (position > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    findViewById(R.id.outputSwitches).setVisibility(View.VISIBLE);
                    String systemModel = systemController.getModelByIndex(position - 1).Model;
                    if (!systemModel.equals(getResources().getString(R.string.Other))) {
                        List<Boolean> listInquiry = new ArrayList<>();
                        for (int i = 0; i < 4; i++)
                            listInquiry.add(false);
                        createRecycler(4, listInquiry);
                    } else {
                        List<Boolean> listInquiry = new ArrayList<>();
                        for (int i = 0; i < 12; i++)
                            listInquiry.add(false);
                        createRecycler(12, listInquiry);
                    }

                } else {
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.outputSwitches).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initView() {
        spinner = findViewById(R.id.SpinnerSystem);
        recyclerView = findViewById(R.id.outputRecyclerView);
        llm = new LinearLayoutManager(this);
        systemController.setNamesToSpinnerById(spinner);
    }

    private void createRecycler(int count, List<Boolean> listInquiry) {
        Count = count;

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        outputAdapter = new OutputAdapter(this, count, listInquiry);
        recyclerView.setAdapter(outputAdapter);
    }

    public void backPage(View view) {
        onBackPressed();
    }

    public void sendSms(String dataSend) {
        int selected = spinner.getSelectedItemPosition();
        if (selected > 0) {
            SystemModel model = systemController.getModelByIndex(selected - 1);
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

    private void LoaderGetStatus(final String phone) {
        indicatorHelper.show();
        timer_ = 0;
        CDT = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                ++timer_;
                String message = smsReceiverConterller.getReceiveMessage(phone);
                if (timer_ == 15){

                    indicatorHelper.dismiss();
                }
                if (!message.equals("null")) {
                    smsReceiverConterller.removeReceivePhone();
                    smsReceiverConterller.removeReceiveMessage();
                    SplitDataMesage(message);
                    CDT.cancel();

                }

            }

            public void onFinish() {
                smsReceiverConterller.removeReceivePhone();
                smsReceiverConterller.removeReceiveMessage();
                indicatorHelper.dismiss();
            }
        }.start();

    }

    private void SplitDataMesage(String Message) {
        indicatorHelper.dismiss();

        String[] messages = Message.split(",");

        List<Boolean> listInquiry = new ArrayList<>();
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].startsWith(" OUT")) {
                listInquiry.add((messages[i].split(":")[1]).replace(" ", "").equals("OFF") ? false : true);
            }
        }
        createRecycler(Count, listInquiry);
        outputAdapter.notifyDataSetChanged();
    }

    public void OutputInquiry(View view) {

        int selected = spinner.getSelectedItemPosition();
        if (selected > 0) {
            sendSms("B");
            SystemModel model = systemController.getModelByIndex(selected - 1);
            LoaderGetStatus(model.phoneNumber);
        } else {
            message.ErrorMessage(getString(R.string.pleaseSelectDevice));
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

        SwitchButtonView sbv = findViewById(R.id.outputSwitches);
        sbv.deselectedBluetooth();
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
