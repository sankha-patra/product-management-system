package com.productmgmt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProductMgmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMgmtApplication.class, args);
    }
}
