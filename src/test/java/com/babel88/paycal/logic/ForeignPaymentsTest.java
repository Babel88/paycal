package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.TelegraphicTransfers;
import com.babel88.paycal.logic.base.ForeignPayments;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.assertEquals;

public class ForeignPaymentsTest {

    private TelegraphicTransfers foreignPayments;
    @Before
    public void setUp() throws Exception {

        foreignPayments = new ForeignPayments();
    }

    @Test
    public void getReverseInvoiceWithExclusiveWthTax() throws Exception {

        BigDecimal reverseInvoice =foreignPayments.getReverseInvoice(
                BigDecimal.valueOf(100000),BigDecimal.valueOf(0.2),true
        );

        assertEquals(BigDecimal.valueOf(125000).setScale(2,HALF_EVEN),
                reverseInvoice);
    }

    @Test
    public void getReverseInvoiceWithInclusiveWthTax() throws Exception {

        BigDecimal reverseInvoice =foreignPayments.getReverseInvoice(
                BigDecimal.valueOf(100000),BigDecimal.valueOf(0.2),false
        );

        assertEquals(BigDecimal.valueOf(100000).setScale(2,HALF_EVEN),
                reverseInvoice);
    }

    @Test
    public void getReverseVat() throws Exception {

        BigDecimal reverseVat =
                foreignPayments.getReverseVat(BigDecimal.valueOf(100000),BigDecimal.valueOf(0.16));

        assertEquals(BigDecimal.valueOf(16000).setScale(2, HALF_EVEN),
                reverseVat);
    }

    @Test
    public void getwithholdingTaxAmount() throws Exception {

        BigDecimal withholdingTax =
                foreignPayments.getwithholdingTaxAmount(
                        BigDecimal.valueOf(100000),BigDecimal.valueOf(0.2));

        assertEquals(BigDecimal.valueOf(20000).setScale(2, HALF_EVEN),
                withholdingTax);
    }

    @Test
    public void getTotalExpenseWithExclusiveWthTax() throws Exception {

        BigDecimal totalExpense =
                foreignPayments.getTotalExpense(BigDecimal.valueOf(125000),
                        BigDecimal.valueOf(0.16),
                        true,
                        BigDecimal.valueOf(100000),
                        BigDecimal.valueOf(20000));

        assertEquals(BigDecimal.valueOf(145000).setScale(2, HALF_EVEN),
                totalExpense);
    }

    @Test
    public void getTotalExpenseWithInclusiveWthTax() throws Exception {

        BigDecimal totalExpense =
                foreignPayments.getTotalExpense(BigDecimal.valueOf(125000),
                        BigDecimal.valueOf(0.16),
                        false,
                        BigDecimal.valueOf(100000),
                        BigDecimal.valueOf(20000));

        assertEquals(BigDecimal.valueOf(116000).setScale(2, HALF_EVEN),
                totalExpense);
    }

    @Test
    public void getPaySupplierWithExclusiveWthTax() throws Exception {

        BigDecimal paySupplier = foreignPayments.getPaySupplier(
                BigDecimal.valueOf(145000),
                BigDecimal.valueOf(25000),
                BigDecimal.valueOf(20000));

        assertEquals(BigDecimal.valueOf(100000).setScale(2, HALF_EVEN),
                paySupplier);
    }

    @Test
    public void getPaySupplierWithInclusiveWthTax() throws Exception {

        BigDecimal paySupplier = foreignPayments.getPaySupplier(
                BigDecimal.valueOf(116000),
                BigDecimal.valueOf(20000),
                BigDecimal.valueOf(16000));

        assertEquals(BigDecimal.valueOf(80000).setScale(2, HALF_EVEN),
                paySupplier);
    }

}