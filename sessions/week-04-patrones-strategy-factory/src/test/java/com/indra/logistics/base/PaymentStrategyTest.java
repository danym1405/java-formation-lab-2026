package com.indra.logistics.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.indra.logistics.base.factory.PaymentStrategyFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.indra.logistics.base.strategy.PaymentStrategy;
import com.indra.logistics.base.strategy.VisaPayment;
import com.indra.logistics.base.strategy.AmexPayment;
import com.indra.logistics.base.strategy.BankTransferPayment;
import com.indra.logistics.base.strategy.DebitCardPayment;
import com.indra.logistics.base.strategy.MastercardPayment;

@SpringBootTest 
public class PaymentStrategyTest {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;

    @Test
    void shouldReturnPaypalStrategy() {

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy("PAYPAL");
        assertEquals("PAYPAL", strategy.methodCode());
    }

    @Test
    void shouldReturnMastercardStrategy() {

        PaymentStrategy strategy = paymentStrategyFactory.getStrategy("MASTERCARD");
        assertEquals("MASTERCARD", strategy.methodCode());
    }

    @Test
    void shouldThrowExceptionForUnknownMethod() {

        assertThrows(
                UnknownPaymentMethodException.class,
                () -> paymentStrategyFactory.getStrategy("UNKNOWN")
        );
    }

    @Test
    void shouldReturnAmexMethodCode() {

        AmexPayment amex = new AmexPayment();

        assertEquals("AMEX", amex.methodCode());
    }

    @Test
    void shouldReturnDebitCardMethodCode() {

        DebitCardPayment debitCard = new DebitCardPayment();

        assertEquals("DEBIT_CARD", debitCard.methodCode());
    }

    @Test 
    void shouldReturnMastercardMethodCode() {

        MastercardPayment mastercard = new MastercardPayment();

        assertEquals("MASTERCARD", mastercard.methodCode());
    }

    @Test
    void shouldNotChargeFeeWhenAmountIsLessThan100() {

        VisaPayment strategy = new VisaPayment();

        BigDecimal fee = strategy.calculateFee(BigDecimal.valueOf(99));

        assertEquals(BigDecimal.ZERO, fee);
    }

    @Test
    void shouldChargeFeeWhenAmountIs100OrMore() {

        VisaPayment strategy = new VisaPayment();

        BigDecimal fee = strategy.calculateFee(BigDecimal.valueOf(100));

        assertEquals(new BigDecimal("3.50"), fee);
    }

    @Test
    void shouldReturnMessageWithoutFee() {

        AmexPayment strategy = new AmexPayment();

        strategy.calculateFee(BigDecimal.valueOf(99));

        assertEquals(
            "Pago con tarjeta American Express procesado, monto no aplica comisión bancaria.",
            strategy.confirmationMessage()
        );
    }

    @Test
    void shouldReturnMessageWithFee() {

        AmexPayment strategy = new AmexPayment();

        strategy.calculateFee(BigDecimal.valueOf(100));

        assertEquals(
            "Pago con tarjeta American Express procesado, se aplica comisión bancaria.",
            strategy.confirmationMessage()
        );
    }

     @Test
    void shouldReturnMessageWithFeeBankTransfer() {

        BankTransferPayment strategy = new BankTransferPayment();

        strategy.calculateFee(BigDecimal.valueOf(100));

        assertEquals(
            "Pago por transferencia bancaria registrado, se aplica comisión bancaria.",
            strategy.confirmationMessage()
        );
    }

}
