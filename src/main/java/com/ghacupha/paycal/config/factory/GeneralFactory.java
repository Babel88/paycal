package com.ghacupha.paycal.config.factory;

import com.ghacupha.paycal.PaycalApp;
import com.ghacupha.paycal.PaymentFactory;
import com.ghacupha.paycal.api.ForeignPaymentDetails;
import com.ghacupha.paycal.api.InvoiceDetails;
import com.ghacupha.paycal.api.PrepaymentDetails;
import com.ghacupha.paycal.api.view.FeedBack;
import com.ghacupha.paycal.api.view.Tables;
import com.ghacupha.paycal.util.aspects.LoggingAspect;
import com.ghacupha.paycal.view.Invoice;
import com.ghacupha.paycal.view.Notifications;
import com.ghacupha.paycal.view.tables.TableMaker;

/**
 * factory containing general purpose objects
 * <p>
 * Created by edwin.njeru on 8/23/17.
 */
public class GeneralFactory {

    private static GeneralFactory instance = new GeneralFactory();

    public static GeneralFactory getInstance() {
        return instance;
    }

    public static PaycalApp createPaycalApp() {

        return PaycalApp.getInstance();
    }

    public static InvoiceDetails createInvoice() {

        return Invoice.getInstance();
    }

    public static FeedBack createFeedback() {

        return Notifications.getInstance();
    }

    public static LoggingAspect createLoggingAspect() {

        return LoggingAspect.getInstance();
    }

    public static Tables createTables() {

        return TableMaker.getInstance();
    }

    public static PrepaymentDetails createPrepaymentDetails() {

        return (PrepaymentDetails) createInvoice();
    }

    public static ForeignPaymentDetails createForeignPaymentDetails() {

        return (ForeignPaymentDetails) createInvoice();
    }

    public static PaymentFactory createPaymentFactory() {

        return PaymentFactory.getInstance();
    }
}
