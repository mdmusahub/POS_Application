package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ProductInventory;

public class ProductInventoryResponse {


    private String location;
    private Long quantity;

   public ProductInventoryResponse(ProductInventory inventory){
       this.location = inventory.getLocation();
       this.quantity = inventory.getQuantity();


   }
}
