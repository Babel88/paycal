package com.babel88.paycal.config;

import com.babel88.paycal.controllers.support.PaymentModelTypicalControllerUpdate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
class ContextConfigUtils{

    @Bean
    public PaymentModelTypicalControllerUpdate paymentModelTypicalControllerUpdate() {

        return new PaymentModelTypicalControllerUpdate();
    }
}
