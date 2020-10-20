package com.siamin.fivestart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.siamin.fivestart.activitys.FirstSettingActivity;
import com.siamin.fivestart.activitys.LoginActivity;
import com.siamin.fivestart.activitys.MainActivity;

public class SplashScreanActivity extends MyActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscrean);



        Timer();
    }

    void Timer() {

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (sp.get_Data("passwordLogin","null").equals("null")) {
                            if (sp.get_Data("FirstRun","null").equals("null")){

                                startActivity(new Intent(SplashScreanActivity.this, FirstSettingActivity.class));
                                finish();
                            }else {
                                bAdapter.enable();
                                startActivity(new Intent(SplashScreanActivity.this, MainActivity.class));
                                finish();
                            }

                        }else{
                            startActivity(new Intent(SplashScreanActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }, 3500);

    }
}
