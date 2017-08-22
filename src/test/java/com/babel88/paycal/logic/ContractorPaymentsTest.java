package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.config.ContextConfigurations;
import com.babel88.paycal.test.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@org.springframework.test.context.ContextConfiguration(classes = ContextConfigurations.class)
public class ContractorPaymentsTest extends TestUtil<Contractors> {

    @Autowired
    private Contractors contractor;

    private BigDecimal invoiceAmount;

    @Before
    public void setUp() throws Exception {

        invoiceAmount = BigDecimal.valueOf(116000);

        setParametersBehaviour();

    }

    @Test
    public void calculatePayableToContractor() throws Exception {

        BigDecimal payable = contractor.calculatePayableToVendor(invoiceAmount);

        assertEquals(BigDecimal.valueOf(107000).setScale(2),payable);
    }

    @Test
    public void calculateContractorWithholdingTax() throws Exception {

        BigDecimal withholdingTax = contractor.calculateWithholdingTax(invoiceAmount);

        assertEquals(BigDecimal.valueOf(3000).setScale(2),withholdingTax);
    }

    @Test
    public void calculateContractorWithholdingVat() throws Exception {

        BigDecimal withholdingVat = contractor.calculateWithholdingVat(invoiceAmount);

        assertEquals(BigDecimal.valueOf(6000).setScale(2),withholdingVat);
    }

}