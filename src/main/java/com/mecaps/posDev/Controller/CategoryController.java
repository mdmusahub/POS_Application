package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.CategoryRequest;
import com.mecaps.posDev.Response.CategoryResponse;
import com.mecaps.posDev.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/createCategory")
    public CategoryResponse createCategory(@RequestBody CategoryRequest req) {
        return categoryService.createCategory(req);
    }

    @GetMapping("/getCategory")
    public List<CategoryResponse> getALlCategory() {
        return categoryService.getCategory();

    }

    @PutMapping("/updateCategory/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest req) {
        return categoryService.updateCategory(id, req);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}
