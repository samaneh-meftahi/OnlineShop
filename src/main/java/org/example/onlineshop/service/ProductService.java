package org.example.onlineshop.service;

import org.example.onlineshop.model.Product;
import org.example.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product updateProduct) {
        return productRepository.findById(id).map(product -> {
            validateProduct(product);
            product.setTitle(updateProduct.getTitle());
            product.setStock(updateProduct.getStock());
            product.setImage_URL(updateProduct.getImage_URL());
            product.setPrice(updateProduct.getPrice());
            product.setCategory(updateProduct.getCategory());
            product.setBrand(updateProduct.getBrand());
            product.setDescription(updateProduct.getDescription());
            productRepository.save(product);
            return product;
        }).orElseThrow(() -> new NoSuchElementException("product with id: " + id + " not found"));
    }

    public void removeProduct(Long id) {
        Product product = findById(id).orElseThrow(() -> new NoSuchElementException("product with id: " + id + " not found"));
        productRepository.delete(product);
    }

    public List<Product> listAllProduct(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return productRepository.findAll(pageable).getContent();
    }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }

    public List<Product> searchByKeyWord(String keyWord) {
        return productRepository.searchByKeyword(keyWord);
    }

    public void updateStock(Long id, int newStock) {
        findById(id).ifPresent(product -> {
            product.setStock(newStock);
            productRepository.save(product);
        });
    }

    public Optional<String> getProductImage(Long productId) {
        return findById(productId).map(product -> product.getImage_URL());
    }

    public void addProductImage(long productId, String productImage_URL) {
        findById(productId).ifPresent(product -> {
            product.setImage_URL(productImage_URL);
            productRepository.save(product);
        });
    }

    private void validateProduct(Product product) {
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
    }
}
