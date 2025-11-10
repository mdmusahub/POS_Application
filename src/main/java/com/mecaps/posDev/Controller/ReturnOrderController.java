package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Request.ReturnOrderItemRequest;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Response.ReturnOrderItemResponse;
import com.mecaps.posDev.Response.ReturnOrderResponse;
import com.mecaps.posDev.Service.ReturnOrderItemService;
import com.mecaps.posDev.Service.ReturnOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/returnOrderItem")
public class ReturnOrderController{

    private final ReturnOrderService returnOrderService;

    public ReturnOrderController(ReturnOrderService returnOrderService) {
        this.returnOrderService = returnOrderService;
    }

    @PostMapping("/createReturnOrder")
    public ReturnOrderResponse createReturnOrder(@RequestBody ReturnOrderRequest req) {
        return returnOrderService.createReturnOrder(req);
    }

    @DeleteMapping("/deleteReturnOrder/{id}")
    public ReturnOrder deleteReturnOrder(@PathVariable Long id){
        return returnOrderService.deleteReturnOrder(id);
    }

    @PutMapping("/updateReturnOrder/{id}")
    public ReturnOrderResponse updateReturnOrder(@PathVariable Long id, @RequestBody ReturnOrderRequest req) {
        return returnOrderService.updateReturnOrder(id, req);
    }

    @GetMapping("/getReturnOrder")
    public List<ReturnOrderResponse> getReturnOrder(){
        return returnOrderService.getReturnOrder();
    }

}
