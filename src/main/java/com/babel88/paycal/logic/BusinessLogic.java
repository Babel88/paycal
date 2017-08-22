package com.babel88.paycal.logic;

import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.api.logic.*;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.PayCalView;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.controllers.PrepaymentController;
import com.babel88.paycal.view.reporting.PaymentAdvice;
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

    //@Autowired at setter
    private Contractors contractor;

    //@Autowired at setter
    private Prepayments prepayable;

    //@Autowired at setter
    private PayCalView view;

    //@Autowired at setter
    private TypicalPayments typicalPayment;

    //@Autowired at setter
    private PaymentParameters parameters;

    //@Autowired at setter
    private WithholdingTaxPayments withholdingTaxPayment;

    //@Autowired at setter
    private PrepaymentController prepaymentController;

    //@Autowired at setter
    private InvoiceDetails invoice;

    //@Autowired at setter
    private ForeignPaymentDetails foreignPaymentDetails;


    //@Autowired at setter
    private TelegraphicTransfers foreignPayments;

    @Autowired
    private PaymentAdvice paymentAdvice;

    @Autowired
    private FeedBack feedBack;

    @Autowired
    public TypicalPaymentsControllers typicalPaymentsController;

    public BusinessLogic() {
    }

    //TODO create controller for the main calculations
    @Override
    public void normal(BigDecimal invoiceAmount) {

        typicalPaymentsController.runCalculation(invoiceAmount);

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

        prepaymentController.setExpenseAmount(total);

        BigDecimal toPrepay = ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

        //Now we initiate the Display class
        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view
    }

    @Override
    public void contractor(BigDecimal invoiceAmount) {

        //TODO contractorPaymentController.runCalculation(invoiceAmount);

        BigDecimal toPayee = contractor.calculatePayableToVendor(invoiceAmount);
        BigDecimal withHoldingTax = contractor.calculateWithholdingTax(invoiceAmount);
        BigDecimal withHoldingVat = contractor.calculateWithholdingVat(invoiceAmount);
//        BigDecimal total = invoiceAmount;
        BigDecimal total = contractor.calculateTotalExpense(invoiceAmount);

        prepaymentController.setExpenseAmount(total);

        BigDecimal toPrepay = ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for view

    }

    @Override
    public void taxToWithhold(BigDecimal invoiceAmount) {


        //TODO reduce calculation steps by using amount before Vat variable
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

        prepaymentController.setExpenseAmount(total);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method
        BigDecimal toPrepay = ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

        // Results submitted for view
        view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);


    }

    //TODO to replace depracated prepayment API with new API
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

    /**
     * <p>Calculates transaction items for invoices of the following criteria</p>
     * <p>a) The payee is chargeable for withholding tax for consultancy</p>
     * <p>b) The payee is not locally domiciled</p>
     * <p>c) The payee chargeable to VAT tax</p>
     * <p>d) The Invoice is not encumbered with duties or levies</p>
     */
    @Override
    public void telegraphicTransfer() {

        boolean doAgain;

        do {
            BigDecimal vatRate = BigDecimal.valueOf(foreignPaymentDetails.vatRate()/100);

            BigDecimal withHoldingTax = BigDecimal.valueOf(foreignPaymentDetails.withHoldingTaxRate()/100);

            BigDecimal invoiceAmount = foreignPaymentDetails.invoiceAmount();

            Boolean exclusive = foreignPaymentDetails.exclusiveOfWithholdingTax();

            BigDecimal reverseInvoice =
                    foreignPayments.getReverseInvoice(invoiceAmount, withHoldingTax,exclusive);

            BigDecimal withHoldingVat =
                    foreignPayments.getReverseVat(reverseInvoice, vatRate);

            BigDecimal total =
                    foreignPayments.getTotalExpense(reverseInvoice,vatRate,exclusive,invoiceAmount,withHoldingVat);

            BigDecimal toPayee =
                    foreignPayments.getPaySupplier(total,withHoldingTax,withHoldingVat);


            BigDecimal toPrepay =
                    ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

            view.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
            // Results submitted for view

            doAgain = foreignPaymentDetails.doAgain();
        } while (doAgain);

    }

    @Autowired
    public BusinessLogic setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    @Autowired
    public BusinessLogic setView(PayCalView view) {
        this.view = view;
        return this;
    }

    @Autowired
    public BusinessLogic setTypicalPayment(TypicalPayments typicalPayment) {
        this.typicalPayment = typicalPayment;
        return this;
    }

    @Autowired
    public BusinessLogic setParameters(PaymentParameters parameters) {
        this.parameters = parameters;
        return this;
    }

    @Autowired
    public BusinessLogic setWithholdingTaxPayment(WithholdingTaxPayments withholdingTaxPayment) {
        this.withholdingTaxPayment = withholdingTaxPayment;
        return this;
    }

    @Autowired
    public BusinessLogic setContractor(Contractors contractor) {
        this.contractor = contractor;
        return this;
    }

    @Autowired
    public BusinessLogic setPrepayable(Prepayments prepayable) {
        this.prepayable = prepayable;
        return this;
    }

    @Autowired
    public BusinessLogic setInvoice(InvoiceDetails invoice) {
        this.invoice = invoice;
        return this;
    }

    @Autowired
    public BusinessLogic setForeignPaymentDetails(ForeignPaymentDetails foreignPaymentDetails) {
        this.foreignPaymentDetails = foreignPaymentDetails;
        return this;
    }

    @Autowired
    public BusinessLogic setForeignPayments(TelegraphicTransfers foreignPayments) {
        this.foreignPayments = foreignPayments;
        return this;
    }
}



