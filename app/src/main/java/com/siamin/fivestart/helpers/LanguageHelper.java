package com.siamin.fivestart.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class LanguageHelper {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private Context context;
    public String KeyEn="en",KeyFa="fa",KeyLanguage="myLanguage";

    public LanguageHelper(Context context){
        this.context = context;
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public void setLocal(String Lang){
        Locale locale = new Locale(Lang);
        Configuration configuration = new Configuration();


        locale.setDefault(locale);

        if (getAndroidVersionRelease() > 23){

            configuration.setLocale(locale);
            context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());
        }else{

            configuration.locale = locale;
            context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());
        }


        sharedPreferencesHelper.SetCode(KeyLanguage,Lang);

    }

    public void loadLanguage(){
        setLocal(sharedPreferencesHelper.get_Data(KeyLanguage,KeyFa));
    }

    public String getLanguage(){
        return sharedPreferencesHelper.get_Data(KeyLanguage,KeyFa);
    }

    public int getAndroidVersionRelease(){
        return Build.VERSION.SDK_INT;
    }

}
