package com.babel88.paycal;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Class contains relevant switch statements for various areas in the program
 */
public class PaymentFactory {

    private static PaymentFactory instance = new PaymentFactory();
    Logger log = LoggerFactory.getLogger(PaymentFactory.class);
    private PaymentModelViewInterface view;
    private InvoiceDetails invoice;
    private Logic logic;

    public PaymentFactory() {

        log.debug("Creating an instance of the PaymentFactory");
        //TODO include in factory
        logic = LogicFactory.getInstance().createMainLogicController();
        view = ModelViewFactory.getInstance().createPaymentModelView();
        invoice = GeneralFactory.getInstance().createInvoice();
    }

    public static PaymentFactory getInstance() {
        return instance;
    }

    private String usrChoice(){

        Scanner keyboard = new Scanner(System.in);
        String choice = new String(keyboard.next());

        return choice.toLowerCase();
    }

    public void mainSwitch(){

        mainOptions options;
        // initiating the enum class
        options = mainOptions.valueOf(usrChoice());

        switch (options){
            case a:
                out.println();
                out.println("Normal transaction:");
                out.println("-------------------");
                logic.normal();
                break;
            case b:
                out.println();
                out.println("Normal trnx with withholding taxes:");
                out.println("----------------------------------");
                logic.taxToWithhold();
                break;
            case c:
                out.println();
                out.println("Rental Expenses Payment:");
                out.println("-----------------------------");
                logic.rentalPayments();
                break;
            case d:
                out.println();
                out.println("PaycalApp paramenter adjustment protocol:");
                out.println("-------------------------------------");
                out.println(" Work in progress on this tool & other complex transactions");
                break;
            case e:
                out.println();
                out.println("Foreign transactions (Telegraphic transfers)");
                out.println("--------------------------------------------");
                logic.telegraphicTransfer();
                break;
            case f:
                out.println();
                out.println("Partially non-taxable payment");
                out.println("--------------------------------------------");
                logic.vatGiven();
                break;
            case g:
                out.println();
                out.println("Contractor payments");
                out.println("--------------------------------------------");
                logic.contractor();
                break;
            default:
                out.println();
                out.println("Kindly input a choice from the above menu");
                break;
        }


    }

    /**
     * mainOptions is a helper class providing enumeration
     * for switching between the main options when the applications
     * starts running
     */
    enum mainOptions{
        a,// Normal payment
        b,// Normal payment with withholding taxes
        c,// Payment with prepayments component
        d,// Adjust main paramenters
        e,// Foreign transactions(Telegraphic Transfers)
        f,// Partially non taxable payment
        g // 3% withholding tax
    }
}
