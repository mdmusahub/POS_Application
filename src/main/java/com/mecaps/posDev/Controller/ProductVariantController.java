package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Service.ProductVariantService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productVariant")
public class ProductVariantController {
final private ProductVariantService productVariantService;


    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

              // FOR CREATE //

           @PostMapping("/createProductVariant")
            public ProductVariantResponse createProductVariant(@RequestBody ProductVariantRequest productVariantRequest){
            return productVariantService.CreateProductVariant(productVariantRequest);
}


                // FOR GET //

            @GetMapping("/getProductVariant")
            public List<ProductVariantResponse> getAll(){
            return productVariantService.getAll();
            }


                // FOR UPDATE //

            @PutMapping("/updateProductVariant/{id}")
            public ProductVariantResponse updateProductVariant(@PathVariable Long id,@RequestBody ProductVariantRequest productVariantRequest){
            return  productVariantService.updateProductVariant(id,productVariantRequest);
            }


                // FOR DELETE //

            @DeleteMapping("/deleteProductVariant/{id}")
            public String deleteProductVariant(@PathVariable Long id){
            return productVariantService.deleteProductVariant(id);
            }
}
