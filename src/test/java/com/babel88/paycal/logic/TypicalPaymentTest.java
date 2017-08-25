package com.babel88.paycal.logic;

import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.base.TypicalPayment;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;


public class TypicalPaymentTest {

    private TypicalPayment typicalPayment;

    private BigDecimal invoiceAmount;

    @Before
    public void setUp() throws Exception {

        typicalPayment = new TypicalPayment(new PaymentParameters());

        invoiceAmount = BigDecimal.valueOf(116000);

        typicalPayment.setInvoiceAmount(invoiceAmount);
    }

    @Test
    public void getInvoiceAmount() throws Exception {

        assertEquals(BigDecimal.valueOf(116000).setScale(2),invoiceAmount.setScale(2));
    }

    @Test
    public void setInvoiceAmount() throws Exception {

        TypicalPayment test = new TypicalPayment(new PaymentParameters());

        test.setInvoiceAmount(BigDecimal.valueOf(140000).setScale(2));

        assertEquals(BigDecimal.valueOf(140000).setScale(2),test.getInvoiceAmount());
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal withholdingVat = typicalPayment.calculateWithholdingVat(invoiceAmount);

        assertEquals(BigDecimal.valueOf(6000).setScale(2),withholdingVat);
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense = typicalPayment.calculateTotalExpense(invoiceAmount).setScale(2);

        assertEquals(BigDecimal.valueOf(116000).setScale(2),totalExpense);
    }

    @Test
    public void calculateAmountPayable() throws Exception {

        BigDecimal payable = typicalPayment.calculatePayableToVendor(invoiceAmount);

        assertEquals(BigDecimal.valueOf(110000).setScale(2),payable);
    }

    @Test
    public void calculateAmountBeforeTax() throws Exception {

        BigDecimal amtB4Tax = typicalPayment.calculateAmountBeforeTax(invoiceAmount).setScale(2);

        assertEquals(BigDecimal.valueOf(100000).setScale(2),amtB4Tax);
    }
}