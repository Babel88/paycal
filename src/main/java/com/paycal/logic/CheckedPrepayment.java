package com.paycal.logic;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.paycal.api.InvoiceDetails;
import com.paycal.api.Prepayable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by edwin.njeru on 03/02/2017.
 * <p>
 * TO DO:
 * <p>
 * Revise dateFormatValidation code
 * Check chronoLocalDate functions for nullity
 */
public class CheckedPrepayment implements Prepayable {

    // dates used in this class
    private LocalDate startDate, refDate, endDate;
    // variable to store amount to prepayable
    private double prepaymentAmount, invoiceAmount;
    // durations on and before reference date
    private double prepaymentPeriod, invoicePeriod;
    // factor of prepayment period to total invoice period
    private double prepaymentCoefficient;

    private InvoiceDetails invoice;

    // using constructor injection for this one
    public CheckedPrepayment(InvoiceDetails invoice) {

        this.invoice = invoice;
    }

    public CheckedPrepayment() {
    }

    private static LocalDate getStartDate() {
        return CheckedPrepayment.getStartDate();
    }

    private void setStartDate(String startDate) {
        // we need to make sure the format is okay
        try {
            dateFormatValidation(startDate);

        } catch (WrongDateFormatException d) {

            System.out.println(d.getMessage());
        }

        this.startDate = toDate(startDate);

        // ok. Let's check if it's making sense
        try {
            logicValidation(this.startDate);

        } catch (WrongDateException e) {

            System.out.println(e.getMessage());
        }
    }

    private static LocalDate getRefDate() {
        return CheckedPrepayment.getRefDate();
    }

    private void setRefDate(String refDate) {

        // we need to make sure the format is okay
        try {
            dateFormatValidation(refDate);

        } catch (WrongDateFormatException d) {

            System.out.println(d.getMessage());
        }


        this.refDate = toDate(refDate);

        // ok. Let's check if it's making sense
        try {
            logicValidation(this.refDate);

        } catch (WrongDateException e) {

            System.out.println(e.getMessage());
        }
    }

    private static LocalDate getEndDate() {
        return CheckedPrepayment.getEndDate();
    }

    private void setEndDate(String endDate) {

        try {
            dateFormatValidation(endDate);

        } catch (WrongDateFormatException d) {

            System.out.println(d.getMessage());
        }


        this.endDate = toDate(endDate);

        // ok. Let's check if it's making sense
        try {
            logicValidation(this.refDate);

        } catch (WrongDateException e) {

            System.out.println(e.getMessage());
        }

    }

    private static double getPrepaymentPeriod() {
        return CheckedPrepayment.getPrepaymentPeriod();
    }

    public void setPrepaymentPeriod(double prepaymentPeriod) {
        this.prepaymentPeriod = prepaymentPeriod;
    }

    private static double getInvoicePeriod() {
        return CheckedPrepayment.getInvoicePeriod();
    }

    public void setInvoicePeriod(double invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }

    private static boolean datePatternMatches(String input)
            throws NullPointerException {

        boolean isMatching = true;

        try {
            Pattern format =
                    Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");

            Matcher matcher =
                    format.matcher(input);

            isMatching = matcher.matches();
        } catch (NullPointerException n) {

            System.out.println(n.getMessage());
        }

        return isMatching;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setRefDate(LocalDate refDate) {
        this.refDate = refDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private double getPrepaymentCoefficient() {
        return prepaymentCoefficient;
    }

    public void setPrepaymentCoefficient(double prepaymentCoefficient) {
        this.prepaymentCoefficient = prepaymentCoefficient;
    }

    @Override
    public double getPrepay(double invoiceAmount) {

        this.invoiceAmount = invoiceAmount;

        this.setStartDate(invoice.getInvoiceStartDate());

        this.setRefDate(invoice.getInvoiceRefDate());

        this.setEndDate(invoice.getInvoiceEndDate());

        this.compute(this.invoiceAmount);

        return prepaymentAmount;
    }

    private LocalDate toDate(String _date) {

        String classDate = makeDates(dateArray(dateString(_date)));
        LocalDate date = null;
        try {

            // date parsed in iso 8601 format
            date = LocalDate.parse(classDate);

        } catch (DateTimeParseException exc) {

            System.out.printf("%s is not parsable!%n", classDate);
        }

        return date;
    }

    private double periodFrom(LocalDate refDate, LocalDate thisDate) {

        double period = 0;

        try {
            period = java.time.temporal.ChronoUnit.DAYS.between(refDate, thisDate);

        } catch (NullPointerException e) {

            System.out.println(e.getMessage());
        }

        return period;
    }

    private Iterable<String> dateString(String scanner) {

        return com.google.common.base.Splitter.on("/").trimResults().split(scanner);
    }

    private String[] dateArray(Iterable<String> dateString) {

        return com.google.common.collect.Iterables.toArray(dateString, String.class);
    }

    private String makeDates(String[] dateArray) {

        return dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
    }

    private void logicValidation(LocalDate givenDate)
            throws WrongDateException, NullPointerException {

        try {
            // check for nullity
            nullCheck(givenDate);

            if (zeroNullity()) {
                zeroNullityValidation();
            } else {
                partialNullityValidation();
            }

        } catch (NullPointerException w) {

            System.out.println(w.getMessage());
        }

    }

    private boolean zeroNullity() {
        return !endDateIsNull() && !refDateIsNull() && !startDateIsNull();
    }

    private void zeroNullityValidation() throws WrongDateException {
        if (startDate.isAfter(refDate)) {

            throw new WrongDateException("Date of reference does not precede invoice start date");

        } else if (refDate.isAfter(endDate)) {

            throw new WrongDateException("The reference cannot come after the end date");

        } else if (startDate.isAfter(endDate)) {

            throw new WrongDateException("The start date cannot be after end date");

        } else if (endDate.isEqual(startDate) || endDate.isEqual(refDate)
                || refDate.isEqual(startDate)) {

            throw new WrongDateException("This is a prepayment transaction. Right?");

        }
    }

    private void partialNullityValidation()
            throws WrongDateException {

        if (startDateIsNull() && !refDateIsNull() && !endDateIsNull()) {

            if (refDate.isAfter(endDate)) {

                throw new WrongDateException("The reference cannot come after the end date");
            }
        } else if (refDateIsNull() && !startDateIsNull() && !endDateIsNull()) {

            if (startDate.isAfter(endDate)) {

                throw new WrongDateException("The start date can come after end date \n" +
                        "when hell freezes over");
            }
        } else if (endDateIsNull() && !startDateIsNull() && !refDateIsNull()) {

            if (startDate.isAfter(refDate)) {

                throw new WrongDateException("The invoice start date can come after the reference date \n" +
                        "when hell freezes over");
            }
        }
        if (endDateIsNull() && refDateIsNull() && startDateIsNull()) {
            return;
        }

    }

    private boolean startDateIsNull() {

        return this.startDate == null;
    }

    private boolean refDateIsNull() {

        return this.refDate == null;
    }

    private boolean endDateIsNull() {

        return this.endDate == null;
    }

    private void dateFormatValidation(String givenDate)
            throws WrongDateFormatException {

        if (!datePatternMatches(givenDate)) {

            throw new WrongDateFormatException("The date entered is" +
                    "\n in the wrong format");
        }
    }

    private void nullCheck(LocalDate valDate)
            throws NullPointerException {

        if (valDate == null) {
            throw new NullPointerException("Please input a date...");
        }

    }

    private void setPrepaymentPeriod() {

        prepaymentPeriod = periodFrom(refDate, endDate);

    }

    private void setInvoicePeriod() {

        invoicePeriod = periodFrom(startDate, endDate);

    }

    private void setPrepaymentFactor() {

        prepaymentCoefficient = prepaymentPeriod / invoicePeriod;

    }

    private void setDates(String start, String ref, String end) {

        setStartDate(start);
        setRefDate(ref);
        setEndDate(end);

    }

    private void compute(double invoiced) {

        setPrepaymentPeriod();
        setInvoicePeriod();
        setPrepaymentFactor();
        setPrepaymentAmount(invoiced);

    }

    // Standard objects implementations for hashing works
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("start date", startDate)
                .add("ref date", refDate)
                .add("end date", endDate)
                .add("total expenses", invoiceAmount)
                .add("prepayment", prepaymentAmount)
                .toString();
    }

    // ceremonial code

    @Override
    public int hashCode() {

        return Objects.hashCode(startDate, refDate, endDate, invoiceAmount, prepaymentAmount);
    }

    public int compareTo(CheckedPrepayment that) {

        return ComparisonChain.start()
                .compare(this.invoiceAmount, that.invoiceAmount)
                .compare(this.startDate, that.startDate)
                .compare(this.refDate, that.refDate)
                .compare(this.endDate, that.endDate)
                .result();

    }

    public boolean equals(CheckedPrepayment this) {return equals();}

    public boolean equals(CheckedPrepayment this, CheckedPrepayment that) {

        return Objects.equal(this, that);
    }

    public double getPrepaymentAmount() {
        return prepaymentAmount;
    }

    private void setPrepaymentAmount(double _invoiceAmount) {

        this.invoiceAmount = _invoiceAmount;

        prepaymentAmount = invoiceAmount * prepaymentCoefficient;

    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public static class WrongDateException
            extends Exception {
        public WrongDateException(String message) {
            super(message);
        }
    }

    public static class WrongDateFormatException
            extends Exception {
        public WrongDateFormatException(String message) {
            super(message);
        }
    }
}
