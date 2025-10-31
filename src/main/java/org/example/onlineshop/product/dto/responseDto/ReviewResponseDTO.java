package org.example.onlineshop.product.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ReviewResponseDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private int rating;
    private LocalDateTime createdAt;
    private String comment;
    private boolean isApproved;
}
