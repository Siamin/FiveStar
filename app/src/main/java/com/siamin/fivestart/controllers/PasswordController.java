package com.siamin.fivestart.controllers;

import android.content.Context;

import com.siamin.fivestart.R;
import com.siamin.fivestart.helpers.SharedPreferencesHelper;
import com.siamin.fivestart.helpers.ValidationHelper;
import com.siamin.fivestart.models.ErrorModel;

/**
 * passwordLogin => key ShaeredPreferences
 */
public class PasswordController {

    private Context context;
    private SharedPreferencesHelper sp;
    private String Key = "passwordLogin";
    private ValidationHelper valid;

    public PasswordController(Context context, SharedPreferencesHelper sp){
        this.context = context;
        this.sp = sp;
        this.valid = new ValidationHelper();
    }

    public ErrorModel setPassword(String password, String ConfirmPassword){
        try {
            if (password.equals(ConfirmPassword)){
                if (valid.validationPassword(password)){
                    sp.SetCode(Key,password);
                    return new ErrorModel(context.getString(R.string.succssesPassword),true);
                }else{
                    return new ErrorModel(context.getString(R.string.numberOfPasswordCharacters),false);
                }

            }else{
                return new ErrorModel(context.getString(R.string.passwordNotMatchConfirmPassword),false);
            }
        }catch (Exception e){
            return new ErrorModel(context.getString(R.string.succssesPassword),false);
        }

    }

    public ErrorModel changePassword(String oldPassword, String NewPassword , String ConfirmPassword){
        try {
            String password = sp.get_Data(Key,"null");
            if (NewPassword.equals(ConfirmPassword)){
                if (password.equals(oldPassword)){
                    sp.SetCode("passwordLogin",NewPassword);
                    return new ErrorModel(context.getString(R.string.succssesPassword),true);
                }else{
                    return new ErrorModel(context.getString(R.string.correctOldPassword),false);
                }
            }else{
                return new ErrorModel(context.getString(R.string.passwordNotMatchConfirmPassword),false);
            }

        }catch (Exception e){
            return new ErrorModel(context.getString(R.string.errorChangePassword),false);
        }

    }

    public ErrorModel checkPassword(String Password){
        String password = sp.get_Data(Key,"null");
        if (password.equals(Password)){
            return new ErrorModel(context.getString(R.string.succssesPasswordLogin),true);
        }else{
            return new ErrorModel(context.getString(R.string.ErrorPasswordLogin),false);
        }
    }

    public ErrorModel removePassword(String oldPassword){
        String password = sp.get_Data(Key,"null");
        if (!oldPassword.isEmpty()){
            if (password.equals(oldPassword)){
                sp.SetCode("passwordLogin","null");
                return new ErrorModel(context.getString(R.string.RemovePassword),true);
            }else{
                return new ErrorModel(context.getString(R.string.correctOldPassword),false);
            }
        }else{
            return new ErrorModel(context.getString(R.string.OldpasswordIsEmpty),false);
        }
    }

    public boolean isPassword(){
        return sp.get_Data(Key,"null").equals("null")?false:true;
    }
}
