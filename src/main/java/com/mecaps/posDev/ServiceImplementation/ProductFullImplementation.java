package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Repository.ProductInventoryRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductFullRequest;
import com.mecaps.posDev.Request.ProductInventoryRequest;
import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Service.ProductFullService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductFullImplementation implements ProductFullService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductInventoryRepository productInventoryRepository;

    public ProductFullImplementation(ProductRepository productRepository,
                                     CategoryRepository categoryRepository,
                                     ProductVariantRepository productVariantRepository,
                                     ProductInventoryRepository productInventoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.productInventoryRepository = productInventoryRepository;
    }
@Transactional
    public String createProductFull(ProductFullRequest productFullRequest){


    Category category = categoryRepository.findById(productFullRequest.getCategory_id()).orElseThrow(()->new RuntimeException("Id Not Found"));


// creating Product
Product product = new Product();
product.setProductName(productFullRequest.getProduct_name());
product.setProduct_description(productFullRequest.getProduct_description());
product.setSku(productFullRequest.getSku());
product.setCategory_id(category);
    Product save1 = productRepository.save(product);

if(productFullRequest.getProductVariantRequests()!= null && !productFullRequest.getProductVariantRequests().isEmpty()) {
    for (ProductVariantRequest request : productFullRequest.getProductVariantRequests()) {

// Creating ProductVariant
        ProductVariant productVariant = new ProductVariant();
        productVariant.setVariantName(request.getProduct_variant_name());
        productVariant.setProduct_variant_value(request.getProduct_variant_value());
        productVariant.setRefundable(request.getRefundable());
        productVariant.setProduct_variant_price(request.getProduct_variant_price());
        productVariant.setProduct_id(save1);

        ProductVariant save2 = productVariantRepository.save(productVariant);

        if(request.getProductInventoryRequest()!=null){
            ProductInventoryRequest productInventoryRequest = request.getProductInventoryRequest();
// Creating ProductInventory
            ProductInventory productInventory = new ProductInventory();

            productInventory.setLocation(productInventoryRequest.getLocation());
            productInventory.setQuantity(productInventoryRequest.getQuantity());
            productInventory.setProductVariant(save2);
            productInventory.setProduct_id(save1);
ProductInventory save3 = productInventoryRepository.save(productInventory);

        }

    }
}
return "Created Successfully";
}

}
