package com.siamin.fivestart.controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.siamin.fivestart.R;
import com.siamin.fivestart.helpers.DialogHelper;
import com.siamin.fivestart.interfaces.BluetoothInterface;
import com.siamin.fivestart.models.BluetoothModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothController {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    private Context context;
    private String TAG = "TAG_BluetoothController";

    public BluetoothController(Context context) {
        this.context = context;
    }


    public void bluetoothConnection(final DialogHelper dH, final BluetoothInterface BI) {
        dH.dialogConnectionBluetooth(getBluetoothNames(), BI);
    }

    public boolean bluetoothIsEnabled() {
        return mBluetoothAdapter.isEnabled();
    }


    private List<BluetoothModel> getBluetoothNames() {
        List<BluetoothModel> list = new ArrayList<BluetoothModel>();
        for (BluetoothDevice bt : pairedDevices) {
            BluetoothModel bm = new BluetoothModel(bt.getAddress(), bt.getName());
            list.add(bm);
        }

        if (list.size() == 0)
            list.add(new BluetoothModel("", context.getString(R.string.EmptyList)));

        return list;
    }


}
