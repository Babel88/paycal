package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;

import java.math.BigDecimal;

public interface BaseController {
    void invoke();

    void updateWithholdingVat(BigDecimal invoiceAmount);

    void updateTotalExpense(BigDecimal invoiceAmount);

    void updateWithholdingTax(BigDecimal invoiceAmount);

    void updatePrepayableAmount();

    void updatePayableToVendor();

    BigDecimal getInvoiceAmount();

    /**
     * This method return an instance of an Object implementing the DefaultPaymentModel interface
     *
     * @return DefaultPaymentModel object
     */
    DefaultPaymentModel getDefaultPaymentModelInstance();

    /**
     * This method returns an instance of an object that implements the DefaultBaseLogic interface
     *
     * @return DefaultBaseLogicModel object
     */
    DefaultBaseLogicModel getDefaultBaseLogicModelInstance();
}
