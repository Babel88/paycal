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

    /**
     * The value returned by this method flags whether or not the invoice price
     * is inclusive of withholding tax
     *
     * @return
     */
    Boolean exclusiveOfWithholdingTax();

    /**
     * This method creates a variable that tells the algorithm whether or not the user
     * would like to repeat a certain computation
     *
     * @return whether or not we should recompute
     */
    boolean doAgain();
}
