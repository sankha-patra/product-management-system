package com.productmgmt.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uniqueId;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        uniqueId = UUID.randomUUID();
        createdAt = LocalDateTime.now();
    }
}
