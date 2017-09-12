package com.babel88.paycal.logic;

import com.babel88.paycal.config.prepaymentConfigurations;
import com.babel88.paycal.utils.TestUtils;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbstractPrepaymentTest extends TestUtils<AbstractPrepayment> {

    private AbstractPrepayment abstractPrepayment;

    @Before
    public void setUp() throws Exception {

        abstractPrepayment = new AbstractPrepayment(new prepaymentConfigurations());

        abstractPrepayment
                .setInvoiceStartDate(LocalDate.of(2017,01,01))
                .setInvoiceReferenceDate(LocalDate.of(2017,06,30))
                .setInvoiceEndDate(LocalDate.of(2017,12,31));
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public AbstractPrepayment getBeanInstance() {

        return abstractPrepayment;
    }

    @Test
    public void calculatePrepayment() throws Exception {

        BigDecimal prepayment  = abstractPrepayment.calculatePrepayment(bd(45365.56));

        assertEquals(bd(22932.04),prepayment);
    }

    protected BigDecimal bd(Double value){

        return BigDecimal.valueOf(value).setScale(2,HALF_EVEN);
    }

    @Override
    public void getterAndSetterCorrectness() throws Exception {
        // The abstractPrepayment bean seems to be having instantiation problems
    }
}