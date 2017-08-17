package com.babel88.paycal.logic;

import com.babel88.paycal.config.PaymentParameters;

import com.babel88.paycal.api.logic.WithholdingTaxPayments;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * <p>Calculates transaction items for invoices of the following criteria</p>
 * <p>a) The payee is chargeable for withholding tax for consultancy</p>
 * <p>b) The payee is locally domiciled</p>
 * <p>c) The payee chargeable to VAT tax</p>
 * <p>d) The payee needs to pay 6% withholding tax</p>
 * <p>e) The Invoice is not encumbered with duties or levies</p>
 */
public class TypicalWithholdingTaxPayment implements WithholdingTaxPayments {

    private final PaymentParameters parameters;


    public TypicalWithholdingTaxPayment(PaymentParameters parameters) {

        this.parameters = parameters;
    }

    /**
     * calculates the amount before taxes for a normal payment with
     * withholding taxes
     *
     * @param invoiceAmount the invoice amount requested for in the invoice
     * @return amountB4Vat
     */
    @Override
    public BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount){

        return amountBeforeTax(invoiceAmount);
    }

    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount){

        BigDecimal amountBeforeVat =
                amountBeforeTax(invoiceAmount);

        return amountBeforeVat.multiply(parameters.getWithholdingVatRate());
    }

    @Override
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount){

        BigDecimal amountBeforeVat =
                amountBeforeTax(invoiceAmount);

        return amountBeforeVat.multiply(parameters.getWithholdingTaxRate());
    }

    private BigDecimal amountBeforeTax(BigDecimal invoiceAmount) {
        return invoiceAmount
                .divide(
                        parameters.getVatRate().add(ONE), HALF_EVEN
                ).setScale(2,HALF_EVEN);
    }

    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount){

        return invoiceAmount.setScale(2, ROUND_HALF_UP);
    }

    @Override
    public BigDecimal calculateAmountPayable(BigDecimal invoiceAmount){

        BigDecimal withholdingVat = calculateWithholdingVat(invoiceAmount);
        BigDecimal withholdingTax = calculateWithholdingTax(invoiceAmount);
        BigDecimal totalExpenseAmount = calculateTotalExpense(invoiceAmount);

        BigDecimal amountPayableToPayee =
                totalExpenseAmount.subtract(withholdingTax).subtract(withholdingVat);

        return amountPayableToPayee.setScale(2, ROUND_HALF_UP);
    }
}
