package com.babel88.paycal.models;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.RoundingMode.*;

/**
 * This class encapsulates the arguments applied in calculations for telegraphic
 * transfers. This is inorder to maintain states between the calculations, the logical
 * models and various delegates and delegators
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class TTArguments implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TTArguments.class);

    private BigDecimal invoiceAmount;
    private BigDecimal reverseVatRate;
    private BigDecimal withholdingTaxRate;
    private BigDecimal amountBeforeTax;
    private Boolean taxExclusionPolicy;

    public TTArguments(){

        log.debug("TTArguments object instance created : {}",this);
    }

    public TTArguments setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount.setScale(2, HALF_EVEN);
        return this;
    }

    public TTArguments setReverseVatRate(BigDecimal reverseVatRate) {
        this.reverseVatRate = setAccuracy(reverseVatRate);
        return this;
    }

    private BigDecimal setAccuracy(BigDecimal reverseVatRate){

        return reverseVatRate.divide(BigDecimal.valueOf(100)).setScale(2,HALF_EVEN);
    }

    public TTArguments setWithholdingTaxRate(BigDecimal withholdingTaxRate) {
        this.withholdingTaxRate = setAccuracy(withholdingTaxRate);
        return this;
    }

    public TTArguments setAmountBeforeTax(BigDecimal amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
        return this;
    }

    public TTArguments setTaxExclusionPolicy(Boolean taxExclusionPolicy) {
        this.taxExclusionPolicy = taxExclusionPolicy;
        return this;
    }

    public BigDecimal getInvoiceAmount() {
        log.debug("Invoice amount returned : {}",invoiceAmount);
        return invoiceAmount;
    }

    public BigDecimal getReverseVatRate() {
        log.debug("Reverse vat rate : {}",reverseVatRate);
        return reverseVatRate;
    }

    public BigDecimal getWithholdingTaxRate() {
        log.debug("Withholding tax rate : {}",withholdingTaxRate);
        return withholdingTaxRate;
    }

    public BigDecimal getAmountBeforeTax() {
        log.debug("Amount before tax : {}",amountBeforeTax);
        return amountBeforeTax;
    }

    public Boolean getTaxExclusionPolicy() {
        log.debug("Withholding tax exclusive : {}",taxExclusionPolicy);
        return taxExclusionPolicy;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("invoiceAmount", invoiceAmount)
                .add("reverseVatRate", reverseVatRate)
                .add("withholdingTaxRate", withholdingTaxRate)
                .add("amountBeforeTax", amountBeforeTax)
                .add("taxExclusionPolicy", taxExclusionPolicy)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTArguments that = (TTArguments) o;
        return Objects.equal(getInvoiceAmount(), that.getInvoiceAmount()) &&
                Objects.equal(getReverseVatRate(), that.getReverseVatRate()) &&
                Objects.equal(getWithholdingTaxRate(), that.getWithholdingTaxRate()) &&
                Objects.equal(getAmountBeforeTax(), that.getAmountBeforeTax()) &&
                Objects.equal(getTaxExclusionPolicy(), that.getTaxExclusionPolicy());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInvoiceAmount(), getReverseVatRate(), getWithholdingTaxRate(),
                getAmountBeforeTax(), getTaxExclusionPolicy());
    }
}
