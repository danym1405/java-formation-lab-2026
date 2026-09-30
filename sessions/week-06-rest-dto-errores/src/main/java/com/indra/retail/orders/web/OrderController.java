package com.indra.retail.orders.web;

import com.indra.retail.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import com.indra.retail.orders.web.dto.CreateOrderRequest;
import com.indra.retail.orders.web.dto.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create( @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse created = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getById(@PathVariable @NotBlank(message = "{orderId.required}") String orderId) {
        return orderService.findById(orderId);
    }
}
