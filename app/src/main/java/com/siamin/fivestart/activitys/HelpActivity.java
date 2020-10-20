package com.siamin.fivestart.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;

public class HelpActivity extends MyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ActivityPrevious = new Intent(this,MainActivity.class);

    }

    public void backPage(View view) {
        onBackPressed();
    }
}
