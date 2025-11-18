package com.mecaps.posDev.ServiceImplementation;


import com.mecaps.posDev.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImplementation implements PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;


    @Value("${razorpay.key.secret}")
    private String secretKey;


    @Override
    public JSONObject createOrder(int amount) throws Exception{
JSONObject orderRequest = new JSONObject();
orderRequest.put("amount",amount * 100);
orderRequest.put("currency","INR");
orderRequest.put("receipt", "txn_" + System.currentTimeMillis());


Order order = razorpayClient.orders.create(orderRequest);
return order.toJson();

    }
    @Override
    public boolean verifyPayment(String orderId, String paymentId, String signature) throws Exception {
        String payload = orderId + "|" + paymentId;
        return Utils.verifySignature(payload, signature, secretKey);

    }
}
