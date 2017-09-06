package com.babel88.paycal.view;

import com.babel88.paycal.api.view.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static java.lang.System.getProperty;
import static java.lang.System.out;

public class FeedBackImpl implements FeedBack {

    private final Logger log = LoggerFactory.getLogger(FeedBackImpl.class);


    public FeedBackImpl(){
        log.debug("Creating an instance of FeedBackImpl : {}",this);
    }

    @Override
    public void printIntro(){
        // To include further info about the app
        // What is it
        // The version
        // What it does
        // Technologies Included & Licence info
        //
        String paycal = "\n" +
                "\n" +
                "                                  _ \n" +
                "                                 | |\n" +
                " _ __    __ _  _   _   ___  __ _ | |\n" +
                "| '_ \\  / _` || | | | / __|/ _` || |\n" +
                "| |_) || (_| || |_| || (__| (_| || |\n" +
                "| .__/  \\__,_| \\__, | \\___|\\__,_||_|\n" +
                "| |             __/ |               \n" +
                "|_|            |___/                \n" +
                "\n";

        out.println(paycal + "4.5-SNAPSHOT \n"
                + "Payments calculator functions \n"
                + "Written in java 8 update 141 \n"
                + "Subject to Oracle Binary code Licence agreement \n"
                + "http://www.oracle.com/technetwork/java/javase/terms/license/index.html\n"
                + "Strict Privacy and Confidentiality Conditions Apply\n"
                + "All rights reserved\n"
                + "Finance Dept (c)2017\n"
                + "====================");
    }

    @Override
    public void mainPrompt(){
        out.println();
        String user = new String(getProperty("user.name"));

        user.toLowerCase();
        // Get username from the system
        out.print(user+"@paycal: ");
    }

    @Override
    public void payeeName(){
        out.println();
        out.println("Kindly provide the name of the payee...");
    }

    @Override
    public void vatRate(){
       out.println();
        out.println("What is the VAT rate applicable\n" +
                "Hint: Use whole numbers e.g. 17.5,20,13.5...");
    }

    @Override
    public void withHoldingTaxRate(){
        out.println();
        out.println("What is the withholding tax rate applicable \n" +
                "Hint: Use whole numbers e.g. 17.5,20,13.5...");
    }


    @Override
    public void initialMenu(){

        out.println();
        out.println(" Transaction menu: \n" +
                "a) Normal payment \n" +
                "b) Normal payment with withholding taxes \n" +
                "   on consultancy\n" +
                "c) Rental Payments \n" +
                "d) Adjust main paramenters \n" +
                "e) Foreign transactions(Telegraphic Transfers)\n" +
                "f) Partially non taxable payment\n" +
                "g) Contractor's payment (3% wht tax) payment\n" +
                "");
    }

    // FeedBackImpl for the Invoice class

    @Override
    public void invoiceAmount(){
        out.println();

        out.println("What is the amount invoiced?");

    }

    @Override
    public void vatAmount(){
        out.println();

        out.println("What is the vat amount according to the Invoice?");

    }

    @Override
    public void withHoldingTaxAmount(){
        out.println();

        out.println("What is the withholding tax amount in the Invoice? ");
    }

    @Override
    public void dateInfo(String date) {
        out.println();

        out.println("What is the invoice " + date + " ? Hint: dd.mm.yyyy");
    }

    @Override
    public void withHoldingVatRate(){
        out.println();

        out.println("What is the withholding vat rate for this transaction? ");
    }

    @Override
    public Boolean printReport() {

        Scanner keyboard = new Scanner(System.in);

        out.println("\nHey there? Would you like to print the report?\n" +
                "Hint : yes or no...");
        Boolean print = false;

        mainPrompt();

        String userInput = keyboard.next();

        print = userInput.equalsIgnoreCase("yes");

        return print;
    }


}
