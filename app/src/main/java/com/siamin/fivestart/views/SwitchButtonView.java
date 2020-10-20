package com.siamin.fivestart.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.interfaces.SwitchInterface;

import java.util.ArrayList;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;


public class SwitchButtonView extends FrameLayout {

    ToggleSwitch toggleSwitch;
    TextView text;
    String BluetoothMessage,SmsMessage;

    public SwitchButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public SwitchButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SwitchButtonView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_switchbtn, this);

        toggleSwitch = findViewById(R.id.btnToggleSwitch);
        text = findViewById(R.id.textMessage);
        ArrayList<String> labels = new ArrayList<>();

        labels.add(this.getResources().getString(R.string.Message));
        labels.add(this.getResources().getString(R.string.Blutooth));

        toggleSwitch.setLabels(labels);
        SmsMessage  = this.getResources().getString(R.string.SmsMessage);
        BluetoothMessage  = this.getResources().getString(R.string.BluetoothMessage);

        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if (position==0){
                    ((SwitchInterface)getContext()).Sms();
                    text.setText(SmsMessage);
                }else{
                    ((SwitchInterface)getContext()).Bluetooth();
                    text.setText(BluetoothMessage);
                }
            }
        });



    }


    public void deselectedBluetooth(){
        toggleSwitch.setCheckedTogglePosition(0);
        text.setText(SmsMessage);
    }


}
