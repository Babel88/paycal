package com.babel88.paycal;

import com.babel88.paycal.config.ContextConfigurations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {


    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(ContextConfigurations.class);

        //TODO  context.registerShutdownHook();

        PaycalApp app = context.getBean(PaycalApp.class);

        app.run();


    }
}

