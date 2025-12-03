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

/**
 * Service implementation for managing product inventory operations such as
 * creating inventory, updating stock, fetching all inventory records, and deleting inventory.
 */
@Service
public class ProductInventoryServiceImplementation implements ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    /**
     * Constructor used for injecting repository dependencies.
     */
    public ProductInventoryServiceImplementation(ProductInventoryRepository productInventoryRepository,
                                                 ProductRepository productRepository,
                                                 ProductVariantRepository productVariantRepository) {
        this.productInventoryRepository = productInventoryRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }

    /**
     * Creates a new inventory record for a product and its variant.
     *
     * @param request the inventory creation details
     * @return the created inventory details as response
     * @throws ProductNotFoundException if product ID is invalid
     * @throws ProductVariantNotFoundException if variant ID is invalid
     */
    @Override
    public ProductInventoryResponse createInventory(ProductInventoryRequest request){
        Product product = productRepository
                .findById(request.getProduct_id())
                .orElseThrow(() ->
                        new ProductNotFoundException("This product Id is not found " + request.getProduct_id()));

        ProductVariant variant = productVariantRepository
                .findById(request.getProduct_variant_id())
                .orElseThrow(() ->
                        new ProductVariantNotFoundException("This variant Id is not found " + request.getProduct_variant_id()));

        ProductInventory inventory = new ProductInventory();
        inventory.setProductVariant(variant);
        inventory.setProductId(product);
        inventory.setQuantity(request.getQuantity());
        inventory.setLocation(request.getLocation());

        ProductInventory save = productInventoryRepository.save(inventory);
        return new ProductInventoryResponse(save);
    }

    /**
     * Updates an existing product inventory record.
     *
     * @param id the inventory ID to update
     * @param request the updated inventory details
     * @return success message after update
     * @throws ProductInventoryNotFoundException if inventory does not exist
     * @throws ProductNotFoundException if product ID is invalid
     * @throws ProductVariantNotFoundException if variant ID is invalid
     */
    @Override
    public String updatedInventory(Long id, ProductInventoryRequest request){

        ProductInventory productInventory = productInventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ProductInventoryNotFoundException("This product inventory Id is not found " + id));

        Product product = productRepository
                .findById(request.getProduct_id())
                .orElseThrow(() ->
                        new ProductNotFoundException("This product Id is not found " + request.getProduct_id()));

        ProductVariant variant = productVariantRepository
                .findById(request.getProduct_variant_id())
                .orElseThrow(() ->
                        new ProductVariantNotFoundException("This variant Id is not found " + request.getProduct_variant_id()));

        productInventory.setLocation(request.getLocation());
        productInventory.setQuantity(request.getQuantity());
        productInventory.setProductId(product);
        productInventory.setProductVariant(variant);
        productInventoryRepository.save(productInventory);

        return "Inventory updated successfully";
    }

    /**
     * Retrieves all inventory records.
     *
     * @return list of inventory responses
     */
    @Override
    public List<ProductInventoryResponse> getAllProductsInventory(){
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        return productInventoryList.stream().map(ProductInventoryResponse::new).toList();
    }

    /**
     * Deletes a specific product inventory record by ID.
     *
     * @param id the inventory ID to delete
     * @return success message after deletion
     * @throws ProductInventoryNotFoundException if the inventory record does not exist
     */
    @Override
    public String deleteProductInventory(Long id){
        ProductInventory inventory = productInventoryRepository.findById(id)
                .orElseThrow(() ->
                        new ProductInventoryNotFoundException("Inventory not found: " + id));

        productInventoryRepository.delete(inventory);
        return "deleted successfully";
    }

}
