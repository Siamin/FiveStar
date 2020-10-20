package com.siamin.fivestart.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.siamin.fivestart.R;

public class IndicatorHelper extends Dialog{

    private Context context;

    public IndicatorHelper(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_indicator);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }


}