package com.babel88.paycal.api.controllers;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface PrepaymentController {
    /**
     * This method takes the total expense and runs all the methods required to calculate
     * prepayment which is returned to the caller
     * <p>
     * Additional value add processes are plugged in in the implementation service to clean
     * up the user input
     *
     * @param totalExpense amount to be prepaid partially
     * @return prepayment amount in BigDecimal
     */
    @NotNull
    @Nonnull
    BigDecimal getPrepayment(@Nonnull BigDecimal totalExpense);

    /**
     * @param expenseAmount to be prepaid
     */
    @NotNull
    @Nonnull
    Object setExpenseAmount(BigDecimal expenseAmount);
}
