package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.ProductInventoryRequest;
import com.mecaps.posDev.Response.ProductInventoryResponse;

import java.util.List;

public interface ProductInventoryService  {

    ProductInventoryResponse createInventory(ProductInventoryRequest request);

    String updatedInventory(Long id,ProductInventoryRequest request);

    List<ProductInventoryResponse> getAllProductsInventory();

    String deleteProductInventory(Long id);
}
