package com.siamin.fivestart.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;

import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.models.SystemModel;

import java.util.List;


public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.cvh> {


    private Context context;
    private int prifixCode = 0, indexSystem = 0;
    private SystemModel systemModel;
    private String TAG = "TAG_PhoneNumberAdapter";
    private List<String> list;

    public PhoneNumberAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public cvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_phonenumber, parent, false);
        return new cvh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final cvh holder, final int position) {


        holder.index.setText(String.valueOf(position+1));

        try {
            holder.number.setText(list.get(position));
        }catch (Exception e){
            Log.i("TAG_","AdapterError=>"+e.toString());
        }


        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Number = holder.number.getText().toString();
                if (!Number.isEmpty()){
                    sendMessage(Number,position + 1);
                }else{
                    //Error number is empty
                    ((SettingActivity)context).message.ErrorMessage(context.getString(R.string.EnterNumberPhone));

                }

            }
        });
    }

    private void sendMessage(String number, int index) {
        try {
            Log.i(TAG,"indexSystem =>"+indexSystem);
            if (indexSystem >= 0){
                Log.i(TAG,"index =>"+index);
                Log.i(TAG,"number =>"+number);
                Log.i(TAG,"prifixCode =>"+prifixCode);
                int newIndex = (index < 10 ? index : 0);
                int newPrifixCode = (index > 9 ? prifixCode + 1 : prifixCode);
                String CodeMessage = "E" + systemModel.pinCode + newPrifixCode + newIndex  + number;
                Log.i(TAG,"step 1");
                ((SettingActivity) context).sendMessage(CodeMessage, indexSystem + 1);
            }else{
                //Error set Model
                ((SettingActivity)context).message.ErrorMessage(context.getString(R.string.selectSystem));
            }
        }catch (Exception e){
            Log.i(TAG,"Error => "+e.toString());
        }


    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class cvh extends RecyclerView.ViewHolder {

        TextView index;
        EditText number;
        CardView submit;


        public cvh(View iv) {
            super(iv);
            index = iv.findViewById(R.id.detilesPhoneNumberindex);
            number = iv.findViewById(R.id.detilesPhoneNumberNumber);
            submit = iv.findViewById(R.id.detilesPhoneNumberSubmit);

        }



    }


    public void setData(SystemModel model,int indexSystem,int prifixCode){
        Log.i(TAG,"model =>"+model.pinCode);
        Log.i(TAG,"indexSystem =>"+indexSystem);
        Log.i(TAG,"prifixCode =>"+prifixCode);
        this.indexSystem = indexSystem;
        this.systemModel = model;
        this.prifixCode = prifixCode;

    }
}