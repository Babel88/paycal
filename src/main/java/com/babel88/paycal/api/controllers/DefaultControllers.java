package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public interface DefaultControllers {

    void runCalculation();

    void updateTotalExpense();

    void updateToPayee();

    void updateWithholdingTax();

    void updateWithholdingVat();

    @NotNull void updateToPrepay();

    DefaultPaymentModel getPaymentModel();
}
