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
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * This controller represents payments to contractors
 *
 */
@SuppressWarnings("ALL")
public class ContractorPaymentsController implements DefaultControllers, PaymentsControllerRunner {

    private final Logger log = LoggerFactory.getLogger(ContractorPaymentsController.class);

    // new PrepaymentsDelegate(this); injected from container
    private PrepaymentsDelegate prepaymentsDelegate;
    private DefaultPaymentModel paymentModel;
    private InvoiceDetails invoiceDetails;
    private DefaultLogic contractorLogic;
    private PrepaymentController prepaymentController;
    private Visitor modelViewerVisitor;
    private Visitor modelPrecisionVisitor;
    private Visitor reportingVisitor;

    private Boolean doAgain;
    private BigDecimal invoiceAmount;

    public ContractorPaymentsController() {

        log.debug("ContractorPaymentsController created : {}",this);
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

    public DefaultControllers setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    public DefaultControllers setInvoice(InvoiceDetails invoice) {
        this.invoiceDetails = invoice;
        return this;
    }

    public DefaultControllers setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public DefaultControllers setContractorLogic(DefaultLogic defaultLogic) {
        this.contractorLogic = defaultLogic;
        return this;
    }

    public DefaultControllers setPrepaymentController(PrepaymentControllerImpl prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    @Override
    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {

        return prepaymentController;
    }

    public ContractorPaymentsController setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public ContractorPaymentsController setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
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

    public ContractorPaymentsController setModelViewerVisitor(Visitor modelViewerVisitor) {
        this.modelViewerVisitor = modelViewerVisitor;
        return this;
    }

    public Visitor getModelPrecisionVisitor() {
        return modelPrecisionVisitor;
    }

    public ContractorPaymentsController setModelPrecisionVisitor(Visitor modelPrecisionVisitor) {
        this.modelPrecisionVisitor = modelPrecisionVisitor;
        return this;
    }

    public Visitor getReportingVisitor() {
        return reportingVisitor;
    }

    public ContractorPaymentsController setReportingVisitor(Visitor reportingVisitor) {
        this.reportingVisitor = reportingVisitor;
        return this;
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

    public ContractorPaymentsController setPrepaymentsDelegate(PrepaymentsDelegate prepaymentsDelegate) {
        this.prepaymentsDelegate = prepaymentsDelegate;
        return this;
    }
}
