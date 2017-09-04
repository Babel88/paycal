package com.babel88.paycal.logic.base;

import com.babel88.paycal.models.TTArguments;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class ExclusiveImportedServiceLogicImplTest {

    private ExclusiveImportedServiceLogicImpl importedServiceLogic;
    private TTArguments ttArguments;
    @Before
    public void setUp() throws Exception {

        importedServiceLogic = new ExclusiveImportedServiceLogicImpl();

        ttArguments = new TTArguments()
                .setInvoiceAmount(BigDecimal.valueOf(100000.00))
                .setTaxExclusionPolicy(true)
                .setWithholdingTaxRate(BigDecimal.valueOf(20))
                .setReverseVatRate(BigDecimal.valueOf(16))
                .setAmountBeforeTax(BigDecimal.valueOf(125000));
    }

    @Test
    public void ReadableCalculateAmountBeforeTax() throws Exception {

        BigDecimal amtB4Tax = importedServiceLogic
                .helperCalculateAmountBeforeTax(ttArguments);

        assertEquals(BigDecimal.valueOf(125000.00).setScale(2,HALF_EVEN),amtB4Tax);
    }

    @Test
    public void ReadableCalculateTotalExpenses() throws Exception {

        BigDecimal totalExpenses = importedServiceLogic
                .helperCalculateTotalExpenses(ttArguments,BigDecimal.valueOf(125000));

        assertEquals(BigDecimal.valueOf(145000).setScale(2, HALF_EVEN),totalExpenses);
    }

    @Test
    public void calculateTotalExpenses() throws Exception {
        BigDecimal totalExpenses = importedServiceLogic
                .calculateTotalExpenses(ttArguments);
        assertEquals(BigDecimal.valueOf(145000).setScale(2, HALF_EVEN),totalExpenses);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateToPayee(ttArguments);
        assertEquals(BigDecimal.valueOf(100000).setScale(2, HALF_EVEN),amount);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateWithholdingTax(ttArguments);
        assertEquals(BigDecimal.valueOf(25000).setScale(2, HALF_EVEN),amount);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal amount = importedServiceLogic
                .calculateWithholdingVat(ttArguments);
        assertEquals(BigDecimal.valueOf(20000).setScale(2, HALF_EVEN),amount);
    }
}