package com.mecaps.posDev.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentOrderResponse {

    private String razorpayOrderId;   // Razorpay generated order ID
    private Double amount;            // Total order amount
    private String currency;          // INR
}
