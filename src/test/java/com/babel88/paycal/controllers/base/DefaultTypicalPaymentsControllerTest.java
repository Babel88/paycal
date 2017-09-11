package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.base.TypicalPaymentsImpl;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.FeedBackImpl;
import com.babel88.paycal.view.Invoice;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.assertNotNull;

public class DefaultTypicalPaymentsControllerTest {

    private ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
    private DefaultTypicalPaymentsController typicalPaymentsController;
    private TypicalPaymentsImpl logic;
    private PaymentParameters paymentParameters;
    private BigDecimal invoiceAmount = BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);

    @Before
    public void setUp() throws Exception {

        typicalPaymentsController = new DefaultTypicalPaymentsController(new Invoice(new FeedBackImpl()));
        paymentParameters = new PaymentParameters();

        logic = new TypicalPaymentsImpl(paymentParameters);

        typicalPaymentsController.setTypicalPaymentsLogic(logic).setPaymentModel(new PaymentModel());
    }

    @Test
    public void controllerIsAccessibleFromContainer() throws Exception {

        assertNotNull((DefaultControllers)context.getBean("typicalPaymentsController"));
    }

    @Test
    public void updateWithholdingTax() throws Exception {
    }

    @Test
    public void updateWithholdingVat() throws Exception {
    }

    @Test
    public void getPaymentModel() throws Exception {
    }

    @Test
    public void getTtArguments() throws Exception {
    }

    @Test
    public void setTypicalPaymentsLogic() throws Exception {
    }

}