package com.babel88.paycal;

import com.babel88.paycal.config.factory.GeneralFactory;

public class Main {


    public static void main(String[] args) {
//
//        ApplicationContext context =
//                new AnnotationConfigApplicationContext(GeneralContext.class);

        //TODO create factory for models
        //TODO craate factory for modelviews

        PaycalApp app = GeneralFactory.getInstance().createPaycalApp();

        app.run();


    }
}

