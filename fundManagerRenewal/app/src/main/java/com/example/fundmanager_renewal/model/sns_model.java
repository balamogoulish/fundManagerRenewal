package com.example.fundmanager_renewal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class sns_model {
    @SerializedName("id")
    @Expose
    private long user_index;

    @SerializedName("sns_id")
    @Expose
    private String sns_id;

    @SerializedName("sns_type")
    @Expose
    private String sns_type;

    public sns_model(long user_index, String sns_id, String sns_type){
        this.user_index = user_index;
        this.sns_id = sns_id;
        this.sns_type = sns_type;
    }

    public long getUser_index() {
        return user_index;
    }

    public String getSns_id() {
        return sns_id;
    }

    public String getSns_type() {
        return sns_type;
    }
}
