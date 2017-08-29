package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.factory.LogicFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public class ContractorLogicTest {


    private DefaultLogic defaultLogic;
    private BigDecimal invoiceAmount;

    @Before
    public void setUp() throws Exception {

        defaultLogic = LogicFactory.getInstance().createContractorLogic();
        invoiceAmount = BigDecimal.valueOf(116000.00).setScale(2, HALF_EVEN);
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense = defaultLogic.calculateTotalExpense(invoiceAmount);

        Assert.assertEquals(invoiceAmount, totalExpense);

    }

    @Test
    public void getInstance() throws Exception {

        DefaultLogic defaultLogic = LogicFactory.getInstance().createContractorLogic();

        Assert.assertEquals(this.defaultLogic, defaultLogic);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal toPayee = defaultLogic.calculateToPayee(invoiceAmount);

        Assert.assertEquals(
                BigDecimal.valueOf(107000.00).setScale(2, HALF_EVEN), toPayee
        );
    }

    @Test
    public void calculateWithholdingTax() throws Exception {

        BigDecimal wthTax = defaultLogic.calculateWithholdingTax(invoiceAmount);

        Assert.assertEquals(
                BigDecimal.valueOf(3000.00).setScale(2, HALF_EVEN), wthTax
        );
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wthVat = defaultLogic.calculateWithholdingVat(invoiceAmount);

        Assert.assertEquals(
                BigDecimal.valueOf(6000.00).setScale(2, HALF_EVEN), wthVat
        );
    }

}