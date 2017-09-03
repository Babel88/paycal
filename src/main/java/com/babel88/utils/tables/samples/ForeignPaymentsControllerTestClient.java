package com.babel88.utils.tables.samples;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.config.factory.ControllerFactory;

public class ForeignPaymentsControllerTestClient {

    private DefaultControllers foreignPaymentsController;

    public ForeignPaymentsControllerTestClient() {

        foreignPaymentsController = ControllerFactory.getForeignPaymentsController();
    }

    public static void main(String[] args){


        ForeignPaymentsControllerTestClient client =
                new ForeignPaymentsControllerTestClient();

        client.foreignPaymentsController.runCalculation();
    }
}
