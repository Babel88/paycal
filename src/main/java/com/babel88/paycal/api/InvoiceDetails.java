package com.babel88.paycal.api;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface InvoiceDetails {
    BigDecimal invoiceAmount();

    double vatAmount();

    double withHoldingTaxAmount();

    double vatRate();

    double withHoldingTaxRate();

    double withHoldingVatRate();

    String payeeName();

    String getInvoiceStartDate();

    String getInvoiceRefDate();

    String getInvoiceEndDate();
}
