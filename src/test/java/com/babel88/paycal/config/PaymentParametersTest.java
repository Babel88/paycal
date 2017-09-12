package com.babel88.paycal.config;

import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by edwin.njeru on 31/08/2017.
 */
public class PaymentParametersTest extends TestUtils<PaymentParameters> {

    private PaymentParameters paymentParameters;


    @Before
    public void setUp() throws Exception {

        paymentParameters = new PaymentParameters();
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public PaymentParameters getBeanInstance() {


        return paymentParameters;
    }

    @Test
    public void getInstance() throws Exception {

        assertNotNull(paymentParameters);
    }

    @Test
    public void getVatRate() throws Exception {

        BigDecimal vatRate = paymentParameters.getVatRate();

        assertEquals(bd(0.16),vatRate);
    }

    @Test
    public void getWithholdingVatRate() throws Exception {

        BigDecimal vatRate = paymentParameters.getWithholdingVatRate();

        assertEquals(bd(0.06),vatRate);
    }

    @Test
    public void getWithholdingTaxRate() throws Exception {

        BigDecimal rate = paymentParameters.getWithholdingTaxRate();

        assertEquals(bd(0.05),rate);
    }

    @Test
    public void getWithholdingTaxContractor() throws Exception {

        BigDecimal rate = paymentParameters.getWithholdingTaxContractor();

        assertEquals(bd(0.03),rate);
    }

    @Test
    public void getWithholdingTaxOnRentalRate() throws Exception {

        BigDecimal rate = paymentParameters.getWithholdingTaxOnRentalRate();

        assertEquals(bd(0.10),rate);
    }

}