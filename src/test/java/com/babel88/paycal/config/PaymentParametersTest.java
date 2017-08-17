package com.babel88.paycal.config;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentParametersTest {

    private PaymentParameters parameters;

    @Before
    public void setUp() throws Exception {

        parameters = new PaymentParameters();

    }

    @Test
    public void getVatRate() throws Exception {

        BigDecimal vatRate = parameters.getVatRate();

        assertEquals(BigDecimal.valueOf(0.16).setScale(2),vatRate);
    }

    @Test
    public void getWithholdingVatRate() throws Exception {

        BigDecimal withhldingVatRate = parameters.getWithholdingVatRate();

        assertEquals(BigDecimal.valueOf(0.06).setScale(2),withhldingVatRate);
    }

    @Test
    public void getWithholdingTaxRate() throws Exception {

        BigDecimal withholdinTaxRate = parameters.getWithholdingTaxRate();

        assertEquals(BigDecimal.valueOf(0.05).setScale(2),withholdinTaxRate);

    }

    @Test
    public void getWithholdingTaxContractor() throws Exception {

        BigDecimal withholdingTaxContractor = parameters.getWithholdingTaxContractor();

        assertEquals(BigDecimal.valueOf(0.03).setScale(2),withholdingTaxContractor);
    }

}