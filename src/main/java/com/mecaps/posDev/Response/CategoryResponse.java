package com.mecaps.posDev.Response;


import com.mecaps.posDev.Entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {


    private String category_name;
    private String category_description;

    public CategoryResponse(Category parent_category) {

        this.category_name = parent_category.getCategoryName();
        this.category_description = parent_category.getCategory_description();
    }


}

