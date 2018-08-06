package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.PaymentParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public class ContractorLogicTest {


    private DefaultLogic contractorLogic;

    private BigDecimal invoiceAmount;

    @Before
    public void setUp() throws Exception {

        contractorLogic = new ContractorLogic(new PaymentParameters());

        invoiceAmount = BigDecimal.valueOf(116000.00).setScale(2, HALF_EVEN);
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense = contractorLogic.calculateTotalExpense(invoiceAmount);

        Assert.assertEquals(invoiceAmount, totalExpense);

    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal toPayee = contractorLogic.calculateToPayee(invoiceAmount);

        Assert.assertEquals(
                BigDecimal.valueOf(107000.00).setScale(2, HALF_EVEN), toPayee
        );
    }

    @Test
    public void calculateWithholdingTax() throws Exception {

        BigDecimal wthTax = contractorLogic.calculateWithholdingTax(invoiceAmount);

        Assert.assertEquals(
                BigDecimal.valueOf(3000.00).setScale(2, HALF_EVEN), wthTax
        );
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wthVat = contractorLogic.calculateWithholdingVat(invoiceAmount);

        Assert.assertEquals(
                BigDecimal.valueOf(6000.00).setScale(2, HALF_EVEN), wthVat
        );
    }

}