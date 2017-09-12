package com.babel88.paycal.logic;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.TTController;
import com.babel88.paycal.controllers.base.DefaultTypicalPaymentsController;
import com.jcabi.aspects.Loggable;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
    @Loggable
    public void setUp() throws Exception {

        InvoiceDetails invoiceDetails = Mockito.mock(InvoiceDetails.class);
        typicalPaymentsController = new DefaultTypicalPaymentsController(invoiceDetails);

        router = Mockito.mock(BusinessLogicRouter.class);
    }

    @Test
    @Loggable
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