package com.siamin.fivestart.helpers;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * key `passwordLogin` is password to login uesr
 * key `FirstRun` is user first run application
 * key `systemModel` is data systemModels application
 */

public class SharedPreferencesHelper  {

    private SharedPreferences sp;
    private String packages = "com.siamin.fivestart";
    private Context context;

    public SharedPreferencesHelper(Context context){
        this.context = context;
    }


    public void SetCode(String name, String code) {
        sp = context.getSharedPreferences(packages, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(name, code);
        edit.commit();

    }

    public void SetCode(String name, int code) {
        sp = context.getSharedPreferences(packages, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(name, code);
        edit.commit();

    }

    public String get_Data(String name, String Null) {
        sp = context.getSharedPreferences(packages, 0);
        return sp.getString(name, Null);
    }

    public int get_Data(String name, int Null) {
        sp = context.getSharedPreferences(packages, 0);
        return sp.getInt(name, Null);
    }



}
