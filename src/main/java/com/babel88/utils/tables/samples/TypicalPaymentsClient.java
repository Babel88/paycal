package com.babel88.utils.tables.samples;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.config.factory.ControllerFactory;

/**
 * Created by edwin.njeru on 30/08/2017.
 */
public class TypicalPaymentsClient {

    private static DefaultControllers typicalController;

    public static void main(String[] args) {

        typicalController = ControllerFactory.getInstance().getDefaultTypicalPaymentsController();

        typicalController.runCalculation();
    }
}
