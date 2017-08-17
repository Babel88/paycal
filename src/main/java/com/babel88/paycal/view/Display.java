package com.babel88.paycal.view;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.api.view.PayCalView;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.Tables;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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

    @Autowired
    public Tables tableString;

    public Display(){}

    @Override
    public void displayResults(BigDecimal total,BigDecimal vatWithheld,BigDecimal withholdingTax,BigDecimal toPrepay,BigDecimal toPayee){
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

        BigDecimal toPay = roundTwoDecimals(total)
                .add(roundTwoDecimals(toPrepay))
                .subtract(roundUp(vatWithheld))
                .subtract(roundUp(withholdingTax));

        String paid = toPay.toString();


        // We are going to get the time when
        // the computation of the Invoice transactions
        // was instantiated
        Date timePaid = paycalApp.getNow();

        out.println("Calculated at: "+timePaid);

        out.println();

        Tables table = tableString.createTable(vatWithhold, withHold, prepayment, expensed, paid, this);


        out.println(table.toString());

        // new feature, for printing reports
        Boolean printReport = feedBack.printReport();

        paymentAdvice.setPrintAdvice(printReport).forPayment(paid, vatWithhold, withHold);


    }

    private String makeString(BigDecimal number){
        Double nbr = new Double(String.valueOf(roundTwoDecimals(number)));

        return nbr.toString();
    }

    private String makeStringUp(BigDecimal number){
        Double nbr = new Double(String.valueOf(roundUp(roundTwoDecimals(number))));

        return nbr.toString();
    }

    /*
     *return the value added up by one, if there be
     *any decimal value
     */
    private BigDecimal roundUp(BigDecimal number) {

        // calculates the modulous for given number
        BigDecimal numberMod = number.remainder(BigDecimal.ONE);

        // returns true if the number is exactly an integer
        // if not it will return false
        boolean modulusIsZero = BigDecimal.ZERO.equals(numberMod);

        // This is the carrier for the value to be returned
        BigDecimal reviewed;

        if (modulusIsZero)
            // that is if the number is an exact integer
        {
            reviewed = number;
        } else {
            // that is if not an integer, we now round up
            // First we subtract the modulous,
            // Then we add one.
            reviewed = number.subtract(numberMod).add(BigDecimal.ONE);
        }

        return reviewed;
    }

    // rounds number to 2 decimal places
    @NotNull
    private BigDecimal roundTwoDecimals(BigDecimal number) {


        // Creating object from DecimalFormat class in text
        // we will cast the same to convert to double from string
        DecimalFormat dformat = new DecimalFormat("#.##");


        return BigDecimal.valueOf(Double.valueOf(dformat.format(number)));
    }
}

