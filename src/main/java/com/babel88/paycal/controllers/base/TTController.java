package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.ExclusiveImportedService;
import com.babel88.paycal.api.logic.InclusiveImportedService;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.controllers.ForeignPaymentsControllerRunner;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.logic.base.ExclusiveImportedServiceImpl;
import com.babel88.paycal.logic.base.InclusiveImportedServiceImpl;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Controller for telegraphic transfers
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class TTController extends ForeignPaymentsControllerRunner
        implements DefaultControllers,PaymentsControllerRunner {

    private final ExclusiveImportedService exclusiveImportedService=new ExclusiveImportedServiceImpl(this);
    private final InclusiveImportedService inclusiveImportedService=new InclusiveImportedServiceImpl(this);
    private final PrepaymentsDelegate prepaymentsDelegate = new PrepaymentsDelegate(this);
    private final com.babel88.paycal.api.controllers.PrepaymentController prepaymentController;
    private static final Logger log = LoggerFactory.getLogger(TTController.class);
    private static final DefaultControllers instance = new TTController();

    public TTController() {
        super();
        log.debug("Initializing the TTController... : {}",this);

        log.debug("Fetching prepayment controller from {}", ControllerFactory.getInstance());
        prepaymentController = ControllerFactory.createPrepaymentController();
    }

    /**
     * Updating the total expense amount in the payment model
     */
    @Override
    public void updateTotalExpense() {

        log.debug("Standby for total expense update...");
        BigDecimal totalExpense;
        if(super.ttArguments.getTaxExclusionPolicy()){
            // Supplier of imported service immune to withholding taxes
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedService object : {}",exclusiveImportedService);
            totalExpense = exclusiveImportedService.calculateTotalExpenses();
            log.debug("Setting total expenses of {} in payment model : {}", totalExpense,super.paymentModel);
            super.paymentModel.setTotalExpense(totalExpense);
            log.debug("Total expense has been set: ",super.paymentModel.getTotalExpense());
        } else if(!super.ttArguments.getTaxExclusionPolicy()){
            log.debug("Imported services vendor subject to withholding tax charges, delegating \n" +
                    "to inclusiveImportedService object : {}",inclusiveImportedService);
            //Supplier of imported service subject to withholding taxes
            totalExpense = inclusiveImportedService.calculateTotalExpenses();
            log.debug("Setting total expenses of {} in payment model : {}", totalExpense,super.paymentModel);
            super.paymentModel.setTotalExpense(totalExpense);
            log.debug("Total expense set : ",super.paymentModel.getTotalExpense());
        }
    }

    @Override
    public void updateToPayee() {
        log.debug("Standby for update to payee...");

        BigDecimal toPayee;
        if(super.ttArguments.getTaxExclusionPolicy()){

            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedService object : {}",exclusiveImportedService);
            toPayee = exclusiveImportedService.calculateToPayee();
            log.debug("Setting toPayee of {} in payment model : {}", toPayee,super.paymentModel);
            super.paymentModel.setToPayee(toPayee);
            log.debug("ToPayee amount has been set: ",super.paymentModel.getToPayee());

        } else if(!super.ttArguments.getTaxExclusionPolicy()){

            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to inclusiveImportedService object : {}",inclusiveImportedService);
            toPayee = inclusiveImportedService.calculateToPayee();
            log.debug("Setting toPayee of {} in payment model : {}", toPayee,super.paymentModel);
            super.paymentModel.setToPayee(toPayee);
            log.debug("ToPayee amount has been set: ",super.paymentModel.getToPayee());
        }
    }

    @Override
    public void updateWithholdingTax() {

        log.debug("Standby for update to the withholding tax...");

        BigDecimal withholdingTax;
        if(super.ttArguments.getTaxExclusionPolicy()){
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to exclusiveImportedService object : {}",exclusiveImportedService);
            withholdingTax = exclusiveImportedService.calculateWithholdingTax();
            log.debug("Setting withholding tax amount of {} in payment model : {}", withholdingTax,super.paymentModel);
            super.paymentModel.setWithholdingTax(withholdingTax);
            log.debug("Withholding tax amount has been set: ",super.paymentModel.getWithholdingTax());

        } else if(!super.ttArguments.getTaxExclusionPolicy())
            log.debug("Imported services vendor immune to withholding tax charges, delegating \n" +
                    "to inclusiveImportedService object : {}",inclusiveImportedService);
        withholdingTax = inclusiveImportedService.calculateWithholdingTax();
        log.debug("Setting withholding tax amount of {} in payment model : {}", withholdingTax,super.paymentModel);
        super.paymentModel.setWithholdingTax(withholdingTax);
        log.debug("Withholding tax amount has been set: ",super.paymentModel.getWithholdingTax());
    }

    @Override
    public void updateWithholdingVat() {

        BigDecimal withholdingVat;
        if(super.ttArguments.getTaxExclusionPolicy()) {
            withholdingVat = exclusiveImportedService.calculateWithholdingVat();
            super.paymentModel.setWithHoldingVat(withholdingVat);
        } else if(!super.ttArguments.getTaxExclusionPolicy()){
            withholdingVat = inclusiveImportedService.calculateWithholdingVat();
            super.paymentModel.setWithHoldingVat(withholdingVat);
        }
    }

    @NotNull
    @Override
    public void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    public TTArguments getTTArgument(){

        log.debug("Fetching ttArguments from the TTController.\n" +
                "Ret val : {}",ttArguments);
        return ttArguments;
    }

    @Contract(pure = true)
    public static DefaultControllers getInstance() {
        return instance;
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
}
