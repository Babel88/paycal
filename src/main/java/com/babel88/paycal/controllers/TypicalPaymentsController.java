package com.babel88.paycal.controllers;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.config.factory.UtilFactory;
import com.babel88.paycal.controllers.support.PaymentModelTypicalControllerUpdate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class TypicalPaymentsController implements TypicalPaymentsControllers {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ResultsViewer viewResults;
    public InvoiceDetails invoice;
    private ReportControllers reportsController;

    private static TypicalPaymentsControllers instance = new TypicalPaymentsController();
    //@Autowired
    private PaymentModelTypicalControllerUpdate paymentModelTypicalControllerUpdate;


    private boolean doAgain;

    private TypicalPaymentsController() {

        log.debug("The typicalPaymentsController has been invoked");
        log.debug("Fetching invoice object from general factory");
        invoice = GeneralFactory.getInstance().createInvoice();
        log.debug("Fetching results viewer object from model view factory");
        viewResults = ModelViewFactory.getInstance().createResultsViewer();
        log.debug("Fetching reports controller object from factory");
        reportsController = ControllerFactory.getInstance().createReportController();
        log.debug("Fetching paymentModelTypicalControllerUpdate object from utility factory");
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
