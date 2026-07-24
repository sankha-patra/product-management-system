package com.productmgmt.repository;
import com.productmgmt.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategoryId(Long categoryId);
    
    @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:search IS NULL OR p.name ILIKE CONCAT('%', :search, '%'))")
    Page<Product> findAllFiltered(@Param("categoryId") Long categoryId, @Param("search") String search, Pageable pageable);
}
