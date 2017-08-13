package com.paycal.logic;

import com.paycal.api.Contractor;
import com.paycal.api.Prepayable;
import com.paycal.models.PaymentParameters;
import com.paycal.api.PayCalView;
import com.paycal.api.Logic;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
public class BusinessLogic implements Logic {

    @Autowired
    private Contractor contractor;

    @Autowired
    private Prepayable prepay;

    @Autowired
    private PayCalView payCalView;

    @Autowired
    private TypicalPayment typicalPayment;

    @Autowired
    private PaymentParameters parameters;

    @Autowired
    TypicalWithholdingTaxPayment withholdingTaxPayment;

    public BusinessLogic() {
    }

    @Override
    public void normal(BigDecimal invoiceAmount) {

        // This is the Invoice amount exclusive of VAT
        BigDecimal amountB4Vat =
                typicalPayment.calculateAmountB4Vat(invoiceAmount);


        BigDecimal withHoldingVat =
                typicalPayment.calculateWithholdingVat(invoiceAmount);

        // i.e. total to be expensed
        BigDecimal total  =
                typicalPayment.calculateTotalExpense(invoiceAmount);


        BigDecimal toPayee = typicalPayment.calculateAmountPayable();

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        BigDecimal withHoldingTax = new BigDecimal(0.00);
        BigDecimal toPrepay = new BigDecimal(0.00);

        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void vatGiven(BigDecimal InvoiceAmount, double vat) {


        /*BigDecimal withHoldingVat = (vat / (vRate)) * withholdVatRate;
        // That is the amount to be withheld

        BigDecimal total = InvoiceAmount;
        // i.e. total to be expensed
        BigDecimal toPayee = total - withHoldingVat;

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        BigDecimal withHoldingTax = 0.00;
        BigDecimal toPrepay = 0.00;

        //Now we initiate the Display class
        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view*/
    }

    @Override
    public void contractor(BigDecimal invoiceAmount) {

        BigDecimal total = invoiceAmount;
        BigDecimal toPrepay = new BigDecimal(0.00);
        BigDecimal toPayee = contractor.calculatePayableToContractor(total);
        BigDecimal withHoldingTax = contractor.calculateContractorWithholdingTax(total);
        BigDecimal withHoldingVat = contractor.calculateContractorWithholdingVat(total);

        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void taxToWithhold(BigDecimal invoiceAmount) {


        // This is the Invoice amount exclusive of VAT
        BigDecimal amountB4Vat = withholdingTaxPayment.calculateAmountB4Vat(invoiceAmount);

        // Amount to be withheld as VAT
        BigDecimal withHoldingVat =
                withholdingTaxPayment.calculateWithholdingVat(invoiceAmount);

        // Amount of withholding tax on consultancy
        BigDecimal withHoldingTax =
                withholdingTaxPayment.calculateWithholdingTax(invoiceAmount);

        // i.e. total to be expensed
        BigDecimal total = withholdingTaxPayment.calculateTotalExpense(invoiceAmount);


        BigDecimal toPayee = withholdingTaxPayment.calculateAmountPayable();

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        BigDecimal toPrepay = BigDecimal.valueOf(0);

        // Results submitted for view
        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);


    }

    @Override
    public void withPrepayment(BigDecimal invoiceAmount) {



        // This is the Invoice amount exclusive of VAT
        //double amountB4Vat = invoiceAmount / (1 + vRate);
        BigDecimal amountB4Vat = prepay.calculateAmountB4Vat(invoiceAmount);

        // Amount to be withheld as VAT
        //BigDecimal withHoldingVat = amountB4Vat * withholdVatRate;
        BigDecimal withHoldingVat = prepay.calculateWithholdingVat(amountB4Vat);

        // Calculate prepayment
        BigDecimal toPrepay = prepay.calculatePrepayment(invoiceAmount);

        // i.e. total to be expensed
        //BigDecimal total = invoiceAmount.subtract(toPrepay);
        BigDecimal total = prepay.calculateTotalExpense(invoiceAmount,toPrepay);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method
        //BigDecimal toPayee = total.add(toPrepay).subtract(withHoldingVat);
        BigDecimal toPayee = prepay.calculateAmountPayable(toPrepay,withHoldingVat);


        BigDecimal withHoldingTax = BigDecimal.ZERO;

        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void tt() {

        /**
         * <p>Calculates transaction items for invoices of the following criteria</p>
         * <p>a) The payee is chargeable for withholding tax for consultancy</p>
         * <p>b) The payee is not locally domiciled</p>
         * <p>c) The payee chargeable to VAT tax</p>
         * <p>d) The Invoice is not encumbered with duties or levies</p>
         */

        // We have tried to do "everything" in the ttModule class.
        // So now lets initiate the ttModule object

        ttModule tt = new ttModule(payCalView);

        // This is the one and most important, method

        tt.telegraphic();

    }
}




