package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Exception.CategoryAlreadyExist;
import com.mecaps.posDev.Exception.CategoryNotFoundException;
import com.mecaps.posDev.Repository.CategoryRepository;
import com.mecaps.posDev.Request.CategoryRequest;
import com.mecaps.posDev.Response.CategoryResponse;
import com.mecaps.posDev.Service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing product categories.
 * Provides operations to create, update, fetch, and delete categories.
 */
@Service
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructor for injecting CategoryRepository.
     *
     * @param categoryRepository repository to manage category operations
     */
    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new category.
     * <p>
     * Validates duplicate category name and supports optional parent category.
     *
     * @param req the category creation request
     * @return created category response
     * @throws CategoryAlreadyExist      if category name already exists
     * @throws CategoryNotFoundException if parent category ID is invalid
     */
    @Override
    public CategoryResponse createCategory(CategoryRequest req) {
        categoryRepository.findByCategoryName(req.getCategory_name())
                .ifPresent(present->{throw
                        new CategoryAlreadyExist("This category already found " + req.getCategory_name());});

        Category parent_category = null;
        if(req.getParent_category() != null){
            parent_category = categoryRepository.findById(req.getParent_category())
                    .orElseThrow(()->new CategoryNotFoundException("Category not Found " + req.getParent_category()));
        }

        Category category = new Category();
        category.setCategoryName(req.getCategory_name());
        category.setParent_category(parent_category);
        category.setCategory_description(req.getCategory_description());

        Category save = categoryRepository.save(category);

        return new CategoryResponse(save);
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return list of CategoryResponse objects
     */
    @Override
    public List<CategoryResponse> getCategory() {
        List<Category> getCategory = categoryRepository.findAll();
        return getCategory.stream().map(CategoryResponse::new).toList();
    }

    /**
     * Updates an existing category.
     * <p>
     * Supports updating parent category and description.
     *
     * @param id  the category ID to update
     * @param req the updated category details
     * @return updated category response
     * @throws CategoryNotFoundException if category or parent category is not found
     */
    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest req) {
        Category updateCategory = categoryRepository.findById(id).orElseThrow(()->
                new CategoryNotFoundException("Category not Found " + id));

        Category parentCategory = null;
        if(req.getParent_category() != null){
            parentCategory = categoryRepository.findById(req.getParent_category())
                    .orElseThrow(() -> new CategoryNotFoundException("Parent category not found: " + req.getParent_category()));
        }

        updateCategory.setParent_category(parentCategory);
        updateCategory.setCategory_description(req.getCategory_description());
        updateCategory.setCategoryName(req.getCategory_name());

        Category save = categoryRepository.save(updateCategory);
        return new CategoryResponse(save);
    }

    /**
     * Deletes a category by ID.
     *
     * @param id the ID of the category to delete
     * @return success message
     * @throws CategoryNotFoundException if category does not exist
     */
    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("This Category Id is not found " + id));
        categoryRepository.delete(category);
        return "Category Deleted successfully";
    }
}
