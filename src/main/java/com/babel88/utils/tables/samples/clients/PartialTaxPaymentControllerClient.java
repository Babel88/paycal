package com.babel88.utils.tables.samples.clients;

import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.config.context.GeneralContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PartialTaxPaymentControllerClient {

    public static void main(String[] args){

        ApplicationContext context =
                new AnnotationConfigApplicationContext(GeneralContext.class);

        PartialTaxPaymentController controller
                = context.getBean(PartialTaxPaymentController.class);

        controller.runCalculation();

    }
}
