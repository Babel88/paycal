package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import com.google.common.base.Objects;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * Default logic for payment of rental requisitions
 *
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentLogic implements DefaultLogic, Serializable {
    private static final Logger log = LoggerFactory.getLogger(RentalPaymentLogic.class);
    private static DefaultLogic instance = new RentalPaymentLogic();
    private final PaymentParameters paymentParameters;

    public RentalPaymentLogic() {

        log.debug("Creating a rental payment logic object");
        paymentParameters = LogicFactory.getInstance().createPaymentParameters();
    }

    @Contract(pure = true)
    public static DefaultLogic getInstance() {
        return instance;
    }

    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        log.debug("calculateTotalExpense({}) method has been called.", invoiceAmount);

        log.debug("Returning total expense : {}.", invoiceAmount);

        return invoiceAmount;
    }

    @Override
    public BigDecimal calculateToPayee(BigDecimal invoiceAmount) {

        log.debug("calculateToPayee({}) method has been called.", invoiceAmount);

        BigDecimal toPayee = invoiceAmount.subtract(
                calculateWithholdingTax(invoiceAmount)
        )
                .subtract(
                        calculateWithholdingVat(invoiceAmount)
                )
                .setScale(2, HALF_EVEN);
        log.debug("Returning payable to payee : {}.", toPayee);

        return toPayee;
    }

    @Override
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {

        log.debug("calculateWithholdingTax({}) method has been called.", invoiceAmount);

        BigDecimal wth = amountBeforeTax(invoiceAmount)
                .multiply(
                        paymentParameters.getWithholdingTaxOnRentalRate()
                )
                .setScale(2, HALF_EVEN);

        log.debug("Returning withholding tax : {}.", wth);

        return wth;
    }

    private BigDecimal amountBeforeTax(BigDecimal invoiceAmount) {

        log.debug("amountBeforeTax({}) method has been called.", invoiceAmount);
        return invoiceAmount
                .divide(
                        ONE.add(paymentParameters.getVatRate()), HALF_EVEN
                )
                .setScale(2, HALF_EVEN);
    }

    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {

        log.debug("calculateWithholdingVat({}) method has been called.", invoiceAmount);

        BigDecimal wthVat = amountBeforeTax(invoiceAmount)
                .multiply(
                        paymentParameters.getWithholdingVatRate()
                )
                .setScale(2, HALF_EVEN);

        log.debug("Returning withholding vat : {}.", wthVat);

        return wthVat;
    }

    @Override
    public boolean equals(Object o) {
        log.debug("Calling the equals contract method, comparing payment parameters object\n" +
                "used in the logic :", paymentParameters.toString());
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalPaymentLogic that = (RentalPaymentLogic) o;
        return Objects.equal(paymentParameters, that.paymentParameters);
    }

    @Override
    public int hashCode() {
        log.debug("Calling the hashcode contract method, comparing payment parameters object\n" +
                "used in the logic :", paymentParameters.toString());
        return Objects.hashCode(paymentParameters);
    }
}
