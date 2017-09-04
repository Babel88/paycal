package com.babel88.paycal.logic;

import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.api.logic.Prepayable;
import com.babel88.paycal.config.GeneralConfigurations;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static java.math.RoundingMode.HALF_EVEN;

/**
 * Implementation of the AbstractPrepayment interface using jsr310
 */
public class AbstractPrepayment implements Prepayable,Serializable {

    private static Prepayable instance = new AbstractPrepayment();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private LocalDate invoiceStartDate,invoiceReferenceDate,invoiceEndDate;
    private Double prepaymentPeriod,invoicePeriod,coefficient;
    // Keeps track of current date format style
    private Enum<FormatStyle> dateFormatStyle;
    // Keeps track of locale settings
    private Locale locale;
    /* This object provides prepayment dates
     * Object has been Autowired through a setter
     */
    private PrepaymentDetails prepaymentDetails;
    /*This object provides general configurations for use with prepayment dates
     * The object has been Autowired through a setter
     */
    private GeneralConfigurations generalConfigurations;
    /**
     * The expense amount to be prepaid
     */
    private BigDecimal expenseAmount;
    /**
     * The prepayment amount
     */
    private BigDecimal prepaymentAmount;

    private AbstractPrepayment() {

        log.debug("Creating abstract prepayment using general configurations and prepayment details \n" +
                "from Logic factory and General factory respectively");
        generalConfigurations = LogicFactory.getGeneralConfigurations();

        prepaymentDetails = GeneralFactory.createPrepaymentDetails();

        log.debug("Creating prepayment with default configurations, from the general \n" +
                "configurations object : {}.",generalConfigurations.toString());

        this.dateFormatStyle = generalConfigurations.getDateFormatStyle();

        locale = generalConfigurations.getLocale();
    }

    public static Prepayable getInstance() {
        return instance;
    }

    /**
     * This method takes any prepayment and calculates the amount of prepayment.
     * This updates use of depracated implementations in SimplePrepayments and Checked
     * prepayments to apply use of JSR 310
     *
     * @param expense amount
     * @return amount of prepayment
     */
    @NotNull
    @Override
    public BigDecimal calculatePrepayment(@NotNull BigDecimal expense) {

        log.debug("Calculating prepayable amount for : {}.",expense);
        this.expenseAmount = expense;

        doPrepayment();

        return prepaymentAmount;
    }

    private void doPrepayment(){

        // important part of this algorithm is setting of dates
        setPrepaymentDates();

        log.debug("Calculating the invoice prepayment period between : {}. and {}.",
                invoiceReferenceDate,invoiceEndDate);
        calculatePrepaymentPeriod();

        log.debug("Calculating the total invoice period between : {}. and {}.",
                invoiceStartDate,invoiceEndDate);
        calculateInvoicePeriod();

        log.debug("Calculating the coefficient for prepayment amount");
        calculatePrepaymentCoefficient();

        log.debug("Finally : Calculating the prepayment amount");
        calculatePrepaymentAmount();

    }

    private BigDecimal calculatePrepaymentAmount(){

        log.debug("Calculating the prepayment amount using expense amount of :{}." +
                "and a coefficient of : {}.",getExpenseAmount(),getCoefficient());
        prepaymentAmount =
                expenseAmount.multiply(BigDecimal.valueOf(coefficient))
                        .setScale(2, HALF_EVEN);

        log.debug("Returning the prepayment amount as : "+ prepaymentAmount);
        return prepaymentAmount;
    }

    /**
     * Calculates the dates between invoice reference dates and invoice end date
     * This period is returned to the caller, but a side effect is that the same period is set
     * in a local variable which is later called in calculations for prepayment amount. This
     * reduces the number of operations for required to calculate the former hence speeding
     * up the algorithm
     *
     * @return prepayment amount
     */
    private Double calculatePrepaymentPeriod(){

//        Period prepPeriod = invoiceReferenceDate.until(invoiceEndDate);

        log.debug("Calculating full invoice period between : \n" +
                "Invoice start date : {}. \n" +
                "Invoice End date : {}. \n",invoiceReferenceDate,invoiceEndDate);

//        prepaymentPeriod = Double.valueOf(prepPeriod.getDays());

        prepaymentPeriod= periodFrom(invoiceReferenceDate,invoiceEndDate);

        log.debug("Invoice period calculated as : {}.",prepaymentPeriod.toString());

        return prepaymentPeriod;
    }

    /**
     * Calculates the dates between invoice start dates and invoice end date
     * This period is returned to the caller, but a side effect is that the same period is set
     * in a local variable which is later called in calculations for prepayment amount. This
     * reduces the number of operations for required to calculate the former hence speeding
     * up the algorithm
     *
     * @return prepayment amount
     */
    private Double calculateInvoicePeriod(){

//        Period fullInvoicePeriod = Period.between(invoiceStartDate,invoiceEndDate);

        log.debug("Calculating full invoice period between : \n" +
                "Invoice start date : {}. \n" +
                "Invoice End date : {}. \n",invoiceStartDate,invoiceEndDate);
//        invoicePeriod = Double.valueOf(fullInvoicePeriod.getDays());

        invoicePeriod = periodFrom(invoiceStartDate,invoiceEndDate);

        log.debug("Invoice period calculated as : {}.",invoicePeriod.toString());

        return invoicePeriod;
    }

    /**
     * This method calculates the coefficient which if multiplied with the invoice expense
     * amount, will result in prepayment amount
     *
     * @return prepayment coefficient in BigDecimal
     */
    private Double calculatePrepaymentCoefficient(){

        log.debug("Calculating the prepayment coefficient using : \n" +
                "Prepayment period : {}. \n" +
                "Invoice period : {}.",prepaymentPeriod,invoicePeriod);

        coefficient = prepaymentPeriod/invoicePeriod;

        log.debug("Returning prepayment coefficient : {}.",coefficient);

        return coefficient;

    }

    private void setPrepaymentDates(){

        log.debug("Setting prepayment dates...");

        try {
            setInvoiceStartDate(prepaymentDetails.getInvoiceStartDate());

            setInvoiceReferenceDateDate(prepaymentDetails.getInvoiceRefDate());

            setInvoiceEndDateDate(prepaymentDetails.getInvoiceEndDate());

        } catch (Exception e){

            log.error("An error was thrown while trying to set dates");
        }

        log.debug("Prepayment dates set successfully without incidence");
    }

    private double periodFrom(LocalDate refDate, LocalDate thisDate) {

        double period = 0;

        log.debug("Calculating the period between, {}. and {}.",refDate,thisDate);

        try {
            period = java.time.temporal.ChronoUnit.DAYS.between(refDate, thisDate);

        } catch (NullPointerException e) {

            log.debug("A null pointer exception has been thrown, please ensure the dates \n" +
                    "given are correct");
        }

        return period;
    }

    @Nullable
    public LocalDate getInvoiceStartDate() {
        return invoiceStartDate;
    }

    private void setInvoiceStartDate(String startDateString){

        log.debug("Setting start date as : {}.",startDateString);

        LocalDate startDate = createDateFromString(startDateString);

        try {
            logicValidation(startDate);
        } catch (WrongDateException e) {
            log.error("You have in input the wrong invoice start date : {}. Care to try again? ",startDate);
        }

        invoiceStartDate = startDate;

        log.debug("Start date successfully set as : {}.",invoiceStartDate.toString());
    }

    public AbstractPrepayment setInvoiceStartDate(LocalDate invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
        return this;
    }

    @Nullable
    public LocalDate getInvoiceReferenceDate() {
        return invoiceReferenceDate;
    }

    public AbstractPrepayment setInvoiceReferenceDate(LocalDate invoiceReferenceDate) {
        this.invoiceReferenceDate = invoiceReferenceDate;
        return this;
    }

    @Nullable
    public LocalDate getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public AbstractPrepayment setInvoiceEndDate(LocalDate invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
        return this;
    }

    private void setInvoiceReferenceDateDate(String refDateString){

        log.debug("Setting reference date as : {}.",refDateString);

        LocalDate refDate = createDateFromString(refDateString);

        try {
            logicValidation(refDate);
        } catch (WrongDateException e) {
            log.error("You have in input the wrong invoice reference date : {}. Care to try again? ",refDate);
        }

        invoiceReferenceDate = refDate;

        log.debug("Start date successfully set as : {}.",invoiceReferenceDate.toString());
    }

    private void setInvoiceEndDateDate(String endDateString){

        log.debug("Setting reference date as : {}.",endDateString);

        LocalDate endDate = createDateFromString(endDateString);

        try {
            logicValidation(endDate);
        } catch (WrongDateException e) {
            log.error("You have in input the wrong invoice end date : {}. Care to try again? ",endDate);
        }

        invoiceEndDate = endDate;

        log.debug("Start date successfully set as : {}.",invoiceEndDate.toString());
    }

    /**
     * Create LocalDate object from the string in the default format "dd.mm.yyyy"
     *
     * @param dateString date from prepayment details in string format
     * @return LocalDate
     */
    private LocalDate createDateFromString(String dateString) {

        log.debug("Parsing date from string {}.",dateString);

        LocalDate parsedDate = LocalDate.MIN;

        DateTimeFormatter paycalFormatter = getDateTimeFormatter();

        try {
            parsedDate = LocalDate.parse(dateString,paycalFormatter);
        } catch (Exception e) {

            log.error("Date parsing has failed : {}. \n Caused by : {}.",
                    e.getStackTrace(),e.getCause());
        }

        log.debug("Date: {}. successfully parsed from string : {}.",
                parsedDate.toString(),dateString);

        return parsedDate;
    }

    @NotNull
    private DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter
                    .ofLocalizedDate((FormatStyle) dateFormatStyle)
                    .withLocale(locale);
    }

    @Nullable
    public Enum<FormatStyle> getDateFormatStyle() {
        return dateFormatStyle;
    }

    public AbstractPrepayment setDateFormatStyle(Enum<FormatStyle> dateFormatStyle) {
        this.dateFormatStyle = dateFormatStyle;
        return this;
    }

    private Double getCoefficient() {
        return coefficient;
    }

    public AbstractPrepayment setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
        return this;
    }

    private BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public AbstractPrepayment setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
        return this;
    }

    public Double getPrepaymentPeriod() {
        return prepaymentPeriod;
    }

    public AbstractPrepayment setPrepaymentPeriod(Double prepaymentPeriod) {
        this.prepaymentPeriod = prepaymentPeriod;
        return this;
    }

    public Double getInvoicePeriod() {
        return invoicePeriod;
    }

    public AbstractPrepayment setInvoicePeriod(Double invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
        return this;
    }

    public Locale getLocale() {
        return locale;
    }

    public AbstractPrepayment setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public PrepaymentDetails getPrepaymentDetails() {
        return prepaymentDetails;
    }

    public AbstractPrepayment setPrepaymentDetails(PrepaymentDetails prepaymentDetails) {

        this.prepaymentDetails = prepaymentDetails;

        return this;
    }

    public GeneralConfigurations getGeneralConfigurations() {
        return generalConfigurations;
    }

    public AbstractPrepayment setGeneralConfigurations(GeneralConfigurations generalConfigurations) {
        this.generalConfigurations = generalConfigurations;
        return this;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public AbstractPrepayment setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPrepayment that = (AbstractPrepayment) o;
        return Objects.equal(log, that.log) &&
                Objects.equal(invoiceStartDate, that.invoiceStartDate) &&
                Objects.equal(invoiceReferenceDate, that.invoiceReferenceDate) &&
                Objects.equal(invoiceEndDate, that.invoiceEndDate) &&
                Objects.equal(prepaymentPeriod, that.prepaymentPeriod) &&
                Objects.equal(invoicePeriod, that.invoicePeriod) &&
                Objects.equal(coefficient, that.coefficient) &&
                Objects.equal(dateFormatStyle, that.dateFormatStyle) &&
                Objects.equal(locale, that.locale) &&
                Objects.equal(prepaymentDetails, that.prepaymentDetails) &&
                Objects.equal(generalConfigurations, that.generalConfigurations) &&
                Objects.equal(expenseAmount, that.expenseAmount) &&
                Objects.equal(prepaymentAmount, that.prepaymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, invoiceStartDate, invoiceReferenceDate, invoiceEndDate,
                prepaymentPeriod, invoicePeriod, coefficient, dateFormatStyle, locale,
                prepaymentDetails, generalConfigurations, expenseAmount, prepaymentAmount);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("invoiceStartDate", invoiceStartDate)
                .add("invoiceReferenceDate", invoiceReferenceDate)
                .add("invoiceEndDate", invoiceEndDate)
                .add("expenseAmount", expenseAmount)
                .add("prepaymentAmount", prepaymentAmount)
                .toString();
    }

    private void logicValidation(LocalDate givenDate)
            throws WrongDateException, NullPointerException {

        try {
            // check for nullity
            log.debug("Checking whether the date given is null...");
            nullCheck(givenDate);

            log.debug("Invoking the zeroNullity() check...");
            if (zeroNullity()) {

                log.debug("Invoking the zeroNullityValidation...");
                zeroNullityValidation();
            } else {

                log.debug("Invoking the partialNullityValidation...");
                partialNullityValidation();
            }

        } catch (NullPointerException w) {

            log.error("A null pointer exception has been thrown. On the bright side, you \n" +
                    "have gotten this far." + w.getMessage());
        }

    }

    private void partialNullityValidation()
            throws WrongDateException {

        log.debug("Running the partialNullityValidation() method on local date variables...");
        if (startDateIsNull() && !refDateIsNull() && !endDateIsNull()) {

            if (invoiceReferenceDate.isAfter(invoiceEndDate)) {

                throw new WrongDateException("The reference cannot come after the end date");
            }
        } else if (refDateIsNull() && !startDateIsNull() && !endDateIsNull()) {

            if (invoiceStartDate.isAfter(invoiceEndDate)) {

                throw new WrongDateException("The start date can come after end date \n" +
                        "when hell freezes over");
            }
        } else if (endDateIsNull() && !startDateIsNull() && !refDateIsNull()) {

            if (invoiceStartDate.isAfter(invoiceReferenceDate)) {

                throw new WrongDateException("The invoice start date can come after the reference date \n" +
                        "when hell freezes over");
            }
        }
        if (endDateIsNull() && refDateIsNull() && startDateIsNull()) {
            //return;
        }

    }

    private void zeroNullityValidation() throws WrongDateException {

        log.debug("Running the zeroNullityValidation() method on local date variables...");
        if (invoiceStartDate.isAfter(invoiceReferenceDate)) {

            throw new WrongDateException("Date of reference does not precede invoice start date");

        } else if (invoiceReferenceDate.isAfter(invoiceEndDate)) {

            throw new WrongDateException("The reference cannot come after the end date");

        } else if (invoiceStartDate.isAfter(invoiceEndDate)) {

            throw new WrongDateException("The start date cannot be after end date");

        } else if (invoiceEndDate.isEqual(invoiceStartDate) || invoiceEndDate.isEqual(invoiceReferenceDate)
                || invoiceReferenceDate.isEqual(invoiceStartDate)) {

            throw new WrongDateException("This is a prepayment transaction. Right?");

        }
    }

    private boolean zeroNullity() {
        return !endDateIsNull() && !refDateIsNull() && !startDateIsNull();
    }

    private boolean startDateIsNull() {

        return invoiceStartDate == null;
    }

    private boolean refDateIsNull() {

        return invoiceReferenceDate == null;
    }


    private boolean endDateIsNull() {

        return invoiceEndDate == null;
    }

    private void nullCheck(LocalDate valDate)
            throws NullPointerException {

        if (valDate == null) {
            throw new NullPointerException("Please input a date...");
        }

    }

    private static class WrongDateFormatException
            extends Exception {
        public WrongDateFormatException(String message) {
            super(message);
        }
    }

    private class WrongDateException extends Exception {

        private WrongDateException(String message) {
            super(message);
        }
    }


}
