package com.babel88.paycal.api.logic;

import java.math.BigDecimal;

public interface PartialTaxPaymentLogic {
    /**
     * Calculates withholding vat given the vat amount
     *
     * @param vatAmount provided at runtime
     * @return withholding vat
     */
    BigDecimal calculateWithholdingVat(BigDecimal vatAmount);

    /**
     * Calculates the total expense given the invoice amount
     *
     * @param invoiceAmount proviced at runtime by user
     * @return total expense amount
     */
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);

    /**
     * Calculates the amount payable to vendor give the total expense and the
     * amount of withholding vat.
     *
     * @param totalExpense calculated at runtime by the  algorithm
     * @param withholdingVatAmount calculated at runtime by the  algorithm
     * @return amount payable to vendor
     */
    BigDecimal calculateAmountPayableToVendor(BigDecimal totalExpense, BigDecimal withholdingVatAmount);

    /**
     * Calculates the amount of withholding tax
     *
     * @return withholding tax. TYpically the amount here is zero
     */
    BigDecimal calculateWithholdingTax();

}
