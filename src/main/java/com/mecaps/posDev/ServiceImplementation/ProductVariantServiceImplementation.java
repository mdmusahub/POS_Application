package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantServiceImplementation {

final private ProductVariantRepository productVariantRepository;

    public ProductVariantServiceImplementation(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }
                     // CREATE METHOD FOR PRODUCTVARIANT //
    public ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest){

        ProductVariant productVariant = new ProductVariant();

productVariant.setProduct_variant_name(productVariantRequest.getProduct_variant_name());
productVariant.setProduct_variant_price(productVariant.getProduct_variant_price());
productVariant.setRefundable(productVariantRequest.getRefundable());
productVariant.setProduct_variant_value(productVariantRequest.getProduct_variant_value());
ProductVariant pv = productVariantRepository.save(productVariant);
return new ProductVariantResponse(pv);
    }

                    // Get METHOD FOR PRODUCTVARIANT //

    public List<ProductVariantResponse> getAll(){
List<ProductVariant> productVariantList =productVariantRepository.findAll();
return productVariantList.stream().map(ProductVariantResponse::new).toList();
    }

                  // DELETE METHOD FOR PRODUCTVARIANT //

public String deleteProductVariant(Long id){
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(()->new RuntimeException("not found"));
       productVariantRepository.delete(productVariant);
       return "deleted Successfully";
}

public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest){
        ProductVariant productVariant1 = productVariantRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found"));
productVariant1.setProduct_variant_name(productVariantRequest.getProduct_variant_name());
    productVariant1.setProduct_variant_value(productVariantRequest.getProduct_variant_value());
    productVariant1.setProduct_variant_price(productVariantRequest.getProduct_variant_price());
ProductVariant save = productVariantRepository.save(productVariant1);
return new ProductVariantResponse(save);
}

}
