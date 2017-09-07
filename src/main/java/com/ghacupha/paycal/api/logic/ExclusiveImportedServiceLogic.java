package com.ghacupha.paycal.api.logic;

import com.ghacupha.paycal.models.TTArguments;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 01/09/2017.
 */
public interface ExclusiveImportedServiceLogic extends TTControllerHelper {

    /**
     * Total expenses when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return total expenses
     */
    BigDecimal calculateTotalExpenses(TTArguments ttArguments);

    /**
     * Payable to vendor when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return amount payable to vendor
     */
    BigDecimal calculateToPayee(TTArguments ttArguments);

    /**
     * Withholding tax when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return withholding tax
     */
    BigDecimal calculateWithholdingTax(TTArguments ttArguments);

    /**
     * Withholding vat when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return withholding vat
     */
    BigDecimal calculateWithholdingVat(TTArguments ttArguments);
}
