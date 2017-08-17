package com.babel88.paycal.logic;

import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.api.*;
import com.babel88.paycal.api.logic.Prepayments;
import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.api.logic.WithholdingTaxPayments;
import com.babel88.paycal.api.view.PayCalView;
import com.babel88.paycal.config.PaymentParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
@Component
@ComponentScan
public class BusinessLogic implements Logic {

    @Autowired
    private Contractors contractor;

    @Autowired
    private Prepayments prepayable;

    @Autowired
    private PayCalView view;

    @Autowired
    private TypicalPayments typicalPayment;

    @Autowired
    private PaymentParameters parameters;

    @Autowired
    private WithholdingTaxPayments withholdingTaxPayment;

    public BusinessLogic() {
    }

    @Override
    public void normal(BigDecimal invoiceAmount) {

        // This is the Invoice amount exclusive of VAT
        BigDecimal amountB4Vat =
                typicalPayment.calculateAmountBeforeTax(invoiceAmount);


        BigDecimal withHoldingVat =
                typicalPayment.calculateWithholdingVat(invoiceAmount);

        // i.e. total to be expensed
        BigDecimal total  =
                typicalPayment.calculateTotalExpense(invoiceAmount);


        BigDecimal toPayee = typicalPayment.calculateAmountPayable(invoiceAmount);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        BigDecimal withHoldingTax = new BigDecimal(0.00);
        BigDecimal toPrepay = new BigDecimal(0.00);

        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void vatGiven(BigDecimal InvoiceAmount, BigDecimal vat) {

        BigDecimal vRate = parameters.getVatRate().divide(BigDecimal.valueOf(100));
        BigDecimal withholdVatRate = parameters.getWithholdingVatRate().divide(BigDecimal.valueOf(100));


        BigDecimal withHoldingVat = (vat.divide(vRate)).multiply(withholdVatRate);
        // That is the amount to be withheld

        BigDecimal total = InvoiceAmount;
        // i.e. total to be expensed
        BigDecimal toPayee = total.subtract(withHoldingVat);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        BigDecimal withHoldingTax = BigDecimal.ZERO;

        BigDecimal toPrepay = BigDecimal.ZERO;

        //Now we initiate the Display class
        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view
    }

    @Override
    public void contractor(BigDecimal invoiceAmount) {

        BigDecimal toPrepay = new BigDecimal(0.00);
        BigDecimal toPayee = contractor.calculatePayableToContractor(invoiceAmount);
        BigDecimal withHoldingTax = contractor.calculateContractorWithholdingTax(invoiceAmount);
        BigDecimal withHoldingVat = contractor.calculateContractorWithholdingVat(invoiceAmount);
//        BigDecimal total = invoiceAmount;
        BigDecimal total = contractor.calculateTotalExpense(invoiceAmount);

        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void taxToWithhold(BigDecimal invoiceAmount) {


        // This is the Invoice amount exclusive of VAT
        BigDecimal amountB4Vat = withholdingTaxPayment.calculateAmountBeforeTax(invoiceAmount);

        // Amount to be withheld as VAT
        BigDecimal withHoldingVat =
                withholdingTaxPayment.calculateWithholdingVat(invoiceAmount);

        // Amount of withholding tax on consultancy
        BigDecimal withHoldingTax =
                withholdingTaxPayment.calculateWithholdingTax(invoiceAmount);

        // i.e. total to be expensed
        BigDecimal total = withholdingTaxPayment.calculateTotalExpense(invoiceAmount);


        BigDecimal toPayee = withholdingTaxPayment.calculateAmountPayable(invoiceAmount);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        BigDecimal toPrepay = BigDecimal.valueOf(0);

        // Results submitted for view
        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);


    }

    @Override
    public void withPrepayment(BigDecimal invoiceAmount) {



        // This is the Invoice amount exclusive of VAT
        //double amountB4Vat = invoiceAmount / (1 + vRate);
        BigDecimal amountB4Vat = prepayable.calculateAmountBeforeTax(invoiceAmount);

        // Amount to be withheld as VAT
        //BigDecimal withHoldingVat = amountB4Vat * withholdVatRate;
        BigDecimal withHoldingVat = prepayable.calculateWithholdingVat(amountB4Vat);

        // Calculate prepayment
        BigDecimal toPrepay = prepayable.calculatePrepayment(invoiceAmount);

        // i.e. total to be expensed
        //BigDecimal total = invoiceAmount.subtract(toPrepay);
        BigDecimal total = prepayable.calculateTotalExpense(invoiceAmount,toPrepay);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method
        //BigDecimal toPayee = total.add(toPrepay).subtract(withHoldingVat);
        BigDecimal toPayee = prepayable.calculateAmountPayable(toPrepay,withHoldingVat);


        BigDecimal withHoldingTax = BigDecimal.ZERO;

        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
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

        ttModule tt = new ttModule(view);

        // This is the one and most important, method

        tt.telegraphic();

    }
}




