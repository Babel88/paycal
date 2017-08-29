package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public abstract class PaymentsControllerRunner {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    private final InvoiceDetails invoice;
    private final ResultsViewer resultsViewer;
    private final DefaultPaymentModel paymentModel;
    private final ReportControllers reportController;
    private final com.babel88.paycal.api.controllers.PrepaymentController prepaymentController;
    protected BigDecimal invoiceAmount;
    private Boolean doAgain;

    protected PaymentsControllerRunner() {
        log.debug("Creating a rental payments controller");
        invoice = GeneralFactory.getInstance().createInvoice();
        resultsViewer = ModelViewFactory.getInstance().createResultsViewer();
        paymentModel = ModelFactory.getInstance().createPaymentModel();
        reportController = ControllerFactory.getInstance().createReportController();
        log.debug("Fetching prepayment controller from controller factory");
        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();
    }


    public void runCalculation() {
        ResultsOutput resultsOutput;

        do {

            invoiceAmount = invoice.invoiceAmount();

            updateWithholdingVat();

            updateWithholdingTax();

            updateTotalExpense();

            updateToPayee();

            updateToPrepay();

            resultsOutput = (ResultsOutput) resultsViewer.forPayment((PaymentModel) paymentModel);

            doAgain = invoice.doAgain();

        } while (doAgain);

        reportController.printReport().forPayment(resultsOutput);
    }

    protected abstract void updateTotalExpense();

    protected abstract void updateToPayee();

    protected abstract void updateWithholdingTax();

    protected abstract void updateWithholdingVat();

    public void updateToPrepay() {

        BigDecimal totalExpense = paymentModel.getTotalExpense();

        prepaymentController.setExpenseAmount(totalExpense);

        paymentModel.setToPrepay(
                ((PrepaymentService) prepaymentController::getPrepayment).prepay(totalExpense)
        );
    }
}
