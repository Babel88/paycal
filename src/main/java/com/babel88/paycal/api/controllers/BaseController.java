package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;

import java.math.BigDecimal;

@Deprecated
public interface BaseController {
    void invoke();

    void updateWithholdingVat(BigDecimal invoiceAmount);

    void updateTotalExpense(BigDecimal invoiceAmount);

    void updateWithholdingTax(BigDecimal invoiceAmount);

    void updatePrepayableAmount();

    void updatePayableToVendor();

//    BigDecimal getInvoiceAmount();

    /**
     * This method returns an instance of an object that implements the DefaultBaseLogic interface
     *
     * @return DefaultBaseLogicModel object
     */
    DefaultBaseLogicModel getDefaultBaseLogicModelInstance();
}
