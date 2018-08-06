package com.babel88.paycal.logic;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.TTController;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.PrepaymentConfigurations;
import com.babel88.paycal.controllers.ReportsController;
import com.babel88.paycal.controllers.base.DefaultTypicalPaymentsController;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.logic.base.TypicalPaymentsImpl;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.view.FeedBackImpl;
import com.babel88.paycal.view.Invoice;
import com.babel88.paycal.view.ModelPrecisionVisitor;
import com.babel88.paycal.view.ReportingVisitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

//TODO include method-calls testing
public class BusinessLogicRouterTest {

    private BusinessLogicRouter router;

    private PartialTaxPaymentController partialTaxPaymentController;
    private DefaultControllers contractorPaymentsController;
    private DefaultControllers withholdingTaxPaymentController;
    private DefaultControllers rentalPaymentsController;
    private DefaultControllers typicalPaymentsController;
    private TTController ttController;

    @Before
    public void setUp() throws Exception {

        InvoiceDetails invoiceDetails = Mockito.mock(InvoiceDetails.class);
        typicalPaymentsController =
                new DefaultTypicalPaymentsController(
                        new PaymentModel(),
                        invoiceDetails,
                        new ResultsOutput(),
                        new ReportsController(),
                        new PrepaymentControllerImpl(new SimplePrepayments(new Invoice(new FeedBackImpl()), new PrepaymentConfigurations()),
                                new FeedBackImpl()),
                        new TypicalPaymentsImpl(new PaymentParameters()),
                        new ModelPrecisionVisitor(),
                        new ModelPrecisionVisitor(),
                        new ReportingVisitor(new FeedBackImpl()));

        router = Mockito.mock(BusinessLogicRouter.class);
    }

    @Test
    public void normal() throws Exception {

        //router.normal();

        //Mockito.verify(router).normal();

        //Mockito.verifyNoMoreInteractions(router);
    }

    @Test
    public void vatGiven() throws Exception {
    }

    @Test
    public void contractor() throws Exception {
    }

    @Test
    public void taxToWithhold() throws Exception {
    }

    @Test
    public void rentalPayments() throws Exception {
    }

    @Test
    public void telegraphicTransfer() throws Exception {
    }

}