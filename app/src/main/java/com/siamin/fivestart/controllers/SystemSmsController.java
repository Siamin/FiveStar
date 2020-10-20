package com.siamin.fivestart.controllers;

import android.content.Context;
import android.util.Log;

import com.siamin.fivestart.R;
import com.siamin.fivestart.helpers.ConvertJsonHelper;
import com.siamin.fivestart.helpers.SharedPreferencesHelper;
import com.siamin.fivestart.helpers.ValidationHelper;
import com.siamin.fivestart.models.ErrorModel;
import com.siamin.fivestart.models.SystemModel;

import java.util.ArrayList;
import java.util.List;

public class SystemSmsController {
    private Context context;
    private ConvertJsonHelper convertJsonHelper;
    private SharedPreferencesHelper sp;
    private String Key = "systemSmsModel";
    private ValidationHelper valid;

    public SystemSmsController(Context context, SharedPreferencesHelper sp) {
        this.context = context;
        this.sp = sp;
        this.convertJsonHelper = new ConvertJsonHelper(context);
        this.valid = new ValidationHelper();

    }

    public ErrorModel addSystem(String systemName, String phoneNumber, String pinCode) {
        if (!systemName.isEmpty()) {
            if (!phoneNumber.isEmpty() && valid.validationPhoneNumber(phoneNumber)) {
                if (!pinCode.isEmpty()) {
                    List<SystemModel> listModel = getListDevice();
                    SystemModel systemModel = new SystemModel();
                    systemModel.id = getListDevice().size()==0?1:listModel.get(listModel.size()-1).id+1;
                    systemModel.systemName = systemName;
                    systemModel.phoneNumber = phoneNumber;
                    systemModel.pinCode = pinCode;
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
    }

    public ErrorModel editSystem(SystemModel model, String newPin, String confirmPin, int index) {
        if (!model.systemName.isEmpty()) {
            if (!model.phoneNumber.isEmpty() && valid.validationPhoneNumber(model.phoneNumber)) {
                List<SystemModel> listModel =  getListDevice();
                SystemModel systemModel = listModel.get(index);
                if (!model.pinCode.isEmpty() && systemModel.pinCode.equals(model.pinCode)) {
                    if(!newPin.isEmpty() && newPin.equals(confirmPin)){

                        systemModel.systemName = model.systemName;
                        systemModel.phoneNumber = model.phoneNumber;
                        systemModel.pinCode = newPin;

                        String Json = convertJsonHelper.convertSystemModelToStrin(listModel);

                        sp.SetCode(Key, Json);

                        return new ErrorModel(context.getString(R.string.DeviceSuccessfullyEdited), true);
                    }else{
                        return new ErrorModel(context.getString(R.string.wrongNewPinAndConfirm), false);
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
    }

    public ErrorModel removeSystem(int index) {

        List<SystemModel> listModel = getListDevice();

        listModel.remove(index);

        String Json = convertJsonHelper.convertSystemModelToStrin(listModel);

        sp.SetCode(Key, Json);

        return new ErrorModel(context.getString(R.string.DeviceSuccessfullyDeleted), true);

    }

    public SystemModel getModelByIndex(int index) {
        List<SystemModel> list = getListDevice();
        return list.get(index);
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
