package com.babel88.paycal.api.logic;

import com.babel88.paycal.models.TTArguments;

import java.math.BigDecimal;

public interface TTControllerHelper {

    /**
     * Helper method to refactor calculation of total expenses, withholding taxes and withholding vat
     *
     * @param ttArguments contains payment description
     * @return amount before taxes
     */
    BigDecimal helperCalculateAmountBeforeTax(TTArguments ttArguments);

    /**
     * Helper method to refactor calculation of total expenses, using ttArguments,and the calculated amount
     * before taxes
     *
     * @param ttArguments     contains payment description
     * @param amountBeforeTax calculates in the calling method
     * @return total expenses returned to the caller
     */
    public BigDecimal helperCalculateTotalExpenses(TTArguments ttArguments, BigDecimal amountBeforeTax);
}
