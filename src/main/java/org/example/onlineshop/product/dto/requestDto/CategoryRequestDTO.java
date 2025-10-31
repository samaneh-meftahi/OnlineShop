package org.example.onlineshop.product.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDTO {
    private String title;
    private Long parentCategoryId;
}
