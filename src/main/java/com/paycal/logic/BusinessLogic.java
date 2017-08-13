package com.paycal.logic;

import com.paycal.api.Contractor;
import com.paycal.api.Prepayable;
import com.paycal.models.PaymentParameters;
import com.paycal.api.PayCalView;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
public class BusinessLogic implements com.paycal.api.Logic {
    private Contractor contractor;
    private Prepayable prepay;
    private PayCalView payCalView;

    /*@Autowired
    private Contractor contractor;

    @Autowired
    private Prepayable prepayable;

    @Autowired
    private Display view;*/

    /*public BusinessLogic() {
    }*/


    public BusinessLogic(Contractor contractor, PayCalView payCalView, Prepayable prepay) {

        this.contractor = contractor;
        this.payCalView = payCalView;
        this.prepay = prepay;

    }

    @Override
    public void normal(double InvoiceAmount, PayCalView payCalView) {

        /**
         * <p>Calculates the transaction items with regards to transactions that fulfill the following criteria</p>
         * <p>a) The payee is not chargeable for withholding tax for consultancy</p>
         * <p>b) The payee is locally domiciled</p>
         * <p>c) The payee chargeable to VAT tax</p>
         * <p>d) The payee needs to pay 6% withholding tax</p>
         * <p>e) The Invoice amount is not inclusive of any duties</p>
         */
        double vRate = PaymentParameters.VAT_RATE / 100;
        double withholdVatRate = PaymentParameters.WITHHOLDING_VAT_RATE / 100;

        double amountB4Vat = InvoiceAmount / (1 + vRate);
        // This is the Invoice amount exclusive of VAT

        double withHoldingVat = amountB4Vat * withholdVatRate;

        double total = InvoiceAmount;
        // i.e. total to be expensed
        double toPayee = total - withHoldingVat;

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        Double withHoldingTax = 0.00;
        Double toPrepay = 0.00;

        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void vatGiven(double InvoiceAmount, double vat) {
        /**
         * <p>Calculates transaction items for invoices of the following criteria</p>
         * <p>a) The payee is not chargeable for withholding tax for consultancy</p>
         * <p>b) The payee is locally domiciled</p>
         * <p>c) The payee chargeable to VAT tax</p>
         * <p>d) The payee needs to pay 6% withholding tax</p>
         * <p>e) The Invoice is encumbered with known or unknown duties and levies</p>
         * <p>f) Or part of the Invoice amount is not chargeable to tax</p>
         */

        double vRate = PaymentParameters.VAT_RATE / 100;
        double withholdVatRate = PaymentParameters.WITHHOLDING_VAT_RATE / 100;

        double withHoldingVat = (vat / (vRate)) * withholdVatRate;
        // That is the amount to be withheld

        double total = InvoiceAmount;
        // i.e. total to be expensed
        double toPayee = total - withHoldingVat;

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        Double withHoldingTax = 0.00;
        Double toPrepay = 0.00;

        //Now we initiate the Display class
        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view
    }

    @Override
    public void contractor(double invoiceAmount) {

        double total = invoiceAmount;
        double toPrepay = 0.00;
        double toPayee = contractor.calculatePayableToContractor(total);
        double withHoldingTax = contractor.calculateContractorWithholdingTax(total);
        double withHoldingVat = contractor.calculateContractorWithholdingVat(total);

        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void taxToWithhold(double InvoiceAmount) {

        /**
         * <p>Calculates transaction items for invoices of the following criteria</p>
         * <p>a) The payee is chargeable for withholding tax for consultancy</p>
         * <p>b) The payee is locally domiciled</p>
         * <p>c) The payee chargeable to VAT tax</p>
         * <p>d) The payee needs to pay 6% withholding tax</p>
         * <p>e) The Invoice is not encumbered with duties or levies</p>
         */

        double vRate = PaymentParameters.VAT_RATE / 100;
        double withholdVatRate = PaymentParameters.WITHHOLDING_VAT_RATE / 100;
        double withholdTaxRate = PaymentParameters.WITHHOLDING_TAX / 100;

        double amountB4Vat = InvoiceAmount / (1 + vRate);
        // This is the Invoice amount exclusive of VAT

        double withHoldingVat = amountB4Vat * withholdVatRate;
        // Amount to be withheld as VAT
        double withHoldingTax = amountB4Vat * withholdTaxRate;
        // Amount of withholding tax on consultancy

        double total = InvoiceAmount;
        // i.e. total to be expensed
        double toPayee = total - withHoldingVat - withHoldingTax;

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        Double toPrepay = 0.00;

        payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void withPrepayment(double InvoiceAmount) {

        /**
         * <p>Calculates transaction items for invoices of the following criteria</p>
         * <p>a) The payee is chargeable for withholding tax for consultancy</p>
         * <p>b) The payee is locally domiciled</p>
         * <p>c) The payee chargeable to VAT tax</p>
         * <p>d) The payee needs to pay 6% withholding tax</p>
         * <p>e) The Invoice is not encumbered with duties or levies</p>
         * <p>f) The Invoice contains a component that is to be prepaid</p>
         */

        double vRate = PaymentParameters.VAT_RATE / 100;
        double withholdVatRate = PaymentParameters.WITHHOLDING_VAT_RATE / 100;

        double amountB4Vat = InvoiceAmount / (1 + vRate);
        // This is the Invoice amount exclusive of VAT

        double withHoldingVat = amountB4Vat * withholdVatRate;
        // Amount to be withheld as VAT

        Double toPrepay = prepay.getPrepay(InvoiceAmount);
        // Calculate prepayment
        double total = InvoiceAmount - toPrepay;
        // i.e. total to be expensed

        double toPayee = total + toPrepay - withHoldingVat;
        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        Double withHoldingTax = 0.00;

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




