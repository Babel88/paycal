package com.babel88.paycal.controllers;

import com.babel88.paycal.api.logic.Prepayable;
import com.babel88.paycal.api.view.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

import static java.lang.System.out;

@Component
@ComponentScan
public class PrepaymentController implements com.babel88.paycal.api.logic.PrepaymentController{

    /*
     * Internal flag for whether or not to run prepayment
     */
    private Boolean prepay;

    /*
     * prepayment amount to be return by controller
     */
    private BigDecimal prepaymentAmount;

    /*
     * expense amount amount to be injected into the controller
     */
    private BigDecimal expenseAmount;

    /*
     * Scanner object to get user input from console
     */
    private Scanner keyboard;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass());

    /*
     * This object delivers the standard user prompt for the application
     */
    private FeedBack feedBack;

    /*
     * This object provides the implementation of the Prepayable interface
     * used here to get the amount to prepay
     */
    //TODO to create a prepayment service complete with input validation
    private Prepayable abstractPrepayment;

    public PrepaymentController(BigDecimal expenseAmount) {

        this.expenseAmount = expenseAmount;

        prepaymentAmount = BigDecimal.ZERO;

        keyboard = new Scanner(System.in);
    }

    private void userPrompt(){

        out.println("\nDo you want to prepay part of the expense?");

        feedBack.mainPrompt();
    }

    @Autowired
    public PrepaymentController setAbstractPrepayment(Prepayable abstractPrepayment) {
        this.abstractPrepayment = abstractPrepayment;
        return this;
    }

    @Autowired
    public PrepaymentController setFeedBack(FeedBack feedBack) {
        this.feedBack = feedBack;
        return this;
    }

    /**
     * This method prompts the user to consider whether to prepay some of the expenses
     * over  a given period. It is a chainable method hence it must return a partially
     * complete object of this interface, after updating an internally accessible
     * boolean flag
     *
     * @return this
     */
    public PrepaymentController setPrepay() {

        log.debug("Setting whether or not we are to prepay the expense amount...");

        String userRequest = keyboard.next();

        this.prepay =
                userRequest.equalsIgnoreCase("yes");

        return this;
    }

    /**
     * After updating the internal flag on whether or not to prepay the expenses,
     * this method runs the prepayment and return the prepayment amount to the caller
     * as BigDecimal
     *
     * @return the value amount of prepayment in BigDecimal
     */
    public BigDecimal runPrepay(BigDecimal expenseAmount) {


        this.prepaymentAmount =
                (prepay) ? abstractPrepayment.calculatePrepayment(expenseAmount) : BigDecimal.ZERO;

        return prepaymentAmount;
    }

    /**
     * This method takes the total expense and runs all the methods required to calculate
     * prepayment which is returned to the caller
     * <p>
     * Additional value add processes are plugged in in the implementation service to clean
     * up the user input
     *
     * @param totalExpense amount to be prepaid partially
     * @return prepayment amount in BigDecimal
     */
    @Override
    public BigDecimal getPrepayment(BigDecimal totalExpense) {

        userPrompt();

        setPrepay();

        return runPrepay(expenseAmount);
    }

    @Override
    public PrepaymentController setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
        return this;
    }
}
