package com.babel88.paycal.config;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.PaymentFactory;
import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.PayCalView;
import com.babel88.paycal.api.view.Tables;
import com.babel88.paycal.logic.BusinessLogic;
import com.babel88.paycal.util.aspects.LoggingAspect;
import com.babel88.paycal.view.Display;
import com.babel88.paycal.view.Invoice;
import com.babel88.paycal.view.Notifications;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import com.babel88.paycal.view.tables.TableMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.util.Scanner;

/**
 * Created by edwin.njeru on 10/08/2017.
 */
@EnableAspectJAutoProxy
@Configuration
@Import({
        ContextConfigModels.class,
        ContextConfigLogic.class,
        ContextConfigControllers.class,
        ContextConfigUtils.class
})
public class ContextConfigurations {

    @Bean
    public PaycalApp paycalApp(){

        return new PaycalApp();
    }

    @Bean
    public PayCalView view(){

        return new Display();
    }

    @Bean
    public InvoiceDetails invoice(){

        return new Invoice();
    }



    @Bean
    public Logic logic(){

        return new BusinessLogic();
    }

    @Bean
    public FeedBack feedBack(){

        return new Notifications();
    }

    @Bean
    public PaymentFactory factory(){

        return new PaymentFactory();
    }

    @Bean
    public PaymentAdvice paymentAdvice() {

        return new PaymentAdvice();
    }

    @Bean
    public LoggingAspect loggingAspect(){

        return new LoggingAspect();
    }

    @Bean
    public Tables tableString(){

        return new TableMaker();
    }

    @Bean
    public PrepaymentDetails prepaymentDetails(){

        return (PrepaymentDetails) invoice();
    }

    @Bean
    public Scanner keyboard(){

        return new Scanner(System.in);
    }

    @Bean
    public ForeignPaymentDetails foreignPaymentDetails(){

        return (ForeignPaymentDetails)invoice();
    }

}
