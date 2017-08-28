package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public interface DefaultControllers {

    void runCalculation();

    void updateTotalExpense();

    void updateToPayee();

    void updateWithholdingTax();

    void updateWithholdingVat();

    void updateToPrepay();

    DefaultPaymentModel getPaymentModel();
}
