package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.TelegraphicTransfers;
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
public class ForeignPayments implements TelegraphicTransfers {

    private final Logger log = LoggerFactory.getLogger(ForeignPayments.class);

    private BigDecimal reverseInvoice, withholdingVat,withHoldingTaxAmount;

    public ForeignPayments() {

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
     * @param withTaxRate// "WithTax_Rate" variable in the main method
     * @return //returns the reverse Invoice amount
     */
    @Override
    public BigDecimal getReverseInvoice(BigDecimal invoiced, BigDecimal withTaxRate, Boolean exclusive) {

        log.debug("Calculating the reverse invoice amount for :\n" +
                "Invoiced amount : {}. \n" +
                "Withholding tax rate : {}. \n" +
                "Exclusive withholding tax : {}. \n",
                invoiced,withTaxRate,exclusive);

        if (!exclusive) {

            this.reverseInvoice = invoiced.setScale(2,HALF_EVEN);

            log.debug("The withholding tax is not exclusive to the invoiced amount  returning \n" +
                    "reversed invoice amount of : {}.",reverseInvoice);

        } else {

            BigDecimal reverseCoefficient = ONE.subtract(withTaxRate);

            log.debug("The withholding tax is exclusive therefore we calculate the reverseInvoiceCoefficient \n" +
                    "as : {}.",reverseCoefficient);

            this.reverseInvoice = invoiced
                                    .divide(reverseCoefficient,HALF_EVEN)
                                    .setScale(2,HALF_EVEN);

            log.debug("The withholding tax is exclusive: Dividing invoiced amount {}. with the reverseInvoiceCoefficient\n " +
                    "of : {}. and reversed invoice amount is given as : {}."
                    ,invoiced,reverseCoefficient,reverseInvoice);
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

        log.debug("Reverse vat calculated when reverseInvoiceAmount is : {}. \n" +
                "and a vat rate of : {}.",reverseInvoiceAmount,vatRate);
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

        log.debug("Withholding tax amount calculated for : {}. with \n" +
                "withholding tax rate as : {}.",reverseInvoiceAmount,withHoldingTaxRate);
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

        log.debug("Getting total expense amount for : \n" +
                "reverse invoice : {}. \n" +
                "vat rate : {}. \n" +
                "withholding tax exclusive : {}. \n" +
                "invoiced amount : {}. \n" +
                "vat amount : {}."
                ,reverseInvoice,vatRate,exclusive,invoiced,vatAmount);

        if (exclusive) {

            totalExpense = reverseInvoice.add(vatAmount);

            log.debug("Total expense is : {}.",totalExpense);

        } else {

            totalExpense = invoiced.multiply(vatRate.add(ONE));

            log.debug("Total expense is : {}.",totalExpense);

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
     * @param withTax//The withholding tax chargeable
     * @param vatAmount//The     reverse VAT amount chargeable
     * @return // The amounts due to supplier, will always be the balance after
     * taxes are withheld on the total expense incurred by the company
     */

    @Override
    public BigDecimal getPaySupplier(BigDecimal total, BigDecimal withTax, BigDecimal vatAmount) {

        log.debug("Calculating payment to supplier when : \n" +
                "Total Expense : {}. \n" +
                "Withholding tax : {}. \n" +
                "Vat amount : {}.",total,withTax,vatAmount);
        return total.subtract(withTax).subtract(vatAmount).setScale(2,HALF_EVEN);
    }

}
