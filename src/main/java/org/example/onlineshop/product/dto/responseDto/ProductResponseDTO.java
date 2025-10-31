package org.example.onlineshop.product.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long categoryId;
    private String brand;
    private String image_URL;

}
