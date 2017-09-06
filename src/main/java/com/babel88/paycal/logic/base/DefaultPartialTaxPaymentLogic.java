package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.LogicFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.*;

public class DefaultPartialTaxPaymentLogic implements PartialTaxPaymentLogic {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private PaymentParameters paymentParameters;

    public DefaultPartialTaxPaymentLogic() {

        log.debug("The default implementation pf the PartialTaxPaymentLogic interface \n" +
                "has been invoked : {}",this);
    }

    /**
     * Calculates withholding vat given the vat amount
     *
     * @param vatAmount provided at runtime
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat(BigDecimal vatAmount) {

        BigDecimal wthVat = null;
        if(paymentParameters != null && vatAmount != null ) {
            wthVat  = vatAmount
                .divide(paymentParameters.getVatRate(), HALF_EVEN)
                    .multiply(paymentParameters.getWithholdingVatRate());
        } else{

            if(paymentParameters != null) {
                log.debug("The vat amount provided is null");
            } else if(vatAmount != null){
                log.debug("The payment parameters object provided is null");
            } else {
                log.error("Both the vat amount and the payment parameters object are null");
            }
        }

        return wthVat.setScale(2, HALF_EVEN);
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

    public DefaultPartialTaxPaymentLogic setPaymentParameters(PaymentParameters paymentParameters) {
        this.paymentParameters = paymentParameters;
        return this;
    }


}
