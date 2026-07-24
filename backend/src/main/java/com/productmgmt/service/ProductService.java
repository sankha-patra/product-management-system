package com.productmgmt.service;
import com.opencsv.CSVReader;
import com.productmgmt.dto.ProductDTO;
import com.productmgmt.dto.PagedResponse;
import com.productmgmt.entity.Category;
import com.productmgmt.entity.Product;
import com.productmgmt.exception.ResourceNotFoundException;
import com.productmgmt.repository.CategoryRepository;
import com.productmgmt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public PagedResponse<ProductDTO> getProducts(int page, int size, String sortBy, String sortDir, String search, Long categoryId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasCategory = categoryId != null;

        Page<Product> productPage;
        if (hasCategory && hasSearch) {
            productPage = productRepository.findByCategoryIdAndNameContaining(categoryId, search.trim(), pageable);
        } else if (hasCategory) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else if (hasSearch) {
            productPage = productRepository.findByNameContaining(search.trim(), pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductDTO> content = productPage.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());
        return new PagedResponse<>(content, productPage.getTotalElements(), productPage.getTotalPages(), productPage.getNumber(), productPage.getSize());
    }

    public ProductDTO getById(Long id) {
        return mapToDTO(getProduct(id));
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Product product = Product.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .price(dto.getPrice())
                .category(category)
                .build();
        return mapToDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = getProduct(id);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setName(dto.getName());
        product.setImage(dto.getImage());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        return mapToDTO(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(getProduct(id));
    }

    @Async
    @Transactional
    public void processBulkUpload(byte[] fileBytes) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(fileBytes)))) {
            String[] line;
            boolean first = true;
            List<Product> batch = new ArrayList<>();
            while ((line = reader.readNext()) != null) {
                if (first) { first = false; continue; }
                if (line.length < 4) continue;
                String name = line[0];
                String image = line[1];
                BigDecimal price = new BigDecimal(line[2]);
                Long catId = Long.parseLong(line[3]);
                Category category = categoryRepository.findById(catId).orElse(null);
                if (category != null) {
                    batch.add(Product.builder().name(name).image(image).price(price).category(category).build());
                }
                if (batch.size() >= 500) {
                    productRepository.saveAll(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                productRepository.saveAll(batch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .uniqueId(product.getUniqueId())
                .name(product.getName())
                .image(product.getImage())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
