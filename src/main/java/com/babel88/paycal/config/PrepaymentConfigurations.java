package com.babel88.paycal.config;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * This class creates an entry point where general settings for the program like
 * the locale the date format style can be interacted with.
 * The date format styles must be compliant with the JSR 310
 */
public class PrepaymentConfigurations {

    private final static Logger log = LoggerFactory.getLogger(PrepaymentConfigurations.class);

    // Keeps track of current date format style
    private Enum<FormatStyle> dateFormatStyle;

    // Keeps track of locale settings
    private Locale locale;

    public PrepaymentConfigurations() {

        log.debug("Creating a general configurations object, with default styles and locale");

        this.dateFormatStyle = FormatStyle.MEDIUM;

        this.locale = Locale.GERMAN;
    }

    public Enum<FormatStyle> getDateFormatStyle() {

        log.debug("Ret val: {}. returned from getDateFormatStyle() method", dateFormatStyle.toString());
        return dateFormatStyle;
    }

    /**
     * Sets the date format style used by the date formatter in the SimplePrepayments class
     *
     * @param dateFormatStyle The format to be used for the date
     * @return Prepayment configurations to be applied
     */
    public PrepaymentConfigurations setDateFormatStyle(Enum<FormatStyle> dateFormatStyle) {
        this.dateFormatStyle = dateFormatStyle;
        log.debug("Date format style set as : ", dateFormatStyle.toString());
        return this;
    }

    public Locale getLocale() {

        log.debug("Ret val: {}. returned from getLocale() method", locale.toString());
        return locale;
    }

    /**
     * Sets the Locale used in the date formatter of the SimplePrepayments class
     *
     * @param locale Default locale
     * @return PrepaymentConfigurations
     */
    public PrepaymentConfigurations setLocale(Locale locale) {
        this.locale = locale;
        log.debug("Locale set as : ", locale.toString());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrepaymentConfigurations that = (PrepaymentConfigurations) o;
        return Objects.equal(log, that.log) &&
                Objects.equal(dateFormatStyle, that.dateFormatStyle) &&
                Objects.equal(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, dateFormatStyle, locale);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dateFormatStyle", dateFormatStyle)
                .add("locale", locale)
                .toString();
    }
}
