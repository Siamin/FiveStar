package com.siamin.fivestart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.fragments.fragmentChargeAndBalance.BalanceFragment;
import com.siamin.fivestart.fragments.fragmentChargeAndBalance.ChargeFragment;
import com.siamin.fivestart.helpers.TabAdapter;



public class ChargeAndBalanceFragment extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chargeandbalance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((SettingActivity)getContext()).systemController.setDefualtSystem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void initView(){
        viewPager =  getView().findViewById(R.id.fragmentChargeViewPager);
        tabLayout =  getView().findViewById(R.id.fragmentChargeTabLayout);
        spinner   =  getView().findViewById(R.id.chargeAndBalanceSystemName);
        ((SettingActivity)getContext()).systemController.setNamesToSpinnerById(spinner);

        setViewPager();
    }

    private void setViewPager() {
        adapter = new TabAdapter(getChildFragmentManager());

        adapter.addFragment(new ChargeFragment(), getString(R.string.Charge));
        adapter.addFragment(new BalanceFragment(), getString(R.string.Balance));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}