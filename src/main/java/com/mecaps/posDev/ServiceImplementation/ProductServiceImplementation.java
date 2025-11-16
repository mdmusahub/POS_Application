package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
import com.mecaps.posDev.Exception.ProductAlreadyExist;
import com.mecaps.posDev.Exception.ProductNotFoundException;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Repository.ProductInventoryRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Request.ProductVariantRequest;
import com.mecaps.posDev.Response.FullResponse;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Response.ViResponse;
import com.mecaps.posDev.Service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository ;
    private final ProductInventoryRepository productInventoryRepository;


    public ProductServiceImplementation(ProductRepository productRepository, CategoryRepository categoryRepository, ProductVariantRepository productVariantRepository, ProductInventoryRepository productInventoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productVariantRepository = productVariantRepository;
        this.productInventoryRepository = productInventoryRepository;
    }


    public ProductResponse createProduct(ProductRequest req) {
        productRepository.findByProductName(req.getProduct_name())
                .ifPresent(present->{throw  new ProductAlreadyExist("This product is already present " + req.getProduct_name());
        });
        Product product = new Product();
        Category category = categoryRepository.findById(req.getCategory_id())
        .orElseThrow(() -> new CategoryNotFoundException("This Category Id is not found " + req.getCategory_id()));
        product.setCategoryId(category);
        product.setProductName(req.getProduct_name());
        product.setSku(req.getSku());
        product.setProduct_description(req.getProduct_description());
        Product save = productRepository.save(product);
        return new ProductResponse(save);

    }


    public Product deleteProduct(Long id) {
        Product deleteProduct = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("This product Id is not found " + id));
        productRepository.delete(deleteProduct);
        return deleteProduct;
    }



    public ProductResponse updateProduct(Long id,ProductRequest req) {
        Product updatePro = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("This product Id is not found " + id));
        Category category = categoryRepository.findById(req.getCategory_id())
                .orElseThrow(() -> new CategoryNotFoundException("This category Id is not found " + req.getCategory_id()));
        updatePro.setProductName(req.getProduct_name());
        updatePro.setProduct_description(req.getProduct_description());
        updatePro.setCategoryId(category);
        Product save = productRepository.save(updatePro) ;
        return new ProductResponse(save);
    }


    public List<ProductResponse> getProduct() {
        List<Product> getProduct = productRepository.findAll();

        return getProduct.stream().map(ProductResponse::new).toList();
    }

    public FullResponse getAllDetailThoughProductId(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("This product is not found"));
        List<ProductVariant> productVariant = productVariantRepository.findByProductId_ProductId(product.getProductId());
        List<ViResponse> viResponseList = productVariant.stream().map(variant -> {
            ProductInventory inventory = productInventoryRepository
                    .findByproductVariant(variant)
                    .orElse(null);
            return new ViResponse(variant, inventory);
        }).toList();


        FullResponse response = new FullResponse();
        response.setProductResponse(new ProductResponse(product));
        response.setViResponseList(viResponseList);
        return response;
       }
}
