package com.ghacupha.paycal.controllers;

import com.ghacupha.paycal.api.DefaultPaymentModel;
import com.ghacupha.paycal.api.InvoiceDetails;
import com.ghacupha.paycal.api.ResultsViewer;
import com.ghacupha.paycal.api.controllers.PaymentsControllerRunner;
import com.ghacupha.paycal.api.controllers.PrepaymentController;
import com.ghacupha.paycal.api.controllers.ReportControllers;
import com.ghacupha.paycal.config.factory.ControllerFactory;
import com.ghacupha.paycal.config.factory.GeneralFactory;
import com.ghacupha.paycal.config.factory.ModelFactory;
import com.ghacupha.paycal.config.factory.ModelViewFactory;
import com.ghacupha.paycal.controllers.base.RentalPaymentsController;
import com.ghacupha.paycal.controllers.delegate.PrepaymentsDelegate;
import com.ghacupha.paycal.models.PaymentModel;
import com.ghacupha.paycal.models.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Implementattion to run the runCalculation method and to call the prepayment delegate
 * <p>
 * Created by edwin.njeru on 29/08/2017.
 */
public abstract class PaymentsControllerRunnerImpl implements PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    protected final DefaultPaymentModel paymentModel;
    private final InvoiceDetails invoice;
    private final ResultsViewer resultsViewer;
    private final ReportControllers reportController;
    private final PrepaymentController prepaymentController;
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    protected BigDecimal invoiceAmount;
    private Boolean doAgain;

    protected PaymentsControllerRunnerImpl() {
        log.debug("Creating a rental payments controller");
        invoice = GeneralFactory.createInvoice();
        resultsViewer = ModelViewFactory.createResultsViewer();
        paymentModel = ModelFactory.getPaymentModel();
        reportController = ControllerFactory.getReportController();
        log.debug("Fetching prepayment controller from controller factory");
        prepaymentController = ControllerFactory.getPrepaymentController();
    }


    @Override
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

    public PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }
}
