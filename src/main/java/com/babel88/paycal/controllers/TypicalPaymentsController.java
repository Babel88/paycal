package com.babel88.paycal.controllers;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.UtilFactory;
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

    private ReportControllers reportsController;

    private static TypicalPaymentsControllers instance = new TypicalPaymentsController();
    //@Autowired
    private PaymentModelTypicalControllerUpdate paymentModelTypicalControllerUpdate;


    private boolean doAgain;

    private TypicalPaymentsController() {

        log.debug("Fetching reports controller object from factory");

        reportsController = ControllerFactory.getInstance().createReportController();

        log.debug("The typicalPaymentsController has been invoked");

        log.debug("Creating payment model typical controller update object from factory");

        paymentModelTypicalControllerUpdate = UtilFactory.getInstance().createPaymentModelTypicalControllerUpdate();
    }

    public static TypicalPaymentsControllers getInstance() {
        return instance;
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
            // Results submitted for paymentModelView

            doAgain = invoice.doAgain();
        } while (doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }
}
