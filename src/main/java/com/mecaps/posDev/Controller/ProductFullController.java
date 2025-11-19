package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.ProductFullRequest;
import com.mecaps.posDev.Service.ProductFullService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productFull")
public class ProductFullController {

    private final ProductFullService productFullService;

    public ProductFullController(ProductFullService productFullService) {
        this.productFullService = productFullService;
    }


    @PostMapping("/createFullProduct")
    public ResponseEntity<String> createProductFull(@RequestBody ProductFullRequest productFullRequest) {
        String response = productFullService.createProductFull(productFullRequest);
        return ResponseEntity.ok(response);

    }
}
