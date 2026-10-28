package com.indra.catalog.products.domain.port;

import java.util.Optional;
import com.indra.catalog.products.domain.model.Product;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);
}
