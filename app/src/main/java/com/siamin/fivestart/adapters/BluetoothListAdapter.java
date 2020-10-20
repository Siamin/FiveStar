package com.siamin.fivestart.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.interfaces.BluetoothInterface;
import com.siamin.fivestart.models.BluetoothModel;

import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.cvh> {


    Context context;
    List<BluetoothModel> model;
    BluetoothInterface bluetoothInterface;
    Dialog dialog;


    public BluetoothListAdapter(Context _context, List<BluetoothModel> model, BluetoothInterface BI, Dialog dialog) {
        this.context = _context;
        this.model = model;
        this.bluetoothInterface = BI;
        this.dialog = dialog;

    }

    @Override
    public cvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bluetooth, parent, false);
        return new cvh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cvh holder, int position) {

        holder.bind(model.get(position));

    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class cvh extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, ip;


        public cvh(View iv) {
            super(iv);

            name = iv.findViewById(R.id.BluetoothName);
            ip = iv.findViewById(R.id.BluetoothIp);

            iv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            dialog.dismiss();
            bluetoothInterface.selectBluetooth(model.get(getAdapterPosition()));
        }

        void bind(BluetoothModel model){

            name.setText(model.getName());
            ip.setText(model.getIp());

        }


    }


}
