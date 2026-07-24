package com.productmgmt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmgmt.dto.AuthRequest;
import com.productmgmt.dto.CategoryDTO;
import com.productmgmt.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
class ProductMgmtApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private Long categoryId;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setupAuth() throws Exception {
        AuthRequest req = new AuthRequest();
        req.setEmail("test" + System.currentTimeMillis() + "@example.com");
        req.setPassword("password");
        
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn();
                
        String response = result.getResponse().getContentAsString();
        token = JsonPath.read(response, "$.token");
    }

    @Test
    void testCategoryAndProductCRUD() throws Exception {
        // Create Category
        CategoryDTO catReq = CategoryDTO.builder().name("Test Cat " + System.currentTimeMillis()).build();
        MvcResult catResult = mockMvc.perform(post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catReq)))
                .andExpect(status().isCreated())
                .andReturn();
        
        categoryId = ((Number) JsonPath.read(catResult.getResponse().getContentAsString(), "$.id")).longValue();

        // Create Product
        ProductDTO prodReq = ProductDTO.builder()
                .name("Test Prod")
                .price(new BigDecimal("99.99"))
                .categoryId(categoryId)
                .build();
                
        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prodReq)))
                .andExpect(status().isCreated());

        // Get Products Paginated
        mockMvc.perform(get("/api/products?page=0&size=10")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
