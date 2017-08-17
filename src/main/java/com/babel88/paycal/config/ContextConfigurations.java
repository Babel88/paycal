package com.babel88.paycal.config;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.PaymentFactory;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.logic.*;
import com.babel88.paycal.api.*;
import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.api.logic.Prepayments;
import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.api.logic.WithholdingTaxPayments;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.PayCalView;
import com.babel88.paycal.api.view.Tables;
import com.babel88.paycal.logic.*;
import com.babel88.paycal.models.Invoice;
import com.babel88.paycal.util.aspects.LoggingAspect;
import com.babel88.paycal.view.Display;
import com.babel88.paycal.view.Notifications;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import com.babel88.paycal.view.tables.TableMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by edwin.njeru on 10/08/2017.
 */
@EnableAspectJAutoProxy
@Configuration
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
    public Contractors contractor(){

        return new ContractorPayments(parameters());
    }

    @Bean
    public Prepayments prepayable(){

        return new SimplePrepayments(parameters());
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
    public PaymentParameters parameters(){

        return new PaymentParameters();
    }

    @Bean
    public TypicalPayments typicalPayment(){

        return new TypicalPayment(parameters());
    }

    @Bean
    public WithholdingTaxPayments withholdingTaxPayment(){

        return new TypicalWithholdingTaxPayment(parameters());

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
    public GeneralConfigurations generalConfigurations(){

        return new GeneralConfigurations();
    }

    @Bean
    @Deprecated// Use interface instead, testing purposes only
    public AbstractPrepayment abstractPrepayment(){

        return new AbstractPrepayment(generalConfigurations());
    }

}
