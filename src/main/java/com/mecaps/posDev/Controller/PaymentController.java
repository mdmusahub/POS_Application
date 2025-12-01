package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Response.PaymentOrderResponse;
import com.mecaps.posDev.ServiceImplementation.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @Value("${razorpay.key.id}")
    private String razorpayKey;   // PUBLIC KEY (not secret)

    /**
     * STEP 1: OPEN PAYMENT PAGE → CALLS Razorpay order creation internally
     */
    @PostMapping("/create/{orderId}")
    public ResponseEntity<PaymentOrderResponse> createRazorpayOrder(@PathVariable Long orderId) {
        PaymentOrderResponse response = paymentService.createRazorpayOrder(orderId);
        return ResponseEntity.ok(response);
    }

    /**
     * STEP 2: VERIFY SIGNATURE AFTER PAYMENT POPUP SUCCESS
     */
    @PostMapping("/verify")
    @ResponseBody
    public Map<String, Object> verifyPayment(@RequestBody Map<String, String> payload) {

        String razorpayOrderId = payload.get("razorpayOrderId");
        String razorpayPaymentId = payload.get("razorpayPaymentId");
        String razorpaySignature = payload.get("razorpaySignature");

        boolean isValid = paymentService.verifySignature(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );

        Map<String, Object> response = new HashMap<>();

        if (isValid) {
            response.put("status", true);
            response.put("message", "Payment Verified Successfully!");
        } else {
            response.put("status", false);
            response.put("message", "Payment Verification Failed!");
        }
        return response;
    }

}
