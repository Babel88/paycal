package com.babel88.paycal;

import com.babel88.paycal.api.view.FeedBack;
import com.google.common.base.Objects;
import org.jetbrains.annotations.Contract;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Main entry point for the application
 * <p>
 * Created by edwin.njeru on 10/08/2017.
 */
public class PaycalApp {

    private static final Date now = gettingTime();

    private FeedBack feedBack;

    private PaymentFactory paymentFactory;


    public PaycalApp(FeedBack feedBack, PaymentFactory paymentFactory) {

        this.feedBack = feedBack;

        this.paymentFactory = paymentFactory;
    }

    /**
     * To get the current date
     * This will be included in the introduction in order to identify
     * when a particular transaction was computed
     *
     * @return starting time
     */
    private static Date gettingTime() {
        //Create a java calendar instance
        Calendar calendar = Calendar.getInstance();
        //get a java.util.Date from the calendar instance.
        //this date represents the current instant, or now
        Date now = calendar.getTime();
        //A Java current time (now) instance
        Date currentTimestamp;
        currentTimestamp = new Timestamp(calendar.getTime().getTime());
        //out.println(currentTimestamp);
        return currentTimestamp;
    }

    @Contract(pure = true)
    public static Date getNow() {

        return now;
    }

    void run() {
        feedBack.printIntro();
        // This leads with header for the app

        feedBack.initialMenu();
        // The user is pressnted with choices of courses of action to follow

        feedBack.mainPrompt();
        // user is prompted to type the reply

        paymentFactory.mainSwitch();
        // Here we run options based on user's choice
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaycalApp paycalApp = (PaycalApp) o;
        return Objects.equal(feedBack, paycalApp.feedBack) &&
                Objects.equal(paymentFactory, paycalApp.paymentFactory);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(feedBack, paymentFactory);
    }
}
