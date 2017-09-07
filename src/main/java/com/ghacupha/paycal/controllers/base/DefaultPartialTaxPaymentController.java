package com.ghacupha.paycal.controllers.base;

import com.ghacupha.paycal.api.DefaultPaymentModel;
import com.ghacupha.paycal.api.InvoiceDetails;
import com.ghacupha.paycal.api.ResultsViewer;
import com.ghacupha.paycal.api.controllers.PartialTaxPaymentController;
import com.ghacupha.paycal.api.controllers.PaymentsControllerRunner;
import com.ghacupha.paycal.api.controllers.PrepaymentController;
import com.ghacupha.paycal.api.controllers.ReportControllers;
import com.ghacupha.paycal.api.logic.PartialTaxPaymentLogic;
import com.ghacupha.paycal.api.view.PaymentModelViewInterface;
import com.ghacupha.paycal.config.PaymentParameters;
import com.ghacupha.paycal.config.factory.ControllerFactory;
import com.ghacupha.paycal.config.factory.GeneralFactory;
import com.ghacupha.paycal.config.factory.LogicFactory;
import com.ghacupha.paycal.config.factory.ModelFactory;
import com.ghacupha.paycal.config.factory.ModelViewFactory;
import com.ghacupha.paycal.controllers.delegate.PrepaymentsDelegate;
import com.ghacupha.paycal.models.PaymentModel;
import com.ghacupha.paycal.models.ResultsOutput;
import com.ghacupha.paycal.models.TTArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class DefaultPartialTaxPaymentController implements PartialTaxPaymentController, PaymentsControllerRunner {

    private static PartialTaxPaymentController instance = new DefaultPartialTaxPaymentController();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private ResultsViewer viewResults;
    private InvoiceDetails invoice;
    private PaymentParameters parameters;
    private PrepaymentController prepaymentController;
    private PaymentModelViewInterface view;
    private PaymentModel paymentModel;
    private ReportControllers reportsController;
    private PartialTaxPaymentLogic partialTaxPaymentLogic;
    //used in computations
    private Boolean doAgain;

    private DefaultPartialTaxPaymentController() {
        log.debug("The PartialTaxPaymentController implementation has been invoked \n" +
                "instantiating internal variables");

        log.debug("Getting inner dependencies from factory...");

        parameters = LogicFactory.getPaymentParameters();
        partialTaxPaymentLogic = LogicFactory.getPartialTaxPaymentLogic();
        reportsController = ControllerFactory.getReportController();
        prepaymentController = ControllerFactory.getPrepaymentController();
        viewResults = ModelViewFactory.createResultsViewer();
        invoice = GeneralFactory.createInvoice();
        view = ModelViewFactory.createPaymentModelView();
        paymentModel = ModelFactory.getPaymentModel();

        doAgain = false;
    }

    public static PartialTaxPaymentController getInstance() {
        return instance;
    }

    @Override
    public void runCalculation() {

        ResultsOutput resultsOutput;

        do {
            BigDecimal invoiceAmount = invoice.invoiceAmount();
            BigDecimal vatAmount = BigDecimal.valueOf(invoice.vatAmount());

            paymentModel.setWithHoldingVat(
                    partialTaxPaymentLogic.calculateWithholdingVat(vatAmount)
            );

            paymentModel.setTotalExpense(
                    partialTaxPaymentLogic.calculateTotalExpense(invoiceAmount)
            );

            paymentModel.setToPayee(
                    partialTaxPaymentLogic
                            .calculateAmountPayableToVendor(paymentModel.getTotalExpense(), paymentModel.getWithHoldingVat())
            );

            paymentModel.setWithholdingTax(
                    partialTaxPaymentLogic.calculateWithholdingTax()
            );

            prepaymentsDelegate.updateToPrepay();

            resultsOutput = (ResultsOutput) viewResults.forPayment(paymentModel);
            // Results submitted for paymentModelView

            doAgain = invoice.doAgain();

        } while (doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }

    /**
     * Retuns the DefaultPaymentModel currently in the delegator's class
     *
     * @return payment model
     */
    @Override
    public DefaultPaymentModel getPaymentModel() {

        return paymentModel;
    }

    /**
     * Returns the PrepaymentController object currently in the delegator's class
     *
     * @return Prepayment controller object
     */
    @Override
    public PrepaymentController getPrepaymentController() {

        return prepaymentController;
    }

    @Override
    public TTArguments getTtArguments() {
        return null;
    }
}
