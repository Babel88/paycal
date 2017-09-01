package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.InclusiveImportedService;
import com.babel88.paycal.controllers.base.TTController;

import java.math.BigDecimal;

/**
 * Logic for telegraphic transfers when the vendor is subject to withholding tax
 * Created by edwin.njeru on 01/09/2017.
 */
public class InclusiveImportedServiceImpl implements InclusiveImportedService {

    private TTController ttController;

    public InclusiveImportedServiceImpl(TTController ttController) {

        this.ttController = ttController;
    }

    /**
     * Calculate total expenses
     *
     * @return total expenses when the withholding tax is inclusive
     */
    @Override
    public BigDecimal calculateTotalExpenses() {
        return null;
    }

    /**
     * @return amount payable to payee when withholding tax is inclusive
     */
    @Override
    public BigDecimal calculateToPayee() {
        return null;
    }

    /**
     * @return withholding tax inclusive in invoice amount
     */
    @Override
    public BigDecimal calculateWithholdingTax() {
        return null;
    }

    /**
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat() {
        return null;
    }
}
