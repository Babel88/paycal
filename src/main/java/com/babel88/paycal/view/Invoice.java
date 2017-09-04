package com.babel88.paycal.view;

import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.config.factory.GeneralFactory;

import java.math.BigDecimal;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains methods for obtaining the particulars in an Invoice
 * These consist of:
 * a) Invoice amount
 * b) Vat amount
 * c) Withholding tax rate amount applicable
 * d) Vat rate
 * e) Withholding tax amount
 * f) Payee's name
 */
public class Invoice implements InvoiceDetails,PrepaymentDetails,ForeignPaymentDetails {

    private static InvoiceDetails instance =
            new Invoice(
                    GeneralFactory.createFeedback(),
                    new Scanner(System.in)
            );
    private final FeedBack notice;
    private final Scanner keyboard;

    private Invoice(FeedBack notice,Scanner keyboard) {

        this.notice = notice;
        this.keyboard = keyboard;
    }

    public static InvoiceDetails getInstance() {
        return instance;
    }

    /**
     * Getter method for Invoice amount
     * @return the string is parsed to remove any spaces and converted to Double
     */
    @Override
    public BigDecimal invoiceAmount(){

        notice.invoiceAmount();
        notice.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return BigDecimal.valueOf(Double.parseDouble(strToConvert));
    }

    @Override
    public double vatAmount(){

        notice.vatAmount();
        notice.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double withHoldingTaxAmount(){

        notice.withHoldingTaxAmount();
        notice.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double vatRate(){

        notice.vatRate();
        notice.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double withHoldingTaxRate(){

        notice.withHoldingTaxRate();
        notice.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double withHoldingVatRate(){

        notice.withHoldingVatRate();
        notice.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public String payeeName(){


        notice.payeeName();
        notice.mainPrompt();

        String str = keyboard.next();

        return str.trim().toUpperCase();
    }

    @Override
    public String getInvoiceStartDate() {

        notice.dateInfo("start date");
        notice.mainPrompt();

        String str = keyboard.next();

        return str.trim();
    }

    @Override
    public String getInvoiceRefDate() {
        notice.dateInfo("reference date");
        notice.mainPrompt();

        String str = keyboard.next();

        return str.trim();
    }

    @Override
    public String getInvoiceEndDate() {
        notice.dateInfo("end date");
        notice.mainPrompt();

        String str = keyboard.next();

        return str.trim();
    }

    /**
     * The value returned by this method flags whether or not the invoice price
     * is inclusive of withholding tax
     *
     * @return Boolean : whether the withholding tax is exclusive in the invoice
     */
    @Override
    public Boolean exclusiveOfWithholdingTax() {

        out.println("Is the amount EXCLUSIVE of withholding");//Prompt
        out.println("tax?");//Prompt
        out.println("Hint: Yes or no");//Prompt

        notice.mainPrompt();

        String str = keyboard.next();

        return str.equalsIgnoreCase("yes");
    }

    /**
     * This method creates a variable that tells the algorithm whether or not the user
     * would like to repeat a certain computation
     *
     * @return whether or not we should recompute
     */
    @Override
    public boolean doAgain() {

        //check if user wants to recalculate
        //Check if user wants to recalculate
        Scanner keyboard = new Scanner(System.in);//initiating user input
        out.println("Do you want to recalculate?");

        String str = keyboard.next();//user types yes or no

        out.println("------");

        return str.equalsIgnoreCase("yes");
    }

}
