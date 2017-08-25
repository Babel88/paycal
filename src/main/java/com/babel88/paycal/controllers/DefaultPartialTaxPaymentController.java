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
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class DefaultPartialTaxPaymentController implements PartialTaxPaymentController {

    private static PartialTaxPaymentController instance = new DefaultPartialTaxPaymentController();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public ResultsViewer viewResults;
    @Autowired
    private InvoiceDetails invoice;
    //@Autowired
    private PaymentParameters parameters;
    //@Autowired
    private PrepaymentController prepaymentController;
    @Autowired
    private PaymentModelViewInterface view;
    @Autowired
    private PaymentModel paymentModel;
    //@Autowired
    private ReportControllers reportsController;
    //@Autowired
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

            paymentModel.setTotal(
                    partialTaxPaymentLogic.calculateTotalExpense(invoiceAmount)
            );

            paymentModel.setToPayee(
                    partialTaxPaymentLogic
                            .calculateAmountPayableToVendor(paymentModel.getTotal(), paymentModel.getWithHoldingVat())
            );

            paymentModel.setWithHoldingTax(
                    partialTaxPaymentLogic.calculateWithholdingTax()
            );

            prepaymentController.setExpenseAmount(paymentModel.getTotal());
            paymentModel.setToPrepay(
                    ((PrepaymentService) prepaymentController::getPrepayment).prepay(paymentModel.getTotal())
            );

            resultsOutput = (ResultsOutput) viewResults.forPayment(paymentModel);
            // Results submitted for paymentModelView

            doAgain = invoice.doAgain();

        }while(doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }
}
