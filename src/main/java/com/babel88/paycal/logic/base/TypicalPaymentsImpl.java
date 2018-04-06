package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.GeneralPayments;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;


/**
 * <p>Calculates the transaction items with regards to transactions that fulfill the following criteria</p>
 * <p>a) The payee is not chargeable for withholding tax for consultancy</p>
 * <p>b) The payee is locally domiciled</p>
 * <p>c) The payee chargeable to VAT tax</p>
 * <p>d) The payee needs to pay 6% withholding tax</p>
 * <p>e) The Invoice amount is not inclusive of any duties</p>
 */
public class TypicalPaymentsImpl implements TypicalPayments, DefaultLogic {

    /* withholding vat rate */
    private final AtomicReference<BigDecimal> withholdVatRate = new AtomicReference<>();

    /**
     * vat rate
     */
    private final AtomicReference<BigDecimal> vatRate = new AtomicReference<>();

    /**
     * to track change in the invoice amount
     */
    private final AtomicReference<BigDecimal> invoiceAmount =
            new AtomicReference<>();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public TypicalPaymentsImpl(PaymentParameters paymentParameters) {

        log.debug("Creating an instance of the typicalPayment object logic : {}", this);

        vatRate.set(paymentParameters.getVatRate());

        withholdVatRate.set(paymentParameters.getWithholdingVatRate());
    }

    /**
     * returns the invoice amount
     *
     * @return invoice amount requested
     */
    @Override
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount.get();
    }

    /**
     * Sets the invoice amount for this object
     *
     * @param invoiceAmount reuqested amount of the invoice
     * @return this
     */
    @Override
    public GeneralPayments setInvoiceAmount(BigDecimal invoiceAmount) {

        log.debug("Setting invoice amount as : {}.", invoiceAmount);
        this.invoiceAmount.set(invoiceAmount);
        return this;
    }

    /**
     * Calculate the withholding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {

        log.debug("Calculating withholding vat for : {}.", invoiceAmount);
        BigDecimal vat = calculateAmountBeforeTax(invoiceAmount)
                .multiply(withholdVatRate.get())
                .setScale(2, HALF_EVEN);

        log.debug("Returning value of withholding vat: {}.", vat);

        return vat;
    }

    /**
     * Calculates the total amount of expense for a typical payment
     *
     * @param invoiceAmount requested by the supplier
     * @return total expenditure
     */
    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        log.debug("Total expense returned as : {}.", invoiceAmount);

        return invoiceAmount;
    }

    /**
     * Calculates the amount payable to the payee
     *
     * @return amount payable to payee
     */
    @Override
    public BigDecimal calculateToPayee(BigDecimal invoiceAmount) {

        log.debug("Calculating amount payable to vendor for invoice amount : {}", invoiceAmount);

        BigDecimal withholdVatAmount = calculateWithholdingVat(invoiceAmount);

        BigDecimal totalExpense = calculateTotalExpense(invoiceAmount);

        BigDecimal toPayee = totalExpense.subtract(withholdVatAmount).setScale(2, HALF_EVEN);

        log.debug("Amount payable to payee calculated as : {}.", toPayee);
        return toPayee;
    }

    /**
     * Calculate the invoice amount before adding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    @Override
    public BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount) {

        BigDecimal amountB4Tax = invoiceAmount.divide(vatRate.get().add(valueOf(1)), ROUND_HALF_UP);

        log.debug("Calculating amount before tax for {}. Amount calculated is : {}.", invoiceAmount, amountB4Tax);
        return amountB4Tax;
    }

    /**
     * Calculate the withholding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {

        log.debug("calculateWithholdingTax has been called...Returning ZERO");
        return BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypicalPaymentsImpl that = (TypicalPaymentsImpl) o;
        return Objects.equal(withholdVatRate, that.withholdVatRate) &&
                Objects.equal(vatRate, that.vatRate) &&
                Objects.equal(invoiceAmount, that.invoiceAmount) &&
                Objects.equal(log, that.log);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(withholdVatRate, vatRate, invoiceAmount, log);
    }
}
