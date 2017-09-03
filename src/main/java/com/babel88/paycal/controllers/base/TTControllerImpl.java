package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.ForeignPaymentsController;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.TTController;
import com.babel88.paycal.api.logic.ExclusiveImportedServiceLogic;
import com.babel88.paycal.api.logic.InclusiveImportedServiceLogic;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.logic.base.InclusiveImportedServiceLogicImpl;
import com.babel88.paycal.models.TTArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Controller for telegraphic transfers
 *
 * Created by edwin.njeru on 01/09/2017.
 */
@Deprecated
public class TTControllerImpl implements TTController {

    private final ExclusiveImportedServiceLogic exclusiveImportedServiceLogic;
    private final InclusiveImportedServiceLogic inclusiveImportedServiceLogic =new InclusiveImportedServiceLogicImpl(this);
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final PrepaymentController prepaymentController;
    private static final Logger log = LoggerFactory.getLogger(TTControllerImpl.class);
    private static final DefaultControllers instance = new TTControllerImpl();
    private ForeignPaymentsController foreignPaymentsController;
    private TTArguments ttArguments;

    private TTControllerImpl() {

        log.debug("Initializing the TTController... : {}",this);

        this.foreignPaymentsController = ControllerFactory.getForeignPaymentsController();

        log.debug("Fetching prepayment controller from {}", ControllerFactory.getInstance());
        prepaymentController = ControllerFactory.getPrepaymentController();

        log.debug("Fetching the exclusive imported service from logic factory");
        exclusiveImportedServiceLogic = LogicFactory.getInstance().getExclusiveImportedServiceLogic();
        log.debug("Initializing the delegates. Starting with {}",exclusiveImportedServiceLogic);
        exclusiveImportedServiceLogic.initialization();

        ttArguments = foreignPaymentsController.getTtArguments();
    }

    /**
     * Updating the total expense amount in the payment model
     */
    @Override
    public void updateTotalExpense() {

        log.debug("Standby for total expense update...");
        BigDecimal totalExpense;
        if(ttArguments.getTaxExclusionPolicy()){
            // Supplier of imported service immune to withholding taxes
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedServiceLogic object : {}", exclusiveImportedServiceLogic);
            totalExpense = exclusiveImportedServiceLogic.calculateTotalExpenses();
            log.debug("Setting total expenses of {} in payment model : {}", totalExpense, foreignPaymentsController.getPaymentModel());
            foreignPaymentsController.getPaymentModel().setTotalExpense(totalExpense);
            log.debug("Total expense has been set: ", foreignPaymentsController.getPaymentModel().getTotalExpense());
        } else if(!ttArguments.getTaxExclusionPolicy()){
            log.debug("Imported services vendor subject to withholding tax charges, delegating \n" +
                    "to inclusiveImportedServiceLogic object : {}", inclusiveImportedServiceLogic);
            //Supplier of imported service subject to withholding taxes
            totalExpense = inclusiveImportedServiceLogic.calculateTotalExpenses();
            log.debug("Setting total expenses of {} in payment model : {}", totalExpense, foreignPaymentsController.getPaymentModel());
            foreignPaymentsController.getPaymentModel().setTotalExpense(totalExpense);
            log.debug("Total expense set : ", foreignPaymentsController.getPaymentModel().getTotalExpense());
        }
    }

    @Override
    public void updateToPayee() {
        log.debug("Standby for update to payee...");

        BigDecimal toPayee;
        if(ttArguments.getTaxExclusionPolicy()){

            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedServiceLogic object : {}", exclusiveImportedServiceLogic);
            toPayee = exclusiveImportedServiceLogic.calculateToPayee();
            log.debug("Setting toPayee of {} in payment model : {}", toPayee, foreignPaymentsController.getPaymentModel());
            foreignPaymentsController.getPaymentModel().setToPayee(toPayee);
            log.debug("ToPayee amount has been set: ", foreignPaymentsController.getPaymentModel().getToPayee());

        } else if(!ttArguments.getTaxExclusionPolicy()){

            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to inclusiveImportedServiceLogic object : {}", inclusiveImportedServiceLogic);
            toPayee = inclusiveImportedServiceLogic.calculateToPayee();
            log.debug("Setting toPayee of {} in payment model : {}", toPayee, foreignPaymentsController.getPaymentModel());
            foreignPaymentsController.getPaymentModel().setToPayee(toPayee);
            log.debug("ToPayee amount has been set: ", foreignPaymentsController.getPaymentModel().getToPayee());
        }
    }

    @Override
    public void updateWithholdingTax() {

        log.debug("Standby for update to the withholding tax...");

        BigDecimal withholdingTax;
        if(ttArguments.getTaxExclusionPolicy()){
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedServiceLogic object : {}", exclusiveImportedServiceLogic);
            withholdingTax = exclusiveImportedServiceLogic.calculateWithholdingTax();
            log.debug("Setting withholding tax amount of {} in payment model : {}", withholdingTax, foreignPaymentsController.getPaymentModel());
            foreignPaymentsController.getPaymentModel().setWithholdingTax(withholdingTax);
            log.debug("Withholding tax amount has been set: ", foreignPaymentsController.getPaymentModel().getWithholdingTax());

        } else if(!ttArguments.getTaxExclusionPolicy())
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to inclusiveImportedServiceLogic object : {}", inclusiveImportedServiceLogic);
        withholdingTax = inclusiveImportedServiceLogic.calculateWithholdingTax();
        log.debug("Setting withholding tax amount of {} in payment model : {}", withholdingTax, foreignPaymentsController.getPaymentModel());
        foreignPaymentsController.getPaymentModel().setWithholdingTax(withholdingTax);
        log.debug("Withholding tax amount has been set: ", foreignPaymentsController.getPaymentModel().getWithholdingTax());
    }

    @Override
    public void updateWithholdingVat() {

        BigDecimal withholdingVat;
        if(ttArguments.getTaxExclusionPolicy()) {
            withholdingVat = exclusiveImportedServiceLogic.calculateWithholdingVat();
            foreignPaymentsController.getPaymentModel().setWithHoldingVat(withholdingVat);
        } else if(!ttArguments.getTaxExclusionPolicy()){
            withholdingVat = inclusiveImportedServiceLogic.calculateWithholdingVat();
            foreignPaymentsController.getPaymentModel().setWithHoldingVat(withholdingVat);
        }
    }

    @Override
    public void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }


    /**
     * Retuns the DefaultPaymentModel currently in the foreignPaymentsController's class
     *
     * @return payment model
     */
    @Override
    public DefaultPaymentModel<Object> getPaymentModel() {

        return foreignPaymentsController.getPaymentModel();
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

    public static DefaultControllers getInstance() {
        return instance;
    }

    @Override
    public TTArguments getTtArguments() {

        return ttArguments;
    }

    public void setTtArguments(TTArguments ttArguments) {
        this.ttArguments = ttArguments;
    }
}
