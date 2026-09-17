package com.indra.logistics.base.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class VisaPayment extends CreditCardPayment implements PaymentStrategy {

    private String message;

    @Override
    public String methodCode() {
        return "VISA";
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(100)) < 0) {
                message = "Pago con tarjeta de crédito Visa procesado, monto no aplica comisión bancaria.";
                return BigDecimal.ZERO;
            } else {
                BigDecimal tariff = creditCardTariff().add(BigDecimal.valueOf(0.005));
                BigDecimal fee = amount.multiply(tariff).setScale(2, RoundingMode.HALF_UP);
                message = "Pago con tarjeta de crédito Visa procesado, se aplica comisión bancaria.";
                return fee;
            }
    }

    @Override
    public String confirmationMessage() {
        return message;
    }
}
