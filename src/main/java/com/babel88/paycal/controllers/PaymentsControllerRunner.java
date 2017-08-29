package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public abstract class PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    protected final DefaultPaymentModel paymentModel;
    private final InvoiceDetails invoice;
    private final ResultsViewer resultsViewer;
    private final ReportControllers reportController;
    private final com.babel88.paycal.api.controllers.PrepaymentController prepaymentController;
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    protected BigDecimal invoiceAmount;
    private Boolean doAgain;

    protected PaymentsControllerRunner() {
        log.debug("Creating a rental payments controller");
        invoice = GeneralFactory.getInstance().createInvoice();
        resultsViewer = ModelViewFactory.getInstance().createResultsViewer();
        paymentModel = ModelFactory.getInstance().createPaymentModel();
        reportController = ControllerFactory.getInstance().createReportController();
        log.debug("Fetching prepayment controller from controller factory");
        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();
    }


    public void runCalculation() {
        ResultsOutput resultsOutput;

        do {

            invoiceAmount = invoice.invoiceAmount();

            updateWithholdingVat();

            updateWithholdingTax();

            updateTotalExpense();

            updateToPayee();

            prepaymentsDelegate.updateToPrepay();

            resultsOutput = (ResultsOutput) resultsViewer.forPayment((PaymentModel) paymentModel);

            doAgain = invoice.doAgain();

        } while (doAgain);

        reportController.printReport().forPayment(resultsOutput);
    }

    protected abstract void updateTotalExpense();

    protected abstract void updateToPayee();

    protected abstract void updateWithholdingTax();

    protected abstract void updateWithholdingVat();

    public void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }
}
