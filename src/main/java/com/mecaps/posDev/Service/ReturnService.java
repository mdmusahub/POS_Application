package com.mecaps.posDev.Service;

import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ReturnOrderResponse;

import java.util.List;

public interface ReturnService {
    ReturnOrderResponse createReturnOrder(ReturnOrderRequest req);

    String deleteReturnOrder(Long id);

    ReturnOrderResponse getById(Long id);

    List<ReturnOrderResponse> getReturnOrder();
}
