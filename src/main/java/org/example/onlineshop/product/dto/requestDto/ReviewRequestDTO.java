package org.example.onlineshop.product.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ReviewRequestDTO {
    private Long userId;
    private Long productId;
    private int rating;
    private LocalDateTime createdAt;
    private String comment;
}
