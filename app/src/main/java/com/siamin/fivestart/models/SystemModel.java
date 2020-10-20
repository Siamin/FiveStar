package com.siamin.fivestart.models;

public class SystemModel {
    public int id;
    public String systemName,phoneNumber,pinCode,Model,ArmCode,DisArmCode;


    public String getArmCode() {
        try {
            return ArmCode.isEmpty()?"5":ArmCode;
        }catch (Exception e){
           return "5";
        }

    }

    public String getDisArmCode() {
        try {
            return DisArmCode.isEmpty()?"6":DisArmCode;
        }catch (Exception e){
            return "6";
        }

    }
}
