package com.indra.retail.orders.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import com.indra.retail.orders.model.OrderItem;

public record CreateOrderRequest(

        @NotEmpty(message = "{customer.id}")
        String customerId,

        @NotNull(message = "{items.required}")
        @Size(min = 1, message = "{items.required}")
        List<OrderItem> items,

        @NotEmpty(message = "{delivery.address}")
        @Size(min = 10, message = "{delivery.address.size}")
        String deliveryAddress) {
}
