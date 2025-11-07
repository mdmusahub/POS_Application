package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.ProductInventoryRequest;
import com.mecaps.posDev.Response.ProductInventoryResponse;
import com.mecaps.posDev.ServiceImplementation.ProductInventoryServiceImplementation;

import java.util.List;

public interface ProductInventoryService  {

    ProductInventoryResponse createInventory(ProductInventoryRequest request);

    String updatedInventory(Long id,ProductInventoryRequest request);

    List<ProductInventoryResponse> getAllProducts();

    String deleteProduct(Long id);
}
