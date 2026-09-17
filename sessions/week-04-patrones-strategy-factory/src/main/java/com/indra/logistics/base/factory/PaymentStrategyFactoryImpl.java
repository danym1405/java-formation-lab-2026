package com.indra.logistics.base.factory;

import java.util.function.Function;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.indra.logistics.base.strategy.PaymentStrategy;
import com.indra.logistics.base.UnknownPaymentMethodException;

/** Factory */
@Component
public class PaymentStrategyFactoryImpl implements PaymentStrategyFactory {

   private final Map<String, PaymentStrategy> strategyMap;

   public PaymentStrategyFactoryImpl(List<PaymentStrategy> strategies) {

       this.strategyMap = strategies.stream()
               .collect(Collectors.toMap(strategy -> strategy.methodCode(), Function.identity()));
   }

    @Override
    public PaymentStrategy getStrategy(String methodCode) {

        PaymentStrategy strategy =
                strategyMap.get(methodCode);

        if (strategy == null) {
            throw new UnknownPaymentMethodException(methodCode);
        }

        return strategy;
    }
}
