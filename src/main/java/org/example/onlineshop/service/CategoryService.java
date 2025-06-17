package org.example.onlineshop.service;

import org.example.onlineshop.model.Category;
import org.example.onlineshop.model.Product;
import org.example.onlineshop.repository.CategoryRepository;
import org.example.onlineshop.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id,Category updatedCategory){
        return findById(id).map(category->{
            category.setTitle(updatedCategory.getTitle());
            category.setSubCategories(updatedCategory.getSubCategories());
            category.setParentCategory(updatedCategory.getParentCategory());
            categoryRepository.save(category);
            return category;
        }).orElseThrow(()->new NoSuchElementException("category with id: "+ id+ " not found"));
    }

    public void removeCategory(Long id){
        Category category=findById(id).orElseThrow(()->new NoSuchElementException("category with id: "+ id+ " not found"));
        categoryRepository.delete(category);
    }

    public List<Category> listAllCategory(int page, int size, String sort){
        Pageable pageable= PageRequest.of(page,size, Sort.by(sort));
        return categoryRepository.findAll(pageable).getContent();
    }

    public void validateCategory(Category category) {
        if (category.getTitle() == null || category.getTitle().isBlank()) {
            throw new IllegalArgumentException("Category title must not be empty");
        }
        if (category.getSubCategories() == null || category.getSubCategories().isEmpty()) {
            throw new IllegalArgumentException("Category must have at least one subcategory");
        }
        if (category.getParentCategory() == null) {
            throw new IllegalArgumentException("Parent category must not be null");
        }
    }
}