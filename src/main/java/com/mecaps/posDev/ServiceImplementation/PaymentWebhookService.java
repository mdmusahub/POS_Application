package com.mecaps.posDev.ServiceImplementation;


import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Enums.OrderStatus;
import com.mecaps.posDev.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    private final OrderRepository orderRepository;
    private final RazorpaySignatureUtil signatureUtil;

    public boolean verifyWebhookSignature(String payload, String headerSignature) {
        try {
            String expectedSignature = signatureUtil.calculateHmacSHA256(payload, webhookSecret);
            return expectedSignature.equals(headerSignature);
        } catch (Exception e) {
            return false;
        }
    }

    public void processWebhook(String payload) {
        JSONObject json = new JSONObject(payload);

        String event = json.getString("event");

        if (event.equals("order.paid")) {

            String razorpayOrderId = json.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity")
                    .getString("order_id");

            String paymentId = json.getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity")
                    .getString("id");

            //  Find your order by razorpayOrderId
            Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            order.setOrder_status(OrderStatus.COMPLETED);

            orderRepository.save(order);
        }
    }
}

