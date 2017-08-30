package com.babel88.paycal.controllers.prepayments;

import com.babel88.paycal.api.logic.Prepayable;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

import static java.lang.System.out;

public class PrepaymentController implements com.babel88.paycal.api.controllers.PrepaymentController {

    private static PrepaymentController instance = new PrepaymentController(BigDecimal.ZERO);
    private final Logger log =
            LoggerFactory.getLogger(this.getClass());
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
    /*
     * This object provides the implementation of the Prepayable interface
     * used here to get the amount to prepay
     */
    private Prepayable abstractPrepayment;
    /*
     * This object delivers the standard user prompt for the application
     */
    private FeedBack feedBack;

    public PrepaymentController(BigDecimal expenseAmount) {

        log.debug("Creating payment controller from factory");

        abstractPrepayment = LogicFactory.getInstance().createPrepayable();

        feedBack = GeneralFactory.getInstance().createFeedback();

        this.expenseAmount = expenseAmount;

        prepaymentAmount = BigDecimal.ZERO;

        keyboard = new Scanner(System.in);
    }

    public static PrepaymentController getInstance() {
        return instance;
    }

    private void userPrompt(){

        out.println("\nDo you want to prepay part of the expense?");

        feedBack.mainPrompt();
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

    public PrepaymentController setPrepay(Boolean prepay) {
        this.prepay = prepay;
        return this;
    }
}
