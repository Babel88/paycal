package com.babel88.paycal.api.logic;

import java.math.BigDecimal;

/**
 * This object holds logic for payment to vendor whose settlement is subject to withholding
 * tax
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public interface InclusiveImportedServiceLogic {

    /**
     * Calculate total expenses
     *
     * @return total expenses when the withholding tax is inclusive
     */
    BigDecimal calculateTotalExpenses();

    /**
     *
     * @return amount payable to payee when withholding tax is inclusive
     */
    BigDecimal calculateToPayee();

    /**
     *
     * @return withholding tax inclusive in invoice amount
     */
    BigDecimal calculateWithholdingTax();

    /**
     *
     * @return withholding vat
     */
    BigDecimal calculateWithholdingVat();
}
