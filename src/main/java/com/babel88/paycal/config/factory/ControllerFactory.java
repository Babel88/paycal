package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.controllers.*;
import com.babel88.paycal.controllers.*;
import com.babel88.paycal.controllers.PrepaymentController;

/**
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

    public static TypicalPaymentsControllers createTypicalPaymentsController(){

        return TypicalPaymentsController.getInstance();
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

    public BaseController createBaseControllerTemplate() {

        return BaseControllerTemplate.getInstance();
    }

    public DefaultControllers createContractorPaymentController() {

        return ContractorPaymentsController.getInstance();
    }
}
