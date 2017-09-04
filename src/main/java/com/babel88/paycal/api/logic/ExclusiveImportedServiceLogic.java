package com.babel88.paycal.api.logic;

import com.babel88.paycal.models.TTArguments;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 01/09/2017.
 */
public interface ExclusiveImportedServiceLogic extends TTControllerHelper {

    /**
     * Total expenses when the vendor's settlement is immune to withholding tax
     * @return total expenses
     * @param ttArguments contains payment description
     */
    BigDecimal calculateTotalExpenses(TTArguments ttArguments);

    /**
     * Payable to vendor when the vendor's settlement is immune to withholding tax
     * @return amount payable to vendor
     * @param ttArguments contains payment description
     */
    BigDecimal calculateToPayee(TTArguments ttArguments);

    /**
     * Withholding tax when the vendor's settlement is immune to withholding tax
     * @return withholding tax
     * @param ttArguments contains payment description
     */
    BigDecimal calculateWithholdingTax(TTArguments ttArguments);

    /**
     * Withholding vat when the vendor's settlement is immune to withholding tax
     * @return withholding vat
     * @param ttArguments contains payment description
     */
    BigDecimal calculateWithholdingVat(TTArguments ttArguments);
}
