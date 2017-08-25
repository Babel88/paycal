package com.babel88.utils.tables.samples;

import com.babel88.paycal.config.PaymentParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ComponentScan
public class ParametersTest {

    private static Logger log = LoggerFactory.getLogger(ParametersTest.class);

    @Autowired
    private PaymentParameters paymentParameters;

    public BigDecimal getVatRate(){

        return paymentParameters.getVatRate();
    }

    public static void main(String[] args){

       /* ApplicationContext context =
                new AnnotationConfigApplicationContext(TestConfiguration.class);

        ParametersTest test = context.getBean(ParametersTest.class);

        log.info("The vat rate : {}.",test.getVatRate());*/


    }
}
