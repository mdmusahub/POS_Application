package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.ReturnOrderItemRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Response.ReturnOrderItemResponse;

import java.util.List;


public interface ReturnOrderItemService {
    ReturnOrderItemResponse createReturnOrderItem(ReturnOrderItemRequest req);

    String deleteReturnOrderItem(Long id);

    ReturnOrderItemResponse updateReturnOrderItem(Long id, ReturnOrderItemRequest req);

    List<ReturnOrderItemResponse> getReturnOrderItem();
}
