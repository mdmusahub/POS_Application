package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.ReturnOrderItem;
import com.mecaps.posDev.Exception.ProductNotFoundExpection;
import com.mecaps.posDev.Repository.ReturnOrderItemRepository;
import com.mecaps.posDev.Request.ReturnOrderItemRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Response.ReturnOrderItemResponse;
import com.mecaps.posDev.Service.ReturnOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnOrderItemServiceImplementation implements ReturnOrderItemService {

    private final ReturnOrderItemRepository returnOrderItemRepository;

    public ReturnOrderItemServiceImplementation(ReturnOrderItemRepository returnOrderItemRepository) {
        this.returnOrderItemRepository = returnOrderItemRepository;
    }

    public ReturnOrderItemResponse createReturnOrderItem(ReturnOrderItemRequest req) {
        return null;
    }


    public String deleteReturnOrderItem(Long id) {
        ReturnOrderItem returnOrderItem = returnOrderItemRepository.findById(id).orElseThrow(()->new ProductNotFoundExpection("This Product Id is not found " + id));
        returnOrderItemRepository.delete(returnOrderItem);
        return "Return Order Item deleted";
    }


    public ReturnOrderItemResponse updateReturnOrderItem(Long id, ReturnOrderItemRequest req) {
        return null;
    }


    public List<ReturnOrderItemResponse> getReturnOrderItem() {

            List<ReturnOrderItem> getReturnOrderItem = returnOrderItemRepository.findAll();
            return getReturnOrderItem.stream().map(ReturnOrderItemResponse::new).toList();
        }
}

