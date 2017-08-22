package com.babel88.paycal.api.logic;

import java.math.BigDecimal;

public interface TelegraphicTransfers {
    /**
     * Calculation of the Reverse Invoice Amount
     * > This is the amount from which we will calculate the VAT
     * multiplying against the VAT rate
     * > Calculation of the reverse Invoice amount hinges heavily
     * on whether or not the amount was invoiced inclusive or
     * exclusive of the withholding tax.
     * > If the amount is exclusive then the reverse Invoice amount is
     * calculated as follows:
     * Reverse Invoice = (invoiced/(1-(WithTaxRate/100)))
     * > If the amount invoiced is inclusive, it will construed to be the
     * the same as the Reverse Invoice.
     *
     * @param Invoiced//    "Invoiced" variable in the main method
     * @param WithTaxRate// "WithTax_Rate" variable in the main method
     * @return //returns the reverse Invoice amount
     */
    BigDecimal getReverseInvoice(BigDecimal Invoiced, BigDecimal WithTaxRate, Boolean exclusive);

    /**
     * Calculating Reverse VAT
     * Here we are now going to use the reverse VAT calculated to
     * calculate the vat amount chargeable
     *
     * @param reverseInvoiceAmount
     * @param vatRate
     * @return //Reverse Vat chargeable
     */

    BigDecimal getReverseVat(BigDecimal reverseInvoiceAmount, BigDecimal vatRate);

    /**
     * Calculating the withholding Tax amount
     * The idea here is to calculate the actual withholding tax to
     * be paid to the revenue authority based on the withholding tax
     * rate which is to be one of the PaymentParameters here and the calculated
     * reverse Invoice where:
     * Withholding Tax = Reverse Invoice * Withholding Tax Rate
     *
     * @param reverseInvoiceAmount // This is the input for the reverse Invoice amount
     * @param withHoldingTaxRate   //Input for input in whole numbers of the withholding tax rate
     * @return //The formula here gives the withholding tax amount chargeable
     */
    BigDecimal getwithholdingTaxAmount(BigDecimal reverseInvoiceAmount, BigDecimal withHoldingTaxRate);

    /**
     * Calculating the total expenditure
     * A lot of variables are going to change once the user decides whether
     * or not that the amount invoiced is inclusive of withholding tax...
     * but...
     * One thing always holds true. The Total Expense will always be:
     * Total Expense=Reverse Invoice*(1+Vat Rate)
     *
     * @param ReverseInvoice
     * @param vatRate
     * @param exclusive      //If   no, the invoiced is not inclusive,
     *                       if yes it is exclusive.
     * @param Invoiced
     * @param VatAmount
     * @return // "Total Expense=Reverse Invoice*(1+Vat Rate)"
     */
    BigDecimal getTotalExpense(BigDecimal ReverseInvoice, BigDecimal vatRate, Boolean exclusive, BigDecimal Invoiced, BigDecimal VatAmount
    );

    /**
     * Calculating amounts owed to supplier
     * The amount is obviously the stated amount that has been invoiced
     * unless...
     * Well, unless the amount invoiced is inclusive of withholding Tax
     * Getting the right figure here is all the rage...
     *
     * @param total//      This is the figure for total Expense
     * @param WithTax//The withholding tax chargeable
     * @param Vat//The     reverse VAT amount chargeable
     * @return // The amounts due to supplier, will always be the balance after
     * taxes are withheld on the total expense incurred by the company
     */
    BigDecimal getPaySupplier(BigDecimal total, BigDecimal WithTax, BigDecimal Vat);
}
