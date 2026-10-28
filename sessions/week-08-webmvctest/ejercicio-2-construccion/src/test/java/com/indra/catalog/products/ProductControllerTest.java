package com.indra.catalog.products;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import com.indra.catalog.products.application.ProductService;
import com.indra.catalog.products.domain.exception.ProductNotFoundException;
import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.infrastructure.security.SecurityConfig;
import com.indra.catalog.products.web.ProductController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest(ProductController.class) 
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnProductWhenProductExists() throws Exception {

        Product product = new Product( "PRD-0001", "Laptop",new BigDecimal("3500000.00"),10);

        when(productService.findById("PRD-0001")).thenReturn(product);

        mockMvc.perform(get("/api/products/PRD-0001")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("PRD-0001"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(3500000.00))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {

        when(productService.findById("PRD-9999")).thenThrow(new ProductNotFoundException("PRD-9999"));

        mockMvc.perform(get("/api/products/PRD-9999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.code")
                        .value("PRODUCT_NOT_FOUND"));
    }

    @Test
    void shouldCreateProductAndReturnLocationHeader()throws Exception {

        Product createdProduct = new Product("PRD-0001","Laptop",new BigDecimal("3500000.00"),10);

        when(productService.create(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products").with(httpBasic("editor","editor123")).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Laptop",
                                    "price":3500000.00,
                                    "stock":10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                                        "Location",
                                        "http://localhost/api/products/PRD-0001"))
                .andExpect(jsonPath("$.id").value("PRD-0001"))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void shouldReturn400WhenNameIsBlank()throws Exception {

        mockMvc.perform(post("/api/products").with(httpBasic("editor","editor123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"",
                                    "price":3500000,
                                    "stock":10
                                }
                                """))
                        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                        .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("name")));
    }

    @Test
    void shouldReturn204WhenProductIsDeleted() throws Exception {

        mockMvc.perform(delete("/api/products/PRD-0001").with(httpBasic("admin","admin123"))).andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn401WhenAnonymousUserCreatesProduct() throws Exception {

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Laptop",
                                    "price":3500000.00,
                                    "stock":10
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn403WhenEditorDeletesProduct()
            throws Exception {

        mockMvc.perform(delete("/api/products/PRD-0001").with(httpBasic("editor","editor123")))
                .andExpect(status().isForbidden());
    }

}
