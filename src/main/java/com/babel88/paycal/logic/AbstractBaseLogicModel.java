package com.babel88.paycal.logic;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * This class provides logic for manipulating the payment model provided that the
 * amount before tax is given.
 * This is done byoverriding the get amount before tax method, which gives us a variable to
 * use as base
 * If not provided the  same is obtained from the invoiceAmount provied at runtime,
 * which will result in less than optimal speed of execution
 * TODO finish methods in this class and subclass with logic objects
 * Created by edwin.njeru on 25/08/2017.
 */
public abstract class AbstractBaseLogicModel implements DefaultBaseLogicModel {

    private final PaymentParameters paymentParameters;
    private final Logger log = LoggerFactory.getLogger(AbstractBaseLogicModel.class);
    private BigDecimal amountBeforeTax;
    private BigDecimal totalExpenses;

    public AbstractBaseLogicModel() {

        log.debug("Creating the abstract base logic model");

        log.debug("Fetching the amount before tax from child class");
        amountBeforeTax = getAmountBeforeTax();

        log.debug("Fetching the amount of total expense from child class");
        totalExpenses = getTotalExpenses();

        log.debug("Fetching the payment parameters from factory");
        paymentParameters = LogicFactory.getInstance().createPaymentParameters();
    }

    /**
     * Calculates the withholding VAT for the caller based on the amount before
     * vat.
     * If null then the invoice amount provided at runtime is used
     *
     * @param invoiceAmount
     * @return
     */
    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {

        log.debug(" calculateWithholdingVat( {}. ) has been called", invoiceAmount);

        if (amountBeforeTax != null) {

            log.debug("amount before tax has been computed. Calculating the withholding vat \n" +
                    "using the withholding tax rate of : {}.", paymentParameters.getWithholdingVatRate());
            return amountBeforeTax
                    .multiply(paymentParameters.getWithholdingVatRate())
                    .setScale(2, HALF_EVEN);
        } else {

            log.warn("The amount before tax is null. Calling the typicalAmountBeforeTax() method");
            return typicalAmountBeforeTax(invoiceAmount)
                    .multiply(paymentParameters.getWithholdingVatRate())
                    .setScale(2, HALF_EVEN);
        }
    }

    /**
     * This is a curious method one whose need is best taken care of through other means.
     * It happens if the amount before tax is null, in which cas an inference is drawn
     * based on the invoice amount and then the local amountBeforeTax variable is updated
     *
     * @param invoiceAmount provided at runtime
     * @return the amount before Taxes
     */
    private BigDecimal typicalAmountBeforeTax(BigDecimal invoiceAmount) {

        log.warn("Amount before tax calculation has been called on a generic \n" +
                "abstract logic model. Under these conditions the computation might result in \n" +
                "less than accurate figures");
        amountBeforeTax = invoiceAmount
                .divide(
                        (paymentParameters.getWithholdingVatRate()
                                .add(ONE)
                        ),
                        HALF_EVEN
                )
                .setScale(2, HALF_EVEN);

        log.debug("Returning amount before tax of : {}.", amountBeforeTax);
        return amountBeforeTax;
    }

    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        log.debug("Calculate total expense has been called...");

        log.debug("Checking if total expenses is null");
        if (totalExpenses != null) {

            log.debug("Returning total expenses of : {}.", totalExpenses);
            return totalExpenses;

        } else if (amountBeforeTax != null) {

            log.warn("The total expenses variable is null. Estimating from the total \n" +
                    "sum of rate and taxes. The accuracy of this approach cannot be guaranteed");
            BigDecimal expenses =
                    amountBeforeTax.multiply(
                            ONE
                                    .add(sumOfTotalTaxRates())
                    )
                            .setScale(2, HALF_EVEN);

            log.debug("Returning total expenses as : {}.", expenses);

            return expenses;

        } else {
            log.warn("Both total expenses and amountBeforeTax variable are null. Estimating from the total \n" +
                    "sum of rate and taxes. The accuracy of this approach cannot be guaranteed \n" +
                    "You are not going to be happy");

            BigDecimal expenses =
                    typicalAmountBeforeTax(invoiceAmount).multiply(

                            ONE
                                    .add(sumOfTotalTaxRates())
                    )
                            .setScale(2, HALF_EVEN);

            log.debug("Returning total expenses as : {}.", expenses);
            return expenses;
        }
    }

    /**
     * This curious method will sum up all taxes which if added to one and multiplied
     * by the amount before vat will result in the total expense
     *
     * @return
     */
    private BigDecimal sumOfTotalTaxRates() {

        return paymentParameters.getVatRate();
    }

    @Override
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {
        return null;
    }

    @Override
    public BigDecimal calculateToPrepay(DefaultPaymentModel partiallyCreatedDefaultPaymentModel) {
        return null;
    }

    @Override
    public BigDecimal calculateToPayee(DefaultPaymentModel partiallyCreatedDefaultPaymentModel) {
        return null;
    }

    /**
     * This method return the amount of money charged in the invoice before taxes are considered
     *
     * @return amount before taxes
     */
    public abstract BigDecimal getAmountBeforeTax();

    /**
     * This method returns the total expenses for the payment in question
     *
     * @return
     */
    public abstract BigDecimal getTotalExpenses();
}
