package com.babel88.paycal.api.logic;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public interface DefaultLogic extends Serializable {
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);

    BigDecimal calculateToPayee(BigDecimal invoiceAmount);

    BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount);

    BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount);
}
