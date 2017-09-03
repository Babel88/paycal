package com.babel88.paycal.api.logic;

import com.babel88.paycal.api.InitializableLogic;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 01/09/2017.
 */
public interface ExclusiveImportedServiceLogic extends InitializableLogic {

    /**
     *
     * @return total expenses
     */
    BigDecimal calculateTotalExpenses();

    /**
     *
     * @return amount payable to vendor
     */
    BigDecimal calculateToPayee();

    /**
     *
     * @return withholding tax
     */
    BigDecimal calculateWithholdingTax();

    /**
     *
     * @return withholding vat
     */
    BigDecimal calculateWithholdingVat();
}
