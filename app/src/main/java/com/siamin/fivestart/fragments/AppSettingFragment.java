package com.siamin.fivestart.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.models.ErrorModel;


public class AppSettingFragment extends Fragment {

    private EditText oldPassword,newPassword,confirmNewPassword;
    private Button removePassword,setPassword;
    private boolean isPassword ,language ;

    public AppSettingFragment(boolean language){
        this.language = language;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appsetting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        removePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorModel model =((SettingActivity)getContext()).passwordController.removePassword(oldPassword.getText().toString());
                if (model.Status){
                    isPassword = false;
                    setVisibilityView();
                    ((SettingActivity)getContext()).message.SuccssesMessage(model.Message);
                }else{
                    ((SettingActivity)getContext()).message.ErrorMessage(model.Message);
                }
            }
        });

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassword){
                    ErrorModel model = ((SettingActivity)getContext()).passwordController.changePassword(oldPassword.getText().toString(),newPassword.getText().toString(),confirmNewPassword.getText().toString());
                    if (model.Status){
                        ((SettingActivity)getContext()).message.SuccssesMessage(model.Message);
                    }else{
                        ((SettingActivity)getContext()).message.ErrorMessage(model.Message);
                    }

                }else{
                    ErrorModel model =((SettingActivity)getContext()).passwordController.setPassword(newPassword.getText().toString(),confirmNewPassword.getText().toString());
                    if (model.Status){
                        isPassword = true;
                        setVisibilityView();
                        ((SettingActivity)getContext()).message.SuccssesMessage(model.Message);
                    }else{
                        ((SettingActivity)getContext()).message.ErrorMessage(model.Message);
                    }
                }
            }
        });

    }

    void initView(){
        oldPassword = getView().findViewById(R.id.fragmentAppSettingOldPassword);
        newPassword = getView().findViewById(R.id.fragmentAppSettingNewPassword);
        confirmNewPassword = getView().findViewById(R.id.fragmentAppSettingConfirmNewPassword);
        removePassword = getView().findViewById(R.id.fragmentAppSettingRemovePassword);
        setPassword = getView().findViewById(R.id.fragmentAppSettingSetPassword);

        isPassword = ((SettingActivity)getContext()).passwordController.isPassword();

        setVisibilityView();

        if (language){
            RadioButton radioButton = getView().findViewById(R.id.fragmentAppSettingLanguageEnglish);
            radioButton.setChecked(true);
        }else{
            RadioButton radioButton = getView().findViewById(R.id.fragmentAppSettingLanguageFarsi);
            radioButton.setChecked(true);
        }


        if (Build.VERSION.SDK_INT<25){
            getView().findViewById(R.id.settingLanguage).setVisibility(View.GONE);
        }

    }

    void setVisibilityView(){
        if(!isPassword){
            getView().findViewById(R.id.fragmentLayoutAppSettingOldPassword).setVisibility(View.GONE);
            removePassword.setVisibility(View.GONE);
        }else{
            getView().findViewById(R.id.fragmentLayoutAppSettingOldPassword).setVisibility(View.VISIBLE);
            removePassword.setVisibility(View.VISIBLE);
        }
    }

}