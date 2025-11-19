package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.ProductNotFoundException;
import com.mecaps.posDev.Exception.ProductVariantAlreadyExist;
import com.mecaps.posDev.Exception.ProductVariantNotFoundException;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Service.ProductVariantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantServiceImplementation implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantServiceImplementation(ProductVariantRepository productVariantRepository, ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest) {

        productVariantRepository.findByVariantName(productVariantRequest.getProduct_variant_name()) // correct method for check variant name
                .ifPresent(present -> {
                    throw new ProductVariantAlreadyExist("This product variant is  already found " + productVariantRequest.getProduct_variant_name());
                });
        Product product = productRepository.findById(productVariantRequest.getProduct_variant()).orElseThrow(() ->
                new ProductNotFoundException("This Product Id is  Not Found " + productVariantRequest.getProduct_variant())); // correct the getter method for fetching the product id

        ProductVariant productVariant = new ProductVariant();
        productVariant.setVariantName(productVariantRequest.getProduct_variant_name());
        productVariant.setProductVariantPrice(productVariantRequest.getProduct_variant_price()); // correct the name of parameter
        productVariant.setRefundable(productVariantRequest.getRefundable());
        productVariant.setProductVariantValue(productVariantRequest.getProduct_variant_value());
        productVariant.setProductId(product);
        ProductVariant pv = productVariantRepository.save(productVariant);
        return new ProductVariantResponse(pv);
    }

    @Override
    public List<ProductVariantResponse> getAll() {
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        return productVariantList.stream().map(ProductVariantResponse::new).toList();
    }

    @Override
    public ProductVariantResponse findProductVariantById(Long variantId) {
        ProductVariant productVariant = productVariantRepository.findById(variantId).orElseThrow(() ->
                new ProductVariantNotFoundException("This Product Variant Id is  Not Found " + variantId));
        return new ProductVariantResponse(productVariant);
    }

    @Override
    public String deleteProductVariant(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(() -> new ProductVariantNotFoundException("This product variant Id is not found " + id));
        productVariantRepository.delete(productVariant);
        return "deleted Successfully";
    }

    @Override
    public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant1 = productVariantRepository.findById(id).orElseThrow(() ->
                new ProductVariantNotFoundException("This product variant Id is not found " + id));
        Product product = productRepository.findById(productVariantRequest.getProduct_variant()).orElseThrow(() ->
                new ProductNotFoundException("This Product Id is not found " + productVariantRequest.getProduct_variant()));
        productVariant1.setVariantName(productVariantRequest.getProduct_variant_name());
        productVariant1.setProductVariantValue(productVariantRequest.getProduct_variant_value());
        productVariant1.setProductVariantPrice(productVariantRequest.getProduct_variant_price());
        productVariant1.setRefundable(productVariantRequest.getRefundable());
        productVariant1.setProductId(product);
        ProductVariant save = productVariantRepository.save(productVariant1);
        return new ProductVariantResponse(save);
    }

   @Override
    public List<ProductVariantResponse> getPaginatedProductVariants(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductVariant> productVariantPage = productVariantRepository.findAll(pageable);
        return productVariantPage.getContent().stream().map(ProductVariantResponse::new).toList();
    }

}
