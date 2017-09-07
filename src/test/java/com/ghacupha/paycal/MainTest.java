package com.ghacupha.paycal;

import com.ghacupha.paycal.config.factory.GeneralFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    private PaycalApp paycalApp;

    @Before
    public void setUp() throws Exception {

        paycalApp = GeneralFactory.createPaycalApp();
    }

    @Test
    public void PaycalAppCreation() throws Exception {

        PaycalApp paycalApp1 = GeneralFactory.createPaycalApp();

        assertEquals(paycalApp, paycalApp1);
    }
}