package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.logic.NullPaymentModelPassedException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * This class provides logic for manipulating the payment model provided that the
 * amount before tax is given.
 * This is done by overriding the get amount before tax method, which gives us a variable to
 * use as base
 * If not provided the  same is obtained from the invoiceAmount provided at runtime,
 * which will result in less than optimal speed of execution.
 * A variable for deductions against total expense is also maintained.This deduction once subtracted
 * from total expense will result in amount payable to payee.
 * The prepayments method will result in a side effect that adjusts the object passed as parameter
 * adjusting the total expense with respect to the prepayment amount calculated. A pointer for
 * deductions against total expense is also maintained incase in future there are items that
 * can be deducted from the total expense like the input VAT deduction
 *
 * TODO make this class concrete
 * Created by edwin.njeru on 25/08/2017.
 */
public class AbstractBaseLogicModel implements DefaultBaseLogicModel {

    @Nullable
    private final PaymentParameters paymentParameters;
    @Nullable
    private final PrepaymentController prepaymentController;
    @Nullable
    private final InvoiceDetails invoice;
    private final Logger log = LoggerFactory.getLogger(AbstractBaseLogicModel.class);
    private BigDecimal amountBeforeTax;
    private BigDecimal totalExpenses;
    private BigDecimal invoiceAmount;


    /*Keeps track of deductions from the invoice amount */
    private BigDecimal deductions;

    /*Keeps track of deductions from the total expense */
    private BigDecimal lessFromTotalExpenses;

    public AbstractBaseLogicModel() {

        log.debug("Creating the abstract base logic model");

        log.debug("Fetching the payment parameters from factory");
        paymentParameters = LogicFactory.getInstance().createPaymentParameters();

        log.debug("Fetching the prepayment controller from the Controller factory");
        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();

        log.debug("Initiating the deductions pointer to track total deductions");
        deductions = new BigDecimal(ZERO);

        log.debug("Initiating the lessFromTotalExpenses pointer to track total from expenses amount");
        lessFromTotalExpenses= new BigDecimal(ZERO);

        invoice = GeneralFactory.getInstance().createInvoice();
    }

    protected DefaultBaseLogicModel initialization() {

        BigDecimal invoiceAmount = invoice.invoiceAmount();
        getAmountBeforeTax(invoiceAmount);
        getTotalExpenses(invoiceAmount);

        return this;
    }

    /**
     * Calculates the withholding VAT for the caller based on the amount before
     * vat.
     * If null then the invoice amount provided at runtime is used
     *
     * @param invoiceAmount
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {

        log.debug(" calculateWithholdingVat( {}. ) has been called", amountBeforeTax);

        BigDecimal wthVat = new BigDecimal(ZERO);

        if (amountBeforeTax != null) {

            log.debug("amount before tax has been computed. Calculating the withholding vat \n" +
                    "using the withholding vat rate of : {}.", paymentParameters.getWithholdingVatRate());
            wthVat = amountBeforeTax
                    .multiply(paymentParameters.getWithholdingVatRate())
                    .setScale(2, HALF_EVEN);
            log.debug("Returning withholding vat amount of {}.",wthVat);

            deductions.add(wthVat);
            log.debug("Total deductions set as : {}.",deductions);
        } else {

            log.error("The amount before tax is null.");

            BigDecimal b4Tax = typicalAmountBeforeTax(invoiceAmount);

            wthVat = amountBeforeTax
                    .multiply(paymentParameters.getWithholdingVatRate())
                    .setScale(2, HALF_EVEN);
            log.debug("Returning withholding vat amount of {}.", wthVat);

            deductions.add(wthVat);
            log.debug("Total deductions set as : {}.", deductions);

            return wthVat;
        }

        return wthVat;
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
     * @return total tax rates
     */
    private BigDecimal sumOfTotalTaxRates() {

        return paymentParameters.getVatRate();
    }

    /**
     * Should definitely override this class in subclasses, especially where the
     * withholding tax is going to be different from the main stream 5%
     *
     * @param invoiceAmount provided at runtime by user
     * @return amount of withholding tax
     */
    @Override
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {

        BigDecimal wthTax = new BigDecimal(ZERO);

        log.debug("calculateWithholdingTax() method has been invoked. Assuming the amount \n" +
                "before tax and the payment parameters is not null");
        if(amountBeforeTax != null && paymentParameters != null) {
            wthTax = this.amountBeforeTax
                    .multiply(
                            paymentParameters
                                    .getWithholdingTaxRate()
                    ).setScale(
                            2, HALF_EVEN
                    );
            log.debug("Withholding tax calculated as : {}.",wthTax);

            deductions.add(wthTax).setScale(2,HALF_EVEN);

            log.debug("Total deductions set as : {}.",deductions);

        } else if(amountBeforeTax == null && paymentParameters != null) {

            log.warn("The payment parameters object is null. Attempting to initiate from factory \n" +
                    "You are about to get very unhappy");

            wthTax = typicalAmountBeforeTax(invoiceAmount)
                    .multiply(
                            paymentParameters
                                    .getWithholdingTaxRate()
                    ).setScale(
                            2, HALF_EVEN
                    );
            log.debug("Withholding tax calculated as : {}.",wthTax);

            deductions.add(wthTax);

            log.debug("Total deductions set as : {}.",deductions);

        } else if(amountBeforeTax == null && paymentParameters == null){

            log.warn("Whoa! Both the amount before tax and the payment parameters are NULL! \n" +
                    "Something ugly has just hit the fan. Recheck the logc subclassing :{}."
            ,this.getClass().getName());

            if(LogicFactory.getInstance() != null) {
                wthTax = typicalAmountBeforeTax(invoiceAmount)
                        .multiply(
                                LogicFactory.getInstance().createPaymentParameters()
                                        .getWithholdingTaxRate()
                        ).setScale(
                                2, HALF_EVEN
                        );
                log.debug("Withholding tax calculated as : {}.",wthTax);

                deductions.add(wthTax);

                log.debug("Total deductions set as : {}.",deductions);

            } else {

                wthTax = typicalAmountBeforeTax(invoiceAmount)
                        .multiply(BigDecimal.valueOf(0.05)
                        ).setScale(
                                2, HALF_EVEN
                        );
                log.debug("Withholding tax calculated as : {}.",wthTax);

                deductions.add(wthTax);

                log.debug("Total deductions set as : {}.",deductions);

            }
        }

        return wthTax;
    }

    /**
     * Hopefull you won't have to override this method, which calculates the prepayment amount
     * based on the total expense. If that doesn't work the amount is called from the default
     * payment model passed as parameter
     *
     * @param partiallyCreatedDefaultPaymentModel to call getTotalExpense()
     *
     * @return prepayment amount
     */
    @Override
    public BigDecimal calculateToPrepay(DefaultPaymentModel partiallyCreatedDefaultPaymentModel) {
        BigDecimal prepay = new BigDecimal(ZERO);
        BigDecimal totalExpense = new BigDecimal(ZERO);

        if(totalExpense != null){

            log.debug("Assigning the total expense from calculations based on subclass of :{}.", totalExpenses);
            totalExpense = totalExpenses;
            log.debug("Total expense for prepayment set as : {}.",totalExpense);
        } else {
            log.warn("Getting the total expense from the partially created default payment model");
            if (partiallyCreatedDefaultPaymentModel != null) {
                totalExpense = partiallyCreatedDefaultPaymentModel.getTotalExpense();
                log.debug("Total expense for prepayment set as : {}.",totalExpense);
            } else {

                log.error("Whoa! The partially created default payment model object you passed is null \n" +
                        "Defaulting to typical total expenses figure.You are getting agitated...");
                // :-I
            }
        }

        if(prepaymentController != null) {
            log.debug("Calculating the total expense from the prepaymentcontroller");
            prepaymentController.setExpenseAmount(totalExpense);
            prepay = ((PrepaymentService) prepaymentController::getPrepayment).prepay(totalExpense);

            log.debug("Returning prepayment amount : {}.",prepay);

            log.debug("Incrementing the amount to less from expenses with the prepayment amount \n" +
                    "and attempting to change the same from the reference given as defaultPaymentModel");
            lessFromTotalExpenses.add(prepay).setScale(2,HALF_EVEN);
            log.debug("Attempting to decrement the total expenses in the defaultPaymentModel with" +
                    "a prepayment amount of : {}.",lessFromTotalExpenses);
            partiallyCreatedDefaultPaymentModel
                    .setTotalExpense(
                            partiallyCreatedDefaultPaymentModel
                                    .getTotalExpense()
                            .subtract(lessFromTotalExpenses)
                    );
        } else{

            log.debug("The prepayment controller is null. Fetching an instance from the Controller factory \n" +
                    "the runtime cost is going to be less than optimal");
            PrepaymentController controller = ControllerFactory.getInstance().createPrepaymentController();

            controller.setExpenseAmount(totalExpense);
            prepay = ((PrepaymentService) controller::getPrepayment).prepay(totalExpense);

            log.debug("Returning prepayment amount : {}.",prepay);

            log.debug("Incrementing the amount to less from expenses with the prepayment amount \n" +
                    "and attempting to change the same from the reference given as defaultPaymentModel");
            lessFromTotalExpenses.add(prepay).setScale(2,HALF_EVEN);
            log.debug("Attempting to decrement the total expenses in the defaultPaymentModel with" +
                    "a prepayment amount of : {}.",lessFromTotalExpenses);
            partiallyCreatedDefaultPaymentModel
                    .setTotalExpense(
                            partiallyCreatedDefaultPaymentModel
                                    .getTotalExpense()
                                    .subtract(lessFromTotalExpenses)
                    );
        }

        return prepay;
    }

    @Override
    public BigDecimal calculateToPayee(DefaultPaymentModel partiallyCreatedDefaultPaymentModel) throws NullPaymentModelPassedException {

        BigDecimal toPayee = new BigDecimal(ZERO);

        if(partiallyCreatedDefaultPaymentModel != null){

            log.debug("Using the payment model passed in parameter : {}. to calculate \n" +
                    "the amount payable to payee, by deducting from total deductions : {}.",
                    partiallyCreatedDefaultPaymentModel, deductions);
            toPayee = partiallyCreatedDefaultPaymentModel.getTotalExpense()
                    .subtract(deductions)
                    .setScale(2,HALF_EVEN);
            log.debug("Returning amount payable to payee as : {}.",toPayee);
        } else {

            throw new NullPaymentModelPassedException("Please note that a null payment model object \n" +
                    "has been passed to logic. Please rerun null checks again");
        }

        return toPayee;
    }

    /**
     * This method return the amount of money charged in the invoice before taxes are considered
     *
     * @return amount before taxes
     */
    public BigDecimal getAmountBeforeTax(BigDecimal invoiceAmount) {

        //TODO Amount before tax
        return invoiceAmount
                .divide(
                        ONE.add(paymentParameters.getVatRate())
                ).setScale(2, HALF_EVEN);
    }

    /**
     * This method returns the total expenses for the payment in question
     *
     * @return
     */
    public BigDecimal getTotalExpenses(BigDecimal invoiceAmount) {

        return invoiceAmount
                .subtract(lessFromTotalExpenses)
                .setScale(2, HALF_EVEN);
    }

    ;
}
