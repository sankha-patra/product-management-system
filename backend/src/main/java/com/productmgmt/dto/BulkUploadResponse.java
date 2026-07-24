package com.productmgmt.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulkUploadResponse {
    private String message;
    private int totalRecords;
}
