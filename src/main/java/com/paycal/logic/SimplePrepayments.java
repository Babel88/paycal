package com.paycal.logic;

import com.paycal.api.Prepayable;

import java.util.Date;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public class SimplePrepayments implements Prepayable {


    public SimplePrepayments() {
    }

    /**
     * Get start date
     * We are using this just to get a start date
     *
     * @return no of seconds from new year 1970 to start date
     */
    private double getStartDate() {
        Scanner giveDate = new Scanner(System.in);//initiate scanner class
        String str[] = {"year", "month", "day"};//Set up several prompt array
        String givenDate = "";//initiate date variable

        out.println("And when is start date of the Invoice?");//General prompt
        for (int i = 0; i < 3; i++) {//loop the str array for user input
            out.println("Enter " + str[i] + ":");//Several prompt array
            givenDate = givenDate + giveDate.next() + "/";//user types date
        }
        givenDate = givenDate.substring(0, givenDate.length() - 1);
        Date date = new Date(givenDate);
        out.println("The Invoice start date is: " + givenDate);
        long diff = date.getTime();
        return diff / 1000000;
    }

    /**
     * Get the date of current reference
     * We are using this just to get a the pivotal reference date
     *
     * @return no of seconds from new year 1970 to reference date
     */
    private double getDateofReference() {
        Scanner giveDate = new Scanner(System.in);//initiate scanner class
        String str[] = {"year", "month", "day"};//Set up several prompt array
        String givenDate = "";//initiate date variable

        out.println("And when is payment date of the Invoice?");//General prompt
        for (int i = 0; i < 3; i++) {//loop the str array for user input
            out.println("Enter " + str[i] + ":");//Several prompt array
            givenDate = givenDate + giveDate.next() + "/";//user types date
        }
        givenDate = givenDate.substring(0, givenDate.length() - 1);
        Date date = new Date(givenDate);
        out.println("The Invoice payment date is: " + givenDate);
        long diff = date.getTime();
        return diff / 1000000;
    }

    /**
     * Get Invoice End date
     * We are using this just to get the last day of the Invoice
     *
     * @return End Date
     */
    private double getEndDate() {
        Scanner giveDate = new Scanner(System.in);//initiate scanner class
        String str[] = {"year", "month", "day"};//Set up several prompt array
        String givenDate = "";//initiate date variable

        out.println("And when is termination date of the Invoice?");//General prompt
        for (int i = 0; i < 3; i++) {//loop the str array for user input
            out.println("Enter " + str[i] + ":");//Several prompt array
            givenDate = givenDate + giveDate.next() + "/";//user types date
        }
        givenDate = givenDate.substring(0, givenDate.length() - 1);
        Date date = new Date(givenDate);
        out.println("The Invoice termination date is: " + givenDate);
        long diff = date.getTime();
        return diff / 1000000;
    }


    /**
     * CheckedPrepayment module: getPrepay
     * We are going to take take the total amount and apportion it
     * to the period for prepayment and hence get the amount to expense
     *
     * @param invoiceAmount
     * @return
     */
    @Override
    public double getPrepay(double invoiceAmount) {

        Scanner keyboard = new Scanner(System.in);//initiating user input
        double prePay;
        double prePeriod;
        double fullPeriod;

        double startDate = getStartDate();
        double refDate = getDateofReference();
        double endDate = getEndDate();
        prePeriod = endDate - refDate;
        fullPeriod = endDate - startDate;
        prePay = prePeriod / fullPeriod;

        return prePay * invoiceAmount;
    }
}
