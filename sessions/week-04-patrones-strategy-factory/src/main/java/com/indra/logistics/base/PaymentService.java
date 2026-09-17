package com.indra.logistics.base;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.indra.logistics.base.factory.PaymentStrategyFactory;
import com.indra.logistics.base.strategy.PaymentStrategy;

/**
 * BASE: toda la lógica de comisión vive en un if-else que crece con cada método de pago nuevo.
 * Agregar un método de pago implica editar esta clase y arriesgar los demás casos.
 */
@Service 
public class PaymentService {

    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentService(PaymentStrategyFactory paymentStrategyFactory) {
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    public PaymentResult process(PaymentRequest request) {
        
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(request.method());
        BigDecimal amount = request.amount();
        BigDecimal fee = strategy.calculateFee(amount);
        BigDecimal total = amount.add(fee);
        String message = strategy.confirmationMessage();

        return new PaymentResult(request.method(), amount, fee, total, message);
    }

}