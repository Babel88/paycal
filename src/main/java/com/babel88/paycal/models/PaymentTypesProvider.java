package com.babel88.paycal.models;

import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.api.logic.PaymentTypeProviders;
import com.babel88.paycal.api.logic.TelegraphicTransfers;
import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.config.ContextConfigurations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PaymentTypesProvider implements PaymentTypeProviders {

    private final ApplicationContext paymentTypesContext;


    public PaymentTypesProvider() {

        paymentTypesContext = new AnnotationConfigApplicationContext(ContextConfigurations.class);
    }

    @Override
    public Object getPaymentType(String paymentTypeString){

        Object returnObject= paymentTypesContext.getBean(paymentTypeString);

        return downCasting(returnObject);
    }

    private Object downCasting(Object o){

        if(o instanceof TypicalPayments){

            TypicalPayments tp = (TypicalPayments) o;

            return tp;
        } else if(o instanceof Contractors){

            Contractors contractors = (Contractors) o;

            return contractors;
        }else if(o instanceof TelegraphicTransfers){

            TelegraphicTransfers tt = (TelegraphicTransfers) o;

            return tt;
        }

        return null;
    }
}
