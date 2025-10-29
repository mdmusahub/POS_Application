package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Service.ProductIVariantService;
import com.mecaps.posDev.ServiceImplementation.ProductVariantServiceImplementation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productVariant")
public class ProductVariantController {
final ProductIVariantService productVariantService;


    public ProductVariantController(ProductVariantServiceImplementation productVariantServiceImplementation) {
        this.productVariantService = productVariantServiceImplementation;
    }

              // FOR CREATE //

            @PostMapping("/createProductVariant")
            public ProductVariantResponse createProductVariant(@RequestBody ProductVariantRequest productVariantRequest){
            return productVariantService.CreateProductVariant( productVariantRequest);
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

            @GetMapping("/paginated")
            public List<ProductVariantResponse> getPaginatedProductVariants
                    (@RequestParam(defaultValue = "0") int page,
                     @RequestParam(defaultValue = "5") int size,
                     @RequestParam(defaultValue = "product_variant_price")String sortBy,
                     @RequestParam(defaultValue = "asc") String direction)
            {
              return productVariantService.getPaginatedProductVariants(page,size,sortBy,direction);
            }
}
