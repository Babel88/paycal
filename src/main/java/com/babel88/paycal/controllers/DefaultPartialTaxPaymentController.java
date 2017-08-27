package com.babel88.paycal.controllers;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class DefaultPartialTaxPaymentController implements PartialTaxPaymentController {

    private static PartialTaxPaymentController instance = new DefaultPartialTaxPaymentController();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
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

        parameters = LogicFactory.getInstance().createPaymentParameters();
        partialTaxPaymentLogic = LogicFactory.getInstance().createPartialTaxPaymentLogic();
        reportsController = ControllerFactory.getInstance().createReportController();
        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();
        viewResults = ModelViewFactory.getInstance().createResultsViewer();
        invoice = GeneralFactory.getInstance().createInvoice();
        view = ModelViewFactory.getInstance().createPaymentModelView();
        paymentModel = ModelFactory.getInstance().createPaymentModel();

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

            prepaymentController.setExpenseAmount(paymentModel.getTotalExpense());
            paymentModel.setToPrepay(
                    ((PrepaymentService) prepaymentController::getPrepayment).prepay(paymentModel.getTotalExpense())
            );

            resultsOutput = (ResultsOutput) viewResults.forPayment(paymentModel);
            // Results submitted for paymentModelView

            doAgain = invoice.doAgain();

        }while(doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }
}
