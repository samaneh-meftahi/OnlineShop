package org.example.onlineshop.product.mapper;

import org.example.onlineshop.product.dto.requestDto.ProductRequestDTO;
import org.example.onlineshop.product.dto.responseDto.ProductResponseDTO;
import org.example.onlineshop.product.model.Category;
import org.example.onlineshop.product.model.Product;

public class ProductMapper {

    public static ProductResponseDTO toDto(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setBrand(product.getBrand());
        dto.setImage_URL(product.getImage_URL());
        dto.setCategoryId(product.getCategory().getId());

        return dto;
    }

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setBrand(dto.getBrand());
        product.setImage_URL(dto.getImage_URL());

        Category category = new Category();
        category.setId(dto.getCategoryId());
        product.setCategory(category);

        return product;
    }
}
