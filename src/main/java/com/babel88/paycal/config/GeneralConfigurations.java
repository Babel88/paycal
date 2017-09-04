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
public class GeneralConfigurations {

    Logger log = LoggerFactory.getLogger(GeneralConfigurations.class);

    // Keeps track of current date format style
    private Enum<FormatStyle> dateFormatStyle;

    // Keeps track of locale settings
    private Locale locale;
    private static GeneralConfigurations instance = new GeneralConfigurations();

    public GeneralConfigurations() {

        log.debug("Creating a general configurations object, with default styles and locale");

        this.dateFormatStyle = FormatStyle.MEDIUM;

        this.locale = Locale.GERMAN;
    }

    public static GeneralConfigurations getInstance() {
        return instance;
    }

    public static void setInstance(GeneralConfigurations instance) {
        GeneralConfigurations.instance = instance;
    }

    public Enum<FormatStyle> getDateFormatStyle() {

        log.debug("Ret val: {}. returned from getDateFormatStyle() method",dateFormatStyle.toString());
        return dateFormatStyle;
    }

    public Locale getLocale() {

        log.debug("Ret val: {}. returned from getLocale() method",locale.toString());
        return locale;
    }

    /**
     * Sets the date format style used by the date formatter in the AbstractPrepayment class
     *
     * @param dateFormatStyle
     * @return
     */
    public GeneralConfigurations setDateFormatStyle(Enum<FormatStyle> dateFormatStyle) {
        this.dateFormatStyle = dateFormatStyle;
        log.debug("Date format style set as : ",dateFormatStyle.toString());
        return this;
    }

    /**
     * Sets the Locale used in the date formatter of the AbstractPrepayment class
     *
     * @param locale
     * @return
     */
    public GeneralConfigurations setLocale(Locale locale) {
        this.locale = locale;
        log.debug("Locale set as : ",locale.toString());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralConfigurations that = (GeneralConfigurations) o;
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
