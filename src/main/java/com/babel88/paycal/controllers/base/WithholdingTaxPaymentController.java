package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.factory.*;
import com.babel88.paycal.controllers.PaymentsControllerRunnerImpl;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.models.ResultsOutput;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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

    @Inject
    private ResultsViewer resultsViewer;

    @Inject
    private DefaultPaymentModel paymentModel;

    @Inject
    private ReportControllers reportController;

    @Inject
    private InvoiceDetails invoiceDetails;

    @Inject
    private DefaultLogic withholdingTaxLogic;

    @Inject
    private PrepaymentController prepaymentController;

    private boolean doAgain;
    private BigDecimal invoiceAmount;

    private WithholdingTaxPaymentController() {

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

            resultsOutput = (ResultsOutput) resultsViewer.forPayment((PaymentModel) paymentModel);

            doAgain = invoiceDetails.doAgain();

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
