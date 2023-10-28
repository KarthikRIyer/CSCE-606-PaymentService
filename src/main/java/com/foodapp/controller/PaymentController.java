package com.foodapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.foodapp.framework.annotation.POST;
import com.foodapp.framework.annotation.RequestBody;
import com.foodapp.framework.controller.Controller;
import com.foodapp.framework.util.HttpResponse;
import com.foodapp.framework.util.JsonUtil;
import com.foodapp.model.TxnDetails;
import com.foodapp.model.TxnResponse;
import com.foodapp.service.PaymentService;

import java.sql.SQLException;

public class PaymentController implements Controller {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST(path = "/postTxn")
    public HttpResponse postTxn(@RequestBody String txnDetailsStr) throws JsonProcessingException, SQLException {
        TxnDetails txnDetails = JsonUtil.fromJson(txnDetailsStr, TxnDetails.class);
        TxnResponse txnResponse = paymentService.postTxn(txnDetails);
        return new HttpResponse(JsonUtil.toJson(txnResponse), 200);
    }

}
