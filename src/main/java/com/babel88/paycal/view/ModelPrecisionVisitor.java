package com.babel88.paycal.view;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.models.PaymentModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.UP;

/**
 * This object will visit a payment model and review the precision of the existing
 * variables as follows:
 * a) The amount payable to vendor is precise to the nearest 0.05
 * b) The amount of withholding tax and withholding vat is precise to the nearest 1.00
 *
 * This object will also adjust the expenses and amount payable to payee to reflect change
 * of precision, under the following considerations:
 * a) Withholding taxes are either rounded up or not rounded up at all
 * b) The amount payable to vendor can be rounded up or down depending on arity towards the
 *    point of precision
 *
 *  Therefore the post precision adjustments are as follows
 *  a) The increment in withholding taxes will reduce the amount payable to vendor, instead of
 *    increasing the expense amount
 *  b) Increment in the amount to the vendor will increase the expense and decrement in the amount
 *    payable to vendor will reduce the expense amount
 */
public class ModelPrecisionVisitor implements Visitor {

    private final Logger log = LoggerFactory.getLogger(ModelPrecisionVisitor.class);

    /*Pointer to the payment model*/
    private DefaultPaymentModel paymentModel;

    public ModelPrecisionVisitor() {

        log.debug("Creating the modelPrecisionVisitor : {}",this);
    }

    @Override
    public void visit(PaymentModel Visitable) {

        this.paymentModel = reviewModelPrecision(Visitable);
    }


    /**
     * Reviews the precision of the amount payable to vendor to be to the nearest 0.05
     * and the amounts of withholding taxes to the nearest 1.00
     *
     * @param paymentModel payment model with raw precision
     *
     * @return payment model with adjusted precision
     */
    public DefaultPaymentModel reviewModelPrecision(DefaultPaymentModel paymentModel) {

        BigDecimal[] preciseWithholdingTax = resetWithholdingTax(paymentModel);

        BigDecimal[] preciseWithholdingVat = resetWithholdingVat(paymentModel);
        BigDecimal[] preciseToPayee =
                resetAmountToPayee(paymentModel,
                        paymentModel.getToPayee().subtract((preciseWithholdingTax[1]).add(preciseWithholdingVat[1])));
        resetTotalExpenses(paymentModel, preciseToPayee[1]);


        return paymentModel;
    }

    private void resetTotalExpenses(DefaultPaymentModel paymentModel, BigDecimal payeeAdjustment) {
        // Adjust total expenses
        if(payeeAdjustment.signum() == 1){

            paymentModel.setTotalExpense(
                    paymentModel.getTotalExpense().subtract(payeeAdjustment)
            );
        } else if(payeeAdjustment.signum() == -1){
            paymentModel.setTotalExpense(
                    paymentModel.getTotalExpense().add(payeeAdjustment)
            );
        } else if(payeeAdjustment.signum() == 0){

            // do nothing
        }
    }

    @NotNull
    private BigDecimal[] resetAmountToPayee(DefaultPaymentModel paymentModel, BigDecimal add) {
        // Adjust amount to payee for withholdingTaxes precision review
        paymentModel.setToPayee(
                add
        );

        BigDecimal[] preciseToPayee = round(paymentModel.getToPayee(),bd("0.05"), HALF_EVEN);
        paymentModel.setToPayee(
                preciseToPayee[0]
        );
        return preciseToPayee;
    }

    @NotNull
    private BigDecimal[] resetWithholdingVat(DefaultPaymentModel paymentModel) {
        BigDecimal[] preciseWithholdingVat = round(paymentModel.getWithholdingVat(),bd("1.00"),UP);
        paymentModel.setWithHoldingVat(
                preciseWithholdingVat[0]
        );
        return preciseWithholdingVat;
    }

    @NotNull
    private BigDecimal[] resetWithholdingTax(DefaultPaymentModel paymentModel) {
        BigDecimal[] preciseWithholdingTax = round(paymentModel.getWithholdingTax(),bd("1.00"),UP);
        paymentModel.setWithholdingTax(
                preciseWithholdingTax[0]
        );
        return preciseWithholdingTax;
    }

    /**
     * Rounds the vendor amount value given to the nearest "increment" amount given
     * @param value amount to be rounded
     * @param increment the amount nearest to which we are to round things
     * @param roundingMode the rounding mode for the function
     * @return rounded up value to the nearest increment amount
     */
    public BigDecimal[] round(BigDecimal value, BigDecimal increment,RoundingMode roundingMode){

        BigDecimal var = null;
        BigDecimal rounded = null;
                if(increment.signum() == 0) {
                    rounded = value;
                    var = BigDecimal.ZERO;
                } else {
                    rounded = (value.divide(increment, 0, roundingMode).multiply(increment));
                    var = rounded.subtract(value);
                }

                return new BigDecimal[]{rounded, var};
    }

    /**
     * Utility to create BigDecimal from string
     * @param value bigdecimal value as string
     * @return value bigdecimal value amount parsed from string
     */
    @NotNull
    private BigDecimal bd(String value){

        return new BigDecimal(value);
    }

    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    public ModelPrecisionVisitor setPaymentModel(DefaultPaymentModel paymentModel) {

        log.debug("Payment model set as : {}",paymentModel);
        this.paymentModel = paymentModel;
        return this;
    }
}
