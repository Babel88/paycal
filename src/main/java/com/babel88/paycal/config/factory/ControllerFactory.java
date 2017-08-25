package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.controllers.DefaultPartialTaxPaymentController;
import com.babel88.paycal.controllers.PrepaymentController;
import com.babel88.paycal.controllers.ReportsController;
import com.babel88.paycal.controllers.TypicalPaymentsController;

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

}
