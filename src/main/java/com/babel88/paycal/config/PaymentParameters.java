package com.babel88.paycal.config;


import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.UNNECESSARY;

/**
 * Created by edwin.njeru on 18/07/2016.
 * This class contains default PaymentParameters for the following:
 * a) vat rate
 * b) withholding tax rate
 * c) Withholding vat rate
 * d) Methods to effect the change of the above
 */
public class PaymentParameters implements Serializable {

    private final Logger log;
    private final BigDecimal vatRate;
    private final BigDecimal withholdingVatRate;
    private final BigDecimal withholdingTaxRate;
    private final BigDecimal withholdingTaxContractor;
    private final BigDecimal withholdingTaxOnRentalRate;

    public PaymentParameters() {

        log = LoggerFactory.getLogger(this.getClass());
        log.debug("\nCreating payment parameters object with : \n" +
                "Vat Rate : {}. \n" +
                "Withholding Vat Rate : {}. \n" +
                "Withholding Tax Rate : {}. \n" +
                        "Contractor withholding Tax Rate : {}. \n" +
                        "Withholding tax on Rental payment",
                "16%", "6%", "5%", "3%", "10%");

        vatRate = divPerCent(BigDecimal.valueOf(16));
        withholdingVatRate = divPerCent(BigDecimal.valueOf(6));
        withholdingTaxRate = divPerCent(BigDecimal.valueOf(5));
        withholdingTaxContractor = divPerCent(BigDecimal.valueOf(3));
        withholdingTaxOnRentalRate = divPerCent(BigDecimal.valueOf(10));

        log.debug("\nPayment parameters object created with : \n" +
                        "Vat Rate : {}. \n" +
                        "Withholding Vat Rate : {}. \n" +
                        "Withholding Tax Rate : {}. \n" +
                        "Contractor withholding Tax Rate : {}.\n" +
                        "Withhoding tax on Rental Income : {}.",
                vatRate, withholdingVatRate, withholdingTaxRate, withholdingTaxContractor, withholdingTaxOnRentalRate);

    }

    @SuppressWarnings(value = "BigDecimal.divide() called without a rounding mode argument")
    private BigDecimal divPerCent(BigDecimal denominator){

        log.debug("Dividing denominator by 100, to create percentage value for : {}.",denominator);
        return denominator.divide(BigDecimal.valueOf(100)).setScale(2);
    }

    public BigDecimal getVatRate() {

        log.debug("Vat rate => Ret val : {}.",vatRate);
        return vatRate;
    }

    public BigDecimal getWithholdingVatRate() {

        log.debug("Withholding Vat rate => Ret val : {}.",withholdingVatRate);
        return withholdingVatRate;
    }

    public BigDecimal getWithholdingTaxRate() {

        log.debug("Withholding Tax rate => Ret val : {}.",withholdingTaxRate);
        return withholdingTaxRate;
    }

    public BigDecimal getWithholdingTaxContractor() {

        log.debug("Withholding Tax Contractor => Ret val : {}.",withholdingTaxContractor);
        return withholdingTaxContractor;
    }

    public BigDecimal getWithholdingTaxOnRentalRate() {
        return withholdingTaxOnRentalRate;
    }

    @Override
    public boolean equals(Object o) {
        log.debug("Calling the equals contract method to compare with : {}.", o);

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentParameters that = (PaymentParameters) o;
        return Objects.equal(vatRate, that.vatRate) &&
                Objects.equal(withholdingVatRate, that.withholdingVatRate) &&
                Objects.equal(withholdingTaxRate, that.withholdingTaxRate) &&
                Objects.equal(withholdingTaxContractor, that.withholdingTaxContractor) &&
                Objects.equal(withholdingTaxOnRentalRate, that.withholdingTaxOnRentalRate);
    }

    @Override
    public int hashCode() {
        log.debug("Calling the hashcode contract method");
        return Objects.hashCode(vatRate, withholdingVatRate, withholdingTaxRate,
                withholdingTaxContractor, withholdingTaxOnRentalRate);
    }
}
