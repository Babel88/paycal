package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.*;
import com.babel88.paycal.api.logic.ExclusiveImportedServiceLogic;
import com.babel88.paycal.api.logic.InclusiveImportedServiceLogic;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.models.ResultsOutput;
import com.google.common.base.Objects;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Controller for telegraphic transfers
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class TTControllerImpl implements TTController {

    private final ExclusiveImportedServiceLogic exclusiveImportedServiceLogic;
    private final InclusiveImportedServiceLogic inclusiveImportedServiceLogic;
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final PrepaymentController prepaymentController;
    private static final Logger log = LoggerFactory.getLogger(TTControllerImpl.class);
    private static final TTController instance = new TTControllerImpl();
    private TTArguments ttArguments;
    private ResultsViewer resultsViewer;
    private DefaultPaymentModel paymentModel;
    private InvoiceDetails invoice;
    private ReportControllers reportController;

    public TTControllerImpl() {

        log.debug("Initializing the TTController... : {}",this);

        log.debug("Fetching prepayment controller from {}", ControllerFactory.getInstance());
        prepaymentController = ControllerFactory.getPrepaymentController();

//        log.debug("Fetching the exclusive imported service from logic factory");
//        exclusiveImportedServiceLogic = LogicFactory.getInstance().getExclusiveImportedServiceLogic();
//        log.debug("Initializing the delegates. Starting with {}",exclusiveImportedServiceLogic);
//        exclusiveImportedServiceLogic.initialization();

        ttArguments = ModelFactory.getTTArguments();

        resultsViewer = ModelViewFactory.createResultsViewer();

        paymentModel = ModelFactory.getPaymentModel();

        invoice = GeneralFactory.createInvoice();

        reportController = ControllerFactory.getReportController();

        exclusiveImportedServiceLogic = LogicFactory.getExclusivImportedServiceLogic();
        inclusiveImportedServiceLogic = LogicFactory.getInclusiveImportedServiceLogic();
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

        do {

            fetchArguments();

            updateWithholdingVat(ttArguments);

            updateWithholdingTax(ttArguments);

            updateTotalExpense(ttArguments);

            updateToPayee(ttArguments);

            updateToPrepay(ttArguments);

            resultsOutput = (ResultsOutput) resultsViewer.forPayment((PaymentModel) paymentModel);

            doAgain = invoice.doAgain();

        } while (doAgain);

        reportController.printReport().forPayment(resultsOutput);
    }

    private void fetchArguments() {
        ttArguments.setReverseVatRate(BigDecimal.valueOf(invoice.vatRate()));
        ttArguments.setWithholdingTaxRate(BigDecimal.valueOf(invoice.withHoldingTaxRate()));
        ttArguments.setInvoiceAmount(invoice.invoiceAmount());
        ttArguments.setTaxExclusionPolicy(invoice.exclusiveOfWithholdingTax());
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

    @Contract(pure = true)
    public static TTController getInstance() {
        return instance;
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
                Objects.equal(resultsViewer, that.resultsViewer) &&
                Objects.equal(getPaymentModel(), that.getPaymentModel()) &&
                Objects.equal(invoice, that.invoice) &&
                Objects.equal(reportController, that.reportController);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exclusiveImportedServiceLogic, inclusiveImportedServiceLogic, prepaymentsDelegate, getPrepaymentController(), getTtArguments(), resultsViewer, getPaymentModel(), invoice, reportController);
    }
}
