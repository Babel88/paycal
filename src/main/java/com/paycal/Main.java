package com.paycal;

import com.paycal.config.PaycalConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {


    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(PaycalConfig.class);

        PaycalApp app = context.getBean(PaycalApp.class);

        app.run();
    }
}

