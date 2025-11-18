package com.mecaps.posDev.Service;

import org.json.JSONObject;

public interface PaymentService {
    JSONObject createOrder(int amount) throws Exception;
    boolean verifyPayment(String orderId, String paymentId, String signature) throws Exception;
}
