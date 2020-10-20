package com.siamin.fivestart.fragments.fragmentChargeAndBalance;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.fragments.ChargeAndBalanceFragment;
import com.siamin.fivestart.models.SystemModel;

public class ChargeFragment extends Fragment {

    Spinner spinner;
    EditText editCode;
    CardView sendCode;
    ImageView hamahaval, irancell, rightell;
    String[] typeCodec = {"*140*#", "*141*", "*141*"};
    int index = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        spinner = ChargeAndBalanceFragment.spinner;

        hamahaval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = 0;
                setItem(hamahaval);
            }
        });

        irancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = 1;
                setItem(irancell);
            }
        });

        rightell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = 2;
                setItem(rightell);
            }
        });


        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChargeCode(editCode.getText().toString(), index);
            }
        });

    }

    private void setItem(ImageView selected){


        irancell.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unselected));
        hamahaval.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unselected));
        rightell.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unselected));

        selected.setBackground(getContext().getResources().getDrawable(R.drawable.button_border_toggle));


    }

    void sendChargeCode(String Code, int type) {
        int index = spinner.getSelectedItemPosition();
        if (index > 0) {
            if (!Code.isEmpty()) {
                String messageCode = typeCodec[type] + Code + "#";
                SystemModel model = ((SettingActivity) getContext()).systemController.getModelByIndex(index - 1);

                messageCode = "G" + model.pinCode + messageCode;
                ((SettingActivity) getContext()).sendMessage(messageCode, index);
            } else {
                ((SettingActivity) getContext()).message.ErrorMessage(getContext().getString(R.string.pleaseEnterCode));
            }
        } else {
            ((SettingActivity) getContext()).message.ErrorMessage(getContext().getString(R.string.pleaseSelectDevice));
        }

    }

    void initView() {

        hamahaval = getView().findViewById(R.id.fragmentChargeHamrahaval);
        irancell = getView().findViewById(R.id.fragmentChargeIrancell);
        rightell = getView().findViewById(R.id.fragmentChargeRightell);

        editCode = getView().findViewById(R.id.fragmentChargeCode);
        sendCode = getView().findViewById(R.id.fragmentChargeSend);


    }
}