package com.babel88.paycal.config;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.PaymentModelCareTaker;
import com.babel88.paycal.view.ResultsOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class ContextConfigModels {

    @Bean(initMethod = "init",destroyMethod = "destroy")
    public PaymentModel paymentModel(){

        return new PaymentModel();
    }

    @Bean
    @Lazy
    @Scope("prototype")
    public PaymentModelCareTaker paymentModelCareTaker() {

        return new PaymentModelCareTaker();
    }

    @Bean(initMethod = "resetOutput", destroyMethod = "resetOutput")
    public ResultsViewer viewResults(){

        return new ResultsOutput();
    }
}
