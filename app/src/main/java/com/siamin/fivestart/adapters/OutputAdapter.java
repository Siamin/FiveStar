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
import com.siamin.fivestart.activitys.OutputActivity;
import com.siamin.fivestart.activitys.ZoneActivity;
import com.siamin.fivestart.models.SystemModel;

import java.util.List;

public class OutputAdapter extends RecyclerView.Adapter<OutputAdapter.cvh> {


    private Context context;
    public int count=4;
    public SystemModel systemModel;
    private String TAG = "TAG_PhoneNumberAdapter";
    public List<Boolean> list;

    public OutputAdapter(Context context, int count, List<Boolean> list){
        this.context = context;
        this.count = count;
        this.list = list;
    }

    @Override
    public cvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_output, parent, false);
        return new cvh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final cvh holder, final int position) {

        final int index = position+1;

        holder.title.setText(context.getResources().getString(R.string.titleOutput)+String.valueOf(index));

        if (list.get(position))
            holder.Momentary.setText(context.getResources().getString(R.string.ON));
        else
            holder.Momentary.setText(context.getResources().getString(R.string.OFF));

        holder.Momentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Momentary.getText().equals(context.getResources().getString(R.string.ON))){
                    holder.Momentary.setText(context.getResources().getString(R.string.OFF));
                }else{
                    holder.Momentary.setText(context.getResources().getString(R.string.ON));
                }
                ((OutputActivity)context).sendSms(String.valueOf(index));
            }
        });

        holder.Permanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OutputActivity)context).sendSms(index+"L");
            }
        });
    }



    @Override
    public int getItemCount() {
        return count;
    }


    public class cvh extends RecyclerView.ViewHolder {

        TextView title;
        Button Momentary,Permanent;


        public cvh(View iv) {
            super(iv);
            title = iv.findViewById(R.id.adapterOutputTitle);
            Permanent = iv.findViewById(R.id.adapterOutputMomentary);
            Momentary = iv.findViewById(R.id.adapterOutputPermanent);

        }



    }

}