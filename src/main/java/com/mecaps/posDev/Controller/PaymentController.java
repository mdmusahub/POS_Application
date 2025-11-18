package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.PaymentRequest;
import com.mecaps.posDev.Request.VerifyPaymentRequest;
import com.mecaps.posDev.Service.PaymentService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;


    @PostMapping("/createOrder")

    public String createOrder(@RequestBody PaymentRequest paymentRequest) throws Exception{
        JSONObject order = paymentService.createOrder(paymentRequest.getAmount());
        return order.toString();
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody VerifyPaymentRequest verifyPaymentRequest) throws Exception {

        boolean isValid = paymentService.verifyPayment(
                verifyPaymentRequest.getOrderId(),
                verifyPaymentRequest.getPaymentId(),
                verifyPaymentRequest.getSignature()
        );

        return isValid ? "Payment Verified Successfully"
                : "Invalid Payment Signature";
    }

}
