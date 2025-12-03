package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Enums.PaymentMode;
import com.mecaps.posDev.Exception.OrderNotFound;
import com.mecaps.posDev.Repository.OrderRepository;
import com.mecaps.posDev.Response.PaymentOrderResponse;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;
    private final RazorpaySignatureUtil signatureUtil;

    @Value("${razorpay.key.secret}")
    private String secretKey;

    /**
     * Creates Razorpay order ONLY when paymentMode is UPI or MIXED.
     * Amount used = online_amount (STRING → DOUBLE)
     */
    public PaymentOrderResponse createRazorpayOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound("Order not found: " + orderId));

        if (order.getPayment_mode() == PaymentMode.CASH) {
            throw new RuntimeException("Cash orders do not require Razorpay payment.");
        }

        // Convert amount from String → double
        double onlineAmount = Double.parseDouble(order.getOnline_amount());

        if (onlineAmount <= 0) {
            throw new RuntimeException("Online amount must be greater than 0 for Razorpay order.");
        }
            try {
                JSONObject request = new JSONObject();
                request.put("amount", (int) (onlineAmount * 100)); // Razorpay requires paise
                request.put("currency", "INR");
                request.put("receipt", "order_rcpt_" + orderId);

                com.razorpay.Order rpOrder = razorpayClient.orders.create(request);

                order.setRazorpayOrderId(rpOrder.get("id"));
                orderRepository.save(order);

                return new PaymentOrderResponse(
                        rpOrder.get("id"),
                        onlineAmount,
                        "INR"
                );

            } catch (Exception e) {
                throw new RuntimeException("Error creating Razorpay order: " + e.getMessage());
            }
        }

    /**
     * VERIFY RAZORPAY PAYMENT SIGNATURE
     */
    public boolean verifySignature(String razorpayOrderId,
                                   String razorpayPaymentId,
                                   String razorpaySignature) {

        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            String expectedSignature = signatureUtil.calculateHmacSHA256(payload, secretKey);

            return expectedSignature.equals(razorpaySignature);

        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed: " + e.getMessage());
        }
    }
}


