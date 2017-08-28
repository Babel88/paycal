package com.babel88.paycal.api.logic.template;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.logic.NullPaymentModelPassedException;

import java.math.BigDecimal;

/**
 * This is a representation of typical logic calculation used in creating the controller
 * <p>
 * Created by edwin.njeru on 25/08/2017.
 */
public interface DefaultBaseLogicModel {

    BigDecimal getAmountBeforeTax(BigDecimal invoiceAmount);

    BigDecimal getTotalExpenses(BigDecimal invoiceAmount);

    BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount);

    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);

    BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount);

    BigDecimal calculateToPrepay(DefaultPaymentModel partiallyCreatedDefaultPaymentModel);

    BigDecimal calculateToPayee(DefaultPaymentModel partiallyCreatedDefaultPaymentModel) throws NullPaymentModelPassedException;
}
