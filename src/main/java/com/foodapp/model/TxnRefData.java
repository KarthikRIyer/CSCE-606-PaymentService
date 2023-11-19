package com.foodapp.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("txn_ref_data")
public class TxnRefData {
    @Id
    private Integer userId;
    private String accNo;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
}
