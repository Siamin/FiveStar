package com.siamin.fivestart.models;

public class BluetoothModel {
    private String Name,Ip;

    public BluetoothModel(String ip,String name){
        this.Ip = ip;
        this.Name = name;
    }
    public void setIp(String ip) {
        Ip = ip;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIp() {
        return Ip;
    }

    public String getName() {
        return Name;
    }
}
