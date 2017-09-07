package com.ghacupha.paycal.controllers.base;

import com.ghacupha.paycal.api.DefaultPaymentModel;
import com.ghacupha.paycal.api.InvoiceDetails;
import com.ghacupha.paycal.api.ResultsViewer;
import com.ghacupha.paycal.api.controllers.DefaultControllers;
import com.ghacupha.paycal.api.controllers.PaymentsControllerRunner;
import com.ghacupha.paycal.api.controllers.PrepaymentController;
import com.ghacupha.paycal.api.controllers.ReportControllers;
import com.ghacupha.paycal.api.logic.DefaultLogic;
import com.ghacupha.paycal.config.factory.ControllerFactory;
import com.ghacupha.paycal.config.factory.GeneralFactory;
import com.ghacupha.paycal.config.factory.LogicFactory;
import com.ghacupha.paycal.config.factory.ModelFactory;
import com.ghacupha.paycal.config.factory.ModelViewFactory;
import com.ghacupha.paycal.controllers.PaymentsControllerRunnerImpl;
import com.ghacupha.paycal.controllers.delegate.PrepaymentsDelegate;
import com.ghacupha.paycal.models.PaymentModel;
import com.ghacupha.paycal.models.ResultsOutput;
import com.ghacupha.paycal.models.TTArguments;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Controller for payments with 5% withholding tax
 * <p>
 * Created by edwin.njeru on 29/08/2017.
 */
@SuppressWarnings("ALL")
public class WithholdingTaxPaymentController extends PaymentsControllerRunnerImpl implements DefaultControllers, PaymentsControllerRunner {

    private static DefaultControllers instance = new WithholdingTaxPaymentController();
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final Logger log = LoggerFactory.getLogger(WithholdingTaxPaymentController.class);
    private final ResultsViewer resultsViewer;
    private final DefaultPaymentModel paymentModel;
    private final ReportControllers reportController;
    private final InvoiceDetails invoice;
    private final DefaultLogic withholdingTaxLogic;
    private final PrepaymentController prepaymentController;
    private boolean doAgain;
    private BigDecimal invoiceAmount;

    private WithholdingTaxPaymentController() {

        super();

        log.debug("Withholding tax payments controller created");

        log.debug("Fetching results viewer object from model view factory");
        resultsViewer = ModelViewFactory.createResultsViewer();

        log.debug("Fetching the payment model object from Model factory");
        paymentModel = ModelFactory.getInstance().getPaymentModel();

        log.debug("Fetching the report controller object from controller factory");
        reportController = ControllerFactory.getReportController();

        log.debug("Fetching the invoice details object from model view factory");
        invoice = GeneralFactory.createInvoice();

        log.debug("Fetching withholding tax payment logic from logic factory");
        withholdingTaxLogic = (DefaultLogic) LogicFactory.getWithholdingTaxPayments();

        log.debug("Fetching prepayment controller from controller factory");
        prepaymentController = ControllerFactory.getPrepaymentController();
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

    @Override
    public TTArguments getTtArguments() {
        return null;
    }
}
