package org.example.onlineshop.product.mapper;

import org.example.onlineshop.product.dto.requestDto.CategoryRequestDTO;
import org.example.onlineshop.product.dto.responseDto.CategoryResponseDTO;
import org.example.onlineshop.product.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static CategoryResponseDTO toDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());

        if (category.getParentCategory() != null) {
            dto.setParentCategoryTitle(category.getParentCategory().getTitle());
        }

        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            List<CategoryResponseDTO> subCategoryDTOs = new ArrayList<>();
            for (Category sub : category.getSubCategories()) {
                subCategoryDTOs.add(toDTO(sub));
            }
            dto.setSubCategories(subCategoryDTOs);
        }

        return dto;
    }

    public static Category toEntity(CategoryRequestDTO dto, Category parentCategory) {
        Category category = new Category();
        category.setTitle(dto.getTitle());
        category.setParentCategory(parentCategory);
        return category;
    }
}
