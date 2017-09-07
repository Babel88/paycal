package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.PartialTaxDetails;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;

/**
 * Controller for payments whose invoice amount consists of invoice amount which is partially applied
 * to the amount before taxes.
 * Testing this class shows that all things are ok, but this is only if you comment out the modules
 * whose implementation involves input output like the prepayment delegate the resultsOutput model, and
 * implement the partialTaxDetails interface in the test
 */
public class DefaultPartialTaxPaymentController implements DefaultControllers,PartialTaxPaymentController, PaymentsControllerRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private PartialTaxDetails partialTaxDetails;

    private PrepaymentController prepaymentController;

    private PaymentModel paymentModel;

    private ReportControllers reportController;

    private PartialTaxPaymentLogic partialTaxPaymentLogic;

    private ResultsOutput resultsOutput;

    //used in computations
    private Boolean doAgain;
    private BigDecimal invoiceAmount;
    private BigDecimal vatAmount;

    public DefaultPartialTaxPaymentController() {
        log.debug("The PartialTaxPaymentController implementation has been invoked : {}",this);

        doAgain = false;
    }

    @Override
    public void runCalculation(){

        //TODO Abstract this logic away from this controller
        ResultsOutput resultsOutput = new ResultsOutput();

        try {
            do {
                invoiceAmount = setInvoiceAmountFromConsole();
                vatAmount = setVatAmountFromConsole();

                updateWithholdingVat();

                updateTotalExpense();

                updateToPayee();

                updateWithholdingTax();

                updateToPrepay();

                //TODO Refactor into testable method(s) with interface
                resultsOutput = (ResultsOutput) this.resultsOutput.forPayment((PaymentModel) paymentModel);
                // Results submitted for paymentModelView

                doAgain = partialTaxDetails.doAgain();

            } while (doAgain);

            reportController.printReport().forPayment(resultsOutput);
        }catch (Exception e){

            if(resultsOutput == null){

                log.debug("The results output object is null");
            } else if(resultsOutput == null){

                log.debug("The resultsOutput object is null");
            }else if(paymentModel == null){

                log.debug("The payment model is null");
            }
            e.printStackTrace();
        }

    }

    /**
     * Updates the withholding vat in the payment model
     */
    @Override
    public void updateWithholdingVat() {

        paymentModel.setWithHoldingVat(
                partialTaxPaymentLogic.calculateWithholdingVat(vatAmount).setScale(2, HALF_EVEN)
        );

    }

    /**
     * Updates the total expense figure in the payment model
     */
    @Override
    public void updateTotalExpense() {

        try {
            if (paymentModel != null && partialTaxPaymentLogic != null &&
                    (invoiceAmount != null && !invoiceAmount.equals(BigDecimal.ZERO )))
            {
                paymentModel.setTotalExpense(
                        partialTaxPaymentLogic
                                .calculateTotalExpense(invoiceAmount)
                                .setScale(2, HALF_EVEN)
                );
            } else {
                if(paymentModel == null){
                    log.error("The payment model is null");
                } else if(partialTaxPaymentLogic == null){
                    log.debug("The partialTaxPaymentLogic is null");
                } else if(invoiceAmount == null || invoiceAmount.equals(BigDecimal.ZERO )){
                    log.debug("The invoice amount is null or zero");
                }

            }
        } catch (Exception e){

            e.printStackTrace();
            e.getMessage();
            e.getCause();
        }
    }

    /**
     * Updates the amount payable to payee in the payment model
     */
    @Override
    public void updateToPayee() {

        paymentModel.setToPayee(
                partialTaxPaymentLogic
                        .calculateAmountPayableToVendor(paymentModel.getTotalExpense(), paymentModel.getWithHoldingVat())
                        .setScale(2, HALF_EVEN)
        );
    }

    /**
     * Updates the withholding tax in the payment model
     */
    @Override
    public void updateWithholdingTax() {

        paymentModel.setWithholdingTax(
                partialTaxPaymentLogic.calculateWithholdingTax().setScale(2, HALF_EVEN)
        );
    }

    /**
     * Updates the amount to prepay in the payment model
     */
    @Override
    public @NotNull void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    /**
     * Fetches the invoice amount from the InvoiceDetails object
     * @return
     */
    @NotNull
    private BigDecimal setVatAmountFromConsole() {
        return BigDecimal.valueOf(partialTaxDetails.vatAmount());
    }

    /**
     * Fetches the vat amount from the InvoiceDetails object
     * @return
     */
    @NotNull
    private BigDecimal setInvoiceAmountFromConsole() {
        return partialTaxDetails.invoiceAmount();
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

    public PartialTaxPaymentLogic getPartialTaxPaymentLogic() {
        return partialTaxPaymentLogic;
    }

    @Override
    public TTArguments getTtArguments() {
        return null;
    }

    public DefaultPartialTaxPaymentController setPartialTaxDetails(PartialTaxDetails partialTaxDetails) {
        this.partialTaxDetails = partialTaxDetails;
        return this;
    }

    public DefaultPartialTaxPaymentController setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public DefaultPartialTaxPaymentController setPaymentModel(PaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    public DefaultPartialTaxPaymentController setReportController(ReportControllers reportController) {
        this.reportController = reportController;
        return this;
    }

    public DefaultPartialTaxPaymentController setPartialTaxPaymentLogic(PartialTaxPaymentLogic partialTaxPaymentLogic) {
        this.partialTaxPaymentLogic = partialTaxPaymentLogic;
        return this;
    }

    public DefaultPartialTaxPaymentController setDoAgain(Boolean doAgain) {
        this.doAgain = doAgain;
        return this;
    }

    public DefaultPartialTaxPaymentController setResultsOutput(ResultsOutput resultsOutput) {
        this.resultsOutput = resultsOutput;
        return this;
    }

    public DefaultPartialTaxPaymentController setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public DefaultPartialTaxPaymentController setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }
}
