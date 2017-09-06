package com.babel88.paycal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.*;

public class MainTest {

    ApplicationContext context = new ClassPathXmlApplicationContext("Beans-test.xml");
    private PaycalApp paycalApp1;

    @Autowired
    private PaycalApp paycalApp;

    @Before
    public void setUp() throws Exception {

        paycalApp1 = (PaycalApp) context.getBean("paycalApp");
    }

    @Test
    public void PaycalAppCreation() throws Exception {

        assertNotNull(paycalApp1);
    }

    @Test
    public void dependencyInjectionIsWorking() throws Exception{

        assertNotNull(paycalApp);
    }
}