package com.babel88.paycal.logic.base;

import com.babel88.paycal.models.TTArguments;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class InclusiveImportedServiceLogicImplTest {

    private InclusiveImportedServiceLogicImpl importedServiceLogic;
    private TTArguments ttArguments;

    @Before
    public void setUp() throws Exception {

        importedServiceLogic = new InclusiveImportedServiceLogicImpl();

        ttArguments = new TTArguments()
                .setInvoiceAmount(BigDecimal.valueOf(100000.00))
                .setTaxExclusionPolicy(true)
                .setWithholdingTaxRate(BigDecimal.valueOf(0.2))
                .setReverseVatRate(BigDecimal.valueOf(0.16))
                .setAmountBeforeTax(BigDecimal.valueOf(125000));
    }

    @Test
    public void calculateAmountBeforeTax() throws Exception {

        BigDecimal amount = importedServiceLogic
                .helperCalculateAmountBeforeTax(ttArguments);

        assertEquals(BigDecimal.valueOf(100000.00),amount);
    }

    @Test
    public void calculateTotalExpensesHelper() throws Exception {

        BigDecimal amount = importedServiceLogic
                .helperCalculateTotalExpenses(ttArguments,BigDecimal.valueOf(100000));

        assertEquals(BigDecimal.valueOf(116000.00).setScale(2, HALF_EVEN),amount);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateWithholdingVat(ttArguments);

        assertEquals(BigDecimal.valueOf(16000.00).setScale(2, HALF_EVEN),amount);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateWithholdingTax(ttArguments);

        assertEquals(BigDecimal.valueOf(20000.00).setScale(2, HALF_EVEN),amount);

    }

    @Test
    public void calculateTotalExpenses() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateTotalExpenses(ttArguments);

        assertEquals(BigDecimal.valueOf(116000.00).setScale(2, HALF_EVEN),amount);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateToPayee(ttArguments);

        assertEquals(BigDecimal.valueOf(80000.00).setScale(2, HALF_EVEN),amount);
    }
}