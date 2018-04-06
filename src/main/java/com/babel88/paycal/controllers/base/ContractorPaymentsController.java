package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * This controller represents payments to contractors
 */
@SuppressWarnings("ALL")
public class ContractorPaymentsController implements DefaultControllers, PaymentsControllerRunner {

    private final Logger log = LoggerFactory.getLogger(ContractorPaymentsController.class);

    // new PrepaymentsDelegate(this); injected from container
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final DefaultPaymentModel paymentModel;
    private final InvoiceDetails invoiceDetails;
    private final DefaultLogic contractorLogic;
    private final PrepaymentController prepaymentController;
    private final Visitor modelViewerVisitor;
    private final Visitor modelPrecisionVisitor;
    private final Visitor reportingVisitor;

    private Boolean doAgain;
    private BigDecimal invoiceAmount;

    public ContractorPaymentsController(DefaultPaymentModel paymentModel, InvoiceDetails invoiceDetails, DefaultLogic contractorLogic, PrepaymentController prepaymentController, Visitor modelViewerVisitor, Visitor modelPrecisionVisitor, Visitor reportingVisitor) {
        this.paymentModel = paymentModel;
        this.invoiceDetails = invoiceDetails;
        this.contractorLogic = contractorLogic;
        this.prepaymentController = prepaymentController;
        this.modelViewerVisitor = modelViewerVisitor;
        this.modelPrecisionVisitor = modelPrecisionVisitor;
        this.reportingVisitor = reportingVisitor;

        log.debug("ContractorPaymentsController created : {}", this);
    }

    @Override
    public void runCalculation() {

        do {
            invoiceAmount = invoiceDetails.invoiceAmount();
            updateTotalExpense();
            updateToPayee();
            updateWithholdingTax();
            updateWithholdingVat();
            prepaymentsDelegate.updateToPrepay();
            paymentModel.accept(modelPrecisionVisitor);
            paymentModel.accept(modelViewerVisitor);

            doAgain = invoiceDetails.doAgain();
        } while (doAgain);

        paymentModel.accept(reportingVisitor);
    }

    @Override
    public void updateTotalExpense() {

        paymentModel.setTotalExpense(
                contractorLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

        paymentModel.setToPayee(
                contractorLogic.calculateToPayee(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingTax() {

        paymentModel.setWithholdingTax(
                contractorLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        paymentModel.setWithHoldingVat(
                contractorLogic.calculateWithholdingVat(invoiceAmount)
        );
    }

    @NotNull
    @Override
    public void updateToPrepay() {

        BigDecimal totalExpense = paymentModel.getTotalExpense();

        prepaymentController.setExpenseAmount(totalExpense);

        paymentModel.setToPrepay(
                ((PrepaymentService) prepaymentController::getPrepayment).prepay(totalExpense)
        );
    }

    @Override
    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    public DefaultControllers setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    @Override
    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {

        return prepaymentController;
    }

    public PrepaymentsDelegate getPrepaymentsDelegate() {
        return prepaymentsDelegate;
    }

    public DefaultLogic getContractorLogic() {
        return contractorLogic;
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

    public Boolean getDoAgain() {
        return doAgain;
    }

    public ContractorPaymentsController setDoAgain(Boolean doAgain) {
        this.doAgain = doAgain;
        return this;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    @Override
    public TTArguments getTtArguments() {
        return null;
    }

    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }
}
