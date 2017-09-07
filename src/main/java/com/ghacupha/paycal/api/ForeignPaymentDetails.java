package com.ghacupha.paycal.api;

import java.math.BigDecimal;

public interface ForeignPaymentDetails {

    /**
     * Vat rate for the payment given as whole numbers
     *
     * @return vat rate as whole
     */
    double vatRate();

    /**
     * The withholding tax rate for foreign payments DTA respective
     *
     * @return withholding tax rate
     */
    double withHoldingTaxRate();

    /**
     * The invoice amount as stated in the invoice request
     *
     * @return invoice amount
     */
    BigDecimal invoiceAmount();

    /**
     * The value returned by this method flags whether or not the invoice price
     * is inclusive of withholding tax
     *
     * @return Boolean : whether the withholding tax is exclusive in the invoice
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
