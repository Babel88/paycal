package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import com.google.common.base.Objects;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

public class ContractorLogic implements com.babel88.paycal.api.logic.DefaultLogic {

    private static final DefaultLogic instance = new ContractorLogic();

    private final PaymentParameters paymentParameters;

    private ContractorLogic() {
        paymentParameters = LogicFactory.createPaymentParameters();
    }

    public static DefaultLogic getInstance() {
        return instance;
    }

    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        return invoiceAmount;
    }


    @Override
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

    @Override
    public BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount) {

        return calculateAmountBeforeTax(invoiceAmount)
                .multiply(
                        paymentParameters.getWithholdingTaxContractor()
                )
                .setScale(2, HALF_EVEN);
    }

    @Override
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
