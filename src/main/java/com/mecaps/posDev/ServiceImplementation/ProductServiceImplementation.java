package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
import com.mecaps.posDev.Exception.ProductAlreadyExist;
import com.mecaps.posDev.Exception.ProductNotFoundException;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Request.ProductRequest;
import com.mecaps.posDev.Response.ProductResponse;
import com.mecaps.posDev.Response.ProductVariantResponse;
import com.mecaps.posDev.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public ProductServiceImplementation(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public ProductResponse createProduct(ProductRequest req) {
        productRepository.findByProductName(req.getProduct_name())
                .ifPresent(present->{throw  new ProductAlreadyExist("This product is already found " + req.getProduct_name());
        });
        Product product = new Product();
        Category category = categoryRepository.findById(req.getCategory_id())
        .orElseThrow(() -> new CategoryNotFoundException("This Category Id is not found " + req.getCategory_id()));
        product.setCategory_id(category);
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
        updatePro.setCategory_id(category);
        Product save = productRepository.save(updatePro) ;
        return new ProductResponse(save);
    }


    public List<ProductResponse> getProduct() {
        List<Product> getProduct = productRepository.findAll();

        return getProduct.stream().map(ProductResponse::new).toList();
    }




    public Page<ProductResponse> getPaginatedProduct(int page, int size, String sortType) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage;
        if (sortType != null && sortType.toLowerCase().startsWith("max")) {
            productPage = productRepository.findAllByMaxVariantPrice(pageable);
        }
      else if (sortType != null && sortType.toLowerCase().startsWith("min")) {
            productPage = productRepository.findAllByMinVariantPrice(pageable);

        }else {
            productPage = productRepository.findAllByMinVariantPrice(pageable);
            System.out.println(" Invalid sortType, defaulting to MIN variant price");        }

        return productPage.map(ProductResponse::new);
    }

    }

