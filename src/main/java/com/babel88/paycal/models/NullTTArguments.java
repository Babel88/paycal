package com.babel88.paycal.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class NullTTArguments extends TTArguments {
    private static final Logger log = LoggerFactory.getLogger(TTArguments.class);

    private BigDecimal invoiceAmount;
    private BigDecimal reverseVatRate;
    private BigDecimal withholdingTaxRate;
    private BigDecimal amountBeforeTax;
    private Boolean taxExclusionPolicy;

    public NullTTArguments() {

        log.debug("Someone is trying to access a nullTTArgument : {}",this);

        invoiceAmount = BigDecimal.ZERO;
        reverseVatRate = BigDecimal.ZERO;
        withholdingTaxRate = BigDecimal.ZERO;
        amountBeforeTax = BigDecimal.ZERO;
        taxExclusionPolicy = false;
    }



    @Override
    public BigDecimal getInvoiceAmount() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getReverseVatRate() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getWithholdingTaxRate() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getAmountBeforeTax() {
        return BigDecimal.ZERO;
    }

    @Override
    public Boolean getTaxExclusionPolicy() {
        return false;
    }

    @SuppressWarnings("all")
    public boolean isNill(){

        return true;
    }

    @Override
    public NullTTArguments setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    @Override
    public NullTTArguments setReverseVatRate(BigDecimal reverseVatRate) {
        this.reverseVatRate = reverseVatRate;
        return this;
    }

    @Override
    public NullTTArguments setWithholdingTaxRate(BigDecimal withholdingTaxRate) {
        this.withholdingTaxRate = withholdingTaxRate;
        return this;
    }

    @Override
    public NullTTArguments setAmountBeforeTax(BigDecimal amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
        return this;
    }

    @Override
    public NullTTArguments setTaxExclusionPolicy(Boolean taxExclusionPolicy) {
        this.taxExclusionPolicy = taxExclusionPolicy;
        return this;
    }
}
