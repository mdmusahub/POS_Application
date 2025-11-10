package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Request.ReturnOrderItemRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Response.ReturnOrderItemResponse;
import com.mecaps.posDev.Service.ProductService;
import com.mecaps.posDev.Service.ReturnOrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/returnOrderItem")
public class ReturnOrderItemController {

            private final ReturnOrderItemService returnOrderItemService;

    public ReturnOrderItemController(ReturnOrderItemService returnOrderItemService) {
        this.returnOrderItemService = returnOrderItemService;
    }


        @PostMapping("/createReturnOrderItem")
        public ReturnOrderItemResponse createReturnOrderItem(@RequestBody ReturnOrderItemRequest req) {
            return returnOrderItemService.createReturnOrderItem(req);
        }

        @DeleteMapping("/deleteReturnOrderItem/{id}")
        public String deleteReturnOrderItem(@PathVariable Long id){
            return returnOrderItemService.deleteReturnOrderItem(id);
        }

        @PutMapping("/updateReturnOrderItem/{id}")
        public ReturnOrderItemResponse updateReturnOrderItem(@PathVariable Long id, @RequestBody ReturnOrderItemRequest req) {
            return returnOrderItemService.updateReturnOrderItem( id, req);
        }

        @GetMapping("/getReturnOrderItem")
        public List<ReturnOrderItemResponse> getReturnOrderItem(){
            return returnOrderItemService.getReturnOrderItem();
        }


    }


