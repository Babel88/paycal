package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public interface DefaultControllers {

    void runCalculation();

    /**
     * Updates the total expense figure in the payment model
     */
    void updateTotalExpense();

    /**
     * Updates the amount payable to payee in the payment model
     */
    void updateToPayee();

    /**
     * Updates the withholding tax in the payment model
     */
    void updateWithholdingTax();

    /**
     * Updates the withholding vat in the payment model
     */
    void updateWithholdingVat();

    /**
     * Updates the amount to prepay in the payment model
     */
    @NotNull
    void updateToPrepay();

    DefaultPaymentModel getPaymentModel();
}
