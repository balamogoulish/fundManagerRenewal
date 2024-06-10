package com.example.fundmanager_renewal.callbacks;

import com.example.fundmanager_renewal.model.transaction_model;

public interface getTranCallback {
    void getTranSuccess(transaction_model result);
    void getTranFail(String t);
}
