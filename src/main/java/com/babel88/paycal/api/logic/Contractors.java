package com.babel88.paycal.api.logic;

import java.math.BigDecimal;

public interface Contractors {

    /**
     * Calculates the amount to go the contractor's account or cheque
     *
     * @param invoiceAmount this is the total amount of the Invoice
     * @return the calculated amount payable to contractor
     */
    BigDecimal calculatePayableToVendor(BigDecimal invoiceAmount);

    /**
     * calculate the amount to withholding on the contractor being 3% of the amount before taxes
     *
     * @param invoiceAmount this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    BigDecimal calculateWithholdingTax(BigDecimal invoiceAmount);

    /**
     * calculates the amount to withhold being 6% of Invoice amount before tax
     *
     * @param invoiceAmount this is the total amount of the Invoice
     * @return 6% of withholding vat to withhold
     */
    BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount);

    /**
     * calculate the total expense for a contractor - involving engagement
     *
     * @param invoiceAmount the amount requested in the payment requisition
     * @return return amount expendable to ledger in BigDecimal
     */
    BigDecimal calculateTotalExpense(BigDecimal invoiceAmount);
}
