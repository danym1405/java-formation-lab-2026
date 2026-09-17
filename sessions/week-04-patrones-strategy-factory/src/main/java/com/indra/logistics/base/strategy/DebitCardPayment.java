package com.indra.logistics.base.strategy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class DebitCardPayment implements PaymentStrategy {

    private String message;

    @Override
    public String methodCode() {
        return "DEBIT_CARD";
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.04)).setScale(2, RoundingMode.HALF_UP);
        message = "Pago con tarjeta de débito procesado, se aplica comisión bancaria.";
        return fee;
    }

    @Override
    public String confirmationMessage() {
        return message;
    }

}
