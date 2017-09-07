package com.ghacupha.paycal.logic;

import java.math.BigDecimal;

public interface GeneralPayments {
    /**
     * Calculate the invoice amount before adding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount);
}
