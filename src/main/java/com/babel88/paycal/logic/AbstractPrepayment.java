package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.Prepayable;
import com.babel88.paycal.config.GeneralConfigurations;
import com.babel88.paycal.api.InvoiceDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Implementation of the Prepayable interface using jsr310
 */
public class AbstractPrepayment implements Prepayable,Serializable {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private LocalDate invoiceStartDate;

    private LocalDate invoiceReferenceDate;

    private LocalDate invoiceEndDate;

    // Keeps track of current date format style
    private Enum<FormatStyle> dateFormatStyle;

    // Keeps track of locale settings
    private Locale locale;

    private InvoiceDetails invoice;

    public AbstractPrepayment(GeneralConfigurations generalConfigurations) {

        log.debug("Creating abstractPrepayment with default configurations, \n" +
                "dateFormatStyle = MEDIUM \n" +
                "locale = GERMAN\n");

        this.dateFormatStyle = generalConfigurations.getDateFormatStyle();

        locale = generalConfigurations.getLocale();
    }

    /**
     * This method takes any prepayment and calculates the amount of prepayment.
     * This updates use of depracated implementations in SimplePrepayments and Checked
     * prepayments to apply use of JSR 310
     *
     * @param expense amount
     * @return amount of prepayment
     */
    @Override
    public BigDecimal calculatePrepayment(BigDecimal expense) {

        doPrepayment();

        return null;
    }

    public void doPrepayment(){

        setInvoiceStartDate(invoice.getInvoiceStartDate());

        setInvoiceReferenceDateDate(invoice.getInvoiceRefDate());

        setInvoiceEndDateDate(invoice.getInvoiceEndDate());
    }

    @Autowired
    public AbstractPrepayment setInvoice(InvoiceDetails invoice) {
        this.invoice = invoice;
        return this;
    }

    @Nullable
    public LocalDate getInvoiceStartDate() {
        return invoiceStartDate;
    }

    @Nullable
    public LocalDate getInvoiceReferenceDate() {
        return invoiceReferenceDate;
    }

    @Nullable
    public LocalDate getInvoiceEndDate() {
        return invoiceEndDate;
    }

    private void setInvoiceStartDate(String startDateString){

        log.debug("Setting start date as : {}.",startDateString);

        invoiceStartDate = createDateFromString(startDateString);

        log.debug("Start date successfully set as : {}.",invoiceStartDate.toString());
    }

    private void setInvoiceReferenceDateDate(String refDateString){

        log.debug("Setting reference date as : {}.",refDateString);

        invoiceReferenceDate = createDateFromString(refDateString);

        log.debug("Start date successfully set as : {}.",invoiceReferenceDate.toString());
    }

    private void setInvoiceEndDateDate(String endDateString){

        log.debug("Setting reference date as : {}.",endDateString);

        invoiceEndDate = createDateFromString(endDateString);

        log.debug("Start date successfully set as : {}.",invoiceEndDate.toString());
    }

    /**
     * Create LocalDate object from the string in the default format "dd.mm.yyyy"
     *
     * @param dateString
     * @return LocalDate
     */
    public LocalDate createDateFromString(String dateString){

        log.debug("Parsing date from string {}.",dateString);

        LocalDate parsedDate = LocalDate.MIN;

        DateTimeFormatter paycalFormatter = getDateTimeFormatter();

        try {
            parsedDate = LocalDate.parse(dateString,paycalFormatter);
        } catch (Exception e) {

            log.error("Date parsing has failed : {}. \n Caused by : {}.",e.getStackTrace(),e.getCause());
        }

        log.debug("Date: {}. successfully parsed from string : {}.",parsedDate.toString(),dateString);

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
}
