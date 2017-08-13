package com.paycal.models;


import com.paycal.api.InvoiceDetails;

/**
 * Created by edwin.njeru on 18/07/2016.
 * This class contains default PaymentParameters for the following:
 * a) vat rate
 * b) withholding tax rate
 * c) Withholding vat rate
 * d) Methods to effect the change of the above
 */
public class PaymentParameters {

    public static final double VAT_RATE = 16;
    public static final double WITHHOLDING_VAT_RATE = 6;
    public static final double WITHHOLDING_TAX = 5;
    public static final double WITHHOLDING_TAX_CONTRACTOR = 3;

    InvoiceDetails InvoiceDetails;

    public double getVatRate(){

        double vat = InvoiceDetails.vatRate();

        return vat;
    }

    public double getWithholdingVatRate(){

        double withHoldingVatRate = InvoiceDetails.withHoldingVatRate();

        return withHoldingVatRate;
    }
}
