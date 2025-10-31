package org.example.onlineshop.product.service;

import org.example.onlineshop.product.dto.requestDto.ProductRequestDTO;
import org.example.onlineshop.product.dto.responseDto.ProductResponseDTO;
import org.example.onlineshop.product.mapper.ProductMapper;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        validateProduct(productRequestDTO);
        logger.info("Creating product: {}", productRequestDTO.getTitle());
        Product product = ProductMapper.toEntity(productRequestDTO);
        Product savedProduct = productRepository.save(product);
        logger.info("Product created with ID: {}", savedProduct.getId());
        return ProductMapper.toDto(savedProduct);
    }

    public Optional<Product> findById(Long id) {
        logger.debug("Finding product by ID: {}", id);
        return productRepository.findById(id);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO updateProduct) {
        validateProduct(updateProduct);
        logger.info("Updating product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Product with id {} not found", id);
                    return new NoSuchElementException("Product with id: " + id + " not found");
                });

        product.setTitle(updateProduct.getTitle());
        product.setStock(updateProduct.getStock());
        product.setImage_URL(updateProduct.getImage_URL());
        product.setPrice(updateProduct.getPrice());
        product.setCategory(updateProduct.getCategory());
        product.setBrand(updateProduct.getBrand());
        product.setDescription(updateProduct.getDescription());

        Product updatedProduct = productRepository.save(product);
        logger.info("Product updated with ID: {}", updatedProduct.getId());
        return ProductMapper.toDto(updatedProduct);
    }

    public void removeProduct(Long id) {
        logger.info("Removing product with ID: {}", id);
        Product product = findById(id).orElseThrow(() -> {
            logger.warn("Product with id {} not found for deletion", id);
            return new NoSuchElementException("Product with id: " + id + " not found");
        });
        productRepository.delete(product);
        logger.info("Product with ID {} deleted", id);
    }

    public List<ProductResponseDTO> listAllProduct(int page, int size, String sortBy) {
        logger.debug("Listing all products: page={}, size={}, sortBy={}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return productRepository.findAll(pageable).getContent()
                .stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<Product> findByCategory(Long categoryId) {
        logger.debug("Finding products by category ID: {}", categoryId);
        return productRepository.findByCategory_Id(categoryId);
    }

    public List<Product> searchByKeyWord(String keyword) {
        logger.debug("Searching products with keyword: {}", keyword);
        return productRepository.searchByKeyword(keyword);
    }

    public void updateStock(Long id, int newStock) {
        logger.info("Updating stock for product ID {}: newStock={}", id, newStock);
        productRepository.findById(id).ifPresentOrElse(product -> {
            product.setStock(newStock);
            productRepository.save(product);
            logger.info("Stock updated for product ID: {}", id);
        }, () -> {
            logger.warn("Product with id {} not found for stock update", id);
            throw new NoSuchElementException("Product with id: " + id + " not found");
        });
    }

    public Optional<String> getProductImage(Long productId) {
        logger.debug("Fetching product image for ID: {}", productId);
        return findById(productId).map(Product::getImage_URL);
    }

    public void updateProductImage(long productId, String productImageURL) {
        logger.info("Updating image URL for product ID: {}", productId);
        findById(productId).ifPresent(product -> {
            product.setImage_URL(productImageURL);
            productRepository.save(product);
            logger.info("Image URL updated for product ID: {}", productId);
        });
    }

    private void validateProduct(ProductRequestDTO product) {
        logger.debug("Validating product request");
        if (product.getTitle() == null || product.getTitle().isBlank()) {
            throw new IllegalArgumentException("Product title must not be empty");
        }
        if (product.getBrand() == null || product.getBrand().isBlank()) {
            throw new IllegalArgumentException("Product brand must not be empty");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Product category must not be null");
        }
        if (product.getImage_URL() == null || product.getImage_URL().isBlank()) {
            throw new IllegalArgumentException("Product image URL must not be empty");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        logger.debug("Product request validation passed");
    }
}
