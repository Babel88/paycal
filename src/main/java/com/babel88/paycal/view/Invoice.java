package com.babel88.paycal.view;

import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.PartialTaxDetails;
import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.api.view.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Invoice implements InvoiceDetails, PrepaymentDetails, ForeignPaymentDetails, PartialTaxDetails {

    private final Logger log = LoggerFactory.getLogger(Invoice.class);
    private final Scanner keyboard;
    private FeedBack feedBack;

    public Invoice(FeedBack feedBack) {
        this.feedBack = feedBack;
        log.debug("Creating an instance of invoiceDetails : {}", this);

        keyboard = new Scanner(System.in);
        //keyboard = new BufferedInputStream(System.in);
    }

    /**
     * Getter method for Invoice amount
     *
     * @return the string is parsed to remove any spaces and converted to Double
     */
    @Override
    public BigDecimal invoiceAmount() {

        feedBack.invoiceAmount();
        feedBack.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return BigDecimal.valueOf(Double.parseDouble(strToConvert));
    }

    @Override
    public double vatAmount() {

        feedBack.vatAmount();
        feedBack.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double withHoldingTaxAmount() {

        feedBack.withHoldingTaxAmount();
        feedBack.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double vatRate() {

        feedBack.vatRate();
        feedBack.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double withHoldingTaxRate() {

        feedBack.withHoldingTaxRate();
        feedBack.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public double withHoldingVatRate() {

        feedBack.withHoldingVatRate();
        feedBack.mainPrompt();

        String str = keyboard.next();

        String strToConvert = str.trim();

        return Double.parseDouble(strToConvert);
    }

    @Override
    public String payeeName() {


        feedBack.payeeName();
        feedBack.mainPrompt();

        String str = keyboard.next();

        return str.trim().toUpperCase();
    }

    @Override
    public String getInvoiceStartDate() {

        feedBack.dateInfo("start date");
        feedBack.mainPrompt();

        String str = keyboard.next();

        return str.trim();
    }

    @Override
    public String getInvoiceRefDate() {
        feedBack.dateInfo("reference date");
        feedBack.mainPrompt();

        String str = keyboard.next();

        return str.trim();
    }

    @Override
    public String getInvoiceEndDate() {
        feedBack.dateInfo("end date");
        feedBack.mainPrompt();

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

        feedBack.mainPrompt();

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
