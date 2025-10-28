package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Service.ProductVariantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantServiceImplementation implements ProductVariantService {

final private ProductVariantRepository productVariantRepository;
final private ProductRepository productRepository;

    public ProductVariantServiceImplementation(ProductVariantRepository productVariantRepository, ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }


                     // CREATE METHOD FOR PRODUCTVARIANT //

        public ProductVariantResponse createProductVariant(ProductVariantRequest productVariantRequest){
        ProductVariant productVariant = new ProductVariant();
        Product product = productRepository.findById(productVariantRequest.getProduct_id())
                .orElseThrow(()->new RuntimeException("Product not found"));

        productVariant.setProduct_id(product);

        productVariant.setProduct_variant_name(productVariantRequest.getProduct_variant_name());
        productVariant.setProduct_variant_price(productVariantRequest.getProduct_variant_price());
        productVariant.setRefundable(productVariantRequest.getRefundable());
        productVariant.setProduct_variant_value(productVariantRequest.getProduct_variant_value());
        ProductVariant save = productVariantRepository.save(productVariant);
        return new ProductVariantResponse(save);
         }


                    // Get METHOD FOR PRODUCTVARIANT //

    public List<ProductVariantResponse> getAllProductVariant(){
List<ProductVariant> productVariantList =productVariantRepository.findAll();
return productVariantList.stream().map(ProductVariantResponse::new).toList();
    }


                    // DELETE METHOD FOR PRODUCTVARIANT //

    public String deleteProductVariant(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(()->new RuntimeException("No variant found"));
        productVariantRepository.delete(productVariant);
        return "Variant deleted";
    }


                        // UPDATE METHOD FOR PRODUCTVARIANT //

    public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant1 = productVariantRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found"));
        productVariant1.setProduct_variant_name(productVariantRequest.getProduct_variant_name());
        productVariant1.setProduct_variant_value(productVariantRequest.getProduct_variant_value());
        productVariant1.setProduct_variant_price(productVariantRequest.getProduct_variant_price());
        ProductVariant save = productVariantRepository.save(productVariant1);
        return new ProductVariantResponse(save);
    }

}

