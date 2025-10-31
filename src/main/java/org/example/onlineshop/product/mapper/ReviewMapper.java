package org.example.onlineshop.product.mapper;

import org.example.onlineshop.product.dto.requestDto.ReviewRequestDTO;
import org.example.onlineshop.product.dto.responseDto.ReviewResponseDTO;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.product.model.Review;
import org.example.onlineshop.user.model.User;

public class ReviewMapper {
    public static ReviewResponseDTO toDto(Review review){
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser() != null ? review.getUser().getId() : null);
        dto.setProductId(review.getProduct() != null ? review.getProduct().getId() : null);
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setApproved(review.isApproved());
        return dto;
    }

    public static Review toEntity(ReviewRequestDTO dto){
        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());

        User user = new User();
        user.setId(dto.getUserId());
        review.setUser(user);

        Product product = new Product();
        product.setId(dto.getProductId());
        review.setProduct(product);

        return review;
    }
}
