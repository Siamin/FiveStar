package com.siamin.fivestart.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextMessageModel {

    @SerializedName("Title")
    @Expose
    public String Title;

    @SerializedName("Body")
    @Expose
    public String Body;


}
