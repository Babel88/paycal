package com.paycal.logic;

import com.paycal.api.Prepayable;
import com.paycal.models.PaymentParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * <p>Calculates transaction items for invoices of the following criteria</p>
 * <p>a) The payee is chargeable for withholding tax for consultancy</p>
 * <p>b) The payee is locally domiciled</p>
 * <p>c) The payee chargeable to VAT tax</p>
 * <p>d) The payee needs to pay 6% withholding tax</p>
 * <p>e) The Invoice is not encumbered with duties or levies</p>
 * <p>f) The Invoice contains a component that is to be prepaid</p>
 */
public class SimplePrepayments implements Prepayable {

    private BigDecimal vatRate;

    private BigDecimal withholdingVatRate;

    private BigDecimal amountBeforeTax;

    private BigDecimal withholdingVat;

    private BigDecimal totalExpense;

    @Autowired
    private PaymentParameters parameters;


    public SimplePrepayments() {

        vatRate = parameters.getVatRate();

        withholdingVatRate = parameters.getWithholdingVatRate();
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
     * CheckedPrepayment module: calculatePrepayment
     * We are going to take take the total amount and apportion it
     * to the period for prepayment and hence get the amount to expense
     *
     * @param invoiceAmount
     * @return
     */
    @Override
    public BigDecimal calculatePrepayment(BigDecimal invoiceAmount) {

        Scanner keyboard = new Scanner(System.in);//initiating user input
        BigDecimal prePay;
        BigDecimal prePeriod;
        BigDecimal fullPeriod;

        BigDecimal startDate = BigDecimal.valueOf(getStartDate());
        BigDecimal refDate = BigDecimal.valueOf(getDateofReference());
        BigDecimal endDate = BigDecimal.valueOf(getEndDate());
        prePeriod = endDate.subtract(refDate);
        fullPeriod = endDate.subtract(startDate);
        prePay = prePeriod.divide(fullPeriod);

        return prePay.multiply(invoiceAmount);
    }

    @Override
    public BigDecimal calculateAmountB4Vat(BigDecimal invoiceAmount) {

        this.amountBeforeTax = invoiceAmount.divide(vatRate.add(BigDecimal.ONE));

        return amountBeforeTax;
    }

    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal amountB4Vat) {

        this.withholdingVat = amountB4Vat.multiply(withholdingVatRate);

        return withholdingVat;
    }

    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount, BigDecimal toPrepay) {

        this.totalExpense = invoiceAmount.subtract(toPrepay);

        return totalExpense;
    }

    @Override
    public BigDecimal calculateAmountPayable(BigDecimal toPrepay, BigDecimal withHoldingVat) {

        return totalExpense.add(toPrepay).subtract(withHoldingVat);
    }
}
