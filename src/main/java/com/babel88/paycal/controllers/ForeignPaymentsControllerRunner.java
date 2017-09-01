package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * This controller implements the PaymentsController with a process unique to typical payments
 * due to complexity of the problem space
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public abstract class ForeignPaymentsControllerRunner implements PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(ForeignPaymentsControllerRunner.class);
    protected final DefaultPaymentModel paymentModel;
    private final ForeignPaymentDetails invoice;
    private final ResultsViewer resultsViewer;
    private final ReportControllers reportController;
    private final com.babel88.paycal.api.controllers.PrepaymentController prepaymentController;
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    protected final TTArguments ttArguments;
    private Boolean doAgain;

    protected ForeignPaymentsControllerRunner() {

        log.debug("ForeingPaymentsController runner created");

        log.debug("Invoice details object from {}",GeneralFactory.getInstance());
        invoice = (ForeignPaymentDetails) GeneralFactory.createInvoice();
        log.debug("Results viewer object from {}",ModelViewFactory.getInstance());
        resultsViewer = ModelViewFactory.createResultsViewer();
        log.debug("Fetching payment model from {}",ModelFactory.getInstance());
        paymentModel = ModelFactory.createPaymentModel();
        log.debug("Reports controller from {}",ControllerFactory.getInstance());
        reportController = ControllerFactory.createReportController();
        log.debug("Fetching prepayment controller from {}",ControllerFactory.getInstance());
        prepaymentController = ControllerFactory.createPrepaymentController();
        log.debug("Fetching TT Arguments from {}",ModelFactory.getInstance());
        ttArguments = ModelFactory.getTTArguments();
    }

    /**
     * Pulls all methods for updating the payment model into a reversible loop
     * and delegates the prepayment service implementation to the prepayment
     * delegate
     */
    @Override
    public void runCalculation() {

        ResultsOutput resultsOutput;

        do {

            fetchArguments();

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

    private void fetchArguments() {
        ttArguments.setReverseVatRate(BigDecimal.valueOf(invoice.vatRate()));
        ttArguments.setWithholdingTaxRate(BigDecimal.valueOf(invoice.withHoldingTaxRate()));
        ttArguments.setInvoiceAmount(invoice.invoiceAmount());
        ttArguments.setTaxExclusionPolicy(invoice.exclusiveOfWithholdingTax());
    }

    /**
     * Updates the payment model with the amount payable to payee
     *
     */
    protected abstract void updateToPayee();

    /**
     * Updates the payment model with total expenses
     *
     */
    protected abstract void updateTotalExpense();

    /**
     * Updates the payment model with the withholding taxes
     *
     */
    protected abstract void updateWithholdingTax();

    /**
     * Updates the payment model with withholding vat
     *
     */
    protected abstract void updateWithholdingVat();

    /**
     * Retuns the DefaultPaymentModel currently in the delegator's class
     *
     * @return payment model
     */
    @Override
    public DefaultPaymentModel<Object> getPaymentModel(){

        return paymentModel;
    };

    /**
     * Returns the PrepaymentController object currently in the delegator's class
     *
     * @return Prepayment controller object
     */
    @Override
    public PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }
}
