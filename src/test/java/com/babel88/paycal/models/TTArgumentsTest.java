package com.babel88.paycal.models;

import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class TTArgumentsTest extends TestUtils<TTArguments> {

    private TTArguments ttArguments;

    @Before
    public void setUp() throws Exception {

        ttArguments = new TTArguments();

        ttArguments.setAmountBeforeTax(BigDecimal.valueOf(100000.00).setScale(2, HALF_EVEN));
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public TTArguments getBeanInstance() {

        return ttArguments;
    }

    @Test
    public void getInstance() throws Exception {

        TTArguments ttArguments1 = TTArguments.getInstance();

        TTArguments ttArguments2 = ModelFactory.getTTArguments();

        assertNotNull(ttArguments1);

        assertEquals(ttArguments1,ttArguments2);
    }

    @Test
    public void setInvoiceAmount() throws Exception {

        ttArguments.setInvoiceAmount(BigDecimal.valueOf(100000));

        assertEquals(BigDecimal.valueOf(100000).setScale(2, HALF_EVEN),ttArguments.getInvoiceAmount());
    }

    @Test
    public void setReverseVatRate() throws Exception {
        ttArguments.setReverseVatRate(BigDecimal.valueOf(16));

        assertEquals(BigDecimal.valueOf(0.16).setScale(2, HALF_EVEN),ttArguments.getReverseVatRate());
    }

    @Test
    public void setWithholdingTaxRate() throws Exception {

        ttArguments.setWithholdingTaxRate(BigDecimal.valueOf(20));

        assertEquals(BigDecimal.valueOf(0.20).setScale(2, HALF_EVEN),ttArguments.getWithholdingTaxRate());
    }

    @Test
    public void setAmountBeforeTax() throws Exception {

        ttArguments.setAmountBeforeTax(BigDecimal.valueOf(100000.00).setScale(2, HALF_EVEN));

        assertEquals(BigDecimal.valueOf(100000.00).setScale(2, HALF_EVEN),ttArguments.getAmountBeforeTax());
    }
}