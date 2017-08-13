package com.paycal.api;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface Contractor {

    /**
     * Calculates the amount to go the contractor's account or cheque
     *
     * @param total this is the total amount of the Invoice
     * @return the calculated amount payable to contractor
     */
    double calculatePayableToContractor(double total);

    /**
     * calculate the amount to withholding on the contractor being 3% of the amount before taxes
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    double calculateContractorWithholdingTax(double total);

    /**
     * calculates the amount to withhold being 6% of Invoice amount before tax
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    double calculateContractorWithholdingVat(double total);
}
