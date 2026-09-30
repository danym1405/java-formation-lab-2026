package com.indra.retail.orders.service;

import com.indra.retail.orders.model.Order;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.indra.retail.orders.web.dto.CreateOrderRequest;
import com.indra.retail.orders.web.dto.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public OrderResponse create(CreateOrderRequest request) {

        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setItems(request.items());
        order.setDeliveryAddress(request.deliveryAddress());

        orders.put(order.getId(), order);

        return toResponse(order);
    }

    public OrderResponse findById(String orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getEstimatedDelivery()
        );
    }
}
