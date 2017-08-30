package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.factory.*;
import com.babel88.paycal.controllers.PaymentsControllerRunnerImpl;
import com.babel88.paycal.controllers.prepayments.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class WithholdingTaxPaymentController extends PaymentsControllerRunnerImpl implements DefaultControllers, PaymentsControllerRunner {

    private static DefaultControllers instance = new WithholdingTaxPaymentController();
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final Logger log = LoggerFactory.getLogger(WithholdingTaxPaymentController.class);
    private final ResultsViewer resultsViewer;
    private final DefaultPaymentModel paymentModel;
    private final ReportControllers reportController;
    private final InvoiceDetails invoice;
    private final DefaultLogic withholdingTaxLogic;
    private final com.babel88.paycal.api.controllers.PrepaymentController prepaymentController;
    private boolean doAgain;
    private BigDecimal invoiceAmount;

    public WithholdingTaxPaymentController() {

        super();

        log.debug("Withholding tax payments controller created");

        log.debug("Fetching results viewer object from model view factory");
        resultsViewer = ModelViewFactory.getInstance().createResultsViewer();

        log.debug("Fetching the payment model object from Model factory");
        paymentModel = ModelFactory.getInstance().createPaymentModel();

        log.debug("Fetching the report controller object from controller factory");
        reportController = ControllerFactory.getInstance().createReportController();

        log.debug("Fetching the invoice details object from model view factory");
        invoice = GeneralFactory.getInstance().createInvoice();

        log.debug("Fetching withholding tax payment logic from logic factory");
        withholdingTaxLogic = (DefaultLogic) LogicFactory.getInstance().createWithholdingTaxPayments();

        log.debug("Fetching prepayment controller from controller factory");
        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();
    }

    @Contract(pure = true)
    public static DefaultControllers getInstance() {
        return instance;
    }

    @Override
    public void runCalculation() {
        ResultsOutput resultsOutput;

        do {

            invoiceAmount = invoice.invoiceAmount();

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

    @Override
    public void updateTotalExpense() {

        paymentModel.setTotalExpense(
                withholdingTaxLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

        paymentModel.setToPayee(
                withholdingTaxLogic.calculateToPayee(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingTax() {

        paymentModel.setWithholdingTax(
                withholdingTaxLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        paymentModel.setWithHoldingVat(
                withholdingTaxLogic.calculateWithholdingVat(invoiceAmount)
        );
    }

    /*@Override
    public void updateToPrepay() {

        BigDecimal totalExpense = paymentModel.getTotalExpense();

        prepaymentController.setExpenseAmount(totalExpense);

        paymentModel.setToPrepay(
                ((PrepaymentService) prepaymentController::getPrepayment).prepay(totalExpense)
        );
    }*/

    @Override
    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }
}
