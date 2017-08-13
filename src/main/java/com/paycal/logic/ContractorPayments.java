package com.paycal.logic;

import com.paycal.api.Contractor;
import com.paycal.models.PaymentParameters;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public class ContractorPayments implements Contractor {

    public ContractorPayments() {
    }

    /**
     * Calculates the amount to go the contractor's account or cheque
     *
     * @param total this is the total amount of the Invoice
     * @return the calculated amount payable to contractor
     */
    @Override
    public double calculatePayableToContractor(double total) {

        // vat rate
        double vatRate = PaymentParameters.VAT_RATE/100;
        double withholdingTaxRate = PaymentParameters.WITHHOLDING_TAX_CONTRACTOR/100;
        double withholdingVatRate = PaymentParameters.WITHHOLDING_VAT_RATE/100;

        double invoiceTotal = total;

        double b4Tax = total/(1+vatRate);

        double withholdingTax = b4Tax * withholdingTaxRate;

        double vatWithholding = b4Tax * withholdingVatRate;

        return invoiceTotal-withholdingTax-vatWithholding;
    }

    /**
     * calculate the amount to withholding on the contractor being 3% of the amount before taxes
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    @Override
    public double calculateContractorWithholdingTax(double total) {
        // vat rate
        double vatRate = PaymentParameters.VAT_RATE/100;
        double withholdingTaxRate = PaymentParameters.WITHHOLDING_TAX_CONTRACTOR/100;

        double invoiceTotal = total;

        double b4Tax = total/(1+vatRate);

        double withholdingTax = b4Tax * withholdingTaxRate;

        return withholdingTax;
    }

    /**
     * calculates the amount to withhold being 6% of Invoice amount before tax
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    @Override
    public double calculateContractorWithholdingVat(double total) {
        // vat rate
        double vatRate = PaymentParameters.VAT_RATE/100;
        double withholdingVatRate = PaymentParameters.WITHHOLDING_VAT_RATE/100;

        double invoiceTotal = total;

        double b4Tax = total/(1+vatRate);

        double vatWithholding = b4Tax * withholdingVatRate;

        return vatWithholding;
    }
}
