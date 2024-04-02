package com.example.fundmanager_renewal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class gain_model {
    @SerializedName("gain_index")
    @Expose
    private long gain_index;
    @SerializedName("user_index_g")
    @Expose
    private long user_index_g;
    @SerializedName("gain")
    @Expose
    private long gain;
    @SerializedName("principal")
    @Expose
    private long principal;
    @SerializedName("gain_time")
    @Expose
    private String gain_time;

    public long getGain_index() {
        return gain_index;
    }

    public void setGain_index(long gain_index) {
        this.gain_index = gain_index;
    }

    public long getUser_index_g() {
        return user_index_g;
    }

    public void setUser_index_g(long user_index_g) {
        this.user_index_g = user_index_g;
    }

    public long getGain() {
        return gain;
    }

    public void setGain(long gain) {
        this.gain = gain;
    }

    public long getPrincipal() {
        return principal;
    }

    public void setPrincipal(long principal) {
        this.principal = principal;
    }

    public String getGain_time() {
        return gain_time;
    }

    public void setGain_time(String gain_time) {
        this.gain_time = gain_time;
    }
}
