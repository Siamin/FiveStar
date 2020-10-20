package com.siamin.fivestart.helpers;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.snackbar.Snackbar;
import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;

public class MessageHelper {

    private Context context;

    public MessageHelper(Context context){
        this.context = context;
    }


    public void Snackbar( String body, String Action, int color_text, int style) {

        if (getAndroidVersionRelease() < 28){
            toastMessage(body);
        }else{
            TSnackbar snackbar = TSnackbar.make(((Activity) context).findViewById(android.R.id.content), body, TSnackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);

            snackbar.setMaxWidth(Snackbar.LENGTH_LONG); //if you want fullsize on tablets
            View snackbarView = snackbar.getView();

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 80, 20, 0);
            snackbarView.setLayoutParams(lp);
            snackbarView.setBackground(context.getResources().getDrawable(style));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(context.getResources().getColor(color_text));
            textView.setGravity(Gravity.CENTER);

            snackbar.show();
        }

    }

    public void SuccssesMessage(String message){

        Snackbar(message,null, R.color.colorGreen,R.drawable.messagestylesuccess);

    }

    public void ErrorMessage(String message){
        Snackbar(message,null, R.color.colorRed,R.drawable.messagestyle_error);
    }

    private void toastMessage(String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }


    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

    public int getAndroidVersionRelease(){
        return Build.VERSION.SDK_INT;
    }
}
