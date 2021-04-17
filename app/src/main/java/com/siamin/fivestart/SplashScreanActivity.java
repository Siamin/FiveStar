package com.siamin.fivestart;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.siamin.fivestart.activitys.FirstSettingActivity;
import com.siamin.fivestart.activitys.LoginActivity;
import com.siamin.fivestart.activitys.MainActivity;
import com.siamin.fivestart.reminder.activities.ViewActivity;
import com.siamin.fivestart.reminder.adapters.ReminderAdapter;

public class SplashScreanActivity extends MyActivity {


    private Animation slide_down_to_up, slide_up_to_down;
    private HTextView hTextView;
    private ImageView Logo;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscrean);

        hTextView = findViewById(R.id.textViewCompanyName);
        frameLayout = findViewById(R.id.splashscreanFrameLayout);
        Logo = findViewById(R.id.splashcreanLogo);


        slide_down_to_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_to_up);
        slide_up_to_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_to_down);

        frameLayout.startAnimation(slide_down_to_up);
        frameLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                Logo.startAnimation(slide_up_to_down);
                hTextView.setAnimateType(HTextViewType.TYPER);
                hTextView.animateText(getResources().getString(R.string.companyName));
                Timer();
            }
        });

    }

    void Timer() {

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (sp.get_Data("passwordLogin", "null").equals("null")) {
                            if (sp.get_Data("FirstRun", "null").equals("null")) {

                                startActivity(new Intent(SplashScreanActivity.this, FirstSettingActivity.class));
                                finish();
                            } else {

                                bAdapter.enable();
                                Intent intent = new Intent(SplashScreanActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            startActivity(new Intent(SplashScreanActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }, 3500);

    }


}
