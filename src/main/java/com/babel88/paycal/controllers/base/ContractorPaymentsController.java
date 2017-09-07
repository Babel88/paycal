package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@SuppressWarnings("ALL")
public class ContractorPaymentsController implements DefaultControllers, PaymentsControllerRunner {

    private final Logger log = LoggerFactory.getLogger(ContractorPaymentsController.class);

    private PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);

    private DefaultPaymentModel paymentModel;

    private InvoiceDetails invoiceDetails;

    private BigDecimal invoiceAmount;

    private DefaultLogic contractorLogic;

    private PrepaymentController prepaymentController;

    private ResultsViewer resultsOutput;

    private ReportControllers reportController;

    private Boolean doAgain;

    public ContractorPaymentsController() {

        log.debug("ContractorPaymentsController created : {}",this);
    }

    @Override
    public void runCalculation() {

        ResultsOutput resultsOutput;

        do {
            invoiceAmount = invoiceDetails.invoiceAmount();
            updateTotalExpense();
            updateToPayee();
            updateWithholdingTax();
            updateWithholdingVat();
            prepaymentsDelegate.updateToPrepay();
            resultsOutput = (ResultsOutput) this.resultsOutput.forPayment((PaymentModel) paymentModel);
            // Results submitted for paymentModelView

            doAgain = invoiceDetails.doAgain();
        } while (doAgain);

        reportController.printReport().forPayment(resultsOutput);
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

    public DefaultControllers setViewResults(ResultsViewer viewResults) {
        this.resultsOutput = viewResults;
        return this;
    }

    public DefaultControllers setReportsController(ReportControllers reportsController) {
        this.reportController = reportsController;
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

    public ContractorPaymentsController setResultsOutput(ResultsViewer resultsOutput) {
        this.resultsOutput = resultsOutput;
        return this;
    }

    public ContractorPaymentsController setReportController(ReportControllers reportController) {
        this.reportController = reportController;
        return this;
    }

    public ContractorPaymentsController setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
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
