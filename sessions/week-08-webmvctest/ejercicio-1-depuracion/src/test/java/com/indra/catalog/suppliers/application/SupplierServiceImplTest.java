package com.indra.catalog.suppliers.application;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.indra.catalog.suppliers.domain.SupplierRepository;
import com.indra.catalog.suppliers.domain.Supplier;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository repository;

    @InjectMocks
    private SupplierServiceImpl service;

    @Test
    void shouldNormalizeSupplierBeforeSaving() {

        Supplier supplier = new Supplier("  acme ltda ", "900.123.456-7", "compras@acme.co");

        when(repository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Supplier saved = service.create(supplier);

        assertEquals("ACME LTDA", saved.getName());
        assertEquals("900123456-7", saved.getTaxId());
    }

}
