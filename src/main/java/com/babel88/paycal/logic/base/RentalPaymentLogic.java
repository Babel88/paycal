package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentLogic implements DefaultLogic {
    private static final Logger log = LoggerFactory.getLogger(RentalPaymentLogic.class);
    private static DefaultLogic instance = new RentalPaymentLogic();

    public RentalPaymentLogic() {
    }

    public static DefaultLogic getInstance() {
        return instance;
    }

    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {
        return null;
    }

    @Override
    public BigDecimal calculateToPayee(BigDecimal invoiceAmount) {
        return null;
    }

    @Override
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {
        return null;
    }

    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {
        return null;
    }
}
