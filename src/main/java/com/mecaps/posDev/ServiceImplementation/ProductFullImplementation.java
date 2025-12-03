package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
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

/**
 * Service implementation for creating a complete product entry including:
 * <ul>
 *     <li>Product details</li>
 *     <li>Product variants</li>
 *     <li>Inventory information</li>
 * </ul>
 * This service saves all related data in a single transactional operation.
 */
@Service
public class ProductFullImplementation implements ProductFullService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductInventoryRepository productInventoryRepository;

    /**
     * Constructor for injecting required repositories.
     */
    public ProductFullImplementation(ProductRepository productRepository,
                                     CategoryRepository categoryRepository,
                                     ProductVariantRepository productVariantRepository,
                                     ProductInventoryRepository productInventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.productInventoryRepository = productInventoryRepository;
    }

    /**
     * Creates a full product entry including product, variant(s), and inventory.
     * <p>
     * Steps performed:
     * <ol>
     *     <li>Validate and fetch category</li>
     *     <li>Create and save product</li>
     *     <li>Create and save multiple product variants (if provided)</li>
     *     <li>Create and save inventory for each variant (if provided)</li>
     * </ol>
     *
     * @param productFullRequest the request containing all product-related details
     * @return success message after creating the full product structure
     * @throws CategoryNotFoundException if the provided category ID does not exist
     */
    @Transactional
    @Override
    public String createProductFull(ProductFullRequest productFullRequest) {

        Category category = categoryRepository.findById(productFullRequest.getCategory_id())
                .orElseThrow(() ->
                        new CategoryNotFoundException("category Not Found" + productFullRequest.getCategory_id()));

        // Creating Product
        Product product = new Product();
        product.setProductName(productFullRequest.getProduct_name());
        product.setProduct_description(productFullRequest.getProduct_description());
        product.setSku(productFullRequest.getSku());
        product.setCategoryId(category);
        Product save1 = productRepository.save(product);

        // Creating Variants and Inventory
        if (productFullRequest.getProductVariantRequests() != null &&
                !productFullRequest.getProductVariantRequests().isEmpty()) {

            for (ProductVariantRequest request : productFullRequest.getProductVariantRequests()) {

                // Creating ProductVariant
                ProductVariant productVariant = new ProductVariant();
                productVariant.setVariantName(request.getProduct_variant_name());
                productVariant.setProductVariantValue(request.getProduct_variant_value());
                productVariant.setRefundable(request.getRefundable());
                productVariant.setProductVariantPrice(request.getProduct_variant_price());
                productVariant.setProductId(save1);

                ProductVariant save2 = productVariantRepository.save(productVariant);

                // Creating ProductInventory (if provided)
                if (request.getProductInventoryRequest() != null) {
                    ProductInventoryRequest productInventoryRequest = request.getProductInventoryRequest();

                    ProductInventory productInventory = new ProductInventory();
                    productInventory.setLocation(productInventoryRequest.getLocation());
                    productInventory.setQuantity(productInventoryRequest.getQuantity());
                    productInventory.setProductVariant(save2);
                    productInventory.setProductId(save1);

                    productInventoryRepository.save(productInventory);
                }
            }
        }

        return "Created Successfully";
    }
}
