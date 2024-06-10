package com.example.fundmanager_renewal.callbacks;

import com.example.fundmanager_renewal.model.transaction_model;

public interface TotalAmountCallback {
    void onTotalAmountReceived(transaction_model tran_result);
}
