package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.models.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;

public class DefaultPartialTaxPaymentController implements PartialTaxPaymentController, PaymentsControllerRunner {

    private static PartialTaxPaymentController instance = new DefaultPartialTaxPaymentController();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);

    @Inject
    private ResultsViewer resultsViewer;

    @Inject
    private InvoiceDetails invoiceDetails;

    @Inject
    private PaymentParameters parametersParameters;

    @Inject
    private PrepaymentController prepaymentController;

    @Inject
    private PaymentModelViewInterface display;

    @Inject
    private PaymentModel paymentModel;

    @Inject
    private ReportControllers reportController;

    @Inject
    private PartialTaxPaymentLogic partialTaxPaymentLogic;
    //used in computations
    private Boolean doAgain;

    private DefaultPartialTaxPaymentController() {
        log.debug("The PartialTaxPaymentController implementation has been invoked : {}",this);

        doAgain = false;
    }

    public static PartialTaxPaymentController getInstance() {
        return instance;
    }

    @Override
    public void runCalculation(){

        ResultsOutput resultsOutput;

        do {
            BigDecimal invoiceAmount = invoiceDetails.invoiceAmount();
            BigDecimal vatAmount = BigDecimal.valueOf(invoiceDetails.vatAmount());

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

            resultsOutput = (ResultsOutput) resultsViewer.forPayment(paymentModel);
            // Results submitted for paymentModelView

            doAgain = invoiceDetails.doAgain();

        }while(doAgain);

        reportController.printReport().forPayment(resultsOutput);

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
    public com.babel88.paycal.api.controllers.PrepaymentController getPrepaymentController() {

        return prepaymentController;
    }

    @Override
    public TTArguments getTtArguments() {
        return null;
    }
}
