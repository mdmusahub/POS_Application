package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public ProductServiceImplementation(ProductRepository productRepository, ProductVariantRepository productVariantRepository, Category category, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public ProductResponse createProduct(ProductRequest req) {
        Product product = new Product();
        Category category = categoryRepository.findById(req.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory_id(category);
        product.setProduct_name(req.getProduct_name());
        product.setProduct_description(req.getProduct_description());
        Product save = productRepository.save(product);
        return new ProductResponse(save);

    }


    public Product deleteProduct(Long id) {
        Product deleteProduct = productRepository.findById(id).orElseThrow(()->new RuntimeException("No product found"));
        productRepository.delete(deleteProduct);
        return deleteProduct;
    }



    public ProductResponse updateProduct(Long id,ProductRequest req) {
        Product updatePro = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        Category category = categoryRepository.findById(req.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updatePro.setProduct_name(req.getProduct_name());
        updatePro.setProduct_description(req.getProduct_description());
        updatePro.setCategory_id(category);
        Product save = productRepository.save(updatePro) ;
        return new ProductResponse(save);
    }


    public List<ProductResponse> getProduct() {
        List<Product> getProduct = productRepository.findAll();

        return getProduct.stream().map(ProductResponse::new).toList();
    }


}
