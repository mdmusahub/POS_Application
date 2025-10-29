package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Repository.ProductInventoryRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductInventoryRequest;
import com.mecaps.posDev.Response.ProductInventoryResponse;
import com.mecaps.posDev.Service.ProductInventoryService;

import java.util.List;

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
              .orElseThrow(()-> new RuntimeException("product not found"));

        ProductVariant variant = productVariantRepository
                .findById(request.getProduct_variant_id())
                .orElseThrow(()-> new RuntimeException("Variant not found"));

        ProductInventory inventory = new ProductInventory();

        inventory.setProduct_id(product);
        inventory.setProduct_variant_id(variant);
        inventory.setQuantity(request.getQuantity());
        inventory.setLocation(request.getLocation());

     ProductInventory save = productInventoryRepository.save(inventory);

        return new ProductInventoryResponse(save);
    }






    public String updatedInvetory(Long id,ProductInventoryRequest request){

        ProductInventory productinventory = productInventoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("id not found"));

        Product product = productRepository
                .findById(request.getProduct_id())
                .orElseThrow(()-> new RuntimeException("product not found"));

        ProductVariant variant = productVariantRepository
                .findById(request.getProduct_variant_id())
                .orElseThrow(()-> new RuntimeException("Variant not found"));

         productinventory.setLocation(request.getLocation());
         productinventory.setQuantity(request.getQuantity());
         productinventory.setProduct_id(product);
         productinventory.setProduct_variant_id(variant);

         return "Inventory updated successfully";
    }



public List<ProductInventoryResponse> getAllProducts(){

      List<ProductInventory> productInventoryList =  productInventoryRepository.findAll();

       return productInventoryList.stream().map(ProductInventoryResponse :: new).toList();

}




public String deleteProduct(Long id){
productInventoryRepository.deleteById(id);
return "deleted successfully";

}



}
