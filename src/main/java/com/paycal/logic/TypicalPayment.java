package com.paycal.logic;

import com.paycal.models.PaymentParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;


/**
 * <p>Calculates the transaction items with regards to transactions that fulfill the following criteria</p>
 * <p>a) The payee is not chargeable for withholding tax for consultancy</p>
 * <p>b) The payee is locally domiciled</p>
 * <p>c) The payee chargeable to VAT tax</p>
 * <p>d) The payee needs to pay 6% withholding tax</p>
 * <p>e) The Invoice amount is not inclusive of any duties</p>
 */
public class TypicalPayment {

    /* the vat rate */
    private final BigDecimal vRate;

    /* withholding vat rate */
    private final BigDecimal withholdVatRate;

    /** to track change in the invoice amount */
    private BigDecimal invoiceAmount;

    /** track the total expense */
    private BigDecimal totalExpenseAmount;

    /** tracks amount payable to payee */
    private BigDecimal amountPayableToPayee;

    /** tracks the withholding vat chargeable*/
    private BigDecimal withholdingVatAmount;


    @Autowired
    private PaymentParameters parameters;

    public TypicalPayment() {

        vRate = parameters.getVatRate().divide(valueOf(100));

        withholdVatRate = parameters.getWithholdingVatRate().divide(valueOf(100));
    }

    /**
     * returns the invoice amount
     * @return invoice amount requested
     */
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * Sets the invoice amount for this object
     *
     * @param invoiceAmount
     * @return
     */
    public TypicalPayment setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    /**
     * Calculate the invoice amount before adding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    public BigDecimal calculateAmountB4Vat(BigDecimal invoiceAmount){

        this.invoiceAmount = invoiceAmount;

        return invoiceAmount.divide(vRate.add(valueOf(1)));
    }

    /**
     * Calculate the withholding vat
     *
     * @param invoiceAmount invoice amount quoted in the invoice request
     * @return amount before vat
     */
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount){

        this.withholdingVatAmount =
                calculateAmountB4Vat(invoiceAmount).multiply(withholdVatRate);

        return withholdingVatAmount;
    }

    /**
     * Calculates the total amount of expense for a typical payment
     *
     * @param invoiceAmount
     * @return total expenditure
     */
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount){

        this.totalExpenseAmount = invoiceAmount;

        return invoiceAmount;
    }

    /**
     * Calculates the amount payable to the payee
     *
     * @return amount payable to payee
     */
    public BigDecimal calculateAmountPayable(){

        this.amountPayableToPayee = this.totalExpenseAmount.subtract(withholdingVatAmount);

        return amountPayableToPayee;
    }
}
