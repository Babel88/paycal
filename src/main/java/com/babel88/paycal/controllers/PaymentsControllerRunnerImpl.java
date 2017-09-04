package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.controllers.base.RentalPaymentsController;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Implementattion to run the runCalculation method and to call the prepayment delegate
 *
 * Created by edwin.njeru on 29/08/2017.
 */
public abstract class PaymentsControllerRunnerImpl implements PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);

    @Inject
    protected DefaultPaymentModel paymentModel;

    @Inject
    private InvoiceDetails invoice;

    @Inject
    private ResultsViewer resultsViewer;

    @Inject
    private ReportControllers reportController;

    @Inject
    private PrepaymentController prepaymentController;
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    protected BigDecimal invoiceAmount;
    private Boolean doAgain;

    protected PaymentsControllerRunnerImpl() {
        log.debug("Creating a rental payments controller",this);
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

    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }
}
