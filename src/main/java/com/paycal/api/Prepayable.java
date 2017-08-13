package com.paycal.api;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface Prepayable {

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

    BigDecimal calculateAmountB4Vat(BigDecimal invoiceAmount);

    BigDecimal calculateWithholdingVat(BigDecimal amountB4Vat);

    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount, BigDecimal toPrepay);

    BigDecimal calculateAmountPayable(BigDecimal toPrepay, BigDecimal withHoldingVat);
}
