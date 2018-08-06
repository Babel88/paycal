package com.babel88.paycal.api.view;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface PaymentModelViewInterface {

    /**
     * Displays the various computed amounts on display
     *
     * @param total          totalExpense amount
     * @param vatWithheld    withholding vat amount
     * @param withholdingTax withholding Tax chargeable
     * @param toPrepay       the amount that should be prepaid
     * @param toPayee        amount to be given to the payee/vendor
     */
    @Deprecated
    void displayResults(BigDecimal total, BigDecimal vatWithheld, BigDecimal withholdingTax, BigDecimal toPrepay, BigDecimal toPayee);
}
