package com.babel88.paycal.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * This class encapsulates the arguments applied in calculations for telegraphic
 * transfers. This is inorder to maintain states between the calculations, the logical
 * models and various delegates and delegators
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class TTArguments {

    private static final Logger log = LoggerFactory.getLogger(TTArguments.class);
    private static final TTArguments instance = new TTArguments();

    private BigDecimal invoiceAmount;
    private BigDecimal reverseVatRate;
    private BigDecimal withholdingTaxRate;
    private BigDecimal amountBeforeTax;
    private Boolean taxExclusionPolicy;

    private TTArguments(){

        log.debug("TTArguments object instance");
    }

    public static TTArguments getInstance() {
        return instance;
    }

    public TTArguments(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public TTArguments(BigDecimal invoiceAmount, BigDecimal reverseVatRate) {
        this(invoiceAmount);
        this.reverseVatRate = reverseVatRate;
    }

    public TTArguments(BigDecimal invoiceAmount, BigDecimal reverseVatRate, BigDecimal withholdingTaxRate) {
        this(invoiceAmount,reverseVatRate);
        this.withholdingTaxRate = withholdingTaxRate;
    }

    public TTArguments(BigDecimal invoiceAmount, BigDecimal reverseVatRate, BigDecimal withholdingTaxRate, BigDecimal amountBeforeTax) {
        this(invoiceAmount,reverseVatRate,withholdingTaxRate);
        this.amountBeforeTax = amountBeforeTax;
    }

    public TTArguments(BigDecimal invoiceAmount, BigDecimal reverseVatRate, BigDecimal withholdingTaxRate, BigDecimal amountBeforeTax, Boolean taxExclusionPolicy) {
        this(invoiceAmount,reverseVatRate,withholdingTaxRate,amountBeforeTax);
        this.taxExclusionPolicy = taxExclusionPolicy;
    }

    public TTArguments setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public TTArguments setReverseVatRate(BigDecimal reverseVatRate) {
        this.reverseVatRate = reverseVatRate;
        return this;
    }

    public TTArguments setWithholdingTaxRate(BigDecimal withholdingTaxRate) {
        this.withholdingTaxRate = withholdingTaxRate;
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
}
