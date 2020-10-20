package com.siamin.fivestart.helpers;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.SplashScreanActivity;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.adapters.BluetoothListAdapter;
import com.siamin.fivestart.controllers.SystemController;
import com.siamin.fivestart.interfaces.BluetoothInterface;
import com.siamin.fivestart.interfaces.DialogInterface;
import com.siamin.fivestart.interfaces.SendSmsInterface;
import com.siamin.fivestart.interfaces.SetPasswordInterface;
import com.siamin.fivestart.interfaces.SystemInterface;
import com.siamin.fivestart.models.BluetoothModel;
import com.siamin.fivestart.models.ErrorModel;
import com.siamin.fivestart.models.SystemModel;

import java.util.List;

public class DialogHelper {

    private Context context;
    private SystemController systemController;
    private MessageHelper messageHelper;
    private ValidationHelper validationHelper = new ValidationHelper();

    public DialogHelper(Context context, SystemController systemController, MessageHelper messageHelper) {
        this.context = context;
        this.messageHelper = messageHelper;
        this.systemController = systemController;
    }

    public void dialogFirstRun(final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_firstrun);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //****************************************************
        TextView Title = dialog.findViewById(R.id.dialogDefault_title);
        TextView Body = dialog.findViewById(R.id.dialogDefault_body);
        CardView okey = dialog.findViewById(R.id.dialogDefault_okey);
        TextView cancle = dialog.findViewById(R.id.dialogDefaultNotNow);
        //****************************************************
        Title.setText(context.getResources().getString(R.string.firstRunDialogTitle));
        Body.setText(context.getResources().getString(R.string.firstRunDialogBody));
        //****************************************************
        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogInterface.ok();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogInterface.cancel();
            }
        });
    }

    public void dialogConnectionBluetooth(final List<BluetoothModel> models, final BluetoothInterface BI) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_connection);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //****************************************************
        RecyclerView recyclerView = dialog.findViewById(R.id.dialogList);
        ImageView close = dialog.findViewById(R.id.dialogListClose);
        LinearLayoutManager llm = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new BluetoothListAdapter(context, models, BI, dialog));
        //****************************************************

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BI.notSelectBlutooth(false);
            }
        });


    }

    public void dialogAddNewSystem(final SystemInterface systemInterface) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addsystem);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //****************************************************
        final EditText systemName = dialog.findViewById(R.id.dialogSystemName);
        final EditText phoneNumber = dialog.findViewById(R.id.dialogPhoneNumber);
        final EditText pinCode = dialog.findViewById(R.id.dialogPincode);
        final Spinner modelSystem = dialog.findViewById(R.id.dialogSpinnerDevice);
        setSpinnerSystemModels(modelSystem);
        TextView add = dialog.findViewById(R.id.dialogAdd);
        CardView close = dialog.findViewById(R.id.dialogClose);
        //****************************************************
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemModel systemModel = new SystemModel();
                systemModel.systemName=systemName.getText().toString();
                systemModel.phoneNumber=phoneNumber.getText().toString();
                systemModel.pinCode=pinCode.getText().toString();
                systemModel.Model = getNameSystemModels(modelSystem.getSelectedItemPosition());
                systemModel.ArmCode = "5";
                systemModel.DisArmCode = "6";

                ErrorModel errorModel = systemController.addSystem(systemModel);
                if (errorModel.Status) {
                    messageHelper.Snackbar(errorModel.Message, null, R.color.colorGreen, R.drawable.messagestylesuccess);
                    dialog.dismiss();

                    systemInterface.setNewSystem(systemModel);
                } else {
                    messageHelper.Snackbar(errorModel.Message, null, R.color.colorRed, R.drawable.messagestyle_error);
                }
            }
        });

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
                    pinCode.setError(context.getResources().getString(R.string.YouCanNotEnterMoreThan4Characters));

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void dialogRemovwSystem(final SystemInterface systemInterface, final int index) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_default);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //****************************************************
        TextView Title = dialog.findViewById(R.id.dialogDefault_title);
        TextView Body = dialog.findViewById(R.id.dialogDefault_body);
        TextView delete = dialog.findViewById(R.id.dialogDefault_okey);
        TextView cancle = dialog.findViewById(R.id.dialogDefault_cancle);
        //****************************************************
        delete.setText(context.getResources().getString(R.string.Delete));
        cancle.setText(context.getResources().getString(R.string.Cancle));
        Title.setText(context.getResources().getString(R.string.Delete));
        Body.setText(context.getResources().getString(R.string.areYouDeleteSystem));
        //****************************************************
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ErrorModel errorModel = systemController.removeSystem(index);
                if (errorModel.Status) {
                    messageHelper.Snackbar(errorModel.Message, null, R.color.colorGreen, R.drawable.messagestylesuccess);
                    systemInterface.removeSystem(index);
                } else {
                    messageHelper.Snackbar(errorModel.Message, null, R.color.colorRed, R.drawable.messagestyle_error);

                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void dialogEditSystem(final SystemInterface systemInterface, final int index) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addsystem);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //****************************************************
        final EditText systemName = dialog.findViewById(R.id.dialogSystemName);
        final EditText phoneNumber = dialog.findViewById(R.id.dialogPhoneNumber);
        final EditText pinCode = dialog.findViewById(R.id.dialogPincode);
        final EditText newPinCode = dialog.findViewById(R.id.dialogNewPincode);
        final EditText confirmPinCode = dialog.findViewById(R.id.dialogConfirmPincode);
        final CheckBox changePass = dialog.findViewById(R.id.dialogcheckbox);
        final Spinner modelSystem = dialog.findViewById(R.id.dialogSpinnerDevice);
        setSpinnerSystemModels(modelSystem);
        dialog.findViewById(R.id.dialoglayoutcheckbox).setVisibility(View.VISIBLE);

        TextView add = dialog.findViewById(R.id.dialogAdd);
        CardView close = dialog.findViewById(R.id.dialogClose);

        final SystemModel modelSelect = systemController.getModelByIndex(index);
        //****************************************************
        final SystemModel model = systemController.getModelByIndex(index);
        systemName.setText(model.systemName);
        pinCode.setText(model.pinCode);
        phoneNumber.setText(model.phoneNumber);

        modelSystem.setSelection(findModelSystem(modelSelect.Model));
        //****************************************************
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewVisibil = View.GONE;
                if (changePass.isChecked()){
                    viewVisibil = View.VISIBLE;
                }
                dialog.findViewById(R.id.dialogLayoutNewPincode).setVisibility(viewVisibil);
                dialog.findViewById(R.id.dialogLayoutConfirmPincode).setVisibility(viewVisibil);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemModel modelEdit = new SystemModel();
                modelEdit.systemName = systemName.getText().toString();
                modelEdit.phoneNumber = phoneNumber.getText().toString();
                modelEdit.pinCode = pinCode.getText().toString();
                modelEdit.Model = getNameSystemModels(modelSystem.getSelectedItemPosition());
                modelEdit.ArmCode = model.getArmCode();
                modelEdit.DisArmCode = model.getDisArmCode();

                String oldPinCode = modelSelect.pinCode;

                ErrorModel errorModel = systemController.editSystem(modelEdit, newPinCode.getText().toString(), confirmPinCode.getText().toString(),changePass.isChecked(), index);


                if (errorModel.Status) {
                    if(changePass.isChecked()){
                        ((SettingActivity)context).sendMessage("0"+oldPinCode+newPinCode.getText().toString(),index+1);
                    }
                    messageHelper.Snackbar(errorModel.Message, null, R.color.colorGreen, R.drawable.messagestylesuccess);
                    dialog.dismiss();
                    modelEdit.pinCode = newPinCode.getText().toString();
                    systemInterface.editSystem(modelEdit, index);
                } else {
                    messageHelper.Snackbar(errorModel.Message, null, R.color.colorRed, R.drawable.messagestyle_error);
                }
            }
        });

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
                    pinCode.setError(context.getResources().getString(R.string.YouCanNotEnterMoreThan4Characters));

            }
        });

        newPinCode.addTextChangedListener(new TextWatcher() {

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
                    newPinCode.setError(context.getResources().getString(R.string.app_name));

            }
        });

        confirmPinCode.addTextChangedListener(new TextWatcher() {

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
                    confirmPinCode.setError(context.getResources().getString(R.string.YouCanNotEnterMoreThan4Characters));

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void dialogSetPassword(final SetPasswordInterface setPasswordInterface) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setpassword);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //****************************************************
        final EditText NewPassword = dialog.findViewById(R.id.dialogSetPasswordPassword);
        final EditText ConfirPassword = dialog.findViewById(R.id.dialogSetPasswordConfirmPassword);
        CardView add = dialog.findViewById(R.id.dialogSetPasswordSubmit);
        CardView close = dialog.findViewById(R.id.dialogClose);

        //****************************************************

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NewPassword.getText().toString().isEmpty() && !ConfirPassword.getText().toString().isEmpty()){
                    setPasswordInterface.setPass(NewPassword.getText().toString(),ConfirPassword.getText().toString());
                }else{
                    messageHelper.ErrorMessage(context.getResources().getString(R.string.pleaseEnterPassword));
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setPasswordInterface.canselSetPass();
            }
        });


    }

    public void dialogReports(final SendSmsInterface sendSmsInterface) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //****************************************************
        Button RemoteActive=dialog.findViewById(R.id.dialogReportsRemoteActive);
        Button RemoteDeactive=dialog.findViewById(R.id.dialogReportsRemoteDeactive);
        Button PowerActive=dialog.findViewById(R.id.dialogReportsPowerActive);
        Button PowerDeactive=dialog.findViewById(R.id.dialogReportsPowerDeactive);
        CardView close = dialog.findViewById(R.id.dialogClose);
        //****************************************************
        RemoteActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send code H1+pinCode
                sendSmsInterface.sendMessage("H1");
            }
        });

        RemoteDeactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //send code H0+pinCode
                sendSmsInterface.sendMessage("H0");
            }
        });

        PowerActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send code I1+pinCode
                sendSmsInterface.sendMessage("I1");
            }
        });

        PowerDeactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //send code I0+pinCode
                sendSmsInterface.sendMessage("I0");
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendSmsInterface.cancle();
            }
        });


    }

    private void setSpinnerSystemModels(Spinner modelSystem){
        String[] SystemList = context.getResources().getStringArray(R.array.SystemList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, SystemList);
        modelSystem.setAdapter(adapter);
    }

    private int findModelSystem(String model){
        String[] SystemList = context.getResources().getStringArray(R.array.SystemList);

        int return_ = 0;
        for (int i = 0;i<SystemList.length;i++){
            if (model.equals(SystemList[i])){
                return_ = i;
                break;
            }

        }

        return  return_;
    }

    public String getNameSystemModels(int index){
        String[] SystemList = context.getResources().getStringArray(R.array.SystemList);

        return SystemList[index];
    }

    public void ChangeLanguage(final Context context, final boolean lang) {

        final LanguageHelper languageHelper = new LanguageHelper(context);

        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_changelanguage);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        //*************************************

        TextView close = dialog.findViewById(R.id.dialogLanguageClose);
        TextView change = dialog.findViewById(R.id.dialogLanguageChange);

        //*************************************
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lang){
                    languageHelper.setLocal(languageHelper.KeyEn);
                }else{
                    languageHelper.setLocal(languageHelper.KeyFa);
                }

                restartApplication(context);
            }
        });
        //*************************************

    }


    private void restartApplication(Context context) {
        Intent mStartActivity = new Intent(context, SplashScreanActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
