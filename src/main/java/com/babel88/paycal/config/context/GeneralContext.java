package com.babel88.paycal.config.context;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.Tables;
import com.babel88.paycal.controllers.support.undo.UndoRedoAspect;
import com.babel88.paycal.util.aspects.LoggingAspect;
import com.babel88.paycal.view.Invoice;
import com.babel88.paycal.view.Notifications;
import com.babel88.paycal.view.tables.TableMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Scanner;

/**
 * Created by edwin.njeru on 10/08/2017.
 */
//@EnableAspectJAutoProxy
@Configuration
@Import({
        Models.class,
        Logic.class,
        Controllers.class,
        Utils.class,
        ModelView.class
})
@ComponentScan
@Deprecated
public class GeneralContext {



//    @Bean
//    public InvoiceDetails invoice(){
//
//        return new Invoice();
//    }



//    @Bean
//    public Logic logic(){
//
//        return new BusinessLogic();
//    }

//    @Bean
//    public FeedBack feedBack(){
//
//        return new Notifications();
//    }

//    @Bean
//    public PaymentFactory factory(){
//
//        return new PaymentFactory();
//    }

//    @Bean
//    public LoggingAspect loggingAspect(){
//
//        return new LoggingAspect();
//    }

//    @Bean
//    public UndoRedoAspect undoRedoAspect(){
//
//        return new UndoRedoAspect();
//    }

//    @Bean
//    public Tables tableString(){
//
//        return new TableMaker();
//    }

//    @Bean
//    public PrepaymentDetails prepaymentDetails(){
//
//        return (PrepaymentDetails) invoice();
//    }

//    @Bean
//    public Scanner keyboard(){
//
//        return new Scanner(System.in);
//    }

//    @Bean
//    public ForeignPaymentDetails foreignPaymentDetails(){
//
//        return (ForeignPaymentDetails)invoice();
//    }

}
