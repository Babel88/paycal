package com.babel88.paycal.logic;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;

public interface GeneralPayments {
    /**
     * Calculate the invoice amount before adding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    public BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount);
}
