package com.babel88.paycal.logic.alpha;

import com.babel88.paycal.api.logic.WiredPayments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

/**
 * <P>Class calculates the transactions to pass for invoices coming
 * from companies, or individuals who are domiciled beyond the local
 * tax jurisdiction.</P>
 * <p>This means that we have to calculate the taxes chargeable and give
 * them to our local tax authority. There being no way for the supplier
 * to submit VAT for self, we have to calculate the VAT, based on the Invoice
 * but in reverse</p>
 */
@ComponentScan
@Component
public class ForeignPaymentsAlpha implements WiredPayments {

    private final Logger log = LoggerFactory.getLogger(ForeignPaymentsAlpha.class);

    private BigDecimal reverseInvoice, withholdingVat,withHoldingTaxAmount;

    private BigDecimal withholdingTaxRate;

    public ForeignPaymentsAlpha() {

        log.debug("Instantiating the reverseInvoiceAmount with ZERO");
        reverseInvoice = new BigDecimal(BigInteger.ZERO);

        log.debug("Instantiating the withholdingVat with ZERO");
        withholdingVat = new BigDecimal(BigInteger.ZERO);

        log.debug("Instantiating the withHoldingTaxAmount with ZERO");
        withHoldingTaxAmount = new BigDecimal(BigInteger.ZERO);
    }

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
     * @param invoiced//    "Invoiced" variable in the main method
     * @param withholdingTaxRate// "WithTax_Rate" variable in the main method
     * @return //returns the reverse Invoice amount
     */
    @Override
    public BigDecimal getReverseInvoice(BigDecimal invoiced, BigDecimal withholdingTaxRate, Boolean exclusive) {

        //boolean isInclusive = !decide.equals("yes");
        //"no" = That we assume the invoiced amount is exclusive
        //"yes"= That we assume the invoiced amount is inclusive

        /*
         * Now with the understanding of the nature of the invoiced amount
         * we proceed to calculate the ReverseInvoice
         */

        BigDecimal reverseInvoice;//Return variable

        if (!exclusive) {

            this.reverseInvoice = invoiced.setScale(2,HALF_EVEN);

        } else {

            //TODO review WithTaxRAte
            BigDecimal reverseCoefficient = ONE.subtract(withholdingTaxRate);

            this.reverseInvoice = invoiced
                                    .divide(reverseCoefficient,HALF_EVEN)
                                    .setScale(2,HALF_EVEN);
        }

        return this.reverseInvoice.setScale(2,HALF_EVEN);
    }

    /**
     * Calculating Reverse VAT
     * Here we are now going to use the reverse VAT calculated to
     * calculate the vat amount chargeable
     *
     * @param reverseInvoiceAmount
     * @param vatRate
     * @return //Reverse Vat chargeable
     */

    @Override
    public BigDecimal getReverseVat(BigDecimal reverseInvoiceAmount, BigDecimal vatRate) {

        this.withholdingVat = reverseInvoiceAmount.multiply(vatRate);

        //withholdingVat = this.reverseInvoice.multiply(vatRate);
        /*
         * well, E.G. VAT=NET*16%
         */

        return this.withholdingVat;
    }

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
    @Override
    public BigDecimal getwithholdingTaxAmount(BigDecimal reverseInvoiceAmount, BigDecimal withHoldingTaxRate) {
        return reverseInvoiceAmount
                .multiply(withHoldingTaxRate)
                .setScale(2,HALF_EVEN);
    }

    /**
     * Calculating the total expenditure
     * A lot of variables are going to change once the user decides whether
     * or not that the amount invoiced is inclusive of withholding tax...
     * but...
     * One thing always holds true. The Total Expense will always be:
     * Total Expense=Reverse Invoice*(1+Vat Rate)
     *
     * @param reverseInvoice
     * @param vatRate
     * @param exclusive      //If   no, the invoiced is not inclusive,
     *                       if yes it is exclusive.
     * @param invoiced
     * @param vatAmount
     * @return // "Total Expense=Reverse Invoice*(1+Vat Rate)"
     */
    @Override
    public BigDecimal getTotalExpense(BigDecimal reverseInvoice, BigDecimal vatRate, Boolean exclusive, BigDecimal invoiced, BigDecimal vatAmount)
    {
        BigDecimal totalExpense;

        if (exclusive) {

            totalExpense = reverseInvoice.add(vatAmount);

        } else {

            totalExpense = invoiced.multiply(vatRate.add(ONE));

        }

        return totalExpense.setScale(2,HALF_EVEN);
    }

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

    @Override
    public BigDecimal getPaySupplier(BigDecimal total, BigDecimal WithTax, BigDecimal Vat) {
        return total.subtract(WithTax).subtract(Vat).setScale(2,HALF_EVEN);
    }

}
