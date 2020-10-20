package com.siamin.fivestart.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.models.TextMessageModel;

import java.lang.reflect.Type;
import java.util.List;

public class ConvertJsonHelper {

    private Context context;
    private Gson gson = new Gson();

    public ConvertJsonHelper(Context context) {
        this.context = context;
    }

    public List<SystemModel> convertStringToSystemModel(String Json) {
        Type listType = new TypeToken<List<SystemModel>>() {
        }.getType();
        List<SystemModel> Return = gson.fromJson(Json.toString(), listType);
        return Return;
    }

    public String convertSystemModelToStrin(List<SystemModel> List){
        return  gson.toJson(List );
    }


    public List<TextMessageModel> convertStringToTextMessageModel(String Json) {
        Type listType = new TypeToken<List<TextMessageModel>>() {
        }.getType();
        List<TextMessageModel> Return = gson.fromJson(Json.toString(), listType);
        return Return;
    }
}
