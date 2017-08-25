package com.babel88.utils.tables.samples.clients;

import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.config.context.GeneralContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

public class TypicalPaymentsControllerTestClient {

    @Autowired
    private TypicalPaymentsControllers typicalPaymentsController;

    public static void main(String[] args){

        ApplicationContext context =
                new AnnotationConfigApplicationContext(GeneralContext.class);

        TypicalPaymentsControllers controller  =
                context.getBean(TypicalPaymentsControllers.class);

        controller.runCalculation(BigDecimal.valueOf(116000));

        /*PaycalApp paycalApp = context.getBean(PaycalApp.class);

        paycalApp.run();*/
    }
}
