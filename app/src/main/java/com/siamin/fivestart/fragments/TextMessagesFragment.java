package com.siamin.fivestart.fragments;

import android.annotation.SuppressLint;
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

                    if(!modelName.equals("GSA-209") && !modelName.equals("other") ){
                        if (modelName.equals("GSA-208")) {
                            labels.add("A");
                            updateRecyclerView(getListDefualtA());
                        }

                        labels.add("B");
                        toggleSwitch.setLabels(labels);
                    }else{
                        ((SettingActivity)getContext()).message.ErrorMessage(getContext().getResources().getString(R.string.errorTheFeatureIsNotEnabled));
                        recyclerView.setVisibility(View.GONE);
                    }

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

        String json = "[{\"Title\":\"متن فعال شدن سیستم\",\"Body\":\"دزدگیر فعال شد\"},{\"Title\":\"متن غیر فعال شدن سیستم\",\"Body\":\"دزدگیر غیر فعال شد\"},{\"Title\":\"متن فعال شدن خروجی ۱\",\"Body\":\"خروجی۱ فعال شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۱\",\"Body\":\"خروجی۱ غیر فعال شد\"},{\"Title\":\"متن فعال شدن خروجی ۲\",\"Body\":\"خروجی ۲ فعال شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۲\",\"Body\":\"خروجی ۲ غیر فعال شد\"},{\"Title\":\"متن فعال شدن خروجی ۳\",\"Body\":\"خروجی ۳ فعال شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۳\",\"Body\":\"خروجی ۳ غیر فعال شد\"},{\"Title\":\"متن فعال شدن خروجی ۴\",\"Body\":\"خروجی ۴ فعال شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۴\",\"Body\":\"خروجی ۴ غیر فعال شد\"},{\"Title\":\"متن قطع برق\",\"Body\":\"برق قطع شد\"},{\"Title\":\"متن وصل برق\",\"Body\":\"برق وصل شد\"},{\"Title\":\"متن آلارم\",\"Body\":\"هشدار!!آژیر!!\"},{\"Title\":\"متن قطع خط تلفن\",\"Body\":\"تلفن قطع شد\"}]";


        return ((SettingActivity)getContext()).convertJsonHelper.convertStringToTextMessageModel(json);

    }


    private List<TextMessageModel> getListDefualtA(){
        String json = "[{\"Title\":\"متن آلارم زون ۱\",\"Body\":\"هشدار!! آلارم زون ۱\"},{\"Title\":\"متن آلارم زون ۲\",\"Body\":\"هشدار!! آلارم زون ۲\"},{\"Title\":\"متن آلارم زون ۳\",\"Body\":\"هشدار!! آلارم زون ۳\"},{\"Title\":\"متن آلارم زون ۴\",\"Body\":\"هشدار!! آلارم زون ۴\"},{\"Title\":\"متن آلارم زون ۵\",\"Body\":\"هشدار!! آلارم زون ۵\"},{\"Title\":\"متن آلارم زون ۶\",\"Body\":\"هشدار!! آلارم زون ۶\"},{\"Title\":\"متن آلارم زون ۷\",\"Body\":\"هشدار!! آلارم زون ۷\"},{\"Title\":\"متن آلارم زون ۸\",\"Body\":\"هشدار!! آلارم زون ۸\"},{\"Title\":\"متن فعال شدن سیستم\",\"Body\":\"سیستم دزدگیر فعال شد\"},{\"Title\":\"متن غیر فعال شدن سیستم\",\"Body\":\"سیستم دزدگیر غیر فعال شد\"},{\"Title\":\"متن نیمه فعال شدن سیستم\",\"Body\":\"سیستم دزدگیر  نیمه فعال شد\"},{\"Title\":\"متن بی صدا فعال شدن سیستم\",\"Body\":\"سیستم دزدگیر بی صدا فعال شد\"},{\"Title\":\"متن فعال شدن خروجی ۱\",\"Body\":\"رله ۱ روشن شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۱\",\"Body\":\"رله ۱ خاموش شد\"},{\"Title\":\"متن فعال شدن خروجی ۲\",\"Body\":\"رله ۲ روشن شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۲\",\"Body\":\"رله ۲ خاموش شد\"},{\"Title\":\"متن فعال شدن خروجی ۳\",\"Body\":\"رله ۳ روشن شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۳\",\"Body\":\"رله ۳ خاموش شد\"},{\"Title\":\"متن فعال شدن خروجی ۴\",\"Body\":\"رله ۴ روشن شد\"},{\"Title\":\"متن غیر فعال شدن خروجی ۴\",\"Body\":\"رله ۴ خاموش شد\"},{\"Title\":\"متن وصل برق\",\"Body\":\"برق وصل شد\"},{\"Title\":\"متن قطع برق\",\"Body\":\"برق قطع شد\"},{\"Title\":\"متن کم بودن شارژ باطری\",\"Body\":\"اخطار کم شدن شارژ باطری\"},{\"Title\":\"متن قطع خط تلفن\",\"Body\":\"تلفن قطع شد\"}]";

       return ((SettingActivity)getContext()).convertJsonHelper.convertStringToTextMessageModel(json);
    }


}