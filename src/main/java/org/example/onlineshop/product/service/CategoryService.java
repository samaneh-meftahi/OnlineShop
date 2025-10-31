package org.example.onlineshop.product.service;

import org.example.onlineshop.product.dto.requestDto.CategoryRequestDTO;
import org.example.onlineshop.product.dto.responseDto.CategoryResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.product.mapper.CategoryMapper;
import org.example.onlineshop.product.model.Category;
import org.example.onlineshop.product.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        logger.info("Creating new category with title: {}", requestDTO.getTitle());

        Category parentCategory = null;
        if (requestDTO.getParentCategoryId() != null) {
            parentCategory = categoryRepository.findById(requestDTO.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + requestDTO.getParentCategoryId()));
        }

        validateCategory(requestDTO);

        Category category = CategoryMapper.toEntity(requestDTO, parentCategory);
        Category saved = categoryRepository.save(category);

        logger.info("Category saved successfully with id: {}", saved.getId());
        return CategoryMapper.toDTO(saved);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        logger.info("Updating category with id: {}", id);

        Category category = findById(id);
        category.setTitle(requestDTO.getTitle());

        if (requestDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(requestDTO.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + requestDTO.getParentCategoryId()));
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        Category updated = categoryRepository.save(category);
        logger.info("Category updated successfully with id: {}", updated.getId());
        return CategoryMapper.toDTO(updated);
    }

    public void removeCategory(Long id) {
        logger.warn("Removing category with id: {}", id);
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    public List<CategoryResponseDTO> listAllCategory(int page, int size, String sortBy) {
        Sort sort = (sortBy == null || sortBy.isBlank()) ? Sort.by("id") : Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return categoryRepository.findAll(pageable)
                .map(CategoryMapper::toDTO)
                .getContent();
    }

    private void validateCategory(CategoryRequestDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new InvalidRequestException("Category title must not be empty");
        }
    }
}
