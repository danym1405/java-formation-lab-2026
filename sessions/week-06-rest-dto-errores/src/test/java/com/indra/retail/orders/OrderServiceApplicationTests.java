package com.indra.retail.orders;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.indra.retail.orders.service.OrderNotFoundException;
import com.indra.retail.orders.service.OrderService;
import com.indra.retail.orders.web.dto.CreateOrderRequest;
import com.indra.retail.orders.web.dto.OrderResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {

        OrderResponse response = new OrderResponse(
                "123",
                "CREATED",
                100.0,
                LocalDate.now().plusDays(5)
        );

        when(orderService.create(any(CreateOrderRequest.class)))
                .thenReturn(response);

        String body = """
            {
              "customerId":"CUST001",
              "items":[
                {
                  "sku":"SKU001",
                  "quantity":2,
                  "unitPrice":50
                }
              ],
              "deliveryAddress":"Calle 123 #45-67"
            }
            """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


   @Test
    void shouldReturnValidationErrors() throws Exception {

    String body = """
        {
          "customerId":"",
          "items":[],
          "deliveryAddress":"abc"
        }
        """;

    mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {

    when(orderService.findById(anyString()))
            .thenThrow(new OrderNotFoundException("123"));

    mockMvc.perform(get("/api/orders/123"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void shouldReturnBadRequestForInvalidOrderId() throws Exception {

        mockMvc.perform(get("/api/orders/ "))
                .andExpect(status().isBadRequest());
    }
}
