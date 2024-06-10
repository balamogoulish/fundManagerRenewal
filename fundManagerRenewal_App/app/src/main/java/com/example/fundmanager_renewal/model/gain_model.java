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
    private double gain;
    @SerializedName("principal")
    @Expose
    private long principal;
    @SerializedName("gain_time")
    @Expose
    private String gain_time;
    public double getGain() {
        double gainRound = Math.round(gain*1000)/1000.0;
        return gainRound;
    }

    public void setGain(double gain) {
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
