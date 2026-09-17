package com.indra.logistics.base.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component 
public class AmexPayment extends CreditCardPayment implements PaymentStrategy {

    private String message;

    @Override
    public String methodCode() {
        return "AMEX";
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(100)) < 0) {
            message = "Pago con tarjeta American Express procesado, monto no aplica comisión bancaria.";
            return BigDecimal.ZERO;
        } else {
            BigDecimal tariff = creditCardTariff().add(BigDecimal.ZERO);
            BigDecimal fee = amount.multiply(tariff).setScale(2, RoundingMode.HALF_UP);
            message = "Pago con tarjeta American Express procesado, se aplica comisión bancaria.";
            return fee;
        }
    }

    @Override
    public String confirmationMessage() {
        return message;
    }

}
