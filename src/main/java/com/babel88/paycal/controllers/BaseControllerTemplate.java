package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.BaseController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;
import com.babel88.paycal.config.factory.*;
import com.babel88.paycal.logic.NullPaymentModelPassedException;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.ResultsOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 25/08/2017.
 */
@Deprecated
public class BaseControllerTemplate implements BaseController {

    private static BaseController instance = new BaseControllerTemplate().initialization();
    private final Logger log = LoggerFactory.getLogger(BaseControllerTemplate.class);
    public ResultsViewer viewResults;
    private Boolean doAgain;
    private InvoiceDetails invoice;
    private ReportControllers reportsController;
    private DefaultPaymentModel paymentModel;
    private DefaultBaseLogicModel baseLogic;
    private BigDecimal invoiceAmount;

    protected BaseControllerTemplate() {

        log.debug("Creating the abstractBaseController...");

        log.debug("Fetching the reports controller from the controller factory");
        reportsController = ControllerFactory.getInstance().createReportController();

        log.debug("Fetching the view results from the model view factory");
        viewResults = ModelViewFactory.getInstance().createResultsViewer();

        log.debug("Fetching the invoice object from the general factory");
        invoice = GeneralFactory.getInstance().createInvoice();

        log.debug("Fetching the default payment model from the model factory");
        paymentModel = ModelFactory.getInstance().createPaymentModel();


    }

    public static BaseController getInstance() {
        return instance;
    }

    public BaseController initialization() {

        log.debug("Fetching the default base logic model from the subclass");
        baseLogic = getDefaultBaseLogicModelInstance();

        log.debug("Checking if the baseLogic is null");
        baseModelsNullCheck(baseLogic);

        return this;
    }

    /**
     * This is the main method of this class
     */
    @Override
    public void invoke() {

        log.debug("Invoke method called..., Fetching the invoice amount from user...");

        ResultsOutput resultsOutput;

        do {
            log.debug("Starting the controller loop...");

            updateWithholdingVat(invoiceAmount);

            updateTotalExpense(invoiceAmount);

            updateWithholdingTax(invoiceAmount);

            updatePrepayableAmount();

            updatePayableToVendor();

            log.debug("Creating the results output from the view results object, and displaying the \n" +
                    "data in a table string, using the paymentModel : {}.", paymentModel);
            resultsOutput = (ResultsOutput) viewResults.forPayment((PaymentModel) paymentModel);

            doAgain = invoice.doAgain();

            log.debug("User has chosen to repeat the calculation : {}.", doAgain);
        } while (doAgain);

        log.debug("Calling the reports controller to print the report, for the resultsOutput \n" +
                "object : {}.", resultsOutput);
        reportsController.printReport().forPayment(resultsOutput);
    }

    /**
     * updates the withholding vat in the payment model
     *
     * @param invoiceAmount supplied at runtime
     */
    @Override
    public void updateWithholdingVat(BigDecimal invoiceAmount) {

        log.debug("Checking if the paymentModel is null");
        baseModelsNullCheck(paymentModel);

        log.debug("Checking if the baseLogic is null");
        baseModelsNullCheck(baseLogic);

        log.debug("updateWithholdingVat({}) method called...", invoiceAmount);
        paymentModel.setWithHoldingVat(
                baseLogic.calculateWithholdingVat(invoiceAmount)
        );
    }

    /**
     * updates the total expense in the payment model
     *
     * @param invoiceAmount supplied at runtime
     */
    @Override
    public void updateTotalExpense(BigDecimal invoiceAmount) {
        log.debug("updateTotalExpense({}) method called...", invoiceAmount);
        paymentModel.setTotalExpense(
                baseLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    /**
     * calculates the withholding tax in the payment model
     *
     * @param invoiceAmount supplied at runtime
     */
    @Override
    public void updateWithholdingTax(BigDecimal invoiceAmount) {
        log.debug("updateWithholdingTax( {}. ) method called...", invoiceAmount);
        paymentModel.setWithholdingTax(
                baseLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    /**
     * gets amount for prepayment and updates the payment model
     */
    @Override
    public void updatePrepayableAmount() {

        log.debug("updatePrepayableAmount() method called...");
        paymentModel.setToPrepay(
                baseLogic.calculateToPrepay(paymentModel)
        );
    }

    /**
     * gets amount calculataed for payee an updates the payment model
     */
    @Override
    public void updatePayableToVendor() {

        log.debug("updatePrepayableAmount() method called...");
        try {
            paymentModel.setToPayee(
                    baseLogic.calculateToPayee(paymentModel)
            );
        } catch (NullPaymentModelPassedException e) {
            log.debug("Whoa!! Did someone pass a null paymentModel object ? :");
            e.printStackTrace();
        }
    }

    private void baseModelsNullCheck(DefaultPaymentModel defaultPaymentModel) {

        log.debug("The baseModelsNullCheck({}) has been called to check if any of the arguments \n" +
                        "is null. If so both methods are called from the factory to avoid null pointer exception \n" +
                        "with default behaviour. As a user you do not want this to happen at all",
                defaultPaymentModel.getClass());

        log.debug("Checking if the paymentModel is null");
        if (defaultPaymentModel != null) {

            // Do nothing
            log.debug("Good boy. The defaltPaymentModel : {}. is not null, proceeding with controller logic",
                    defaultPaymentModel.getClass());
        } else {

            log.debug("The developer has failed us, by calling a null paymentModel, calling \n" +
                    "the object from the factory");
            paymentModel = ModelFactory.getInstance().createPaymentModel();
        }
    }

    private void baseModelsNullCheck(DefaultBaseLogicModel defaultBaseLogicModel) {

        log.debug("The baseModelsNullCheck({}) has been called to check if any of the arguments \n" +
                        "is null. If so both methods are called from the factory to avoid null pointer exception \n" +
                        "with default behaviour. As a user you do not want this to happen at all",
                defaultBaseLogicModel.getClass());

        log.debug("Checking if the paymentModel is null");
        if (defaultBaseLogicModel != null) {

            // Do nothing
            log.debug("Good boy. The defaltPaymentModel : {}. is not null, proceeding with controller logic",
                    defaultBaseLogicModel.getClass());
        } else {

            log.debug("The developer has failed us, by calling a null paymentModel, calling \n" +
                    "the object from the factory");
            //baseLogic = LogicFactory.getInstance().createBaseLogicModel();
        }
    }

    /**
     * This method returns an instance of an object that implements the DefaultBaseLogic interface
     *
     * @return DefaultBaseLogicModel object
     */
    @Override
    public DefaultBaseLogicModel getDefaultBaseLogicModelInstance() {

        return LogicFactory.getInstance().createBaseLogicModelTemplate();
    }
}
