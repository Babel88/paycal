package com.babel88.paycal.controllers;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class TypicalPaymentsController implements TypicalPaymentsControllers {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PrepaymentController prepaymentController;

    @Autowired
    private ResultsViewer viewResults;

    @Autowired
    public InvoiceDetails invoice;

    @Autowired
    private PaymentModel paymentModel;

    @Autowired
    private ReportControllers reportsController;

    @Autowired
    private TypicalPayments typicalPayment;

    private boolean doAgain;

    private BigDecimal invoiceAmount;

    public TypicalPaymentsController() {
    }

    @Override
    public void runCalculation(BigDecimal invoiceAmount) {

        setInvoiceAmount(invoiceAmount);

        ResultsOutput resultsOutput;
        do {
            paymentModel.setAmountB4Vat(
                    typicalPayment.calculateAmountBeforeTax(invoiceAmount));

            //TODO logic for saving state and undoing

            paymentModel.setWithHoldingVat(
                    typicalPayment.calculateWithholdingVat(invoiceAmount));

            //TODO logic for saving state and undoing

            BigDecimal total =typicalPayment.calculateTotalExpense(invoiceAmount);

            paymentModel.setTotal(total);

            //TODO logic for saving state and undoing

            paymentModel.setToPayee(
                    typicalPayment.calculatePayableToVendor(invoiceAmount));

            // These variables have not been computed but we do need to have them ready
            // as Zero values in the displayResults method

            paymentModel.setWithHoldingTax(ZERO);

            prepaymentController.setExpenseAmount(total);

            BigDecimal toPrepay = ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

            paymentModel.setToPrepay(toPrepay);

            //TODO create controllers and view service for the view objects
            //TODO to subtract prepayment from expense in display
            //view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);

            resultsOutput = (ResultsOutput) viewResults.forPayment(paymentModel);
            // Results submitted for view

            doAgain = invoice.doAgain();
        } while (doAgain);

        reportsController.printReport().forPayment(resultsOutput);

    }

    public TypicalPaymentsController setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public TypicalPaymentsController setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;

        log.debug("Invoice amount set as : {}.",invoiceAmount);
        return this;
    }

    public PaymentModel getPaymentModel() {

        log.debug("Returning payment model : {}.",paymentModel.toString());
        return paymentModel;
    }

    public TypicalPaymentsController setPaymentModel(PaymentModel paymentModel) {
        this.paymentModel = paymentModel;

        log.debug("Setting payment model as : {}.",paymentModel);
        return this;
    }

    public TypicalPaymentsController setTypicalPayment(TypicalPayments typicalPayment) {

        log.debug("Setting the typicalPaymentsController as : {}.",typicalPayment.toString());
        this.typicalPayment = typicalPayment;
        return this;
    }
}
