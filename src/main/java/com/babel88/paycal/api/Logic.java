package com.babel88.paycal.api;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface Logic {
    void normal(BigDecimal InvoiceAmount);

    void vatGiven(BigDecimal InvoiceAmount, BigDecimal vat);

    void contractor(BigDecimal invoiceAmount);

    void taxToWithhold(BigDecimal InvoiceAmount);

    void withPrepayment(BigDecimal InvoiceAmount);

    @NotNull
    void telegraphicTransfer();
}
