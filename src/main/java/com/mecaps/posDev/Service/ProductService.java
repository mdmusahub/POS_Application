package com.mecaps.posDev.Service;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.ProductResponse;

import java.util.List;

public interface ProductService {

    Product deleteProduct(Long id);

    ProductResponse updateProduct(Long id, ProductRequest req);

    List<ProductResponse> getProduct();

    ProductResponse createProduct(ProductRequest req);
}
