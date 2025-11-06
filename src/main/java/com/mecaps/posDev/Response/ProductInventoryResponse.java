package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ProductInventory;
import lombok.Getter;
import lombok.Setter;


public class ProductInventoryResponse {


    private String location;
    private Long quantity;

   public ProductInventoryResponse(ProductInventory inventory){
       this.location = inventory.getLocation();
       this.quantity = inventory.getQuantity();


   }
}
