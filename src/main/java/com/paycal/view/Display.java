package com.paycal.view;

import com.paycal.PaycalApp;
import com.paycal.api.FeedBack;
import com.paycal.api.PayCalView;
import com.paycal.api.Tables;
import com.paycal.view.reporting.PaymentAdvice;
import com.paycal.view.tables.TableMaker;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.Date;

import static java.lang.System.out;

/**
 * Created by edwin.njeru on 18/07/2016.
 * This class displays the values computed in the BusinessLogic class, in an
 * "Easy on the eye" format
 * The figures are also rounded off to 2 decimal places
 * The withholding Vat in particular is to rounded up to one decimal
 * place. This is because the itax system will round up the tax
 * payable up by 1 if there is anything after the decimal place
 */
public class Display implements PayCalView {

    @Autowired
    private PaycalApp paycalApp;

    @Autowired
    private FeedBack feedBack;

    @Autowired
    private PaymentAdvice paymentAdvice;

    public Display(){}

    @Override
    public void displayResults(Double total,
                               Double vatWithheld,
                               Double withholdingTax,
                               Double toPrepay,
                               Double toPayee){
        // Pending:
        // To convert numbers to string than view them
        // Dsiplay zero values for number not given, e.g.
        // ... when withholding tax is not provided

        //Make some changes to the variables
        String vatWithhold = makeStringUp(vatWithheld);
        // Converts vat withheld to vat to view in String format

        String withHold = makeStringUp(withholdingTax);
        // Withholding tax on consultancy rounded and converted to String

        String prepayment = makeString(toPrepay);

        String expensed = makeString(total);

        Double toPay = roundTwoDecimals(total)+roundTwoDecimals(toPrepay)-
                roundUp(vatWithheld)-roundUp(withholdingTax);
        String paid = toPay.toString();


        // We are going to get the time when
        // the computation of the Invoice transactions
        // was instantiated
        Date timePaid = paycalApp.getNow();

        out.println("Calculated at: "+timePaid);

        out.println();

        // We are now going to create an object in table maker, which we'll user
        // to view data on the console

        Tables table = new TableMaker();


        table.addString(0,0,"");
        table.addString(0,1,"RESULTS!!!");
        table.addString(0,2,"");
        // This is the title row

        table.addString(1,0,"");
        table.addString(1,1,"");
        table.addString(1,2,"");
        // That should create an empty ROW

        table.addString(2,0,"Particulars");
        table.addString(2,1,"Debit");
        table.addString(2,2,"Credit");
        // That should label the columns

        table.addString(3,0,"a) Expense");
        table.addString(4, 0, "b) Prepayment");
        table.addString(5,0,"c) Withhold VAT");
        table.addString(6,0,"d) Withhold TAX");
        table.addString(7,0,"e) To Payee");
        // That should label the rows

        table.addString(3,1,expensed);
        table.addString(3,2,"-");
        // That's your expense

        table.addString(4,1,prepayment);
        table.addString(4,2,"-");
        // That your prepayment

        table.addString(5,1,"-");
        table.addString(5,2,vatWithhold);
        // That's the withholding VAT

        table.addString(6,1,"-");
        table.addString(6,2,withHold);
        // That's the withholding tax chargeable

        table.addString(7,1,"-");
        table.addString(7,2,paid);
        // That's the amount payable to payee

        out.println(table.toString());

        // new feature, for printing reports
        Boolean printReport = feedBack.printReport();

        paymentAdvice.setPrintAdvice(printReport).forPayment(paid, vatWithhold, withHold);


    }

    private String makeString(Double number){
        Double nbr = new Double(roundTwoDecimals(number));

        return nbr.toString();
    }

    private String makeStringUp(Double number){
        Double nbr = new Double(roundUp(roundTwoDecimals(number)));

        return nbr.toString();
    }

    private double roundUp(double number) {
        // return the value added up by one, if there be
        // any decimal value

        Double zero = new Double("0");
        // Creates an object containing zero as a number

        double numberMod = number%1;
        // calculates the modulous for given number

        Double numberObj = new Double(numberMod);

        boolean isEqual = zero.equals(numberObj);
        // returns true if the number is exactly an integer
        // if not it will return false

        double reviewed = 0;
        // This is the carrier for the value to be returned

        if (isEqual)
            // that is if the number is an exact integer
        {
            reviewed = number;
        } else {
            // that is if not an integer, we now round up
            // First we subtract the modulous,
            // Then we add one.
            reviewed = (number - numberMod)+1;
        }

        return reviewed;
    }

    private double roundTwoDecimals(double number) {
        // rounds number to 2 decimal places

        DecimalFormat dformat = new DecimalFormat("#.##");
        // Creating object from DecimalFormat class in text
        // we will cast the same to convert to double from string

        return Double.valueOf(dformat.format(number));
    }
}

