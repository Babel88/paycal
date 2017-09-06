package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.utils.TestUtilityFunctions;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Basic tests typicalPayments logic
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class TypicalPaymentTest extends TestUtils<DefaultLogic>{

    private DefaultLogic typicalPayment;

    private BigDecimal invoiceAmount;

    private PaymentParameters paymentParameters;

    @Before
    public void setUp() throws Exception {
        paymentParameters = new PaymentParameters();
        typicalPayment = new TypicalPaymentsImpl(new PaymentParameters());
        invoiceAmount = setAccuracy(116000.00);
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public DefaultLogic getBeanInstance() {

        return typicalPayment;
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wth = typicalPayment.calculateWithholdingVat(invoiceAmount);

        assertEquals(setAccuracy(6000.00), wth);
    }

    @Test
    public void typicalPaymentObjectIsNotNull(){

        assertNotNull(typicalPayment);
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense = typicalPayment.calculateTotalExpense(invoiceAmount);

        assertEquals(setAccuracy(116000.00),totalExpense);

    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal toPayee = typicalPayment.calculateToPayee(invoiceAmount);

        assertEquals(setAccuracy(110000.00),toPayee);
    }

    @Test
    public void calculateAmountBeforeTax() throws Exception {

        TypicalPaymentsImpl typicalPayment = new TypicalPaymentsImpl(paymentParameters);

        BigDecimal beforeTax = typicalPayment.calculateAmountBeforeTax(invoiceAmount);

        assertEquals(setAccuracy(100000.00),beforeTax);
    }

    public TypicalPaymentTest setTypicalPayment(DefaultLogic typicalPayment) {
        this.typicalPayment = typicalPayment;
        return this;
    }

    @Test
    @Override
    public void getterAndSetterCorrectness() throws Exception {

//        final BeanTester beanTester = new BeanTester();
//        beanTester.testBean(getBeanInstance().getClass());
        //Not needed
    }
}