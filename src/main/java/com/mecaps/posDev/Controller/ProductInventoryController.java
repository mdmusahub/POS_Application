package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.ProductInventoryRequest;
import com.mecaps.posDev.Response.ProductInventoryResponse;
import com.mecaps.posDev.Service.ProductInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class ProductInventoryController {

    private final ProductInventoryService productInventoryService;


    public ProductInventoryController(ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    @PostMapping("/createInventory")
    public ProductInventoryResponse createProductInventory(@RequestBody ProductInventoryRequest request){
        return productInventoryService.createInventory(request);
    }

    @PutMapping("/updateInventory/{id}") // add id variable
    public String updateProductInventory(@PathVariable Long id , @RequestBody ProductInventoryRequest request){
        return productInventoryService.updatedInventory(id,request);
    }

    @GetMapping("/getInventory")
    public List<ProductInventoryResponse> getAll(){
        return productInventoryService.getAllProductsInventory();
    }

    @DeleteMapping("/deleteInventory/{id}")
    public String deleteInventory(@PathVariable Long id){
        return productInventoryService.deleteProductInventory(id);
    }


}