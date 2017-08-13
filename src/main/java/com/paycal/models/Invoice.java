package com.paycal.models;

import com.paycal.api.FeedBack;
import com.paycal.api.InvoiceDetails;
import com.paycal.view.Notifications;

import java.math.BigDecimal;
import java.util.Scanner;

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
public class Invoice implements InvoiceDetails {

    FeedBack notice;
    Scanner keyboard;

    public Invoice(FeedBack notice) {
        this.notice = notice;
        this.keyboard = new Scanner(System.in);
    }

    public Invoice() {
        this.notice = new Notifications();
        keyboard = new Scanner(System.in);
    }

    /**
     * Getter method for Invoice amount
     * @return the string is parsed to remove any spaces and converted to Double
     */
    @Override
    public BigDecimal invoiceAmount(){

        notice.invoiceAmount();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        BigDecimal invoice = Double.parseDouble(strToConvert);

        return invoice;
    }

    @Override
    public double vatAmount(){

        notice.vatAmount();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        Double vat = Double.parseDouble(strToConvert);

        return vat;
    }

    @Override
    public double withHoldingTaxAmount(){

        notice.withHoldingTaxAmount();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        Double withHoldingTaxAmount = Double.parseDouble(strToConvert);

        return withHoldingTaxAmount;
    }

    @Override
    public double vatRate(){

        notice.vatRate();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        Double vatRate = Double.parseDouble(strToConvert);

        return vatRate;
    }

    @Override
    public double withHoldingTaxRate(){

        notice.withHoldingTaxRate();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        Double withHoldingTaxRate = Double.parseDouble(strToConvert);

        return withHoldingTaxRate;
    }

    @Override
    public double withHoldingVatRate(){

        notice.withHoldingVatRate();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        Double withHoldingVatRate = Double.parseDouble(strToConvert);

        return withHoldingVatRate;
    }

    @Override
    public String payeeName(){


        notice.payeeName();
        notice.mainPrompt();

        String str = new String(keyboard.next());

        return str.trim().toUpperCase();
    }

    @Override
    public String getInvoiceStartDate() {

        notice.dateInfo("start date");
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        return strToConvert;
    }

    @Override
    public String getInvoiceRefDate() {
        notice.dateInfo("reference date");
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        return strToConvert;
    }

    @Override
    public String getInvoiceEndDate() {
        notice.dateInfo("end date");
        notice.mainPrompt();

        String str = new String(keyboard.next());

        String strToConvert = str.trim();

        return strToConvert;
    }

}
