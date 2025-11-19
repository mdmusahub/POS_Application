package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ReturnOrderResponse;
import com.mecaps.posDev.Service.ReturnService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/return")
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/create")
    public ReturnOrderResponse createReturnOrder(@RequestBody ReturnOrderRequest req) {
        return returnService.createReturnOrder(req);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteReturnOrder(@PathVariable Long id){
        return returnService.deleteReturnOrder(id);
    }

    @PutMapping("/getById/{id}")
    public ReturnOrderResponse updateReturnOrder(@PathVariable Long id) {
        return returnService.getById(id);
    }

    @GetMapping("/get")
    public List<ReturnOrderResponse> getReturnOrder(){
        return returnService.getReturnOrder();
    }

}
