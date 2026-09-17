package com.indra.logistics.base.strategy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPayment implements PaymentStrategy {

    private String message;

    @Override
    public String methodCode() {
        return "BANK_TRANSFER";
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.025)).setScale(2, RoundingMode.HALF_UP);
        message = "Pago por transferencia bancaria registrado, se aplica comisión bancaria.";
        return fee;
    }

    @Override
    public String confirmationMessage() {
        return message;
    }

}
