package org.example.onlineshop.product.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.product.model.Category;

import java.math.BigDecimal;
@Getter
@Setter
public class ProductRequestDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long categoryId;
    private String brand;
    private String image_URL;
    private Category category;
}
