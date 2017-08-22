package com.babel88.utils.tables.samples;

import com.babel88.paycal.config.PaymentParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public PaymentParameters paymentParameters(){

        return new PaymentParameters();
    }

    @Bean
    public ParametersTest parametersTest(){

        return new ParametersTest();
    }
}
