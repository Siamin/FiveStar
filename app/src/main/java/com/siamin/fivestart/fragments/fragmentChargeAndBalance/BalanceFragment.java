package com.siamin.fivestart.fragments.fragmentChargeAndBalance;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.fragments.ChargeAndBalanceFragment;
import com.siamin.fivestart.models.SystemModel;

public class BalanceFragment extends Fragment {

    String[] typeCodec={"*140*11#","*141*1#","*140#"};
    Spinner spinner;
    CardView send;
    ImageView hamahaval,irancell,rightell;
    String SendData="";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        spinner = ChargeAndBalanceFragment.spinner;

        hamahaval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData =(typeCodec[0]);
                setItem(hamahaval);
            }
        });

        irancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData =(typeCodec[1]);
                setItem(irancell);
            }
        });

        rightell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData = (typeCodec[2]);
                setItem(rightell);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBalanceCode(SendData);
            }
        });

    }

    private void setItem(ImageView selected){


        hamahaval.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unselected));
        irancell.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unselected));
        rightell.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unselected));

        selected.setBackground(getContext().getResources().getDrawable(R.drawable.button_border_toggle));


    }

    void sendBalanceCode(String Code){
        int index = spinner.getSelectedItemPosition();
        if(index>0){
            if (!Code.isEmpty()){
                SystemModel model = ((SettingActivity)getContext()).systemController.getModelByIndex(index-1);
                Code = "G"+model.pinCode+Code;
                ((SettingActivity)getContext()).sendMessage(Code,index);
            }else{
                ((SettingActivity)getContext()).message.Snackbar(getContext().getString(R.string.pleaseEnterCode),null,R.color.colorRed,R.drawable.messagestyle_error);
            }
        }else{
            ((SettingActivity)getContext()).message.Snackbar(getContext().getString(R.string.pleaseSelectDevice),null,R.color.colorRed,R.drawable.messagestyle_error);
        }

    }

    void initView(){

        hamahaval = getView().findViewById(R.id.balanceHamrahaval);
        irancell = getView().findViewById(R.id.balanceIrancell);
        rightell = getView().findViewById(R.id.balanceRightell);

        send = getView().findViewById(R.id.fragmentBalanceSend);


    }
}