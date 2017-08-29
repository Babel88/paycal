package com.babel88.paycal.logic.base;

import com.babel88.paycal.utils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentLogicTest extends TestUtils<RentalPaymentLogic> {

    private RentalPaymentLogic rentalPaymentLogic;
    private BigDecimal invoiceAmount = BigDecimal.valueOf(116000).setScale(2, RoundingMode.HALF_EVEN);

    @Before
    public void setUp() throws Exception {

        rentalPaymentLogic = new RentalPaymentLogic();
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

        Assert.assertEquals(setAccuracy(116000.00), totalExpense);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal payee = rentalPaymentLogic.calculateToPayee(invoiceAmount);

        Assert.assertEquals(setAccuracy(100000.00), payee);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {
        BigDecimal wth = rentalPaymentLogic.calculateWithholdingTax(invoiceAmount);

        Assert.assertEquals(setAccuracy(10000.00), wth);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wthVat = rentalPaymentLogic.calculateWithholdingVat(invoiceAmount);

        Assert.assertEquals(setAccuracy(6000.00), wthVat);
    }


}