package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.config.PaymentParameters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;


/**
 * <p>Calculates the transaction items with regards to transactions that fulfill the following criteria</p>
 * <p>a) The payee is not chargeable for withholding tax for consultancy</p>
 * <p>b) The payee is locally domiciled</p>
 * <p>c) The payee chargeable to VAT tax</p>
 * <p>d) The payee needs to pay 6% withholding tax</p>
 * <p>e) The Invoice amount is not inclusive of any duties</p>
 */
public class TypicalPayment implements TypicalPayments {

    /* withholding vat rate */
    private final AtomicReference<BigDecimal> withholdVatRate = new AtomicReference<>();

    /** vat rate */
    private final AtomicReference<BigDecimal> vatRate = new AtomicReference<>();

    /** to track change in the invoice amount */
    private final AtomicReference<BigDecimal> invoiceAmount =
            new AtomicReference<>();


    public TypicalPayment(PaymentParameters parameters) {

        vatRate.set(parameters.getVatRate());

        withholdVatRate.set(parameters.getWithholdingVatRate());
    }

    /**
     * returns the invoice amount
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
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount){

        return calculateAmountBeforeTax(invoiceAmount)
                .multiply(withholdVatRate.get())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * Calculates the total amount of expense for a typical payment
     *
     * @param invoiceAmount requested by the supplier
     * @return total expenditure
     */
    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount){

        return invoiceAmount;
    }

    /**
     * Calculates the amount payable to the payee
     *
     * @return amount payable to payee
     */
    @Override
    public BigDecimal calculateAmountPayable(BigDecimal invoiceAmount){

        BigDecimal withholdVatAmount = calculateWithholdingVat(invoiceAmount);

        BigDecimal totalExpense = calculateTotalExpense(invoiceAmount);

        return totalExpense.subtract(withholdVatAmount);
    }

    /**
     * Calculate the invoice amount before adding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    @Override
    public BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount){

        return invoiceAmount.divide(vatRate.get().add(valueOf(1)),ROUND_HALF_UP);
    }
}
