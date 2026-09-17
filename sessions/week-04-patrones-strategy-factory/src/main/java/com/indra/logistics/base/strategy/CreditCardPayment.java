package com.indra.logistics.base.strategy;

import java.math.BigDecimal;


public abstract class CreditCardPayment {

    protected BigDecimal creditCardTariff() {
        return BigDecimal.valueOf(0.03);
    }
}
