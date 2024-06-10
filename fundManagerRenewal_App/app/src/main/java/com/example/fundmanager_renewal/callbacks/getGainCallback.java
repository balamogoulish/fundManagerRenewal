package com.example.fundmanager_renewal.callbacks;

import com.example.fundmanager_renewal.model.gain_model;

public interface getGainCallback {
    void getGainSuccess(gain_model result);
    void getGainFail(String t);
}
