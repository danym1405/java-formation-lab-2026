package com.indra.catalog.products.web.dto;

public record ProductResponse(

    String id,
    String name,
    java.math.BigDecimal price,
    Integer stock) {

}
