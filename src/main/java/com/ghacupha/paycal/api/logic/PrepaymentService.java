package com.ghacupha.paycal.api.logic;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

@FunctionalInterface
public interface PrepaymentService {

    /**
     * This method takes an expense amount and calculates prepayment
     *
     * @param total expense amount
     * @return prepayment amount
     */
    @Nonnull
    BigDecimal prepay(@Nonnull BigDecimal total);
}
