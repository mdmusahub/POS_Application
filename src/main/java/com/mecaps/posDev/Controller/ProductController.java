package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController
{
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

        @PostMapping("/product/create")
        public ProductResponse createproduct(@RequestBody ProductRequest req) {
        return productService.createPost(req);
        }

        @DeleteMapping("/product/delete/{id}")
        public Product deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
        }

        @PutMapping("/product/update/{id}")
        public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest req) {
        return productService.updateProduct(id, req);
        }

        @GetMapping("/product/get")
        public List<ProductResponse> getProduct(){
        return productService.getProduct();
        }


}

