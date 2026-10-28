package com.indra.catalog.products.web;

import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.web.dto.ProductResponse;

public class ProductWebMapper {

    private ProductWebMapper() {
    }

    public static ProductResponse toResponse(Product product) {

        return new ProductResponse(product.getId(),product.getName(),product.getPrice(),product.getStock());
    }

}
