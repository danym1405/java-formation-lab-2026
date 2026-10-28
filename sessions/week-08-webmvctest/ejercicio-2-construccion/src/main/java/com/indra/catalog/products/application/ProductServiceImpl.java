package com.indra.catalog.products.application;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.indra.catalog.products.domain.exception.ProductNotFoundException;
import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.domain.port.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final AtomicLong sequence = new AtomicLong();

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product product) {

        String id = "PRD-%04d".formatted(sequence.incrementAndGet());
        Product created = product.withId(id);

        return repository.save(created);
    }

    @Override
    public Product findById(String id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id));
    }

    @Override
    public void delete(String id) {
        
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        repository.deleteById(id);
    }

}
