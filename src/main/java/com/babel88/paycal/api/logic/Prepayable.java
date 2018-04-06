package com.babel88.paycal.api.logic;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

@FunctionalInterface
public interface Prepayable {

    /**
     * This method takes any prepayment and calculates the amount of prepayment.
     * This updates use of depracated implementations in SimplePrepayments and Checked
     * prepayments to apply use of JSR 310
     *
     * @param expense amount
     * @return amount of prepayment
     */
    @NotNull
    @Nonnull
    BigDecimal calculatePrepayment(@Nonnull BigDecimal expense);
}
