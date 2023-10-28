package com.foodapp.service;

import com.foodapp.model.TxnDetails;
import com.foodapp.model.TxnResponse;

import java.sql.SQLException;
import java.util.UUID;

public class PaymentService {
    private PaymentDataAdapter paymentDataAdapter;

    public PaymentService(PaymentDataAdapter paymentDataAdapter) {
        this.paymentDataAdapter = paymentDataAdapter;
    }

    public TxnResponse postTxn(TxnDetails txnDetails) throws SQLException {
        String creditAcc = paymentDataAdapter.getCreditAcc(txnDetails.getRestaurantId());
        String txnId = UUID.randomUUID().toString();
        boolean saved = paymentDataAdapter.saveTxn(txnDetails.getCardNo(), creditAcc, txnDetails.getAmount(), txnId, txnDetails.getOrderId());
        if (!saved) {
            throw new RuntimeException("Unable to save transaction");
        }
        TxnResponse txnResponse = new TxnResponse();
        txnResponse.setTxnId(txnId);
        return txnResponse;
    }
}
