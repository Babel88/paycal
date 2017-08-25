package com.babel88.paycal.config.context;

import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.PaymentModelCareTaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@Deprecated
public class Models {

    @Bean(initMethod = "init",destroyMethod = "destroy")
    public PaymentModel paymentModel(){

        return new PaymentModel();
    }

    @Bean
    public PaymentModelCareTaker paymentModelCareTaker() {

        return new PaymentModelCareTaker();
    }

}
