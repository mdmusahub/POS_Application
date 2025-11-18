package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.FullResponse;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Service.ProductService;
import org.springframework.data.domain.Page;
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

        @PostMapping("/createProduct")
        public ProductResponse createProduct(@RequestBody ProductRequest req) {
        return productService.createProduct(req);
        }

        @DeleteMapping("/deleteProduct/{id}")
        public Product deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
        }

        @PutMapping("/updateProduct/{id}")
        public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest req) {
        return productService.updateProduct( id, req);
        }

        @GetMapping("/getProduct")
        public List<ProductResponse> getProduct(){
        return productService.getProduct();
        }


        @GetMapping("/getAllDetailThoughProductId/{id}")
        public FullResponse getAllDetailThoughProductId(@PathVariable Long id ) {
            return productService.getAllDetailThoughProductId(id);
        }

        @GetMapping("/paginatedProduct")
        public List<ProductResponse> getPaginatedProduct(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "min") String sortType) {

            return productService.getPaginatedProduct(page, size, sortType);
        }


}

