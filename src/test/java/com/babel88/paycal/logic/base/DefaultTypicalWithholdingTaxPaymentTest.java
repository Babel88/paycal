package com.babel88.paycal.logic.base;

import com.babel88.paycal.config.PaymentParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class DefaultTypicalWithholdingTaxPaymentTest {

    private DefaultTypicalWithholdingTaxPayment withholdingTaxLogic;
    private BigDecimal invoiceAMount = BigDecimal.valueOf(116000.00);

    @Before
    public void setUp() throws Exception {

        withholdingTaxLogic = new DefaultTypicalWithholdingTaxPayment();
        withholdingTaxLogic.setPaymentParameters(new PaymentParameters());
    }

    @Test
    public void calculateAmountBeforeTax() throws Exception {

        BigDecimal amtB4Tax = withholdingTaxLogic.calculateAmountBeforeTax(invoiceAMount);

        Assert.assertEquals(setAccuracy(101754.40), amtB4Tax);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wth = withholdingTaxLogic.calculateWithholdingVat(invoiceAMount);

        Assert.assertEquals(setAccuracy(2035.09), wth);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {

        BigDecimal wth = withholdingTaxLogic.calculateWithholdingTax(invoiceAMount);

        Assert.assertEquals(setAccuracy(5087.72), wth);
    }

    @Test
    public void calculateTotalExpense() throws Exception {
        BigDecimal total = withholdingTaxLogic.calculateTotalExpense(invoiceAMount);

        Assert.assertEquals(setAccuracy(116000.00), total);
    }

    @Test
    public void calculateAmountPayable() throws Exception {

        BigDecimal payee = withholdingTaxLogic.calculateAmountPayable(invoiceAMount);

        Assert.assertEquals(setAccuracy(108877.19), payee);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal payee = withholdingTaxLogic.calculateToPayee(invoiceAMount);

        Assert.assertEquals(setAccuracy(108877.19), payee);
    }

    private BigDecimal setAccuracy(Double amount) {

        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

}