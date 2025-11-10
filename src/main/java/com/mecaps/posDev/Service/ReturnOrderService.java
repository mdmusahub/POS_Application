package com.mecaps.posDev.Service;

import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ReturnOrderResponse;

import java.util.List;

public interface ReturnOrderService {
    ReturnOrderResponse createReturnOrder(ReturnOrderRequest req);

    ReturnOrder deleteReturnOrder(Long id);

    ReturnOrderResponse updateReturnOrder(Long id, ReturnOrderRequest req);

    List<ReturnOrderResponse> getReturnOrder();
}
