package com.ghacupha.paycal.logic.base;

import com.ghacupha.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by edwin.njeru on 01/09/2017.
 */
@Deprecated
public class ForeignPaymentsTest extends TestUtils<ForeignPayments> {

    private ForeignPayments foreignPayments;

    @Before
    public void setUp() throws Exception {

        foreignPayments = new ForeignPayments();
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public ForeignPayments getBeanInstance() {
        return foreignPayments;
    }

    @Test
    public void getExclusiveReverseInvoice() throws Exception {

        BigDecimal reverse =
                foreignPayments.getReverseInvoice(
                        setAccuracy(100000.00),
                        BigDecimal.valueOf(20),
                        true);

        assertEquals(setAccuracy(125000.00), reverse);
    }

    @Test
    public void getInclusiveReverseInvoice() throws Exception {

        BigDecimal reverse =
                foreignPayments.getReverseInvoice(
                        setAccuracy(100000.00),
                        BigDecimal.valueOf(20),
                        true);

        assertEquals(setAccuracy(100000.00), reverse);
    }

    @Test
    public void getExclusiveReverseVat() throws Exception {

        BigDecimal wthVat =
                foreignPayments.getReverseVat(setAccuracy(125000.00), setAccuracy(0.16));

        assertEquals(setAccuracy(20000.00), wthVat);
    }

    @Test
    public void getInclusiveReverseVat() throws Exception {

        BigDecimal wthVat =
                foreignPayments.getReverseVat(setAccuracy(100000.00), setAccuracy(0.16));

        assertEquals(setAccuracy(16000.00), wthVat);
    }

    @Test
    public void getExclusiveWithholdingTaxAmount() throws Exception {

        BigDecimal amount =
                foreignPayments.getwithholdingTaxAmount(setAccuracy(125000.00), setAccuracy(0.16));

        assertEquals(setAccuracy(25000.00), amount);
    }

    @Test
    public void getInclusiveWithholdingTaxAmount() throws Exception {

        BigDecimal amount =
                foreignPayments.getwithholdingTaxAmount(setAccuracy(100000.00), setAccuracy(0.16));

        assertEquals(setAccuracy(16000.00), amount);
    }

    @Test
    public void getExclusiveTotalExpense() throws Exception {
        BigDecimal amount =
                foreignPayments.getTotalExpense(setAccuracy(125000.00),
                        BigDecimal.valueOf(0.16),
                        true,
                        BigDecimal.valueOf(100000),
                        BigDecimal.valueOf(20000));

        assertEquals(setAccuracy(145000.00), amount);
    }

    @Test
    public void getInclusiveTotalExpense() throws Exception {
        BigDecimal amount =
                foreignPayments.getTotalExpense(setAccuracy(100000.00),
                        BigDecimal.valueOf(0.16),
                        false,
                        BigDecimal.valueOf(100000),
                        BigDecimal.valueOf(16000));

        assertEquals(setAccuracy(116000.00), amount);
    }

    @Test
    public void getPayExclusiveSupplier() throws Exception {

        BigDecimal amount = foreignPayments.getPaySupplier(setAccuracy(145000.00), setAccuracy(25000.00), setAccuracy(20000.00));

        assertEquals(setAccuracy(100000.00), amount);
    }

    @Test
    public void getPayInclusiveSupplier() throws Exception {

        BigDecimal amount = foreignPayments.getPaySupplier(setAccuracy(116000.00), setAccuracy(20000.00), setAccuracy(16000.00));

        assertEquals(setAccuracy(80000.00), amount);
    }
}