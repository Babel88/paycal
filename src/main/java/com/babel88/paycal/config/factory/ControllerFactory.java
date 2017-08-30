package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.controllers.ReportsController;
import com.babel88.paycal.controllers.base.*;
import com.babel88.paycal.controllers.prepayments.PrepaymentController;

/**
 * Factory containing all controller objects
 *
 * Created by edwin.njeru on 8/23/17.
 */
public class ControllerFactory {

    private static ControllerFactory instance =
            new ControllerFactory();

    private ControllerFactory() {
    }

    public static ControllerFactory getInstance() {
        return instance;
    }

    public static PartialTaxPaymentController createPartialTaxPaymentController(){

        return DefaultPartialTaxPaymentController.getInstance();
    }

    public static ReportControllers createReportController(){

        return ReportsController.getInstance();
    }

    public static PrepaymentController createPrepaymentController(){

        return PrepaymentController.getInstance();
    }

    public DefaultControllers createContractorPaymentController() {

        return ContractorPaymentsController.getInstance();
    }

    public DefaultControllers createWithholdingTaxPaymentController() {

        return WithholdingTaxPaymentController.getInstance();
    }

    public DefaultControllers createRentalPaymentsController() {

        return RentalPaymentsController.getInstance();
    }

    public DefaultControllers getDefaultTypicalPaymentsController() {
        return DefaultTypicalPaymentsController.getInstance();
    }
}
