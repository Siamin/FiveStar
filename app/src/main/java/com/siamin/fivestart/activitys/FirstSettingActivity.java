package com.siamin.fivestart.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.interfaces.DialogInterface;
import com.siamin.fivestart.interfaces.SetPasswordInterface;
import com.siamin.fivestart.models.ErrorModel;
import com.siamin.fivestart.models.SystemModel;

public class FirstSettingActivity extends MyActivity implements DialogInterface, SetPasswordInterface {


    EditText systemName, phoneNumber, pinCode;
    Spinner modelSystem;
    String[] SystemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstsetting);

        initView();

        pinCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 4)
                    pinCode.setError(getResources().getString(R.string.YouCanNotEnterMoreThan4Characters));

            }
        });
    }

    void initView() {
        systemName = findViewById(R.id.firstSettingSystrmName);
        phoneNumber = findViewById(R.id.firstSettingPhoneNumber);
        pinCode = findViewById(R.id.firstSettingPinCode);
        modelSystem = findViewById(R.id.SpinnerSystem);


        SystemList = getResources().getStringArray(R.array.SystemList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SystemList);
        modelSystem.setAdapter(adapter);

    }

    public void SubmitFirstSetting(View view) {
        SystemModel systemModel = new SystemModel();
        systemModel.systemName = systemName.getText().toString();
        systemModel.phoneNumber = phoneNumber.getText().toString();
        systemModel.pinCode = pinCode.getText().toString();
        systemModel.Model = SystemList[modelSystem.getSelectedItemPosition()];
        systemModel.ArmCode = "5";
        systemModel.DisArmCode = "6";

        ErrorModel errorModel = systemController.addSystem(systemModel);
        if (errorModel.Status) {
            sp.SetCode("FirstRun", "isFirstRun");
            dialog.dialogFirstRun(this);
        } else {
            message.ErrorMessage(errorModel.Message);
        }


    }


    @Override
    public void ok() {
        //open dialog set Password
        dialog.dialogSetPassword(this);
    }

    @Override
    public void cancel() {
        GotoMainActivity();

    }

    @Override
    public void setPass(String pass,String confirm) {
        ErrorModel errorModel = passwordController.setPassword(pass,confirm);
        if (errorModel.Status){
            message.SuccssesMessage(errorModel.Message);
            if(message.getAndroidVersionRelease()>= 28){
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                GotoMainActivity();
                            }
                        }, 1500);
            }else{
                GotoMainActivity();
            }


        }else{
            message.ErrorMessage(errorModel.Message);
        }
    }

    @Override
    public void canselSetPass() {
        GotoMainActivity();
    }

    private void GotoMainActivity(){
        bAdapter.enable();
        startActivity(new Intent(FirstSettingActivity.this,MainActivity.class));
        finish();
    }
}
