package com.example.fundmanager_renewal.callbacks;

public interface sendEmailCallback {
    void sendEmailSuccess(String code);
    void sendEmailFail(String t);
}
