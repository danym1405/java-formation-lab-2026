package com.indra.logistics.base.strategy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class PaypalPayment implements PaymentStrategy {

    private String message;

    @Override
    public String methodCode() {
        return "PAYPAL";
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.02)).setScale(2, RoundingMode.HALF_UP);
        message = "Pago con PayPal procesado, se aplica comisión bancaria.";
        return fee;
    }

    @Override
    public String confirmationMessage() {
        return message;
    }

}
