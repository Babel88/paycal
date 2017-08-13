package com.paycal.logic;

import com.paycal.models.PaymentParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * <p>Calculates transaction items for invoices of the following criteria</p>
 * <p>a) The payee is chargeable for withholding tax for consultancy</p>
 * <p>b) The payee is locally domiciled</p>
 * <p>c) The payee chargeable to VAT tax</p>
 * <p>d) The payee needs to pay 6% withholding tax</p>
 * <p>e) The Invoice is not encumbered with duties or levies</p>
 */
public class TypicalWithholdingTaxPayment {

    @Autowired
    private PaymentParameters parameters;

    private BigDecimal vRate;
    private BigDecimal withholdVatRate;
    private BigDecimal withholdTaxRate;
    private BigDecimal amountB4Vat;
    private BigDecimal withholdingVat;
    private BigDecimal withholdingTax;
    private BigDecimal totalExpenseAmount;
    private BigDecimal amountPayableToPayee;

    public TypicalWithholdingTaxPayment() {

        BigDecimal vRate =
                parameters.getVatRate().divide(BigDecimal.valueOf(100));
        BigDecimal withholdVatRate =
                parameters.getWithholdingVatRate().divide(BigDecimal.valueOf(100));
        BigDecimal withholdTaxRate =
                parameters.getWithholdingTax().divide(BigDecimal.valueOf(100));
    }

    public PaymentParameters getParameters() {
        return parameters;
    }

    public BigDecimal getvRate() {
        return vRate;
    }

    public BigDecimal getWithholdVatRate() {
        return withholdVatRate;
    }

    public BigDecimal getWithholdTaxRate() {
        return withholdTaxRate;
    }

    /**
     * calculates the amount before taxes for a normal payment with
     * withholding taxes
     *
     * @param invoiceAmount
     * @return amountB4Vat
     */
    public BigDecimal calculateAmountB4Vat(BigDecimal invoiceAmount){

        amountB4Vat = amountBeforeVat(invoiceAmount);

        return amountB4Vat;
    }

    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount){

        BigDecimal amountBeforeVat =
                amountBeforeVat(invoiceAmount);

        withholdingVat = amountBeforeVat.multiply(withholdVatRate);

        return withholdingVat;
    }

    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount){

        BigDecimal amountBeforeVat =
                amountBeforeVat(invoiceAmount);

        withholdingTax = amountBeforeVat.multiply(withholdTaxRate);

        return withholdingTax;
    }

    private BigDecimal amountBeforeVat(BigDecimal invoiceAmount) {
        return invoiceAmount.divide(vRate.add(BigDecimal.valueOf(1)));
    }

    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount){

        totalExpenseAmount = invoiceAmount;

        return totalExpenseAmount;
    }

    public BigDecimal calculateAmountPayable(){

        this.amountPayableToPayee = totalExpenseAmount.subtract(withholdingTax).subtract(withholdingVat);

        return this.amountPayableToPayee;
    }
}
