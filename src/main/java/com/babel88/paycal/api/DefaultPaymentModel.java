package com.babel88.paycal.api;

import java.math.BigDecimal;

/**
 * This is a representation of a typical payment model which is used by a typical controller
 * to sho payment calculations in a typical viewer object.
 * The same is implemented by the PaymentModel but this interface is used to create the BaseControllerTemplate
 * is a decoupled fashion
 * <p>
 * Created by edwin.njeru on 25/08/2017.
 */
public interface DefaultPaymentModel<T> {
    T setWithHoldingVat(BigDecimal invoiceAmount);

    T setTotalExpense(BigDecimal totalExpense);

    T setWithholdingTax(BigDecimal withholdingTax);

    T setToPrepay(BigDecimal toPrepay);

    T setToPayee(BigDecimal toPayee);

    /**
     *
     * @return amount of total expense
     */
    BigDecimal getTotalExpense();

    BigDecimal getToPrepay();

    BigDecimal getToPayee();

    BigDecimal getWithholdingTax();
}
