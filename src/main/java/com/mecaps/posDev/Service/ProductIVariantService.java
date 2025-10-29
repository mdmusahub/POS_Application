package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;

import java.util.List;

public interface ProductIVariantService {
    ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest);
    List<ProductVariantResponse> getAll();
    ProductVariantResponse findProductVariantById(Long id);
    String deleteProductVariant(Long id);
    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest);
    List<ProductVariantResponse> getPaginatedProductVariants(int page, int size, String sortBy, String direction);
}
