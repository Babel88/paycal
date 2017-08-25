package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultPartialTaxPaymentLogic implements PartialTaxPaymentLogic {

    private static PartialTaxPaymentLogic instance = new DefaultPartialTaxPaymentLogic();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PaymentParameters paymentParameters;

    public DefaultPartialTaxPaymentLogic() {

        log.debug("The default implementation pf the PartialTaxPaymentLogic interface \n" +
                "has been invoked");

        log.debug("Calling the paymentParameter singleton from factory...");

        paymentParameters = LogicFactory.getInstance().createPaymentParameters();
    }

    public static PartialTaxPaymentLogic getInstance() {
        return instance;
    }

    /**
     * Calculates withholding vat given the vat amount
     *
     * @param vatAmount provided at runtime
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal vatAmount) {

        return vatAmount
                .divide(paymentParameters.getVatRate())
                .multiply(paymentParameters.getWithholdingVatRate());
    }

    /**
     * Calculates the total expense given the invoice amount
     *
     * @param invoiceAmount proviced at runtime by user
     * @return total expense amount
     */
    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        return invoiceAmount;
    }

    /**
     * Calculates the amount payable to vendor give the total expense and the
     * amount of withholding vat.
     *
     * @param totalExpense         calculated at runtime by the  algorithm
     * @param withholdingVatAmount calculated at runtime by the  algorithm
     * @return amount payable to vendor
     */
    @Override
    public BigDecimal calculateAmountPayableToVendor(BigDecimal totalExpense, BigDecimal withholdingVatAmount) {

        return totalExpense.subtract(withholdingVatAmount);
    }

    /**
     * Calculates the amount of withholding tax
     *
     * @return withholding tax. TYpically the amount here is zero
     */
    @Override
    public BigDecimal calculateWithholdingTax() {

        return BigDecimal.ZERO;
    }
}
