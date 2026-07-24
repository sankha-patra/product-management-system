package com.productmgmt.repository;
import com.productmgmt.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategoryId(Long categoryId);
    
    @Query(value = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id " +
       "WHERE (:categoryId IS NULL OR p.category_id = :categoryId) " +
       "AND (:search IS NULL OR p.name ILIKE CONCAT('%', :search, '%'))",
       countQuery = "SELECT COUNT(*) FROM products p WHERE (:categoryId IS NULL OR p.category_id = :categoryId) " +
       "AND (:search IS NULL OR p.name ILIKE CONCAT('%', :search, '%'))",
       nativeQuery = true)
    Page<Product> findAllFiltered(@Param("categoryId") Long categoryId, @Param("search") String search, Pageable pageable);
}
