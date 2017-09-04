package com.babel88.paycal;

import com.babel88.paycal.config.factory.GeneralFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bootstrap for the paycal application
 */
public class Main {


    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("Beans.xml");

        //PaycalApp paycalApp = GeneralFactory.createPaycalApp();

        PaycalApp paycalApp = (PaycalApp) context.getBean("paycalApp");

        paycalApp.run();


    }
}

