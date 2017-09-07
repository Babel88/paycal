package com.ghacupha.paycal.controllers.prepayments;

import com.ghacupha.paycal.api.controllers.PrepaymentController;
import com.ghacupha.paycal.api.logic.Prepayable;
import com.ghacupha.paycal.api.view.FeedBack;
import com.ghacupha.paycal.config.factory.GeneralFactory;
import com.ghacupha.paycal.config.factory.LogicFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

import static java.lang.System.out;

public class PrepaymentControllerImpl implements PrepaymentController {

    private static PrepaymentController instance = new PrepaymentControllerImpl(BigDecimal.ZERO);
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

    public PrepaymentControllerImpl(BigDecimal expenseAmount) {

        log.debug("Creating payment controller from factory");

        abstractPrepayment = LogicFactory.getPrepayable();

        feedBack = GeneralFactory.createFeedback();

        this.expenseAmount = expenseAmount;

        prepaymentAmount = BigDecimal.ZERO;

        keyboard = new Scanner(System.in);
    }

    public static PrepaymentController getInstance() {
        return instance;
    }

    private void userPrompt() {

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
    private PrepaymentControllerImpl setPrepay() {

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
    private BigDecimal runPrepay(BigDecimal expenseAmount) {


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
    @NotNull
    @Override
    public BigDecimal getPrepayment(@NotNull BigDecimal totalExpense) {

        userPrompt();

        setPrepay();

        return runPrepay(expenseAmount);
    }

    @NotNull
    @Override
    public PrepaymentControllerImpl setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
        return this;
    }

    public PrepaymentControllerImpl setPrepay(Boolean prepay) {
        this.prepay = prepay;
        return this;
    }
}
