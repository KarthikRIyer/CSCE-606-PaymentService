package com.foodapp.service;

import com.foodapp.model.Ledger;
import com.foodapp.model.TxnRefData;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDataAdapter {
//    private Connection connection;

    private Datastore datastore;

    public PaymentDataAdapter(Datastore datastore) {
        this.datastore = datastore;
    }

    public String getCreditAcc(int restaurantId) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement("select * from TXN_REF_DATA where user_id = ?");
//        preparedStatement.setInt(1, restaurantId);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if (resultSet.next()) {
//            return resultSet.getString(2);
//        }
//        return null;
        String creditAcc = datastore.find(TxnRefData.class)
                .filter(Filters.eq("_id", restaurantId))
                .stream()
                .map(TxnRefData::getAccNo)
                .findFirst().orElse(null);
        return creditAcc;
    }

    public boolean saveTxn(String debitAcc, String creditAcc, double amount, String txnId, int orderId) throws SQLException {
//        PreparedStatement saveStatement = connection.prepareStatement("insert into LEDGER (debit_acc, credit_acc, amount, txn_id, order_id) values (?,?,?,?,?)");
//        saveStatement.setString(1, debitAcc);
//        saveStatement.setString(2, creditAcc);
//        saveStatement.setDouble(3, amount);
//        saveStatement.setString(4, txnId);
//        saveStatement.setInt(5, orderId);
//        int result = saveStatement.executeUpdate();
//        return result > 0;
        Ledger ledger = new Ledger();
        ledger.setAmount(amount);
        ledger.setTxnId(txnId);
        ledger.setOrderId(orderId);
        ledger.setDebitAcc(debitAcc);
        ledger.setCreditAcc(creditAcc);
        datastore.save(ledger);
        return true;
    }
}
