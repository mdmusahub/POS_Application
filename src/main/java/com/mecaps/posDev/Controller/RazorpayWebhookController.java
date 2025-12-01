package com.mecaps.posDev.Controller;

import com.mecaps.posDev.ServiceImplementation.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook/razorpay")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final PaymentWebhookService paymentWebhookService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        if (!paymentWebhookService.verifyWebhookSignature(payload, signature)) {
            return ResponseEntity.status(400).body("Invalid signature");
        }

        paymentWebhookService.processWebhook(payload);

        return ResponseEntity.ok("Webhook received");
    }
}
