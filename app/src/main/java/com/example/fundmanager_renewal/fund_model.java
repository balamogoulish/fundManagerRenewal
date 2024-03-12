package com.example.fundmanager_renewal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class fund_model {
    @SerializedName("fund_index")
    @Expose
    private long fund_index;

    @SerializedName("fund_money")
    @Expose
    private long fund_money;

    @SerializedName("fund_gain")
    @Expose
    private long fund_gain;

    @SerializedName("fund_output")
    @Expose
    private long fund_output;

    @SerializedName("least")
    @Expose
    private long least;

    @SerializedName("fund_time")
    @Expose
    private String fund_time;

    public String getFund_time() {
        return fund_time;
    }

    public void setFund_time(String fund_time) {
        this.fund_time = fund_time;
    }

    public long getLeast() {
        return least;
    }

    public void setLeast(long least) {
        this.least = least;
    }

    public long getFund_output() {
        return fund_output;
    }

    public void setFund_output(long fund_output) {
        this.fund_output = fund_output;
    }

    public long getFund_gain() {
        return fund_gain;
    }

    public void setFund_gain(long fund_gain) {
        this.fund_gain = fund_gain;
    }

    public long getFund_money() {
        return fund_money;
    }

    public void setFund_money(long fund_money) {
        this.fund_money = fund_money;
    }

    public long getFund_index() {
        return fund_index;
    }

    public void setFund_index(long fund_index) {
        this.fund_index = fund_index;
    }
}
