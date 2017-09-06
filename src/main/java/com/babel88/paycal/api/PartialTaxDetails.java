package com.babel88.paycal.api;

import java.math.BigDecimal;

/**
 * Fetches from consoles details required for partialTaxPayments
 */
public interface PartialTaxDetails {

    /**
     * Fetches the vat amount from console
     *
     * @return vat amount as double
     */
    double vatAmount();

    /**
     * Fetches the invoice amount from console
     *
     * @return the invoice amount
     */
    BigDecimal invoiceAmount();

    /**
     * Queries the user whether they would like to repeat the computation routine
     *
     * @return whether or not to repeat
     */
    boolean doAgain();
}
