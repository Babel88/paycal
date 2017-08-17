package com.babel88.paycal.logic;

import com.babel88.paycal.config.ContextConfigurations;
import com.babel88.paycal.config.GeneralConfigurations;
import com.babel88.paycal.api.InvoiceDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ContextConfigurations.class)
public class AbstractPrepaymentTest {

    @InjectMocks
    private AbstractPrepayment abstractPrepayment;

    private BigDecimal expenseAmount;

    @Mock
    public GeneralConfigurations generalConfigurations;

    private String dateStringDotted;

    private String dateStringDashed;

    @Mock
    private InvoiceDetails invoice;

    @Before
    public void setUp() throws Exception {

        expenseAmount = BigDecimal.valueOf(116000);

        MockitoAnnotations.initMocks(AbstractPrepaymentTest.class);

        abstractPrepayment = new AbstractPrepayment(new GeneralConfigurations());

        dateStringDotted = "12.03.2017";

        dateStringDashed = "12-03-2017";

        ReflectionTestUtils.setField(this,"invoice",InvoiceDetails.class);

//        setGeneralConfigurationsBehaviour();
    }
//
//    public void setGeneralConfigurationsBehaviour() throws Exception{
//
//        when(generalConfigurations.getDateFormatStyle()).thenReturn(FormatStyle.MEDIUM);
//        when(generalConfigurations.getLocale()).thenReturn(Locale.GERMAN);
//    }

    @Test
    public void generalConfigurationsIsNotNull() throws Exception{

        assertNotNull(generalConfigurations);
    }

    @Test
    public void createInvoiceStartDateDottedFormat() throws Exception {

        LocalDate localDate = abstractPrepayment.createDateFromString(dateStringDotted);

        assertEquals(localDate.toString(), "2017-03-12");
    }

    @Test
    public void createInvoiceStartDateControlFormat() throws Exception {

        LocalDate localDate = abstractPrepayment.createDateFromString("12.03.2017");

        assertEquals(localDate.toString(), "2017-03-12");
    }

    @Test
    public void createInvoiceStartDateDashedFormat() throws Exception {

        LocalDate localDate = abstractPrepayment.createDateFromString(dateStringDashed);

        assertNotEquals(localDate.toString(), "2017-03-12");
    }

    @Test
    public void settersForDatesAreWorking() throws Exception{

        when(invoice.getInvoiceStartDate()).thenReturn("09.08.2017");
        when(invoice.getInvoiceRefDate()).thenReturn("09.08.2017");
        when(invoice.getInvoiceEndDate()).thenReturn("09.08.2017");

        abstractPrepayment.calculatePrepayment(expenseAmount);

        assertNotNull(abstractPrepayment.getInvoiceStartDate());
        assertNotNull(abstractPrepayment.getInvoiceReferenceDate());
        assertNotNull(abstractPrepayment.getInvoiceEndDate());
    }

}