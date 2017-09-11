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
public class DefaultTypicalPaymentsController implements DefaultControllers,PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);

    // Injected from IOC container
    private DefaultPaymentModel paymentModel;
    private InvoiceDetails invoiceDetails;
    private ResultsViewer resultsViewer;
    private ReportControllers reportController;
    private PrepaymentController prepaymentController;
    private DefaultLogic typicalPaymentsLogic;
    private Visitor modelViewerVisitor;
    private Visitor modelPrecisionVisitor;
    private Visitor reportingVisitor;

    //DO NOT INJECT
    private ResultsOutput resultsOutput;

    private BigDecimal invoiceAmount;

    private Boolean doAgain;

    public DefaultTypicalPaymentsController(InvoiceDetails invoiceDetails) {
        log.debug("Creating a rental payments controller using constructor in PaymentsControllerRunner : {} \n" +
                "with {} as argument",this,invoiceDetails);

        this.invoiceDetails = invoiceDetails;

        invoiceAmount = new BigDecimal(BigInteger.ZERO);
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
    public @NotNull void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    @Override
    public TTArguments getTtArguments() {
        return new TTArguments();
    }

    public DefaultTypicalPaymentsController setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    public DefaultTypicalPaymentsController setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
    }

    public DefaultTypicalPaymentsController setResultsViewer(ResultsViewer resultsViewer) {
        this.resultsViewer = resultsViewer;
        return this;
    }

    public DefaultTypicalPaymentsController setReportController(ReportControllers reportController) {
        this.reportController = reportController;
        return this;
    }

    public DefaultTypicalPaymentsController setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public DefaultTypicalPaymentsController setTypicalPaymentsLogic(DefaultLogic typicalPaymentsLogic) {
        this.typicalPaymentsLogic = typicalPaymentsLogic;
        return this;
    }

    public DefaultTypicalPaymentsController setResultsOutput(ResultsOutput resultsOutput) {
        this.resultsOutput = resultsOutput;
        return this;
    }

    public DefaultTypicalPaymentsController setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public DefaultTypicalPaymentsController setDoAgain(Boolean doAgain) {
        this.doAgain = doAgain;
        return this;
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

    public DefaultTypicalPaymentsController setModelViewerVisitor(Visitor modelViewerVisitor) {
        this.modelViewerVisitor = modelViewerVisitor;
        return this;
    }

    public DefaultTypicalPaymentsController setModelPrecisionVisitor(Visitor modelPrecisionVisitor) {
        this.modelPrecisionVisitor = modelPrecisionVisitor;
        return this;
    }

    public DefaultTypicalPaymentsController setReportingVisitor(Visitor reportingVisitor) {
        this.reportingVisitor = reportingVisitor;
        return this;
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

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public Boolean getDoAgain() {
        return doAgain;
    }
}
