package com.siamin.fivestart.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.MyActivity;
import com.siamin.fivestart.R;
import com.siamin.fivestart.adapters.ZoneAdabter;
import com.siamin.fivestart.models.ErrorModel;
import com.siamin.fivestart.models.SystemModel;


public class ZoneActivity extends MyActivity {


    private Spinner spinner;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private ZoneAdabter zoneAdabter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        ActivityPrevious = new Intent(this, MainActivity.class);

        initView();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                systemController.setDefualtSystem(position);
                if (position > 0) {
                    String systemModel = systemController.getModelByIndex(position - 1).Model;
                    if (systemModel.equals(getResources().getString(R.string.GSA_208))) {
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        message.ErrorMessage(getResources().getString(R.string.ZonesAreOnlyUsedForGSA_208DesignModels));
                    }

                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initView() {
        spinner = findViewById(R.id.SpinnerSystem);
        recyclerView = findViewById(R.id.zoonRecyclerView);
        llm = new LinearLayoutManager(this);
        systemController.setNamesToSpinnerById(spinner);

        createRecycler(8);
    }

    private void createRecycler(int count) {
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        zoneAdabter = new ZoneAdabter(this, count);
        recyclerView.setAdapter(zoneAdabter);
    }

    public void backPage(View view) {
        onBackPressed();
    }

    public void sendSms(String dataSend) {
        int selected = spinner.getSelectedItemPosition();
        if (selected > 0) {
            SystemModel model = systemController.getModelByIndex(selected - 1);
            String indext = (dataSend.replace("D", "")).replace("A", "");

            model.ArmCode =  indext + "A";
            model.DisArmCode = indext + "D";

            ErrorModel errorModel = systemController.editSystem(model, "", "", false, selected - 1);

            if (errorModel.Status) {
                message.Snackbar(errorModel.Message, null, R.color.colorGreen, R.drawable.messagestylesuccess);
            } else {
                message.Snackbar(errorModel.Message, null, R.color.colorRed, R.drawable.messagestyle_error);
            }
            model = systemController.getModelByIndex(selected - 1);

            dataSend += model.pinCode;
            sendSMS(model.phoneNumber, dataSend);
        } else {
            message.ErrorMessage(getString(R.string.pleaseSelectDevice));
        }
    }


}
