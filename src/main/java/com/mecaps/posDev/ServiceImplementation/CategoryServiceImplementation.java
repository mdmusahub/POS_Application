package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Exception.CategoryAlreadyExist;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Request.CategoryRequest;
import com.mecaps.posDev.Response.CategoryResponse;
import com.mecaps.posDev.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest req) {
            categoryRepository.findByCategory_name(req.getCategory_name()).ifPresent(present->{throw new CategoryAlreadyExist("category already found " + req.getCategory_name());
});
            Category category1 = categoryRepository.findById(req.getParent_category()).orElseThrow(()->new CategoryNotFoundException("category not Found " + req.getParent_category()));
            Category category = new Category();
            category.setCategory_name(req.getCategory_name());
            category.setParent_category(category1);
            category.setCategory_description(req.getCategory_name());

            Category save = categoryRepository.save(category);
            return new CategoryResponse(save);
    }


    public List<CategoryResponse> getCategory() {
        List<Category> getCategory = categoryRepository.findAll();
        return getCategory.stream().map(CategoryResponse::new).toList();
    }


    public CategoryResponse updateCategory(Long id, CategoryRequest req) {
        Category updateCategory = categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Product not found " + id));
        updateCategory.setParent_category(updateCategory);
        updateCategory.setCategory_description(req.getCategory_description());
        updateCategory.setCategory_name(req.getCategory_name());
        Category save = categoryRepository.save(updateCategory);
        return new CategoryResponse(save);
    }

    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Category not found " + id));
        categoryRepository.delete(category);
        return "Category Deleted successfully";
    }
}
