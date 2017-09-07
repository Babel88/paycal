package com.ghacupha.paycal.api.logic;

import com.ghacupha.paycal.logic.GeneralPayments;

import java.math.BigDecimal;

public interface TypicalPayments extends GeneralPayments {
    /**
     * returns the invoice amount
     *
     * @return invoice amount requested
     */
    BigDecimal getInvoiceAmount();

    /**
     * Sets the invoice amount for this object
     *
     * @param invoiceAmount
     * @return
     */
    GeneralPayments setInvoiceAmount(BigDecimal invoiceAmount);

    /**
     * Calculate the withholding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount);

    /**
     * Calculates the total amount of expense for a typical payment
     *
     * @param invoiceAmount
     * @return total expenditure
     */
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);

    /**
     * Calculates the amount payable to the payee
     *
     * @return amount payable to payee
     */
    BigDecimal calculateToPayee(BigDecimal invoiceAmount);

    /**
     * Calculate the invoice amount before adding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    @Override
    BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount);
}
