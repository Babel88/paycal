package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TTController;
import com.babel88.paycal.api.logic.ExclusiveImportedServiceLogic;
import com.babel88.paycal.api.logic.InclusiveImportedServiceLogic;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.models.TTArguments;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Controller for telegraphic transfers
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class TTControllerImpl implements TTController {

    private ExclusiveImportedServiceLogic exclusiveImportedServiceLogic;
    private InclusiveImportedServiceLogic inclusiveImportedServiceLogic;
    private PrepaymentController prepaymentController;
    private TTArguments ttArguments;
    private ResultsViewer resultsOutput;
    private DefaultPaymentModel paymentModel;
    private InvoiceDetails invoiceDetails;
    private ReportControllers reportController;

    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);


    private static final Logger log = LoggerFactory.getLogger(TTControllerImpl.class);



    public TTControllerImpl(InvoiceDetails invoiceDetails) {

        log.debug("Initializing the TTController... : {}",this);
        this.invoiceDetails = invoiceDetails;
    }

    /**
     * Pulls all methods for updating the payment model into a reversible loop
     * and delegates the prepayment service implementation to the prepayment
     * delegate
     */
    @Override
    public void runCalculation() {

        ResultsOutput resultsOutput;
        Boolean doAgain;

        try {

            do {

                fetchArguments();

                updateWithholdingVat(ttArguments);

                updateWithholdingTax(ttArguments);

                updateTotalExpense(ttArguments);

                updateToPayee(ttArguments);

                updateToPrepay(ttArguments);

                resultsOutput = (ResultsOutput) this.resultsOutput.forPayment((PaymentModel) paymentModel);

                doAgain = invoiceDetails.doAgain();

            } while (doAgain);

            reportController.printReport().forPayment(resultsOutput);
        } catch (Exception e){

            if(invoiceDetails == null) {
                log.error("The invoice details object is null");
                e.printStackTrace();
            } else if(this.resultsOutput == null){
                log.error("The invoice details object is null");
                e.printStackTrace();
            } else if(prepaymentsDelegate == null){

                log.error("The prepaymentDelegate object is null");

                e.printStackTrace();
            }

            e.printStackTrace();
        }
    }

    private void fetchArguments() {
        try {
            ttArguments.setReverseVatRate(BigDecimal.valueOf(invoiceDetails.vatRate()));
            ttArguments.setWithholdingTaxRate(BigDecimal.valueOf(invoiceDetails.withHoldingTaxRate()));
            ttArguments.setInvoiceAmount(invoiceDetails.invoiceAmount());
            ttArguments.setTaxExclusionPolicy(invoiceDetails.exclusiveOfWithholdingTax());
        } catch (Exception e) {

            if(invoiceDetails == null){

                log.error("The invoice details object is null");

                e.printStackTrace();
            } else if(ttArguments == null){

                log.error("The TTArguments object is null");

                e.printStackTrace();
            }
        }
    }

    /**
     * Updating the total expense amount in the payment model
     */
    @Override
    public DefaultPaymentModel updateTotalExpense(TTArguments ttArguments) {

        log.debug("Standby for total expense update...");
        BigDecimal totalExpense;
        if(ttArguments.getTaxExclusionPolicy()){
            // Supplier of imported service immune to withholding taxes
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedServiceLogic object : {}", exclusiveImportedServiceLogic);
            totalExpense = exclusiveImportedServiceLogic.calculateTotalExpenses(ttArguments);
            log.debug("Setting total expenses of {} in payment model : {}", totalExpense, paymentModel);
            paymentModel.setTotalExpense(totalExpense);
            log.debug("Total expense has been set: ", paymentModel.getTotalExpense());
        } else if(!ttArguments.getTaxExclusionPolicy()){
            log.debug("Imported services vendor subject to withholding tax charges, delegating \n" +
                    "to inclusiveImportedServiceLogic object : {}", inclusiveImportedServiceLogic);
            //Supplier of imported service subject to withholding taxes
            totalExpense = inclusiveImportedServiceLogic.calculateTotalExpenses(ttArguments);
            log.debug("Setting total expenses of {} in payment model : {}", totalExpense, paymentModel);
            paymentModel.setTotalExpense(totalExpense);
            log.debug("Total expense set : ", paymentModel.getTotalExpense());
        }

        return paymentModel;
    }

    @Override
    public DefaultPaymentModel updateToPayee(TTArguments ttArguments) {
        log.debug("Standby for update to payee...");

        BigDecimal toPayee;
        if(ttArguments.getTaxExclusionPolicy()){

            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedServiceLogic object : {}", exclusiveImportedServiceLogic);
            toPayee = exclusiveImportedServiceLogic.calculateToPayee(ttArguments);
            log.debug("Setting toPayee of {} in payment model : {}", toPayee, paymentModel);
            paymentModel.setToPayee(toPayee);
            log.debug("ToPayee amount has been set: ",paymentModel.getToPayee());

        } else if(!ttArguments.getTaxExclusionPolicy()){

            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to inclusiveImportedServiceLogic object : {}", inclusiveImportedServiceLogic);
            toPayee = inclusiveImportedServiceLogic.calculateToPayee(ttArguments);
            log.debug("Setting toPayee of {} in payment model : {}", toPayee,paymentModel);
            paymentModel.setToPayee(toPayee);
            log.debug("ToPayee amount has been set: ", paymentModel.getToPayee());
        }

        return paymentModel;
    }

    @Override
    public DefaultPaymentModel updateWithholdingTax(TTArguments ttArguments) {

        log.debug("Standby for update to the withholding tax...");

        BigDecimal withholdingTax;
        if(ttArguments.getTaxExclusionPolicy()){
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedServiceLogic object : {}", exclusiveImportedServiceLogic);
            withholdingTax = exclusiveImportedServiceLogic.calculateWithholdingTax(ttArguments);
            log.debug("Setting withholding tax amount of {} in payment model : {}", withholdingTax, paymentModel);
            paymentModel.setWithholdingTax(withholdingTax);
            log.debug("Withholding tax amount has been set: ",paymentModel.getWithholdingTax());

        } else if(!ttArguments.getTaxExclusionPolicy()) {
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to inclusiveImportedServiceLogic object : {}", inclusiveImportedServiceLogic);
            withholdingTax = inclusiveImportedServiceLogic.calculateWithholdingTax(ttArguments);
            log.debug("Setting withholding tax amount of {} in payment model : {}", withholdingTax, paymentModel);
            paymentModel.setWithholdingTax(withholdingTax);
            log.debug("Withholding tax amount has been set: ", paymentModel.getWithholdingTax());
        }

        return paymentModel;
    }

    @Override
    public DefaultPaymentModel updateWithholdingVat(TTArguments ttArguments) {

        BigDecimal withholdingVat;
        if(ttArguments.getTaxExclusionPolicy()) {
            withholdingVat = exclusiveImportedServiceLogic.calculateWithholdingVat(ttArguments);
            paymentModel.setWithHoldingVat(withholdingVat);
        } else if(!ttArguments.getTaxExclusionPolicy()){
            withholdingVat = inclusiveImportedServiceLogic.calculateWithholdingVat(ttArguments);
            paymentModel.setWithHoldingVat(withholdingVat);
        }

        return paymentModel;
    }

    @Override
    public void updateToPrepay(TTArguments ttArguments) {

        prepaymentsDelegate.updateToPrepay();
    }


    /**
     * Retuns the DefaultPaymentModel currently in the foreignPaymentsController's class
     *
     * @return payment model
     */
    @Override
    public DefaultPaymentModel getPaymentModel() {

        return paymentModel;
    }

    /**
     * Returns the PrepaymentController object currently in the foreignPaymentsController's class
     *
     * @return Prepayment controller object
     */
    @Override
    public PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }


    @Override
    public TTArguments getTtArguments() {

        return ttArguments;
    }

    public void setTtArguments(TTArguments ttArguments) {
        this.ttArguments = ttArguments;
    }


    public TTControllerImpl setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTControllerImpl that = (TTControllerImpl) o;
        return Objects.equal(exclusiveImportedServiceLogic, that.exclusiveImportedServiceLogic) &&
                Objects.equal(inclusiveImportedServiceLogic, that.inclusiveImportedServiceLogic) &&
                Objects.equal(prepaymentsDelegate, that.prepaymentsDelegate) &&
                Objects.equal(getPrepaymentController(), that.getPrepaymentController()) &&
                Objects.equal(getTtArguments(), that.getTtArguments()) &&
                Objects.equal(resultsOutput, that.resultsOutput) &&
                Objects.equal(getPaymentModel(), that.getPaymentModel()) &&
                Objects.equal(invoiceDetails, that.invoiceDetails) &&
                Objects.equal(reportController, that.reportController);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exclusiveImportedServiceLogic, inclusiveImportedServiceLogic, prepaymentsDelegate, getPrepaymentController(), getTtArguments(), resultsOutput, getPaymentModel(), invoiceDetails, reportController);
    }

    public TTControllerImpl setExclusiveImportedServiceLogic(ExclusiveImportedServiceLogic exclusiveImportedServiceLogic) {
        this.exclusiveImportedServiceLogic = exclusiveImportedServiceLogic;
        return this;
    }

    public TTControllerImpl setInclusiveImportedServiceLogic(InclusiveImportedServiceLogic inclusiveImportedServiceLogic) {
        this.inclusiveImportedServiceLogic = inclusiveImportedServiceLogic;
        return this;
    }

    public TTControllerImpl setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public TTControllerImpl setResultsOutput(ResultsViewer resultsOutput) {
        this.resultsOutput = resultsOutput;
        return this;
    }

    public TTControllerImpl setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
    }

    public TTControllerImpl setReportController(ReportControllers reportController) {
        this.reportController = reportController;
        return this;
    }
}
