package com.babel88.paycal.logic;

import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.config.GeneralConfigurations;
import com.babel88.paycal.test.AbstractJavaBeanTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPrepaymentTest extends AbstractJavaBeanTest<AbstractPrepayment> {

    private AbstractPrepayment abstractPrepayment;

    @Mock
    private PrepaymentDetails prepaymentDetails;

    @Override
    @Before
    public void setUp() throws Exception {

        abstractPrepayment = new AbstractPrepayment(new GeneralConfigurations());

        prepaymentDetails = Mockito.mock(PrepaymentDetails.class);

        when(prepaymentDetails.getInvoiceStartDate()).thenReturn("01.01.2017");
        when(prepaymentDetails.getInvoiceRefDate()).thenReturn("30.06.2017");
        when(prepaymentDetails.getInvoiceEndDate()).thenReturn("31.12.2017");

        abstractPrepayment.setPrepaymentDetails(prepaymentDetails);
        abstractPrepayment.getInvoiceEndDate();
    }

    @Test
    public void calculatePrepaymentCorrectness() throws Exception {

        BigDecimal prepayment =
                abstractPrepayment.calculatePrepayment(BigDecimal.valueOf(100000));

        assertEquals(BigDecimal.valueOf(50549.45).setScale(2, RoundingMode.HALF_EVEN),
                prepayment);
    }

    /**
     * This method returns an instance of the bean being tested, after being created
     * by the class extending this
     *
     * @return instance of the bean under test
     */
    @Override
    public AbstractPrepayment getBeanInstance() {


        return new AbstractPrepayment(new GeneralConfigurations());
    }
}