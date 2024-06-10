package com.example.fundmanager_renewal.callbacks;

public interface postTranCallback {
    void postTranSuccess(long total, String input);
    void postTranFail(String t);
}
