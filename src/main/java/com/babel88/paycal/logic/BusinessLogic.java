package com.babel88.paycal.logic;

import com.babel88.paycal.api.DefaultPrepayable;
import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.api.factory.Incarnatable;
import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.logic.TelegraphicTransfers;
import com.babel88.paycal.api.logic.WithholdingTaxPayments;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.controllers.PrepaymentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BusinessLogic implements Logic,Incarnatable {

    private static Logic instance = new BusinessLogic();

    private Contractors contractor;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public DefaultPrepayable defaultPrepayment;

    private WithholdingTaxPayments withholdingTaxPayment;

    private PrepaymentController prepaymentController;
    @Autowired
    private PaymentModelViewInterface paymentModelView;

    private TelegraphicTransfers foreignPayments;
    @Autowired
    private ForeignPaymentDetails foreignPaymentDetails;
    private TypicalPaymentsControllers typicalPaymentsController;
    private PartialTaxPaymentController partialTaxPaymentController;

    private BusinessLogic() {

        log.debug("Creating an instance of the main business logic controller");

        withholdingTaxPayment = LogicFactory.getInstance().createWithholdingTaxPayments();

        contractor = LogicFactory.getInstance().createContractors();

        defaultPrepayment = LogicFactory.getInstance().createDefaultPrepayment();

        foreignPayments = LogicFactory.getInstance().createTelegraphicTransfers();

        log.debug("Fetching controller singleton from Controller Factory");

        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();

        typicalPaymentsController = ControllerFactory.getInstance().createTypicalPaymentsController();

        partialTaxPaymentController = ControllerFactory.getInstance().createPartialTaxPaymentController();

    }

    public static Logic getInstance(){

        return instance;
    }

//    public BusinessLogic(PaymentParameters parameters){}

    //TODO create controller for the main calculations
    @Override
    public void normal(BigDecimal invoiceAmount) {

        typicalPaymentsController.runCalculation(invoiceAmount);

    }

    @Override
    public void vatGiven() {

        partialTaxPaymentController.runCalculation();

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

        paymentModelView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for paymentModelView

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

        // Results submitted for paymentModelView
        paymentModelView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);


    }

    //TODO to replace depracated prepayment API with new API
    @Override
    public void withPrepayment(BigDecimal invoiceAmount) {



        // This is the Invoice amount exclusive of VAT
        //double amountB4Vat = invoiceAmount / (1 + vRate);
        BigDecimal amountB4Vat = defaultPrepayment.calculateAmountBeforeTax(invoiceAmount);

        // Amount to be withheld as VAT
        //BigDecimal withHoldingVat = amountB4Vat * withholdVatRate;
        BigDecimal withHoldingVat = defaultPrepayment.calculateWithholdingVat(amountB4Vat);

        // Calculate prepayment
        BigDecimal toPrepay = defaultPrepayment.calculatePrepayment(invoiceAmount);

        // i.e. total to be expensed
        //BigDecimal total = invoiceAmount.subtract(toPrepay);
        BigDecimal total = defaultPrepayment.calculateTotalExpense(invoiceAmount,toPrepay);

        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method
        //BigDecimal toPayee = total.add(toPrepay).subtract(withHoldingVat);
        BigDecimal toPayee = defaultPrepayment.calculateAmountPayable(toPrepay,withHoldingVat);


        BigDecimal withHoldingTax = BigDecimal.ZERO;

        paymentModelView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
        // Results submitted for paymentModelView

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

            paymentModelView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
            // Results submitted for paymentModelView

            doAgain = foreignPaymentDetails.doAgain();
        } while (doAgain);

    }

}




