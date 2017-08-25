package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.WithholdingTaxPayments;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.base.DefaultTypicalWithholdingTaxPayment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;


public class TypicalWithholdingTaxPaymentTest {


    private WithholdingTaxPayments withholdingTaxPayment;

    private BigDecimal invoiceAmount;

    @Before
    public void setUp() throws Exception {

        withholdingTaxPayment = new DefaultTypicalWithholdingTaxPayment(new PaymentParameters());

        MockitoAnnotations.initMocks(this);

        invoiceAmount = BigDecimal.valueOf(116000);
    }

    @Test
    public void calculateAmountBeforeTax() throws Exception {

        BigDecimal amtB4Tax =
                withholdingTaxPayment
                        .calculateAmountBeforeTax(invoiceAmount)
                        .setScale(2);

        assertEquals(BigDecimal.valueOf(100000).setScale(2),amtB4Tax);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal withholdingVat =
                withholdingTaxPayment
                .calculateWithholdingVat(invoiceAmount)
                .setScale(2);

        assertEquals(BigDecimal.valueOf(6000).setScale(2),withholdingVat);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {

        BigDecimal withholdingTax =
                withholdingTaxPayment
                .calculateWithholdingTax(invoiceAmount)
                .setScale(2);

        assertEquals(BigDecimal.valueOf(5000).setScale(2),withholdingTax);
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense =
                withholdingTaxPayment
                .calculateTotalExpense(invoiceAmount)
                .setScale(2);

        assertEquals(BigDecimal.valueOf(116000).setScale(2),totalExpense);
    }

    @Test
    public void calculateAmountPayable() throws Exception {

        BigDecimal payable =
                withholdingTaxPayment
                .calculateAmountPayable(invoiceAmount)
                .setScale(2);

        assertEquals(BigDecimal.valueOf(105000).setScale(2),payable);
    }

}