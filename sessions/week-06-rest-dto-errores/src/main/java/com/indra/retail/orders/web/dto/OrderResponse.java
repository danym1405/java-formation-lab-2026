package com.indra.retail.orders.web.dto;

import java.time.LocalDate;

public record OrderResponse(
        String orderId,
        String status,
        double totalAmount,
        LocalDate estimatedDelivery) {
}