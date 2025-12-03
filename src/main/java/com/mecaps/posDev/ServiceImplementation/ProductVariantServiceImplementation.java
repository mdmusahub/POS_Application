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

/**
 * Service implementation for managing Product Variants, including
 * creation, update, deletion, and paginated listing.
 */
@Service
public class ProductVariantServiceImplementation implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    /**
     * Constructor for injecting dependencies.
     */
    public ProductVariantServiceImplementation(ProductVariantRepository productVariantRepository,
                                               ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    /**
     * Creates a new product variant after validating product existence
     * and checking for duplicate variant name.
     *
     * @param productVariantRequest request containing variant details
     * @return created product variant response
     * @throws ProductVariantAlreadyExist if variant name already exists
     * @throws ProductNotFoundException   if product ID is invalid
     */
    @Override
    public ProductVariantResponse CreateProductVariant(ProductVariantRequest productVariantRequest) {

        productVariantRepository.findByVariantName(productVariantRequest.getProduct_variant_name())
                .ifPresent(present -> {
                    throw new ProductVariantAlreadyExist(
                            "This product variant is already found " + productVariantRequest.getProduct_variant_name());
                });

        Product product = productRepository.findById(productVariantRequest.getProduct_id())
                .orElseThrow(() ->
                        new ProductNotFoundException("This Product Id is Not Found " + productVariantRequest.getProduct_id()));

        ProductVariant productVariant = new ProductVariant();
        productVariant.setVariantName(productVariantRequest.getProduct_variant_name());
        productVariant.setProductVariantPrice(productVariantRequest.getProduct_variant_price());
        productVariant.setRefundable(productVariantRequest.getRefundable());
        productVariant.setProductVariantValue(productVariantRequest.getProduct_variant_value());
        productVariant.setProductId(product);

        ProductVariant pv = productVariantRepository.save(productVariant);
        return new ProductVariantResponse(pv);
    }

    /**
     * Fetches all product variants.
     *
     * @return list of product variant responses
     */
    @Override
    public List<ProductVariantResponse> getAll() {
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        return productVariantList.stream().map(ProductVariantResponse::new).toList();
    }

    /**
     * Fetches a single product variant by ID.
     *
     * @param variantId variant ID
     * @return product variant response
     * @throws ProductVariantNotFoundException if ID is invalid
     */
    @Override
    public ProductVariantResponse findProductVariantById(Long variantId) {
        ProductVariant productVariant = productVariantRepository.findById(variantId)
                .orElseThrow(() ->
                        new ProductVariantNotFoundException("This Product Variant Id is Not Found " + variantId));
        return new ProductVariantResponse(productVariant);
    }

    /**
     * Deletes a product variant using ID.
     *
     * @param id variant ID
     * @return success message
     * @throws ProductVariantNotFoundException if variant does not exist
     */
    @Override
    public String deleteProductVariant(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() ->
                        new ProductVariantNotFoundException("This product variant Id is not found " + id));
        productVariantRepository.delete(productVariant);
        return "deleted Successfully";
    }

    /**
     * Updates product variant details such as price, value, name, refundable status, etc.
     *
     * @param id variant ID
     * @param productVariantRequest update request containing new details
     * @return updated product variant response
     * @throws ProductVariantNotFoundException if variant does not exist
     * @throws ProductNotFoundException        if product ID is invalid
     */
    @Override
    public ProductVariantResponse updateProductVariant(Long id, ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant1 = productVariantRepository.findById(id)
                .orElseThrow(() ->
                        new ProductVariantNotFoundException("This product variant Id is not found " + id));

        Product product = productRepository.findById(productVariantRequest.getProduct_id())
                .orElseThrow(() ->
                        new ProductNotFoundException("This Product Id is not found " + productVariantRequest.getProduct_id()));

        productVariant1.setVariantName(productVariantRequest.getProduct_variant_name());
        productVariant1.setProductVariantValue(productVariantRequest.getProduct_variant_value());
        productVariant1.setProductVariantPrice(productVariantRequest.getProduct_variant_price());
        productVariant1.setRefundable(productVariantRequest.getRefundable());
        productVariant1.setProductId(product);

        ProductVariant save = productVariantRepository.save(productVariant1);
        return new ProductVariantResponse(save);
    }

    /**
     * Returns paginated and sortable product variant list.
     *
     * @param page      page number
     * @param size      number of records per page
     * @param sortBy    field to sort
     * @param direction "asc" or "desc"
     * @return list of product variant responses
     */
    @Override
    public List<ProductVariantResponse> getPaginatedProductVariants(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductVariant> productVariantPage = productVariantRepository.findAll(pageable);

        return productVariantPage.getContent()
                .stream()
                .map(ProductVariantResponse::new)
                .toList();
    }
}
