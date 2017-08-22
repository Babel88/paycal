package com.babel88.paycal.config;

import com.babel88.paycal.api.logic.*;
import com.babel88.paycal.controllers.PrepaymentController;
import com.babel88.paycal.logic.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.math.BigDecimal;

@Configuration
public class ContextConfigLogic {

    @Bean
    @Scope("prototype")
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
    public PrepaymentController prepaymentController(){

        return new PrepaymentController(BigDecimal.ZERO);
    }

    @Bean
    public GeneralConfigurations generalConfigurations(){

        return new GeneralConfigurations();
    }

    @Bean
    public Prepayable abstractPrepayment(){

        return new AbstractPrepayment(generalConfigurations());
    }

    @Bean
    public TelegraphicTransfers foreignPayments(){

        return new ForeignPayments();
    }

    @Bean
    public Contractors contractor(){

        return new ContractorPayments(parameters());
    }

    @Bean
    public Prepayments prepayable(){

        return new SimplePrepayments(parameters());
    }
}
