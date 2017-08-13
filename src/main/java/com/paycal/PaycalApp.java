package com.paycal;

import com.paycal.api.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by edwin.njeru on 10/08/2017.
 */
public class PaycalApp {
    private final Date now;

    @Autowired
    private FeedBack feedBack;

    @Autowired
    private PaymentFactory factory;


    public PaycalApp(){
        now = gettingTime();
    }

    public void run(){
        feedBack.printIntro();
        // This leads with header for the app

        feedBack.initialMenu();
        // The user is pressnted with choices of courses of action to follow

        feedBack.mainPrompt();
        // user is prompted to type the reply

        factory.mainSwitch();
        // Here we run options based on user's choice
    }

    /**To get the current date
     * This will be included in the introduction in order to identify
     * when a particular transaction was computed
     * @return
     */
    public static Date gettingTime(){
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



    public Date getNow() {

        return now;
    }
}
