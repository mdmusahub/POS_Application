package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;


    public ProductServiceImplementation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public ProductResponse createPost(ProductRequest req) {

        return null;
    }

    public Product deleteProduct(Long id) {
        Product deleteProduct = productRepository.findById(id).orElseThrow(()->new RuntimeException("No product found"));
        productRepository.delete(deleteProduct);
        return deleteProduct;
    }


    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Product updatePro = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        updatePro.setProduct_name(req.getProduct_name());
        updatePro.setProduct_description(req.getProduct_description());
        Product save = productRepository.save(updatePro) ;
        return null;
    }


    public List<ProductResponse> getProduct() {
        List<Product> getPro = productRepository.findAll();

        return getPro.stream().map(ProductResponse::new).toList();
    }
}
