package com.paycal.api;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface Logic {
    void normal(double InvoiceAmount, PayCalView payCalView);

    void vatGiven(double InvoiceAmount, double vat);

    void contractor(double invoiceAmount);

    void taxToWithhold(double InvoiceAmount);

    void withPrepayment(double InvoiceAmount);

    void tt();
}
