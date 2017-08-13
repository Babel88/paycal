package com.paycal.api;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface Logic {
    void normal(BigDecimal InvoiceAmount);

    void vatGiven(BigDecimal InvoiceAmount, double vat);

    void contractor(BigDecimal invoiceAmount);

    void taxToWithhold(BigDecimal InvoiceAmount);

    void withPrepayment(BigDecimal InvoiceAmount);

    void tt();
}
