package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {


    private String category_name;
    private Long parent_category;
}
