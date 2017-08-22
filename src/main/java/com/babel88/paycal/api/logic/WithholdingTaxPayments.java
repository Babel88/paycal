package com.babel88.paycal.api.logic;


import java.math.BigDecimal;

public interface WithholdingTaxPayments {
    /**
     * calculates the amount before taxes for a normal payment with
     * withholding taxes
     *
     * @param invoiceAmount the invoice amount requested for in the invoice
     * @return amountB4Vat
     */
    BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount);

    /**
     * Calculates the withholding vat
     *
     * @param invoiceAmount
     * @return withholding vat in big decimal
     */
    BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount);

    /**
     * Calculating withholding tax
     *
     * @param invoiceAmount
     * @return withholding tax chargeable
     */
    BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount);

    /**
     * Calculating total expense
     *
     * @param invoiceAmount
     * @return total expense to the ledger
     */
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);

    /**
     * Caclulate amount payable to vendor
     *
     * @param invoiceAmount
     * @return amount payable to vendor
     */
    BigDecimal calculateAmountPayable(BigDecimal invoiceAmount);
}
