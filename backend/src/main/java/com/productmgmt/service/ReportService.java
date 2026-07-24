package com.productmgmt.service;
import com.opencsv.CSVWriter;
import com.productmgmt.entity.Product;
import com.productmgmt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.OutputStreamWriter;
import java.util.List;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ProductRepository productRepository;

    public StreamingResponseBody generateCsvReport() {
        return outputStream -> {
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
                writer.writeNext(new String[]{"ID", "Name", "Price", "Category"});
                List<Product> products = productRepository.findAll();
                for (Product p : products) {
                    writer.writeNext(new String[]{
                            String.valueOf(p.getId()), p.getName(), p.getPrice().toString(), p.getCategory().getName()
                    });
                }
            }
        };
    }

    // public StreamingResponseBody generateXlsxReport() {
    //     return outputStream -> {
    //         try (Workbook workbook = new XSSFWorkbook()) {
    //             Sheet sheet = workbook.createSheet("Products");
    //             Row header = sheet.createRow(0);
    //             header.createCell(0).setCellValue("ID");
    //             header.createCell(1).setCellValue("Name");
    //             header.createCell(2).setCellValue("Price");
    //             header.createCell(3).setCellValue("Category");

    //             List<Product> products = productRepository.findAll();
    //             int rowIdx = 1;
    //             for (Product p : products) {
    //                 Row row = sheet.createRow(rowIdx++);
    //                 row.createCell(0).setCellValue(p.getId());
    //                 row.createCell(1).setCellValue(p.getName());
    //                 row.createCell(2).setCellValue(p.getPrice().doubleValue());
    //                 row.createCell(3).setCellValue(p.getCategory().getName());
    //             }
    //             workbook.write(outputStream);
    //         }
    //     };
    // }
    public StreamingResponseBody generateXlsxReport() {
    return outputStream -> {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Price");
            header.createCell(3).setCellValue("Category");
            List<Product> products = productRepository.findAll();
            int rowIdx = 1;
            for (Product p : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getPrice().doubleValue());
                row.createCell(3).setCellValue(p.getCategory().getName());
            }
            workbook.write(outputStream);
            workbook.dispose();
        }
    };
}
}
