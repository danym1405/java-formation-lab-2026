package com.indra.catalog.products.domain.model;

import java.math.BigDecimal;

public class Product {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final Integer stock;

    public Product(
            String id,
            String name,
            BigDecimal price,
            Integer stock) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio");
        }

        if (price == null ||
                price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero");
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo");
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product withId(String id) {
        return new Product(
                id,
                name,
                price,
                stock
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    
}
