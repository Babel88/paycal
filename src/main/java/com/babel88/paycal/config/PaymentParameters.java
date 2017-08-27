package com.babel88.paycal.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.math.RoundingMode.UNNECESSARY;

/**
 * Created by edwin.njeru on 18/07/2016.
 * This class contains default PaymentParameters for the following:
 * a) vat rate
 * b) withholding tax rate
 * c) Withholding vat rate
 * d) Methods to effect the change of the above
 */
public class PaymentParameters {

    private final ThreadLocal<Logger> log;

    private final BigDecimal vatRate;
    private final BigDecimal withholdingVatRate;
    private final BigDecimal withholdingTaxRate;
    private final BigDecimal withholdingTaxContractor;

    //Code for singleTon pattern
    private static PaymentParameters instance = new PaymentParameters();

    private PaymentParameters() {

        log = ThreadLocal.withInitial(() -> LoggerFactory.getLogger(this.getClass()));
        log.get().debug("\nCreating payment parameters object with : \n" +
                "Vat Rate : {}. \n" +
                "Withholding Vat Rate : {}. \n" +
                "Withholding Tax Rate : {}. \n" +
                "Contractor withholding Tax Rate : {}. \n",
                "16%","6%","5%","3%");

        vatRate = divPerCent(BigDecimal.valueOf(16));
        withholdingVatRate = divPerCent(BigDecimal.valueOf(6));
        withholdingTaxRate = divPerCent(BigDecimal.valueOf(5));
        withholdingTaxContractor = divPerCent(BigDecimal.valueOf(3));

        log.get().debug("\nPayment parameters object created with : \n" +
                        "Vat Rate : {}. \n" +
                        "Withholding Vat Rate : {}. \n" +
                        "Withholding Tax Rate : {}. \n" +
                        "Contractor withholding Tax Rate : {}.\n",
                vatRate,withholdingVatRate,withholdingTaxRate,withholdingTaxContractor);
    }

    public static PaymentParameters getInstance(){

        return instance;
    }


    @SuppressWarnings(value = "BigDecimal.divide() called without a rounding mode argument")
    private BigDecimal divPerCent(BigDecimal denominator){

        log.get().debug("Dividing denominator by 100, to create percentage value for : {}.",denominator);
        return denominator.divide(BigDecimal.valueOf(100).setScale(2, UNNECESSARY));
    }

    public BigDecimal getVatRate() {

        log.get().debug("Vat rate => Ret val : {}.",vatRate);
        return vatRate;
    }

    public BigDecimal getWithholdingVatRate() {

        log.get().debug("Withholding Vat rate => Ret val : {}.",withholdingVatRate);
        return withholdingVatRate;
    }

    public BigDecimal getWithholdingTaxRate() {

        log.get().debug("Withholding Tax rate => Ret val : {}.",withholdingTaxRate);
        return withholdingTaxRate;
    }

    public BigDecimal getWithholdingTaxContractor() {

        log.get().debug("Withholding Tax Contractor => Ret val : {}.",withholdingTaxContractor);
        return withholdingTaxContractor;
    }

}
