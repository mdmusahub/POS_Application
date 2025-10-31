package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.ProductNotFoundExpection;
import com.mecaps.posDev.Exception.ProductVariantAlreadyExist;
import com.mecaps.posDev.Exception.ProductVariantNotFoundExpection;
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

    public ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest){

        productVariantRepository.findByVariantName(productVariantRequest.getVariantName()) // correct method for check variant name
        .ifPresent(present->{
            throw new ProductVariantAlreadyExist("This product variant is  already found " + productVariantRequest.getVariantName()) ;
        });
        Product product = productRepository.findById(productVariantRequest.getProduct_id()).orElseThrow(()->
        new ProductNotFoundExpection("This Product Id is  Not Found " + productVariantRequest.getProduct_id())); // correct the getter method for fetching the product id

        ProductVariant productVariant = new ProductVariant();
        productVariant.setVariantName(productVariantRequest.getVariantName());
        productVariant.setProduct_variant_price(productVariantRequest.getProduct_variant_price()); // correct the name of parameter
        productVariant.setRefundable(productVariantRequest.getRefundable());
        productVariant.setProduct_variant_value(productVariantRequest.getProduct_variant_value());
        productVariant.setProduct_id(product);  // add product id
        ProductVariant pv = productVariantRepository.save(productVariant);
        return new ProductVariantResponse(pv);
    }



    public List<ProductVariantResponse> getAll(){
        List<ProductVariant> productVariantList =productVariantRepository.findAll();
        return productVariantList.stream().map(ProductVariantResponse::new).toList();
    }



    public String deleteProductVariant(Long id){
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(()->new ProductVariantNotFoundExpection("This product variant Id is not found " + id));
        productVariantRepository.delete(productVariant);
        return "deleted Successfully";
}

    public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest){
        ProductVariant productVariant1 = productVariantRepository.findById(id).orElseThrow(()-> new ProductVariantNotFoundExpection("This product variant Id is not found " + id));
        productVariant1.setVariantName(productVariantRequest.getVariantName());
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


}
