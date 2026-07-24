package com.productmgmt.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private UUID uniqueId;
    @NotBlank
    private String name;
    private String image;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
}
