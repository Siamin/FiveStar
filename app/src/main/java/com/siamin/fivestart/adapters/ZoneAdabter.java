package com.siamin.fivestart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.ZoneActivity;
import com.siamin.fivestart.models.SystemModel;

public class ZoneAdabter extends RecyclerView.Adapter<ZoneAdabter.cvh> {


    private Context context;
    public int count=4;
    public SystemModel systemModel;
    private String TAG = "TAG_PhoneNumberAdapter";


    public ZoneAdabter(Context context,int count){
        this.context = context;
        this.count = count;
    }

    @Override
    public cvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_zone, parent, false);
        return new cvh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final cvh holder, final int position) {

        final int index = position+1;

        holder.title.setText(holder.title.getText()+String.valueOf(index));

        holder.Active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZoneActivity)context).sendSms("Z"+index+"A");
            }
        });

        holder.Deactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZoneActivity)context).sendSms("Z"+index+"D");
            }
        });
    }



    @Override
    public int getItemCount() {
        return count;
    }


    public class cvh extends RecyclerView.ViewHolder {

        TextView title;
        Button Active,Deactive;


        public cvh(View iv) {
            super(iv);
            title = iv.findViewById(R.id.adapterZoneTitle);
            Active = iv.findViewById(R.id.adapterZoneActive);
            Deactive = iv.findViewById(R.id.adapterZoneDeactive);

        }



    }

}
