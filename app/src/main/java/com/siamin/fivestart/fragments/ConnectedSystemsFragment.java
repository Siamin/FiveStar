package com.siamin.fivestart.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.adapters.SystemAdapter;
import com.siamin.fivestart.models.SystemModel;

import java.util.List;

public class ConnectedSystemsFragment extends Fragment {

    private List<SystemModel> models;
    private SystemAdapter adapter;

    public ConnectedSystemsFragment(List<SystemModel> models){
        this.models = models;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connectedsystems, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.listSystems);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new SystemAdapter(getContext(), models);
        recyclerView.setAdapter(adapter);

    }



    public void AddItem(SystemModel model) {
        Log.i("TAG_","AddItem");
        models.add(model);
        adapter.notifyDataSetChanged();
    }


    public void RemoveItem(int index){
        models.remove(index);
        adapter.notifyDataSetChanged();

    }


    public void EditItem(SystemModel model,int index){

        models.get(index).systemName= model.systemName;
        models.get(index).phoneNumber= model.phoneNumber;
        models.get(index).pinCode= model.pinCode;
        models.get(index).Model = model.Model;
        models.get(index).ArmCode = model.ArmCode;
        models.get(index).DisArmCode = model.DisArmCode;

        adapter.notifyDataSetChanged();

    }
}