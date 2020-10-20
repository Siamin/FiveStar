package com.siamin.fivestart.models;

public class ErrorModel {
    public String Message;
    public boolean Status;

    public ErrorModel(String message,boolean status){
        this.Message = message;
        this.Status = status;
    }
}
