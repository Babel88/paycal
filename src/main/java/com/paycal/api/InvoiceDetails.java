package com.paycal.api;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface InvoiceDetails {
    double invoiceAmount();

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
