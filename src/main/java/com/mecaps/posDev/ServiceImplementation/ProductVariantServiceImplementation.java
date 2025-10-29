package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Service.ProductIVariantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantServiceImplementation implements ProductIVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantServiceImplementation(ProductVariantRepository productVariantRepository, ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }
                     // CREATE METHOD FOR PRODUCTVARIANT //
    public ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest){
        Product product = productRepository.findById(productVariantRequest.getProduct_id()).orElseThrow(()-> new RuntimeException("Product Not Found"));

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



public List<ProductVariantResponse> getPaginatedProductVariants(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductVariant> productVariantPage = productVariantRepository.findAll(pageable);
        return productVariantPage.getContent().stream().map(ProductVariantResponse :: new).toList();
}

public ProductVariantResponse findUserById(Long id){
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(()->new RuntimeException("User not found "));
        return new  ProductVariantResponse(productVariant);
}

}
