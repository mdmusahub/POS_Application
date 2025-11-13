package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViResponse {


    private ProductVariantResponse productVariantResponse ;
    private ProductInventoryResponse productInventoryResponse ;

    public ViResponse( ProductVariant request2 , ProductInventory request3 ){

     this.productVariantResponse=new ProductVariantResponse(request2);
     this.productInventoryResponse=new ProductInventoryResponse(request3);
    }
    }


