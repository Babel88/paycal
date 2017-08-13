package com.paycal.config;

import com.paycal.PaycalApp;
import com.paycal.PaymentFactory;
import com.paycal.api.*;
import com.paycal.logic.*;
import com.paycal.models.Invoice;
import com.paycal.models.PaymentParameters;
import com.paycal.view.Display;
import com.paycal.view.Notifications;
import com.paycal.view.reporting.PaymentAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by edwin.njeru on 10/08/2017.
 */
@Configuration
public class PaycalConfig {

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
    public Contractor contractor(){

        return new ContractorPayments();
    }

    @Bean
    public Prepayable prepayable(){

        return new SimplePrepayments();
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

        return new PaymentFactory(invoice(),logic());
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
    public TypicalPayment typicalPayment(){

        return new TypicalPayment();
    }

    @Bean
    public TypicalWithholdingTaxPayment withholdingTaxPayment(){

        return new TypicalWithholdingTaxPayment();

    }
}
