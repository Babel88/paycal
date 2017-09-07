package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.controllers.base.RentalPaymentsController;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.models.TTArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implementattion to run the runCalculation method and to call the prepayment delegate
 *
 * Created by edwin.njeru on 29/08/2017.
 */
public class PaymentsControllerRunnerImpl implements PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);

    protected DefaultPaymentModel paymentModel;

    private InvoiceDetails invoiceDetails;

    private ResultsViewer resultsViewer;

    private ReportControllers reportController;

    private PrepaymentController prepaymentController;

    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);

    protected BigDecimal invoiceAmount;
    private Boolean doAgain;

    public PaymentsControllerRunnerImpl(InvoiceDetails invoiceDetails) {
        log.debug("Creating an instance of the PaymentsControllerRunner superclass",this);
        invoiceAmount = new BigDecimal(BigInteger.ZERO);
        this.invoiceDetails = invoiceDetails;
    }

    public PaymentsControllerRunnerImpl() {
    }


    @Override
    public void runCalculation() {
        ResultsOutput resultsOutput;

        if(invoiceDetails != null) {

            do {

                invoiceAmount = invoiceDetails.invoiceAmount();

                updateWithholdingVat();

                updateWithholdingTax();

                updateTotalExpense();

                updateToPayee();

                updateToPrepay();

                resultsOutput = (ResultsOutput) resultsViewer.forPayment((PaymentModel) paymentModel);

                doAgain = invoiceDetails.doAgain();

            } while (doAgain);

            reportController.printReport().forPayment(resultsOutput);
        } else {

            log.error("Invoice details object is null");
        }

    }

    protected void updateTotalExpense(){};

    protected void updateToPayee(){};

    protected void updateWithholdingTax(){};

    protected void updateWithholdingVat(){};

    @Override
    public TTArguments getTtArguments() {
        return null;
    }

    public void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }

    public PaymentsControllerRunnerImpl setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    public PaymentsControllerRunnerImpl setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
    }

    public PaymentsControllerRunnerImpl setResultsOutput(ResultsViewer resultsOutput) {
        this.resultsViewer = resultsOutput;
        return this;
    }

    public PaymentsControllerRunnerImpl setReportController(ReportControllers reportController) {
        this.reportController = reportController;
        return this;
    }

    public PaymentsControllerRunnerImpl setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public PaymentsControllerRunnerImpl setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public PaymentsControllerRunnerImpl setDoAgain(Boolean doAgain) {
        this.doAgain = doAgain;
        return this;
    }
}
