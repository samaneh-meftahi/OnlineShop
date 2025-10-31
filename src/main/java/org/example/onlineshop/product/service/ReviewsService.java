package org.example.onlineshop.product.service;

import org.example.onlineshop.product.dto.requestDto.ReviewRequestDTO;
import org.example.onlineshop.product.dto.responseDto.ReviewResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.product.mapper.ReviewMapper;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.product.model.Review;
import org.example.onlineshop.user.model.User;
import org.example.onlineshop.product.repository.ProductRepository;
import org.example.onlineshop.user.repository.ReviewRepository;
import org.example.onlineshop.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewsService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewsService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO) {
        validateReview(reviewRequestDTO);
        Review review= reviewRepository.save(ReviewMapper.toEntity(reviewRequestDTO));
        return ReviewMapper.toDto(review);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProduct_Id(productId);
    }

    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO updatedReview) {
        validateReview(updatedReview);
        Review review=reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        User user=userRepository.findById(updatedReview.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user with id " + updatedReview.getUserId() + " not found"));
        Product product=productRepository.findById(updatedReview.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Review with id " + updatedReview.getProductId() + " not found"));
        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);

        return ReviewMapper.toDto(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        reviewRepository.delete(review);
    }

    private void validateReview(ReviewRequestDTO review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new InvalidRequestException("Rating must be between 1 and 5");
        }
        if (review.getComment() == null || review.getComment().isBlank()) {
            throw new InvalidRequestException("Comment must not be empty");
        }
        if (review.getUserId() == null) {
            throw new InvalidRequestException("Review must be associated with a user");
        }
        if (review.getProductId() == null) {
            throw new InvalidRequestException("Review must be associated with a product");
        }
    }
}
