package com.productmgmt.service;
import com.productmgmt.dto.CategoryDTO;
import com.productmgmt.entity.Category;
import com.productmgmt.exception.ResourceNotFoundException;
import com.productmgmt.repository.CategoryRepository;
import com.productmgmt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getById(Long id) {
        return mapToDTO(getCategory(id));
    }

    public CategoryDTO create(CategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category category = Category.builder().name(dto.getName()).build();
        return mapToDTO(categoryRepository.save(category));
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = getCategory(id);
        if (categoryRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new IllegalArgumentException("Category name already exists");
        }
        category.setName(dto.getName());
        return mapToDTO(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = getCategory(id);
        if (productRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Cannot delete category with existing products");
        }
        categoryRepository.delete(category);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .uniqueId(category.getUniqueId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
