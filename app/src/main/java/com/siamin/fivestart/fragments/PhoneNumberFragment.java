package com.siamin.fivestart.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.adapters.PhoneNumberAdapter;
import com.siamin.fivestart.models.PhoneNumberSmsModel;
import com.siamin.fivestart.models.SystemModel;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class PhoneNumberFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private Spinner systemName;
    private String TAG = "TAG_PhoneNumberFragment";
    private PhoneNumberAdapter phoneNumberAdapter;
    private String modelName = "";
    private ToggleSwitch toggleSwitch;
    private TextView inquery;
    private PhoneNumberSmsModel phoneNumberSmsModel = new PhoneNumberSmsModel();
    public int prifixCode = 0, indexSystem = 0;
    public SystemModel systemModel = new SystemModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_phonenumber, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");

        initView();

        systemName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((SettingActivity)getContext()).systemController.setDefualtSystem(position);
                indexSystem = position - 1;

                Log.i(TAG,"indexSystem => "+indexSystem);
                if (phoneNumberSmsModel.TelephoneNumber != null && phoneNumberSmsModel.TelephoneNumber.size() > 0)
                    phoneNumberSmsModel.TelephoneNumber.clear();


                if (phoneNumberSmsModel.TelephoneNumber != null && phoneNumberSmsModel.SimCardNumber.size() > 0)
                    phoneNumberSmsModel.SimCardNumber.clear();


                if (phoneNumberSmsModel.TelephoneNumber != null && phoneNumberSmsModel.SmsNumber.size() > 0)
                    phoneNumberSmsModel.SmsNumber.clear();


                if (position > 0) {
                    inquery.setVisibility(View.VISIBLE);
                    toggleSwitch.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    systemModel = ((SettingActivity) getContext()).systemController.getModelByIndex(indexSystem);
                    modelName = systemModel.Model;





                    ArrayList<String> labels = new ArrayList<>();
                    labels.add(getContext().getString(R.string.TitleSmsPhoneNumber));

                    if (!modelName.equals(getContext().getResources().getString(R.string.GSA_209))) {
                        labels.add(getContext().getString(R.string.TitleTelePhone));
                    }

                    labels.add(getContext().getString(R.string.TitleCallPhoneNumber));
                    toggleSwitch.setLabels(labels);


                    phoneNumberAdapter.setData(systemModel,indexSystem,prifixCode);

                } else {
                    inquery.setVisibility(View.INVISIBLE);
                    toggleSwitch.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    systemModel = new SystemModel();
                    modelName = "";

                    phoneNumberAdapter.setData(systemModel,indexSystem,prifixCode);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {

                if (modelName.equals("GSA-209") && position == 2) {
                    prifixCode = 1;

                } else {
                    prifixCode = position;
                }

                setOption();

            }
        });

        inquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemModel systemModel = ((SettingActivity) getContext()).systemController.getModelByIndex(indexSystem);
                if (((SettingActivity) getContext()).sendMessage("C" + systemModel.pinCode, indexSystem + 1)) {
                    ((SettingActivity) getContext()).LoaderGetStatus(systemModel.phoneNumber, "phoneNumberFragment", 3);
                }
            }
        });

    }

    void initView() {

        inquery = getActivity().findViewById(R.id.detilesPhoneNumbeInquiry);
        systemName = getActivity().findViewById(R.id.SpinnerSystem);
        recyclerView = getActivity().findViewById(R.id.detilesPhoneNumbeRecycler);
        llm = new LinearLayoutManager(getActivity());
        toggleSwitch = getActivity().findViewById(R.id.detilesPhoneNumbeToggleSwitch);
        toggleSwitch.setVisibility(View.GONE);

        ((SettingActivity) getContext()).systemController.setNamesToSpinnerById(systemName);

        createRecycler(phoneNumberSmsModel.SmsNumber);
        recyclerView.setVisibility(View.GONE);
    }

    private void createRecycler(List<String> list) {
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        phoneNumberAdapter = new PhoneNumberAdapter(getActivity(), list);
        recyclerView.setAdapter(phoneNumberAdapter);
    }


    public void splitDataSms(String sms) {
        Log.i("TAG_", " => " + sms);
        SystemModel systemModel = ((SettingActivity) getContext()).systemController.getModelByIndex(indexSystem);

        String[] SmsArray = sms.split(",");

        for (int i = 0; i < SmsArray.length; i++) {

            String[] data = SmsArray[i].split(":");
            int indexValue = Integer.parseInt(data[0].replace("M", ""));
            String value = data[1];

            if (indexValue < 11) {

                if (value.length() > 0) {
                    phoneNumberSmsModel.SmsNumber.add(value);
                } else {
                    phoneNumberSmsModel.SmsNumber.add(" ");
                }


            } else if (indexValue > 10 && indexValue < 21) {
                if (!systemModel.Model.equals(getContext().getResources().getString(R.string.GSA_209))) {
                    phoneNumberSmsModel.TelephoneNumber.add(SmsArray[i].split(":")[1]);
                } else {
                    phoneNumberSmsModel.SimCardNumber.add(SmsArray[i].split(":")[1]);
                }


            } else if (indexValue > 20) {
                phoneNumberSmsModel.SimCardNumber.add(SmsArray[i].split(":")[1]);
            }
        }

        setOption();

    }

    private void setOption() {
        if (prifixCode == 0) {

            createRecycler(phoneNumberSmsModel.SmsNumber);

        } else if (prifixCode == 1) {
            createRecycler(phoneNumberSmsModel.TelephoneNumber);

        } else if (prifixCode == 2) {
            createRecycler(phoneNumberSmsModel.SimCardNumber);
        }
    }


}