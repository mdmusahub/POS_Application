package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.ProductInventory;
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



    @PostMapping("/create")
    public ProductInventoryResponse createProductInventory(@RequestBody ProductInventoryRequest request){
       return productInventoryService.createInventory(request);

    }




    @PutMapping("/update")
    public String updateProductInventory(@PathVariable Long id , @RequestBody ProductInventoryRequest request){
        return productInventoryService.updatedInvetory(id,request);
    }




    @GetMapping("/get")
    public List<ProductInventoryResponse> getAll(){
        return productInventoryService.getAllProducts();
    }




    @DeleteMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Long id){
        return  productInventoryService.deleteProduct(id);

    }


}
