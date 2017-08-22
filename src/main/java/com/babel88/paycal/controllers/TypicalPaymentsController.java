package com.babel88.paycal.controllers;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.controllers.support.PaymentModelTypicalControllerUpdate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class TypicalPaymentsController implements TypicalPaymentsControllers {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private ResultsViewer viewResults;

    @Autowired
    public InvoiceDetails invoice;

    @Autowired
    private ReportControllers reportsController;

    @Autowired
    public PaymentModelTypicalControllerUpdate paymentModelTypicalControllerUpdate;


    private boolean doAgain;

    public TypicalPaymentsController() {

        log.debug("The typicalPaymentsController has been invoked");
    }

    @Override
    public void runCalculation(BigDecimal invoiceAmount) {

        ResultsOutput resultsOutput;
        do {
            PaymentModel paymentModel =
                    paymentModelTypicalControllerUpdate
                            .setInvoiceAmount(invoiceAmount)
                            .invoke();

            resultsOutput = (ResultsOutput) viewResults.forPayment(paymentModel);
            // Results submitted for view

            doAgain = invoice.doAgain();
        } while (doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }
}
