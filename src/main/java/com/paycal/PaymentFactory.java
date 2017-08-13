package com.paycal;

import com.paycal.api.InvoiceDetails;
import com.paycal.api.Logic;
import com.paycal.api.PayCalView;
import com.paycal.logic.TypicalPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Class contains relevant switch statements for various areas in the program
 */
public class PaymentFactory {

    Logger log = LoggerFactory.getLogger(PaymentFactory.class);

    @Autowired
    private PayCalView view;

    private InvoiceDetails invoice;

    private Logic logic;

    public PaymentFactory(InvoiceDetails invoice,Logic logic) {

        this.invoice = invoice;
        this.logic = logic;
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
                logic.normal(invoice.invoiceAmount());
                break;
            case b:
                out.println();
                out.println("Normal trnx with withholding taxes:");
                out.println("----------------------------------");
                logic.taxToWithhold(invoice.invoiceAmount());
                break;
            case c:
                out.println();
                out.println("Payment with prepaid expenses:");
                out.println("-----------------------------");
                logic.withPrepayment(invoice.invoiceAmount());
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
                logic.tt();
                break;
            case f:
                out.println();
                out.println("Partially non-taxable payment");
                out.println("--------------------------------------------");
                logic.vatGiven(invoice.invoiceAmount(), invoice.vatAmount());
                break;
            case g:
                out.println();
                out.println("Contractor payments");
                out.println("--------------------------------------------");
                logic.contractor(invoice.invoiceAmount());
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
