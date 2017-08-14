package com.paycal.models;


import com.paycal.api.InvoiceDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 18/07/2016.
 * This class contains default PaymentParameters for the following:
 * a) vat rate
 * b) withholding tax rate
 * c) Withholding vat rate
 * d) Methods to effect the change of the above
 */
public class PaymentParameters {

    private final BigDecimal vatRate;
    private final BigDecimal withholdingVatRate;
    private final BigDecimal withholdingTax;
    private final BigDecimal withholdingTaxContractor;

    public PaymentParameters() {

        vatRate = BigDecimal.valueOf(16);
        withholdingVatRate = BigDecimal.valueOf(6);
        withholdingTax = BigDecimal.valueOf(5);
        withholdingTaxContractor = BigDecimal.valueOf(3);
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public BigDecimal getWithholdingVatRate() {
        return withholdingVatRate;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimal getWithholdingTaxContractor() {
        return withholdingTaxContractor;
    }

}
