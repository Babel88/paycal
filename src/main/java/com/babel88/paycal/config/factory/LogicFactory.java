package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.Router;
import com.babel88.paycal.api.logic.*;
import com.babel88.paycal.config.GeneralConfigurations;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.AbstractPrepayment;
import com.babel88.paycal.logic.BusinessLogicRouter;
import com.babel88.paycal.logic.base.*;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Will implement this and other factories while I figure out why the spring IOC
 * is not working
 *
 * Created by edwin.njeru on 8/23/17.
 */
public class LogicFactory {

    private static final Logger log = LoggerFactory.getLogger(LogicFactory.class);
    private static LogicFactory instance = new LogicFactory();


    private LogicFactory() {

        log.debug("Creating Logic factory instance");
    }

    public static LogicFactory getInstance(){

        return instance;
    }

    public static PaymentParameters getPaymentParameters(){

        log.debug("Returning a singleton instance of PaymentParameters object");

        return PaymentParameters.getInstance();

    }

    public static DefaultLogic getTypicalPayments() {

        log.debug("Returning a singleton instance of TypicalPayments object");

        return TypicalPayment.getInstance();
    }

    public static WithholdingTaxPayments getWithholdingTaxPayments(){

        log.debug("Returning a singleton instance of WithholdingTaxPayments object");

        return DefaultTypicalWithholdingTaxPayment.getInstance();
    }

    public static Router getMainLogicController(){

        log.debug("Returning a singleton instance of Logic object");

        return BusinessLogicRouter.getInstance();
    }

    public static GeneralConfigurations getGeneralConfigurations(){

        log.debug("Returning a singleton instance of GeneralConfigurations object");

        return GeneralConfigurations.getInstance();
    }

    public static Prepayable getPrepayable(){

        log.debug("Returning a singleton instance of Prepayable object");

        return AbstractPrepayment.getInstance();
    }

    public static TelegraphicTransfers getTelegraphicTransfers(){

        log.debug("Returning a singleton instance of TelegraphicTransfers object");

        return ForeignPayments.getInstance();
    }

    public static PartialTaxPaymentLogic getPartialTaxPaymentLogic(){

        log.debug("Returning a singleton instance of PartialTaxPayment object");

        return DefaultPartialTaxPaymentLogic.getInstance();
    }

    public static Contractors getContractors(){

        log.debug("Returning a singleton instance of Contractors object");

        return ContractorPayments.getInstance();
    }

    @Contract(pure = true)
    public static DefaultLogic getContractorLogic() {

        log.debug("Returning a singleton instance of Contractor logic");

        return ContractorLogic.getInstance();
    }

    @Contract(pure = true)
    public static DefaultLogic getRentalPaymentLogic() {

        log.debug("Returning a singleton instance of RentalPaymentLogic object");

        return RentalPaymentLogic.getInstance();
    }

    @Contract(pure = true)
    public static ExclusiveImportedServiceLogic getExclusivImportedServiceLogic() {
        return ExclusiveImportedServiceLogicImpl.getInstance();
    }

    @Contract(pure = true)
    public static InclusiveImportedServiceLogic getInclusiveImportedServiceLogic() {
        return InclusiveImportedServiceLogicImpl.getInstance();
    }
}

