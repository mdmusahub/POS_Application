package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.ServiceImplementation.ProductVariantServiceImplementation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productVariant")
public class ProductVariantController {
final private ProductVariantServiceImplementation productVariantServiceImplementation;


    public ProductVariantController(ProductVariantServiceImplementation productVariantServiceImplementation) {
        this.productVariantServiceImplementation = productVariantServiceImplementation;
    }

              // FOR CREATE //

           @PostMapping("/createProductVariant")
            public ProductVariantResponse createProductVariant(@RequestBody ProductVariantRequest productVariantRequest){
            return productVariantServiceImplementation.CreateProductVariant(productVariantRequest);
}


                // FOR GET //

            @GetMapping("/getProductVariant")
            public List<ProductVariantResponse> getAll(){
            return productVariantServiceImplementation.getAll();
            }


                // FOR UPDATE //

            @PutMapping("/updateProductVariant/{id}")
            public ProductVariantResponse updateProductVariant(@PathVariable Long id,@RequestBody ProductVariantRequest productVariantRequest){
            return  productVariantServiceImplementation.updateProductVariant(id,productVariantRequest);
            }


                // FOR DELETE //

            @DeleteMapping("/deleteProductVariant/{id}")
            public String deleteProductVariant(@PathVariable Long id){
            return productVariantServiceImplementation.deleteProductVariant(id);
            }

            @GetMapping("/getProudctVariantById/{id}")
    public ProductVariantResponse findUserById(@PathVariable Long id){
        return productVariantServiceImplementation.findByUserId(id);
            }
}
