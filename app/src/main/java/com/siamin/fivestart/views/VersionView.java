package com.siamin.fivestart.views;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.siamin.fivestart.R;

public class VersionView  extends AppCompatTextView {

    public VersionView(Context context, AttributeSet attis) {
        super(context);


        this.setText(context.getResources().getString(R.string.Version) + " " + getVersion(context));
    }


    public static String getVersion(Context context) {

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}
