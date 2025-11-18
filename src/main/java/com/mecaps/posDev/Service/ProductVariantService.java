package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {
    ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest);
    List<ProductVariantResponse> getAll();

    String deleteProductVariant(Long id);
    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest);
    List<ProductVariantResponse> getPaginatedProductVariants(int page, int size, String sortBy, String direction);
    ProductVariantResponse findProductVariantById(Long variantId);
}
