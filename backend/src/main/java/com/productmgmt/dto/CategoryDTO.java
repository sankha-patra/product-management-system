package com.productmgmt.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private UUID uniqueId;
    @NotBlank
    private String name;
    private LocalDateTime createdAt;
}
