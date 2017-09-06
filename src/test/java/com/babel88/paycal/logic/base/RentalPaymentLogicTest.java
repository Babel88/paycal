package com.babel88.paycal.logic.base;

import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentLogicTest extends TestUtils<RentalPaymentLogic> {


    private RentalPaymentLogic rentalPaymentLogic;
    private BigDecimal invoiceAmount = BigDecimal.valueOf(116000).setScale(2, RoundingMode.HALF_EVEN);

    @Before
    public void setUp() throws Exception {

        rentalPaymentLogic = new RentalPaymentLogic(new PaymentParameters());
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public RentalPaymentLogic getBeanInstance() {

        return rentalPaymentLogic;
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense = rentalPaymentLogic.calculateTotalExpense(invoiceAmount);

        assertEquals(setAccuracy(116000.00), totalExpense);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal payee = rentalPaymentLogic.calculateToPayee(invoiceAmount);

        assertEquals(setAccuracy(100000.00), payee);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {
        BigDecimal wth = rentalPaymentLogic.calculateWithholdingTax(invoiceAmount);

        assertEquals(setAccuracy(10000.00), wth);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wthVat = rentalPaymentLogic.calculateWithholdingVat(invoiceAmount);

        assertEquals(setAccuracy(6000.00), wthVat);
    }

    @Test
    public void calculateTotalExpense1() throws Exception {

        super.totalExpense = rentalPaymentLogic.calculateTotalExpense(super.invoiceAmount);

        assertEquals(setAccuracy(116000.00),totalExpense);
    }

    @Test
    public void calculateToPayee1() throws Exception {
        super.toPayee = rentalPaymentLogic.calculateToPayee(super.invoiceAmount);

        assertEquals(setAccuracy(100000.00),toPayee);

    }

    @Test
    public void calculateWithholdingTax1() throws Exception {
        super.wthTax = rentalPaymentLogic.calculateWithholdingTax(super.invoiceAmount);

        assertEquals(setAccuracy(10000.00),wthTax);
    }

    @Test
    public void calculateWithholdingVat1() throws Exception {

        super.wthVat = rentalPaymentLogic.calculateWithholdingVat(super.invoiceAmount);

        assertEquals(setAccuracy(6000.00),wthVat);
    }


}