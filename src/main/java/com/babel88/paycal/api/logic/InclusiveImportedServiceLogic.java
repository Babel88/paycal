package com.babel88.paycal.api.logic;

import com.babel88.paycal.models.TTArguments;

import java.math.BigDecimal;

/**
 * This object holds logic for payment to vendor whose settlement is subject to withholding
 * tax
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public interface InclusiveImportedServiceLogic extends TTControllerHelper{

    /**
     * Calculate total expenses
     *
     * @return total expenses when the withholding tax is inclusive
     *
     * @param ttArguments contains payment description
     */
    BigDecimal calculateTotalExpenses(TTArguments ttArguments);

    /**
     * Calculate amount to payee
     *
     * @return amount payable to payee when withholding tax is inclusive
     *
     * @param ttArguments contains payment description
     */
    BigDecimal calculateToPayee(TTArguments ttArguments);

    /**
     * Calculate withholding tax
     *
     * @return withholding tax inclusive in invoice amount
     *
     * @param ttArguments contains payment description
     */
    BigDecimal calculateWithholdingTax(TTArguments ttArguments);

    /**
     *  Calculate withholding vat
     *
     * @return withholding vat
     *
     * @param ttArguments contains payment description
     */
    BigDecimal calculateWithholdingVat(TTArguments ttArguments);
}
