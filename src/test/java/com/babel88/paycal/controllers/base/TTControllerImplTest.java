package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.controllers.TTController;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.utils.TestUtils;
import com.babel88.paycal.view.FeedBackImpl;
import com.babel88.paycal.view.Invoice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.management.Notification;

import static org.junit.Assert.*;

public class TTControllerImplTest extends TestUtils<TTController> {

    private TTController ttController;

    private TTArguments ttArguments;

    private DefaultPaymentModel paymentModel;


    @Before
    public void setUp() throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans-test.xml");

        ttController = (TTController) context.getBean("ttController");

        ttArguments = new TTArguments()
                .setInvoiceAmount(setAccuracy(100000.00))
                .setReverseVatRate(setAccuracy(0.16))
                .setWithholdingTaxRate(setAccuracy(0.20))
                .setTaxExclusionPolicy(true);

    }

    @Override
    public TTController getBeanInstance() {

        return ttController;
    }

    @Test
    public void updateTotalExpense() throws Exception {

        paymentModel = ttController.updateTotalExpense(ttArguments);

        assertEquals(paymentModel.getTotalExpense(),setAccuracy(145000.00));
    }

    @Test
    public void updateToPayee() throws Exception {

        paymentModel = ttController.updateToPayee(ttArguments);

        assertEquals(paymentModel.getToPayee(),setAccuracy(100000.00));
    }

    @Test
    public void updateWithholdingTax() throws Exception {

        paymentModel = ttController.updateWithholdingTax(ttArguments);

        assertEquals(paymentModel.getWithholdingTax(),setAccuracy(25000.00));
    }

    @Test
    public void updateWithholdingVat() throws Exception {

        ttController.updateWithholdingVat(ttArguments);

        assertEquals(ttController.getPaymentModel().getWithholdingVat(),setAccuracy(20000.00));
    }

    @Test
    public void updateToPrepay() throws Exception {

        ttController.updateToPrepay(ttArguments);

        assertEquals(ttController.getPaymentModel().getToPrepay(),setAccuracy(0.00));
    }

    @Test
    public void getPaymentModel() throws Exception {

        TTControllerImpl testController = new TTControllerImpl(new Invoice(new FeedBackImpl()));
        testController.setPaymentModel(paymentModel);

        DefaultPaymentModel testModel = testController.getPaymentModel();

        assertEquals(testModel,paymentModel);
    }

    @Test
    public void setTtArguments() throws Exception {

        TTController testController = new TTControllerImpl(new Invoice(new FeedBackImpl()));
        testController.setTtArguments(ttArguments);

        TTArguments testTTArgument = testController.getTtArguments();

        assertEquals(testTTArgument,ttArguments);
    }

}