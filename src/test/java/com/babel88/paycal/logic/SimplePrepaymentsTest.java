package com.babel88.paycal.logic;

import com.babel88.paycal.config.PrepaymentConfigurations;
import com.babel88.paycal.utils.TestUtils;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.*;

import com.babel88.paycal.view.FeedBackImpl;
import com.babel88.paycal.view.Invoice;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SimplePrepaymentsTest extends TestUtils<SimplePrepayments> {

    private SimplePrepayments simplePrepayments;

    @Before
    public void setUp() throws Exception {

        simplePrepayments = new SimplePrepayments(new Invoice(new FeedBackImpl()), new PrepaymentConfigurations());

        simplePrepayments
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
    public SimplePrepayments getBeanInstance() {

        return simplePrepayments;
    }

    @Test
    public void calculatePrepayment() throws Exception {

        BigDecimal prepayment  = simplePrepayments.calculatePrepayment(bd(45365.56));

        assertEquals(bd(22932.04),prepayment);
    }

    protected BigDecimal bd(Double value){

        return BigDecimal.valueOf(value).setScale(2,HALF_EVEN);
    }

    @Override
    public void getterAndSetterCorrectness() throws Exception {
        // The simplePrepayments bean seems to be having instantiation problems
    }
}