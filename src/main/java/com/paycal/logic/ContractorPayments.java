package com.paycal.logic;

import com.paycal.api.Contractor;
import com.paycal.models.PaymentParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public class ContractorPayments implements Contractor {

    private final BigDecimal vatRate;
    private final BigDecimal withholdingTaxRate;
    private final BigDecimal withholdingVatRate;
    private BigDecimal b4Tax;

    @Autowired
    private PaymentParameters parameters;

    public ContractorPayments() {

        vatRate = parameters.getVatRate().divide(BigDecimal.valueOf(100));
        withholdingTaxRate = parameters.getWithholdingTaxContractor().divide(BigDecimal.valueOf(100));
        withholdingVatRate = parameters.getWithholdingVatRate().divide(BigDecimal.valueOf(100));

    }

    /**
     * Calculates the amount to go the contractor's account or cheque
     *
     * @param total this is the total amount of the Invoice
     * @return the calculated amount payable to contractor
     */
    @Override
    public BigDecimal calculatePayableToContractor(BigDecimal total) {


        BigDecimal invoiceTotal = total;

        BigDecimal b4Tax = total.divide(vatRate.add(BigDecimal.ONE));

        BigDecimal withholdingTax = b4Tax.multiply(withholdingTaxRate);

        BigDecimal vatWithholding = b4Tax.multiply(withholdingVatRate);

        return invoiceTotal.subtract(withholdingTax).subtract(vatWithholding);
    }

    /**
     * calculate the amount to withholding on the contractor being 3% of the amount before taxes
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    @Override
    public BigDecimal calculateContractorWithholdingTax(BigDecimal total) {


        BigDecimal invoiceTotal = total;

        BigDecimal b4Tax = total.divide(vatRate.add(BigDecimal.ONE));

        BigDecimal withholdingTax = b4Tax.multiply(withholdingTaxRate);

        return withholdingTax;
    }

    /**
     * calculates the amount to withhold being 6% of Invoice amount before tax
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    @Override
    public BigDecimal calculateContractorWithholdingVat(BigDecimal total) {
        // vat rate
        double vatRate = PaymentParameters.vatRate /100;
        double withholdingVatRate = PaymentParameters.withholdingVatRate /100;

        BigDecimal invoiceTotal = total;

        double b4Tax = total/(1+vatRate);

        BigDecimal vatWithholding = b4Tax * withholdingVatRate;

        return vatWithholding;
    }
}
