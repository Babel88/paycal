package com.babel88.paycal.api;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
@Deprecated
public interface Contractor {

    /**
     * Calculates the amount to go the contractor's account or cheque
     *
     * @param invoiceAmount this is the total amount of the Invoice
     * @return the calculated amount payable to contractor
     */
    BigDecimal calculatePayableToContractor(BigDecimal invoiceAmount);

    /**
     * calculate the amount to withholding on the contractor being 3% of the amount before taxes
     *
     * @param invoiceAmount this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    BigDecimal calculateContractorWithholdingTax(BigDecimal invoiceAmount);

    /**
     * calculates the amount to withhold being 6% of Invoice amount before tax
     *
     * @param invoiceAmount this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    BigDecimal calculateContractorWithholdingVat(BigDecimal invoiceAmount);

    /**
     * calculate the total expense for a contractor - involving engagement
     *
     * @param invoiceAmount the amount requested in the payment requisition
     *
     * @return return amount expendable to ledger in BigDecimal
     */
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);
}
