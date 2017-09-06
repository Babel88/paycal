package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

public class ContractorLogic implements com.babel88.paycal.api.logic.DefaultLogic {

    private final Logger log = LoggerFactory.getLogger(ContractorLogic.class);

    private PaymentParameters paymentParameters;

    public ContractorLogic(PaymentParameters paymentParameters) {

        this.paymentParameters = paymentParameters;

        log.debug("An instance of ContractorLogic has been created : {}",this);
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
