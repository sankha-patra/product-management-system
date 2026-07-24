package com.productmgmt.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uniqueId;
    
    @Column(nullable = false)
    private String name;
    
    private String image;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        uniqueId = UUID.randomUUID();
        createdAt = LocalDateTime.now();
    }
}
