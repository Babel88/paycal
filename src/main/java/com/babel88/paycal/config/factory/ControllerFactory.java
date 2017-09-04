package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.controllers.*;
import com.babel88.paycal.controllers.ReportsController;
import com.babel88.paycal.controllers.base.*;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory containing all controller objects
 *
 * Created by edwin.njeru on 8/23/17.
 */
public class ControllerFactory {

    private static final ControllerFactory instance = new ControllerFactory();
    private static final Logger log = LoggerFactory.getLogger(ControllerFactory.class);
    private static PaymentsControllerRunner paymentsControllerRunner;

    private ControllerFactory() {
    }

    public static ControllerFactory getInstance() {

        log.debug("Returning and instance of the controller factory");
        return instance;
    }

    public static PartialTaxPaymentController getPartialTaxPaymentController() {

        log.debug("Returning and instance of : {}", DefaultPartialTaxPaymentController.getInstance());
        return DefaultPartialTaxPaymentController.getInstance();
    }

    public static ReportControllers getReportController() {

        log.debug("Returning and instance of : {}", ReportsController.getInstance());
        return ReportsController.getInstance();
    }

    public static PrepaymentController getPrepaymentController() {

        log.debug("Returning and instance of : {}", PrepaymentControllerImpl.getInstance());
        return PrepaymentControllerImpl.getInstance();
    }

    public static DefaultControllers getContractorPaymentController() {

        log.debug("Returning and instance of : {}", ContractorPaymentsController.getInstance());
        return ContractorPaymentsController.getInstance();
    }

    public static DefaultControllers getWithholdingTaxPaymentController() {

        log.debug("Returning and instance of : {}", WithholdingTaxPaymentController.getInstance());
        return WithholdingTaxPaymentController.getInstance();
    }

    public static DefaultControllers getRentalPaymentsController() {

        log.debug("Returning and instance of : {}", RentalPaymentsController.getInstance());
        return RentalPaymentsController.getInstance();
    }

    public static DefaultControllers getDefaultTypicalPaymentsController() {
        log.debug("Returning and instance of : {}", DefaultTypicalPaymentsController.getInstance());
        return DefaultTypicalPaymentsController.getInstance();
    }

    public static TTController getTTController(){

        log.debug("Returning an instance of : {} , from the {}",
                TTControllerImpl.getInstance(),ControllerFactory.getInstance());
        return (TTController) TTControllerImpl.getInstance();
    }
}
