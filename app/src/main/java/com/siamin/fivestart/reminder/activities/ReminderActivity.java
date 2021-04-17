package com.siamin.fivestart.reminder.activities;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.MainActivity;
import com.siamin.fivestart.helpers.SharedPreferencesHelper;
import com.siamin.fivestart.reminder.adapters.ReminderAdapter;
import com.siamin.fivestart.reminder.adapters.ViewPageAdapter;



public class ReminderActivity extends AppCompatActivity implements ReminderAdapter.RecyclerListener {

    PagerSlidingTabStrip pagerSlidingTabStrip;
    Toolbar toolbar;
    ViewPager viewPager;
    FloatingActionButton floatingActionButton;
    SharedPreferencesHelper sp;
    private boolean fabIsHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initView();

        if("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER) && !sp.getBoolean("protected",false)) {
            android.app.AlertDialog.Builder builder  = new android.app.AlertDialog.Builder(this);
            builder.setTitle(R.string.titleDialogPermission).setMessage(R.string.bodyDialogPermission)
                    .setPositiveButton(R.string.Okey, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                            startActivity(intent);
                            sp.setBoolean("protected",true);
                        }
                    }).setNegativeButton(R.string.Cancle,null).create().show();
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderActivity.this, CreateEditActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void initView() {
        pagerSlidingTabStrip = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        floatingActionButton = findViewById(R.id.fab_button);

        sp = new SharedPreferencesHelper(this);
    }


    @Override
    public void hideFab() {
        floatingActionButton.hide();
        fabIsHidden = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fabIsHidden) {
            floatingActionButton.show();
            fabIsHidden = false;
        }
    }


}