package com.babel88.paycal.config.factory;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.PaymentFactory;
import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.Tables;
import com.babel88.paycal.util.aspects.LoggingAspect;
import com.babel88.paycal.view.Invoice;
import com.babel88.paycal.view.Notifications;
import com.babel88.paycal.view.tables.TableMaker;

/**
 * factory containing general purpose objects
 *
 * Created by edwin.njeru on 8/23/17.
 */
public class GeneralFactory {

    private static GeneralFactory instance = new GeneralFactory();

    public static GeneralFactory getInstance() {
        return instance;
    }

    public static PaycalApp createPaycalApp(){

        return PaycalApp.getInstance();
    }

    public static InvoiceDetails createInvoice(){

        return Invoice.getInstance();
    }

    public static FeedBack createFeedback(){

        return Notifications.getInstance();
    }

    public static LoggingAspect createLoggingAspect(){

        return LoggingAspect.getInstance();
    }

    public static Tables createTables(){

        return TableMaker.getInstance();
    }

    public static PrepaymentDetails createPrepaymentDetails(){

        return (PrepaymentDetails)createInvoice();
    }

    public static ForeignPaymentDetails createForeignPaymentDetails(){

        return (ForeignPaymentDetails)createInvoice();
    }

    public static PaymentFactory createPaymentFactory(){

        return PaymentFactory.getInstance();
    }
}
