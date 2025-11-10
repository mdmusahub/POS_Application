package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import com.mecaps.posDev.Exception.ProductNotFoundExpection;
import com.mecaps.posDev.Repository.ReturnOrderRepository;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ReturnOrderResponse;
import com.mecaps.posDev.Service.ReturnOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnOrderServiceImplementation implements ReturnOrderService {

    private final ReturnOrderRepository returnOrderRepository;

    public ReturnOrderServiceImplementation(ReturnOrderRepository returnOrderRepository) {
        this.returnOrderRepository = returnOrderRepository;
    }

    public ReturnOrderResponse createReturnOrder(ReturnOrderRequest req) {
        return null;
    }


    public ReturnOrder deleteReturnOrder(Long id) {
        ReturnOrder returnOrderItem = returnOrderRepository.findById(id).orElseThrow(() -> new ProductNotFoundExpection("This Product Id is not found " + id));
        returnOrderRepository.delete(returnOrderItem);
        return returnOrderItem;
    }


        public ReturnOrderResponse updateReturnOrder (Long id, ReturnOrderRequest req){
            return null;
        }


        public List<ReturnOrderResponse> getReturnOrder () {

            List<ReturnOrder> getReturnOrder = returnOrderRepository.findAll();
            return getReturnOrder.stream().map(ReturnOrderResponse::new).toList();
        }
}

