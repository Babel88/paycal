package com.ghacupha.paycal;

import com.ghacupha.paycal.config.factory.GeneralFactory;

/**
 * Bootstrap for the paycal application
 */
public class Main {


    public static void main(String[] args) {

        PaycalApp paycalApp = GeneralFactory.createPaycalApp();

        paycalApp.run();


    }
}

