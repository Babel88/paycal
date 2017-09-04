package com.babel88.paycal;

import com.babel88.paycal.config.factory.GeneralFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class MainTest {

    ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
    private PaycalApp paycalApp;
    @Before
    public void setUp() throws Exception {

        paycalApp = (PaycalApp) context.getBean("paycalApp");
    }

    @Test
    public void PaycalAppCreation() throws Exception {

        PaycalApp paycalApp1 = GeneralFactory.createPaycalApp();

        assertEquals(paycalApp,paycalApp1);
    }
}