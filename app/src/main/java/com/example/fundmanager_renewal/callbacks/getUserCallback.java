package com.example.fundmanager_renewal.callbacks;

import com.example.fundmanager_renewal.model.user_model;

public interface getUserCallback {
    void getUserSuccess(user_model result);
    void getUserFail(String t);
}
