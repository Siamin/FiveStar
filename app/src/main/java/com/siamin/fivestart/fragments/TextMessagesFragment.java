package com.siamin.fivestart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.adapters.TextMessageAdapter;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.models.TextMessageModel;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;


public class TextMessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private Spinner systemName;
    private String TAG="TAG_TextMessagesFragment";
    private ToggleSwitch toggleSwitch;
    private TextMessageAdapter textMessageAdapter;
    private String modelName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView");
        return inflater.inflate(R.layout.fragment_textmessages, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,"onViewCreated");

        initView();

        systemName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((SettingActivity)getContext()).systemController.setDefualtSystem(position);
                textMessageAdapter.indexSystem = position - 1;
                if (position > 0) {
                    toggleSwitch.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    textMessageAdapter.systemModel= ((SettingActivity) getContext()).systemController.getModelByIndex(textMessageAdapter.indexSystem);
                    modelName = textMessageAdapter.systemModel.Model;

                    ArrayList<String> labels = new ArrayList<>();

                    if (modelName.equals("GSA-208") || modelName.equals("GSA-209")) {
                        labels.add("A");
                        updateRecyclerView(getListDefualtA());
                    }

                    labels.add("B");
                    toggleSwitch.setLabels(labels);

                }else{
                    toggleSwitch.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    modelName = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {

                if ((modelName.equals("GSA-208") || modelName.equals("GSA-209")) && position == 0) {
                    textMessageAdapter.prifixCode = "A";
                    updateRecyclerView(getListDefualtA());
                } else {
                    textMessageAdapter.prifixCode = "B";
                    updateRecyclerView(getListDefualtB());
                }

            }
        });



    }

    private void initView(){
        systemName = getActivity().findViewById(R.id.textmessagesSystemName);
        recyclerView = getActivity().findViewById(R.id.textmessagesRecyclerView);
        llm = new LinearLayoutManager(getActivity());
        toggleSwitch = getActivity().findViewById(R.id.textmessagesToggleSwitch);
        toggleSwitch.setVisibility(View.GONE);

        ((SettingActivity) getContext()).systemController.setNamesToSpinnerById(systemName);
        ShowRecyclerView(getListDefualtB());
    }

    private void updateRecyclerView(List<TextMessageModel> model){
        textMessageAdapter.model = model;
        textMessageAdapter.notifyDataSetChanged();
    }

    private void ShowRecyclerView(List<TextMessageModel> model){
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        textMessageAdapter = new TextMessageAdapter(getActivity(), model);
        recyclerView.setAdapter(textMessageAdapter);
    }
    private List<TextMessageModel> getListDefualtB(){

        String json = "[{\n" +
                "\"Title\":\"متن فعال شدن سیستم\",\n" +
                "\"Body\":\"دزدگیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن سیستم\",\n" +
                "\"Body\":\"دزدگیر غیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۱\",\n" +
                "\"Body\":\"خروجی۱ فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۱\",\n" +
                "\"Body\":\"خروجی۱ غیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۲\",\n" +
                "\"Body\":\"خروجی ۲ فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۲\",\n" +
                "\"Body\":\"خروجی ۲ غیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۳\",\n" +
                "\"Body\":\"خروجی ۳ فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۳\",\n" +
                "\"Body\":\"خروجی ۳ غیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۴\",\n" +
                "\"Body\":\"خروجی ۴ فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۴\",\n" +
                "\"Body\":\"خروجی ۴ غیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن قطع برق\",\n" +
                "\"Body\":\"برق قطع شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن وصل برق\",\n" +
                "\"Body\":\"برق وصل شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم\",\n" +
                "\"Body\":\"هشدار!!آژیر!!\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن قطع خط تلفن\",\n" +
                "\"Body\":\"تلفن قطع شد\"\n" +
                "}]";


        return  ((SettingActivity)getContext()).convertJsonHelper.convertStringToTextMessageModel(json);

    }

    private List<TextMessageModel> getListDefualtA(){
        String json = "[{\n" +
                "\"Title\":\"متن آلارم زون ۱\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۱\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۲\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۲\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۳\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۳\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۴\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۴\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۵\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۵\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۶\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۶\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۷\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۷\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن آلارم زون ۸\",\n" +
                "\"Body\":\"هشدار!! آلارم زون ۸\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن سیستم\",\n" +
                "\"Body\":\"سیستم دزدگیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن سیستم\",\n" +
                "\"Body\":\"سیستم دزدگیر غیر فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن نیمه فعال شدن سیستم\",\n" +
                "\"Body\":\"سیستم دزدگیر  نیمه فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن بی صدا فعال شدن سیستم\",\n" +
                "\"Body\":\"سیستم دزدگیر بی صدا فعال شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۱\",\n" +
                "\"Body\":\"رله ۱ روشن شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۱\",\n" +
                "\"Body\":\"رله ۱ خاموش شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۲\",\n" +
                "\"Body\":\"رله ۲ روشن شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۲\",\n" +
                "\"Body\":\"رله ۲ خاموش شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۳\",\n" +
                "\"Body\":\"رله ۳ روشن شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۳\",\n" +
                "\"Body\":\"رله ۳ خاموش شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن فعال شدن خروجی ۴\",\n" +
                "\"Body\":\"رله ۴ روشن شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن غیر فعال شدن خروجی ۴\",\n" +
                "\"Body\":\"رله ۴ خاموش شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن وصل برق\",\n" +
                "\"Body\":\"برق وصل شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن قطع برق\",\n" +
                "\"Body\":\"برق قطع شد\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن کم بودن شارژ باطری\",\n" +
                "\"Body\":\"اخطار کم شدن شارژ باطری\"\n" +
                "},\n" +
                "{\n" +
                "\"Title\":\"متن قطع خط تلفن\",\n" +
                "\"Body\":\"تلفن قطع شد\"\n" +
                "}]";
        return  ((SettingActivity)getContext()).convertJsonHelper.convertStringToTextMessageModel(json);
    }


}