package com.example.fundmanager_renewal.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class transaction_model {
    @SerializedName("transaction_index")
    @Expose
    private long transaction_index;
    @SerializedName("user_index_t")
    @Expose
    private long user_index_t;
    @SerializedName("deposit")
    @Expose
    private long deposit;
    @SerializedName("withdrawal")
    @Expose
    private long withdrawal;
    @SerializedName("total_amount")
    @Expose
    private long total_amount;
    @SerializedName("transaction_time")
    @Expose
    private String transaction_time;

    public long getTransaction_index() {
        return transaction_index;
    }

    public void setTransaction_index(long transaction_index) {
        this.transaction_index = transaction_index;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }

    public long getUser_index_t() {
        return user_index_t;
    }

    public void setUser_index_t(long user_index_t) {
        this.user_index_t = user_index_t;
    }

    public long getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(long withdrawal) {
        this.withdrawal = withdrawal;
    }

    public long getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }
}
