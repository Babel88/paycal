package com.babel88.paycal.api.logic;

import com.babel88.paycal.models.TTArguments;

import java.math.BigDecimal;

/**
 * This object holds logic for payment to vendor whose settlement is subject to withholding
 * tax
 * <p>
 * Created by edwin.njeru on 01/09/2017.
 */
public interface InclusiveImportedServiceLogic extends TTControllerHelper {

    /**
     * Calculate total expenses
     *
     * @param ttArguments contains payment description
     * @return total expenses when the withholding tax is inclusive
     */
    BigDecimal calculateTotalExpenses(TTArguments ttArguments);

    /**
     * Calculate amount to payee
     *
     * @param ttArguments contains payment description
     * @return amount payable to payee when withholding tax is inclusive
     */
    BigDecimal calculateToPayee(TTArguments ttArguments);

    /**
     * Calculate withholding tax
     *
     * @param ttArguments contains payment description
     * @return withholding tax inclusive in invoice amount
     */
    BigDecimal calculateWithholdingTax(TTArguments ttArguments);

    /**
     * Calculate withholding vat
     *
     * @param ttArguments contains payment description
     * @return withholding vat
     */
    BigDecimal calculateWithholdingVat(TTArguments ttArguments);
}
