package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(ProductVariantRequest productVariantRequest);

    List<ProductVariantResponse> getAllProductVariant();

    String deleteProductVariant(Long id);

    ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest);
}
