package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.DefaultPrepayable;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.logic.*;
import com.babel88.paycal.config.GeneralConfigurations;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.AbstractPrepayment;
import com.babel88.paycal.logic.BusinessLogic;
import com.babel88.paycal.logic.DefaultPrepayment;
import com.babel88.paycal.logic.base.*;
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

    public static PaymentParameters createPaymentParameters(){

        log.debug("Returning a singleton instance of PaymentParameters object");

        return PaymentParameters.getInstance();

    }

    public static TypicalPayments createTypicalPayments(){

        log.debug("Returning a singleton instance of TypicalPayments object");

        return TypicalPayment.getInstance();
    }

    public static WithholdingTaxPayments createWithholdingTaxPayments(){

        log.debug("Returning a singleton instance of WithholdingTaxPayments object");

        return DefaultTypicalWithholdingTaxPayment.getInstance();
    }

    public static Logic createMainLogicController(){

        log.debug("Returning a singleton instance of Logic object");

        return BusinessLogic.getInstance();
    }

    public static GeneralConfigurations createGeneralConfigurations(){

        log.debug("Returning a singleton instance of GeneralConfigurations object");

        return GeneralConfigurations.getInstance();
    }

    public static Prepayable createPrepayable(){

        log.debug("Returning a singleton instance of Prepayable object");

        return AbstractPrepayment.getInstance();
    }

    public static TelegraphicTransfers createTelegraphicTransfers(){

        log.debug("Returning a singleton instance of TelegraphicTransfers object");

        return ForeignPayments.getInstance();
    }

    public static PartialTaxPaymentLogic createPartialTaxPaymentLogic(){

        log.debug("Returning a singleton instance of PartialTaxPayment object");

        return DefaultPartialTaxPaymentLogic.getInstance();
    }

    public static Contractors createContractors(){

        log.debug("Returning a singleton instance of Contractors object");

        return ContractorPayments.getInstance();
    }

    public static DefaultPrepayable createDefaultPrepayment(){

        log.debug("Returning a singleton instance of DefaultPrepayable object");

        return DefaultPrepayment.getInstance();
    }

}

