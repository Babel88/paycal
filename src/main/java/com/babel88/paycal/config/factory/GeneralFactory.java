package com.babel88.paycal.config.factory;

import org.jetbrains.annotations.Contract;

/**
 * factory containing general purpose objects
 * <p>
 * Created by edwin.njeru on 8/23/17.
 */
@Deprecated
public class GeneralFactory {

    private static GeneralFactory instance = new GeneralFactory();

    @Contract(pure = true)
    public static GeneralFactory getInstance() {
        return instance;
    }

//    public static InvoiceDetails createInvoice(){
//
//        return Invoice.getInstance();
//    }

//    public static FeedBack createFeedback(){
//
//        return FeedBackImpl.getInstance();
//    }

//    public static LoggingAspect createLoggingAspect(){
//
//        return LoggingAspect.getInstance();
//    }

//    public static Tables createTables(){
//
//        return TablesImpl.getInstance();
//    }

//    public static PrepaymentDetails createPrepaymentDetails(){
//
//        return (PrepaymentDetails)createInvoice();
//    }
//
//    public static ForeignPaymentDetails createForeignPaymentDetails(){
//
//        return (ForeignPaymentDetails)createInvoice();
//    }

//    public static PaymentFactory createPaymentFactory(){
//
//        return PaymentFactory.getInstance();
//    }
}
