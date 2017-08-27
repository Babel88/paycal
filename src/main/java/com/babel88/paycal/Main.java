package com.babel88.paycal;

import com.babel88.paycal.config.factory.GeneralFactory;

/**
 * Main entry point for the paycal application
 */
public class Main {


    public static void main(String[] args) {

        PaycalApp app = GeneralFactory.getInstance().createPaycalApp();

        app.run();


    }
}

