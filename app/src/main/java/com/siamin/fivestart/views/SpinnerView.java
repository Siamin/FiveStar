package com.siamin.fivestart.views;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.siamin.fivestart.R;
import com.siamin.fivestart.controllers.SystemController;
import com.siamin.fivestart.helpers.SharedPreferencesHelper;

public class SpinnerView extends androidx.appcompat.widget.AppCompatSpinner {


    public SpinnerView(Context context) {
        super(context);

        SystemController systemController = new SystemController(context,new SharedPreferencesHelper(context));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.view_spinner, systemController.getSystemNames());
        this.setAdapter(adapter);


    }


}
