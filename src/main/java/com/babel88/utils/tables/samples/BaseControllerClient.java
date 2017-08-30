package com.babel88.utils.tables.samples;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.controllers.base.ContractorPaymentsController;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public class BaseControllerClient {

    private static DefaultControllers controller;


    public static void main(String[] args) {

        controller = new ContractorPaymentsController();

        controller.runCalculation();
    }

}
