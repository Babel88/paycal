package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Thi controller replaces the deprecated Typical payments controller, inorder to
 * provide adherence to DefaultControllers interface which is also compatible to
 * the PrepaymentDelegate
 * <p>
 * Created by edwin.njeru on 30/08/2017.
 */
public class DefaultTypicalPaymentsController implements DefaultControllers, PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    // new PrepaymentsDelegate(this); injected in IOC container
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);

    // Injected from IOC container
    private final DefaultPaymentModel paymentModel;
    private final InvoiceDetails invoiceDetails;
    private final ResultsViewer resultsViewer;
    private final ReportControllers reportController;
    private final PrepaymentController prepaymentController;
    private final DefaultLogic typicalPaymentsLogic;
    private final Visitor modelViewerVisitor;
    private final Visitor modelPrecisionVisitor;
    private final Visitor reportingVisitor;

    //DO NOT INJECT
    private ResultsOutput resultsOutput;

    private BigDecimal invoiceAmount;

    private Boolean doAgain;

    public DefaultTypicalPaymentsController(DefaultPaymentModel paymentModel, InvoiceDetails invoiceDetails, ResultsViewer resultsViewer, ReportControllers reportController, PrepaymentController prepaymentController, DefaultLogic typicalPaymentsLogic, Visitor modelViewerVisitor, Visitor modelPrecisionVisitor, Visitor reportingVisitor) {
        this.paymentModel = paymentModel;
        this.resultsViewer = resultsViewer;
        this.reportController = reportController;
        this.prepaymentController = prepaymentController;
        this.typicalPaymentsLogic = typicalPaymentsLogic;
        this.modelViewerVisitor = modelViewerVisitor;
        this.modelPrecisionVisitor = modelPrecisionVisitor;
        this.reportingVisitor = reportingVisitor;
        log.debug("Creating a rental payments controller using constructor in PaymentsControllerRunner : {} \n" +
                "with {} as argument", this, invoiceDetails);

        this.invoiceDetails = invoiceDetails;

        invoiceAmount = new BigDecimal(BigInteger.ZERO);
    }

    @Override
    public void runCalculation() {
        ResultsOutput resultsOutput;

        if (invoiceDetails != null) {

            do {

                invoiceAmount = invoiceDetails.invoiceAmount();

                updateWithholdingVat();

                updateWithholdingTax();

                updateTotalExpense();

                updateToPayee();

                updateToPrepay();

                paymentModel.accept(modelPrecisionVisitor);

                paymentModel.accept(modelViewerVisitor);

                doAgain = invoiceDetails.doAgain();

            } while (doAgain);

            paymentModel.accept(reportingVisitor);

        } else {

            log.error("Invoice details object is null");
        }

    }

    @Override
    public void updateTotalExpense() {

        paymentModel.setTotalExpense(
                typicalPaymentsLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

        paymentModel.setToPayee(
                typicalPaymentsLogic.calculateToPayee(invoiceAmount)
        );

    }

    @Override
    public void updateWithholdingTax() {

        paymentModel.setWithholdingTax(
                typicalPaymentsLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        paymentModel.setWithHoldingVat(
                typicalPaymentsLogic.calculateWithholdingVat(invoiceAmount)
        );

    }

    /**
     * Updates the amount to prepay in the payment model
     */
    @Override
    public @NotNull
    void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    @Override
    public TTArguments getTtArguments() {
        return new TTArguments();
    }

    public PrepaymentsDelegate getPrepaymentsDelegate() {
        return prepaymentsDelegate;
    }

    @Override
    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }


    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }

    public ResultsViewer getResultsViewer() {
        return resultsViewer;
    }

    public ReportControllers getReportController() {
        return reportController;
    }

    @Override
    public PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }

    public DefaultLogic getTypicalPaymentsLogic() {
        return typicalPaymentsLogic;
    }

    public ResultsOutput getResultsOutput() {
        return resultsOutput;
    }

    public DefaultTypicalPaymentsController setResultsOutput(ResultsOutput resultsOutput) {
        this.resultsOutput = resultsOutput;
        return this;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public DefaultTypicalPaymentsController setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public Boolean getDoAgain() {
        return doAgain;
    }

    public DefaultTypicalPaymentsController setDoAgain(Boolean doAgain) {
        this.doAgain = doAgain;
        return this;
    }

    public Visitor getModelViewerVisitor() {
        return modelViewerVisitor;
    }

    public Visitor getModelPrecisionVisitor() {
        return modelPrecisionVisitor;
    }

    public Visitor getReportingVisitor() {
        return reportingVisitor;
    }

}
