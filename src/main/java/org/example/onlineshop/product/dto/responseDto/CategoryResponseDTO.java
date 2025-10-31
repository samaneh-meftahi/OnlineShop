package org.example.onlineshop.product.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CategoryResponseDTO {
    private Long id;
    private String title;
    private String parentCategoryTitle;
    private List<CategoryResponseDTO> subCategories;
}
