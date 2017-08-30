package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.*;
import com.babel88.paycal.controllers.prepayments.PrepaymentController;
import com.babel88.paycal.controllers.prepayments.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
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

        parameters = LogicFactory.createPaymentParameters();
        partialTaxPaymentLogic = LogicFactory.createPartialTaxPaymentLogic();
        reportsController = ControllerFactory.createReportController();
        prepaymentController = ControllerFactory.createPrepaymentController();
        viewResults = ModelViewFactory.createResultsViewer();
        invoice = GeneralFactory.createInvoice();
        view = ModelViewFactory.createPaymentModelView();
        paymentModel = ModelFactory.createPaymentModel();

        doAgain = false;
    }

    public static PartialTaxPaymentController getInstance() {
        return instance;
    }

    @Override
    public void runCalculation(){

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

        }while(doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }

    /**
     * Retuns the DefaultPaymentModel currently in the delegator's class
     *
     * @return payment model
     */
    @Override
    public DefaultPaymentModel<Object> getPaymentModel() {

        return paymentModel;
    }

    /**
     * Returns the PrepaymentController object currently in the delegator's class
     *
     * @return Prepayment controller object
     */
    @Override
    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {

        return prepaymentController;
    }
}
