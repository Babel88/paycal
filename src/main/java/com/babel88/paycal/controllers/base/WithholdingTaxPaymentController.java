package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Controller for payments with 5% withholding tax
 * <p>
 * Created by edwin.njeru on 29/08/2017.
 */
@SuppressWarnings("ALL")
public class WithholdingTaxPaymentController implements DefaultControllers, PaymentsControllerRunner, Serializable {

    private final Logger log = LoggerFactory.getLogger(WithholdingTaxPaymentController.class);

    // new PrepaymentsDelegate(this); inject in IOC
    private PrepaymentsDelegate prepaymentsDelegate;

    private final DefaultPaymentModel paymentModel;
    private final InvoiceDetails invoiceDetails;
    private final DefaultLogic withholdingTaxPayments;
    private final PrepaymentController prepaymentController;
    private final Visitor modelViewerVisitor;
    private final Visitor modelPrecisionVisitor;
    private final Visitor reportingVisitor;

    private boolean doAgain;
    private BigDecimal invoiceAmount;

    public WithholdingTaxPaymentController(DefaultPaymentModel paymentModel, InvoiceDetails invoiceDetails, DefaultLogic withholdingTaxPayments, PrepaymentController prepaymentController, Visitor modelViewerVisitor, Visitor modelPrecisionVisitor, Visitor reportingVisitor) {
        this.paymentModel = paymentModel;
        this.invoiceDetails = invoiceDetails;
        this.withholdingTaxPayments = withholdingTaxPayments;
        this.prepaymentController = prepaymentController;
        this.modelViewerVisitor = modelViewerVisitor;
        this.modelPrecisionVisitor = modelPrecisionVisitor;
        this.reportingVisitor = reportingVisitor;

        log.debug("Withholding tax payments controller created : {}", this);
    }

    @Override
    public void runCalculation() {

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
    }

    @Override
    public void updateTotalExpense() {

        paymentModel.setTotalExpense(
                withholdingTaxPayments.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

        paymentModel.setToPayee(
                withholdingTaxPayments.calculateToPayee(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingTax() {

        paymentModel.setWithholdingTax(
                withholdingTaxPayments.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        paymentModel.setWithHoldingVat(
                withholdingTaxPayments.calculateWithholdingVat(invoiceAmount)
        );
    }

    /**
     * Updates the amount to prepay in the payment model
     */
    @Override
    @NotNull
    public void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    @Override
    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    public PrepaymentsDelegate getPrepaymentsDelegate() {
        return prepaymentsDelegate;
    }

    public WithholdingTaxPaymentController setPrepaymentsDelegate(PrepaymentsDelegate prepaymentsDelegate) {
        this.prepaymentsDelegate = prepaymentsDelegate;
        return this;
    }

    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }

    public DefaultLogic getWithholdingTaxPayments() {
        return withholdingTaxPayments;
    }

    @Override
    public PrepaymentController getPrepaymentController() {
        return prepaymentController;
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

    public boolean isDoAgain() {
        return doAgain;
    }

    public WithholdingTaxPaymentController setDoAgain(boolean doAgain) {
        this.doAgain = doAgain;
        return this;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public WithholdingTaxPaymentController setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }
}
