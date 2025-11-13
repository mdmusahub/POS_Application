package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.ProductInventoryNotFoundException;
import com.mecaps.posDev.Exception.ProductNotFoundException;
import com.mecaps.posDev.Exception.ProductVariantNotFoundException;
import com.mecaps.posDev.Repository.ProductInventoryRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductInventoryRequest;
import com.mecaps.posDev.Response.ProductInventoryResponse;
import com.mecaps.posDev.Service.ProductInventoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductInventoryServiceImplementation implements ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductInventoryServiceImplementation(ProductInventoryRepository productInventoryRepository, ProductRepository productRepository, ProductVariantRepository productVariantRepository) {
        this.productInventoryRepository = productInventoryRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }


    public ProductInventoryResponse createInventory(ProductInventoryRequest request){
      Product product = productRepository
              .findById(request.getProduct_id())
              .orElseThrow(()-> new ProductNotFoundException("This product Id is not found " + request.getProduct_id()));

        ProductVariant variant = productVariantRepository
                .findById(request.getProduct_variant_id())
                .orElseThrow(()-> new ProductVariantNotFoundException("This variant Id is not found " + request.getProduct_variant_id()));

        ProductInventory inventory = new ProductInventory();
        inventory.setProductVariant(variant);  // add variant id in inventory
        inventory.setProductId(product);
        inventory.setQuantity(request.getQuantity());
        inventory.setLocation(request.getLocation());


        ProductInventory save = productInventoryRepository.save(inventory);

        return new ProductInventoryResponse(save);
    }


    public String updatedInventory(Long id,ProductInventoryRequest request){

        ProductInventory productInventory = productInventoryRepository.findById(id)  // correct variable name of productInventory
                .orElseThrow(()-> new ProductInventoryNotFoundException("This product inventory Id is not found " + id));

        Product product = productRepository
                .findById(request.getProduct_id())
                .orElseThrow(()-> new ProductNotFoundException("This product Id is not found " + request.getProduct_id()));

        ProductVariant variant = productVariantRepository
                .findById(request.getProduct_variant_id())
                .orElseThrow(()-> new ProductVariantNotFoundException("This variant Id is not found " + request.getProduct_variant_id()));

         productInventory.setLocation(request.getLocation());
         productInventory.setQuantity(request.getQuantity());
         productInventory.setProductId(product);
         productInventory.setProductVariant(variant);
         productInventoryRepository.save(productInventory);  // add save of productInventory

         return "Inventory updated successfully";
    }

public List<ProductInventoryResponse> getAllProducts(){
      List<ProductInventory> productInventoryList =  productInventoryRepository.findAll();
       return productInventoryList.stream().map(ProductInventoryResponse :: new).toList();
}

public String deleteProduct(Long id){
    ProductInventory inventory = productInventoryRepository.findById(id)  // add check method before deletion
            .orElseThrow(() -> new ProductInventoryNotFoundException("Inventory not found: " + id));
    productInventoryRepository.delete(inventory);
  return "deleted successfully";
}

}
