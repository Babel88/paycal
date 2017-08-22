package com.babel88.paycal.config;

import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.controllers.ReportsController;
import com.babel88.paycal.controllers.TypicalPaymentsController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfigControllers {

    @Bean
    public TypicalPaymentsControllers typicalPaymentsController(){

        return new TypicalPaymentsController();
    }


    @Bean
    public ReportControllers reportsController(){

        return new ReportsController();
    }
}
