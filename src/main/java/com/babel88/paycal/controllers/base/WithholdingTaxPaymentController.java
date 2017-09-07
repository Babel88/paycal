package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.controllers.PaymentsControllerRunnerImpl;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.models.TTArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Controller for payments with 5% withholding tax
 *
 * Created by edwin.njeru on 29/08/2017.
 */
@SuppressWarnings("ALL")
public class WithholdingTaxPaymentController extends PaymentsControllerRunnerImpl implements DefaultControllers, PaymentsControllerRunner {

    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final Logger log = LoggerFactory.getLogger(WithholdingTaxPaymentController.class);

    private ResultsViewer resultsOutput;

    private DefaultPaymentModel paymentModel;

    private ReportControllers reportController;

    private InvoiceDetails invoiceDetails;

    private DefaultLogic withholdingTaxPayments;

    private PrepaymentController prepaymentController;

    private boolean doAgain;
    private BigDecimal invoiceAmount;

    public WithholdingTaxPaymentController() {

        super();

        log.debug("Withholding tax payments controller created : {}",this);
    }

    @Override
    public void runCalculation() {
        ResultsOutput resultsOutput;

        do {

            invoiceAmount = invoiceDetails.invoiceAmount();

            updateWithholdingVat();

            updateWithholdingTax();

            updateTotalExpense();

            updateToPayee();

            prepaymentsDelegate.updateToPrepay();

            resultsOutput = (ResultsOutput) this.resultsOutput.forPayment((PaymentModel) paymentModel);

            doAgain = invoiceDetails.doAgain();

        } while (doAgain);

        reportController.printReport().forPayment(resultsOutput);
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

    /*@Override
    public void updateToPrepay() {

        BigDecimal totalExpense = paymentModel.getTotalExpense();

        prepaymentController.setExpenseAmount(totalExpense);

        paymentModel.setToPrepay(
                ((PrepaymentService) prepaymentController::getPrepaymentConfigurations).prepay(totalExpense)
        );
    }*/

    @Override
    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    @Override
    public TTArguments getTtArguments() {
        return null;
    }

    @Override
    public WithholdingTaxPaymentController setResultsOutput(ResultsViewer resultsOutput) {
        this.resultsOutput = resultsOutput;
        return this;
    }

    @Override
    public WithholdingTaxPaymentController setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    @Override
    public WithholdingTaxPaymentController setReportController(ReportControllers reportController) {
        this.reportController = reportController;
        return this;
    }

    @Override
    public WithholdingTaxPaymentController setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
    }

    public WithholdingTaxPaymentController setWithholdingTaxPayments(DefaultLogic withholdingTaxPayments) {
        this.withholdingTaxPayments = withholdingTaxPayments;
        return this;
    }

    @Override
    public WithholdingTaxPaymentController setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }
}
