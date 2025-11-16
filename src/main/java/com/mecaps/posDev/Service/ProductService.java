package com.mecaps.posDev.Service;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.FullResponse;
import com.mecaps.posDev.Response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product deleteProduct(Long id);

    ProductResponse updateProduct(Long id, ProductRequest req);

    List<ProductResponse> getProduct();

    ProductResponse createProduct(ProductRequest req);

    FullResponse getAllDetailThoughProductId(Long id);

    public Page<ProductResponse> getPaginatedProduct(int page, int size, String sortType);

}
