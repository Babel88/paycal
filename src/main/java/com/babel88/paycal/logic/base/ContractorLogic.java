package com.babel88.paycal.logic.base;

import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

public class ContractorLogic {

    private static final ContractorLogic instance = new ContractorLogic();

    private final PaymentParameters paymentParameters;

    public ContractorLogic() {
        paymentParameters = LogicFactory.getInstance().createPaymentParameters();
    }

    public static ContractorLogic getInstance() {
        return instance;
    }

    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        return invoiceAmount;
    }


    public BigDecimal calculateToPayee(BigDecimal invoiceAmount) {

        return invoiceAmount
                .subtract(
                        calculateWithholdingVat(invoiceAmount)
                )
                .subtract(
                        calculateWithholdingTax(invoiceAmount)
                )
                .setScale(2, HALF_EVEN);
    }

    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {

        return calculateAmountBeforeTax(invoiceAmount)
                .multiply(
                        paymentParameters.getWithholdingTaxContractor()
                )
                .setScale(2, HALF_EVEN);
    }

    public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {

        return calculateAmountBeforeTax(invoiceAmount)
                .multiply(
                        paymentParameters.getWithholdingVatRate()
                )
                .setScale(2, HALF_EVEN);
    }

    private BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount) {

        return invoiceAmount.divide(
                ONE.add(
                        paymentParameters.getVatRate()
                ), HALF_EVEN
        );
    }
}
