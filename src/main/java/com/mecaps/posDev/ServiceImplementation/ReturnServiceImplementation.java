package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import com.mecaps.posDev.Exception.ResourceNotFoundException;
import com.mecaps.posDev.Repository.ReturnOrderItemRepository;
import com.mecaps.posDev.Repository.ReturnOrderRepository;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ReturnOrderItemResponse;
import com.mecaps.posDev.Response.ReturnOrderResponse;
import com.mecaps.posDev.Service.ReturnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnServiceImplementation implements ReturnService {

    private final ReturnOrderItemRepository returnOrderItemRepository;
    private final ReturnOrderRepository returnOrderRepository;

    public ReturnServiceImplementation(ReturnOrderItemRepository returnOrderItemRepository, ReturnOrderRepository returnOrderRepository) {
        this.returnOrderItemRepository = returnOrderItemRepository;
        this.returnOrderRepository = returnOrderRepository;
    }


    @Override
    public ReturnOrderResponse createReturnOrder(ReturnOrderRequest req) {
        return null;
    }

    @Override
    public String deleteReturnOrder(Long id) {
        ReturnOrder returnOrder = returnOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("This Product Id is not found " + id));
        Order order = returnOrder.getOrder();
        List<ReturnOrderItem> returnOrderItem = returnOrderItemRepository.findAllReturnOrderItemsByOrder(order);
        returnOrderItemRepository.deleteAll(returnOrderItem);
        returnOrderRepository.delete(returnOrder);
        return "Deleted successfully";
    }

    public ReturnOrderResponse getById(Long id) {
       ReturnOrder returnOrder = returnOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Return order id not found"));
       List<ReturnOrderItem> returnOrderItems =  returnOrderItemRepository.findAllReturnOrderItemsByOrder(returnOrder.getOrder());

        return null;
    }

    public List<ReturnOrderResponse> getReturnOrder() {
       List<ReturnOrder> returnOrders = returnOrderRepository.findAll();
        return returnOrders.stream().map(returnOrder -> {
            List<ReturnOrderItem> returnOrderItems = returnOrderItemRepository.findAllReturnOrderItemsByOrder(returnOrder.getOrder());
       return new ReturnOrderResponse(returnOrder, returnOrderItems);
        }).toList();
    }
}




