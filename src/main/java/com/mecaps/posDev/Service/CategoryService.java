package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.CategoryRequest;
import com.mecaps.posDev.Response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    

    CategoryResponse createCategory(CategoryRequest req);

    List<CategoryResponse> getCategory();

    CategoryResponse updateCategory(Long id, CategoryRequest req);

    String deleteCategory(Long id);
}
