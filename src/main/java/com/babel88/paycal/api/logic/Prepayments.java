package com.babel88.paycal.api.logic;

import java.math.BigDecimal;

public interface Prepayments {

    /**
     * CheckedPrepayment module: calculatePrepayment
     * We are going to take take the total amount and apportion it
     * to the period for prepayment and hence get the amount to expense
     * <p>
     * The invoice details are to be injected into the constructor of the implementing object
     *
     * @param invoiceAmount
     * @return
     */
    //TODO to enhance prepayment algorithm
    BigDecimal calculatePrepayment(BigDecimal invoiceAmount);

    /**
     * Calculates the amount before taxes are added
     *
     * @param invoiceAmount
     * @return
     */
    BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount);

    /**
     * calculates the withholding vat
     *
     * @param amountB4Vat
     * @return
     */
    BigDecimal calculateWithholdingVat(BigDecimal amountB4Vat);

    /**
     * calculates the total amount expendable
     *
     * @param invoiceAmount
     * @param toPrepay
     * @return
     */
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount, BigDecimal toPrepay);

    /**
     * calculates the amount prepayable
     *
     * @param toPrepay
     * @param withHoldingVat
     * @return
     */
    BigDecimal calculateAmountPayable(BigDecimal toPrepay, BigDecimal withHoldingVat);
    
}
