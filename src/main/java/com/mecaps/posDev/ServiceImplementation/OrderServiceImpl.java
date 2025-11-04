package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Enums.OrderStatus;
import com.mecaps.posDev.Enums.PaymentMode;
import com.mecaps.posDev.Repository.CustomerRepository;
import com.mecaps.posDev.Repository.OrderItemRepository;
import com.mecaps.posDev.Repository.OrderRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {
private final OrderRepository orderRepository;
private final ProductRepository productRepository;
private final CustomerRepository customerRepository;
private final OrderItemRepository orderItemRepository;

public String createOrder(OrderRequest orderRequest) {
    Customer customer = customerRepository.findByPhoneNumber(orderRequest.getUser_phone_number()).
            orElseGet(()-> {
                Customer newCustomer = new Customer();
                newCustomer.setPhoneNumber(orderRequest.getUser_phone_number());
                return newCustomer;
            });

    Order order = new Order();
    order.setCustomer(customer);
    order.setUser_phone_number(orderRequest.getUser_phone_number());
    order.setPayment_mode(orderRequest.getPaymentMode());
    order.setOrder_status(OrderStatus.PENDING);
    order.setPayment_mode(PaymentMode.PENDING);
    order.setCash_amount(orderRequest.getCash_amount());
    order.setOnline_amount(orderRequest.getOnline_amount());
    order.setTax(orderRequest.getTax());
    Order saveOrder = orderRepository.save(order);
    double total =
    return "order created successfully";

}
}
