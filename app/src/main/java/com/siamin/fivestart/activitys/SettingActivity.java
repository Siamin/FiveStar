package com.siamin.fivestart.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.fragments.AppSettingFragment;
import com.siamin.fivestart.fragments.ChargeAndBalanceFragment;
import com.siamin.fivestart.fragments.PhoneNumberFragment;
import com.siamin.fivestart.fragments.TextMessagesFragment;
import com.siamin.fivestart.helpers.TabAdapter;
import com.siamin.fivestart.fragments.ConnectedSystemsFragment;
import com.siamin.fivestart.interfaces.SettingInterface;
import com.siamin.fivestart.interfaces.SystemInterface;
import com.siamin.fivestart.models.SystemModel;

import java.util.ArrayList;
import java.util.List;


public class SettingActivity extends MyActivity implements SystemInterface, SettingInterface {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ConnectedSystemsFragment connectedSystemsFragment;
    private PhoneNumberFragment phoneNumberFragment;
    private TextMessagesFragment textMessagesFragment;
    private CountDownTimer CDT;
    private int count = 0,timer_=0;
    private String Message = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActivityPrevious = new Intent(this, MainActivity.class);
        initView();


    }


    void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setViewPager();
    }

    private void setViewPager() {

        adapter = new TabAdapter(getSupportFragmentManager());

        connectedSystemsFragment = new ConnectedSystemsFragment(systemController.getListDevice());
        phoneNumberFragment = new PhoneNumberFragment();
        textMessagesFragment = new TextMessagesFragment();

        adapter.addFragment(connectedSystemsFragment, getString(R.string.connectedSystems));
        adapter.addFragment(phoneNumberFragment, getString(R.string.phoneNumber));
        adapter.addFragment(textMessagesFragment, getString(R.string.textMessages));

        adapter.addFragment(new ChargeAndBalanceFragment(), getString(R.string.chargeAndBalance));
        adapter.addFragment(new AppSettingFragment(languageHelper.getLanguage().equals(languageHelper.KeyEn)?true:false), getString(R.string.appSetting));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




    }

    public void AddNewSystem(View view) {
        dialog.dialogAddNewSystem(this);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.fragmentAppSettingLanguageEnglish:
                if (checked)
                    dialog.ChangeLanguage(this,true);
                    break;
            case R.id.fragmentAppSettingLanguageFarsi:
                if (checked)
                    dialog.ChangeLanguage(this,false);
                    break;
        }
    }

    public boolean sendMessage(String Code, int selected) {
        SystemModel model = systemController.getModelByIndex(selected - 1);
        return sendSMS(model.phoneNumber, Code);
    }

    @Override
    public void setNewSystem(SystemModel model) {
        connectedSystemsFragment.AddItem(model);
    }

    @Override
    public void removeSystem(int index) {
        connectedSystemsFragment.RemoveItem(index);
    }

    @Override
    public void editSystem(SystemModel model, int index) {
        connectedSystemsFragment.EditItem(model, index);
    }

    @Override
    public void showDialogRemoveSystem(int index) {
        dialog.dialogRemovwSystem(this, index);
    }

    @Override
    public void showDialogEditSystem(int index) {

        dialog.dialogEditSystem(this, index);
    }

    public void BackPage(View view) {
        onBackPressed();
    }

    public void LoaderGetStatus(final String phone, final String fragment, final int counter) {

        indicatorHelper.show();
        count = 0;
        timer_ = 0;
        Message = "";
        CDT = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                String message = smsReceiverConterller.getReceiveMessage(phone);

                ++timer_;
                if (timer_ == 15){

                    indicatorHelper.dismiss();
                }
                if (!message.equals("null")) {
                    smsReceiverConterller.removeReceivePhone();
                    smsReceiverConterller.removeReceiveMessage();
                    Message += (count > 0 ? "," : "") + message;
                    if ((++count) >= counter) {
                        SwitchSms(Message, fragment);

                        CDT.cancel();
                    }
                }
            }


            public void onFinish() {
                smsReceiverConterller.removeReceivePhone();
                smsReceiverConterller.removeReceiveMessage();
                indicatorHelper.dismiss();
            }


        }.start();

    }

    private void SwitchSms(String Message, String Fragment) {
        switch (Fragment) {
            case "phoneNumberFragment":
//                phoneNumberFragment.splitDataSms(Message);
                break;

        }
    }


}
