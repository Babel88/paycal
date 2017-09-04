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
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.models.ResultsOutput;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;

@SuppressWarnings("ALL")
public class ContractorPaymentsController implements DefaultControllers, PaymentsControllerRunner {

    private final Logger log = LoggerFactory.getLogger(ContractorPaymentsController.class);

    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private DefaultPaymentModel paymentModel;

    @Inject
    private InvoiceDetails invoiceDetails;

    private BigDecimal invoiceAmount;

    @Inject
    private DefaultLogic contractorLogic;

    @Inject
    private PrepaymentController prepaymentController;

    @Inject
    private ResultsViewer resultsViewer;

    @Inject
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
            resultsOutput = (ResultsOutput) resultsViewer.forPayment((PaymentModel) paymentModel);
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
        this.resultsViewer = viewResults;
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

    @Override
    public TTArguments getTtArguments() {
        return null;
    }
}
