package com.siamin.fivestart.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.helpers.ConvertJsonHelper;
import com.siamin.fivestart.helpers.SharedPreferencesHelper;
import com.siamin.fivestart.helpers.ValidationHelper;
import com.siamin.fivestart.models.ErrorModel;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class SystemController {

    private Context context;
    private ConvertJsonHelper convertJsonHelper;
    private SharedPreferencesHelper sp;
    private String Key = "systemModel",KeyDefualt="DefualtSysatem";
    private ValidationHelper valid;

    public SystemController(Context context, SharedPreferencesHelper sp) {
        this.context = context;
        this.sp = sp;
        this.convertJsonHelper = new ConvertJsonHelper(context);
        this.valid = new ValidationHelper();

    }

    public void setDefualtSystem(int value){

        sp.SetCode(KeyDefualt,value);

    }

    public int getDefualtSystem(){
        return sp.get_Data(KeyDefualt,0);
    }

    public ErrorModel editSystem(SystemModel model, String newPin, String confirmPin, boolean changePass, int index) {
        if (valid.validationModelSystemName(model.Model)) {
            if (!model.systemName.isEmpty()) {
                if (!model.phoneNumber.isEmpty() && valid.validationPhoneNumber(model.phoneNumber)) {
                    List<SystemModel> listModel = getListDevice();
                    SystemModel systemModel = listModel.get(index);
                    if (!model.pinCode.isEmpty() && systemModel.pinCode.equals(model.pinCode)) {
                        if (!model.pinCode.isEmpty() && systemModel.pinCode.equals(model.pinCode)) {

                            if (!newPin.isEmpty() && newPin.equals(confirmPin) || !changePass) {

                                if (valid.validationPinCode(newPin) || !changePass) {
                                    systemModel.systemName = model.systemName;
                                    systemModel.phoneNumber = model.phoneNumber;
                                    systemModel.Model = model.Model;
                                    systemModel.pinCode = changePass ? newPin : model.pinCode;
                                    systemModel.ArmCode = model.ArmCode;
                                    systemModel.DisArmCode = model.DisArmCode;

                                    String Json = convertJsonHelper.convertSystemModelToStrin(listModel);

                                    sp.SetCode(Key, Json);

                                    return new ErrorModel(context.getString(R.string.DeviceSuccessfullyEdited), true);
                                } else {
                                    return new ErrorModel(context.getString(R.string.lenghtCharPin), false);
                                }

                            } else {
                                return new ErrorModel(context.getString(R.string.wrongNewPinAndConfirm), false);
                            }

                        } else {
                            return new ErrorModel(context.getString(R.string.worngPinCode), false);
                        }

                    } else {
                        return new ErrorModel(context.getString(R.string.pleaseEnterPinCode), false);
                    }
                } else {
                    return new ErrorModel(context.getString(R.string.pleaseEnterPhoneNumber), false);
                }
            } else {
                return new ErrorModel(context.getString(R.string.pleaseEnterSystem), false);
            }
        } else {
            return new ErrorModel(context.getString(R.string.pleaseSelectedModelSystem), false);
        }

    }

    public ErrorModel removeSystem(int index) {

        List<SystemModel> listModel = getListDevice();

        listModel.remove(index);

        String Json = convertJsonHelper.convertSystemModelToStrin(listModel);

        sp.SetCode(Key, Json);
        if((getDefualtSystem()-1)==index){
            setDefualtSystem(0);
        }

        return new ErrorModel(context.getString(R.string.DeviceSuccessfullyDeleted), true);

    }

    public ErrorModel addSystem(SystemModel systemModel) {

        if (valid.validationModelSystemName(systemModel.Model)) {
            if (!systemModel.systemName.isEmpty()) {
                if (!systemModel.phoneNumber.isEmpty() && valid.validationPhoneNumber(systemModel.phoneNumber)) {
                    if (!systemModel.pinCode.isEmpty()) {
                        List<SystemModel> listModel = getListDevice();
                        systemModel.id = getListDevice().size() == 0 ? 1 : listModel.get(listModel.size() - 1).id + 1;
                        listModel.add(systemModel);

                        String Json = convertJsonHelper.convertSystemModelToStrin(listModel);
                        sp.SetCode(Key, Json);

                        return new ErrorModel(context.getString(R.string.DeviceSuccessfullyAdded), true);

                    } else {
                        return new ErrorModel(context.getString(R.string.pleaseEnterPinCode), false);
                    }
                } else {
                    return new ErrorModel(context.getString(R.string.pleaseEnterPhoneNumber), false);
                }
            } else {
                return new ErrorModel(context.getString(R.string.pleaseEnterSystem), false);
            }
        } else {
            return new ErrorModel(context.getString(R.string.pleaseSelectedModelSystem), false);
        }


    }

    public boolean validModelSystem(String modelSystem) {
        return valid.validationModelSystemName(modelSystem);
    }

    public void setNamesToSpinnerById(Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.view_spinner, getSystemNames());
        spinner.setAdapter(adapter);
        try{
            spinner.setSelection(getDefualtSystem());
        }catch (Exception e){

        }

    }

    public void setCodeToSpinnerById(Spinner spinner, boolean status) {
        String[] titleCode = context.getResources().getStringArray(R.array.TitleCodeReminder);

        if (status)
            titleCode = context.getResources().getStringArray(R.array.TitleCodeReminderOther);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.view_spinner, titleCode);
        spinner.setAdapter(adapter);
    }

    public SystemModel getModelByIndex(int index) {
        List<SystemModel> list = getListDevice();
        return list.get(index);
    }

    public String getCodeByIndex(int index, String pinCode) {
        return context.getResources().getStringArray(R.array.TitleCodeReminderOther)[index] + pinCode;
    }

    public int findIndexSystemByPhone(String name) {
        List<SystemModel> list = getListDevice();
        int _return = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).phoneNumber.equals(name)) {
                _return = i + 1;
            }
        }

        return _return;
    }

    public int findIndexCodeByName(String title) {
        String[] titleArray = context.getResources().getStringArray(R.array.TitleCodeReminder);
        int _return = 0;
        for (int i = 0; i < titleArray.length; i++) {
            if (titleArray[i].equals(title)) {
                _return = i + 1;
            }
        }

        return _return;
    }

    public List<String> getSystemNames() {
        List<String> names = new ArrayList<String>();

        names.add(context.getString(R.string.selectSystem));

        List<SystemModel> systemModelList = getListDevice();

        for (SystemModel system : systemModelList) {
            names.add(system.systemName);
        }

        return names;
    }

    public List<SystemModel> getListDevice() {
        String systemJson = sp.get_Data(Key, "[]");
        return convertJsonHelper.convertStringToSystemModel(systemJson);
    }



}
