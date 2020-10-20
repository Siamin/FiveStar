package com.siamin.fivestart.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.interfaces.SettingInterface;
import com.siamin.fivestart.models.SystemModel;

import java.util.List;


public class SystemAdapter extends RecyclerView.Adapter<SystemAdapter.cvh> {


    Context context;
    List<SystemModel> model;


    public SystemAdapter(Context context, List<SystemModel> model) {
        this.context = context;
        this.model = model;

    }

    @Override
    public cvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_system, parent, false);
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

        TextView name, phone,pincode,edit,delete,systemModel,Arm,DisArm;


        public cvh(View iv) {
            super(iv);

            name = iv.findViewById(R.id.adapterSystemName);
            phone = iv.findViewById(R.id.adapterSystemPhone);
            pincode = iv.findViewById(R.id.adapterSystemPincode);
            edit = iv.findViewById(R.id.adapterSystemEdit);
            delete = iv.findViewById(R.id.adapterSystemDelete);
            systemModel = iv.findViewById(R.id.adapterSystemModel);
            Arm = iv.findViewById(R.id.adapterSystemArm);
            DisArm = iv.findViewById(R.id.adapterSystemDisArm);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SettingInterface)context).showDialogEditSystem(getAdapterPosition());

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SettingInterface)context).showDialogRemoveSystem(getAdapterPosition());
                }
            });

            iv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }

        void bind(SystemModel model){

            name.setText(model.systemName);
            phone.setText(model.phoneNumber);
            systemModel.setText(model.Model);
            Arm.setText(model.ArmCode);
            DisArm.setText(model.DisArmCode);

//            pincode.setText(model.pinCode);


        }


    }


}