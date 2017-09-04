package com.babel88.paycal;

import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Main entry point for the application
 *
 * Created by edwin.njeru on 10/08/2017.
 */
public class PaycalApp {

    private final Logger log = LoggerFactory.getLogger(PaycalApp.class);
    private final Date now;
    private FeedBack feedBack;
    private PaymentFactory factory;


    public PaycalApp() {

        log.debug("Creating an instance of the paycalApp");

        feedBack = GeneralFactory.createFeedback();
        factory = GeneralFactory.createPaymentFactory();
        now = gettingTime();
    }
    /**To get the current date
     * This will be included in the introduction in order to identify
     * when a particular transaction was computed
     * @return starting time
     */
    private static Date gettingTime() {
        //Create a java calendar instance
        Calendar calendar=Calendar.getInstance();
        //get a java.util.Date from the calendar instance.
        //this date represents the current instant, or now
        Date now=calendar.getTime();
        //A Java current time (now) instance
        Date currentTimestamp;
        currentTimestamp = new Timestamp(calendar.getTime().getTime());
        //out.println(currentTimestamp);
        return currentTimestamp;
    }

    void run() {
        feedBack.printIntro();
        // This leads with header for the app

        feedBack.initialMenu();
        // The user is pressnted with choices of courses of action to follow

        feedBack.mainPrompt();
        // user is prompted to type the reply

        factory.mainSwitch();
        // Here we run options based on user's choice
    }

    public Date getNow() {

        return now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaycalApp paycalApp = (PaycalApp) o;
        return Objects.equal(log, paycalApp.log) &&
                Objects.equal(getNow(), paycalApp.getNow()) &&
                Objects.equal(feedBack, paycalApp.feedBack) &&
                Objects.equal(factory, paycalApp.factory);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, getNow(), feedBack, factory);
    }
}
