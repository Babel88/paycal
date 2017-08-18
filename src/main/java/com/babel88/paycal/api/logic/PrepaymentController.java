package com.babel88.paycal.api.logic;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public interface PrepaymentController {
    /**
     * This method takes the total expense and runs all the methods required to calculate
     * prepayment which is returned to the caller
     *
     * Additional value add processes are plugged in in the implementation service to clean
     * up the user input
     *
     * @param totalExpense amount to be prepaid partially
     * @return prepayment amount in BigDecimal
     */
    @Nonnull
    BigDecimal getPrepayment(@Nonnull BigDecimal totalExpense);

    /**
     *
     * @param expenseAmount to be prepaid
     */
    @Nonnull
    Object setExpenseAmount(BigDecimal expenseAmount);
}
