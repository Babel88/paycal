package com.babel88.paycal.controllers.support;

import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.controllers.PrepaymentController;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.PaymentModelCareTaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class PaymentModelTypicalControllerUpdate {

    private static int calculationSteps = 0;
    private static boolean undo = false;
    private static boolean redo = false;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PrepaymentController prepaymentController;
    @Autowired
    private PaymentModel paymentModel;
    @Autowired
    private TypicalPayments typicalPayment;
    @Autowired
    private PaymentModelCareTaker paymentModelCareTaker;
    private BigDecimal invoiceAmount;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal toPrepay = BigDecimal.ZERO;

    public PaymentModelTypicalControllerUpdate() {

        log.debug("PaymentModelTypicalControllerUpdate invoked...");
    }

    public PaymentModelTypicalControllerUpdate setInvoiceAmount(BigDecimal invoiceAmount) {

        log.debug("Setting invoice amount as : {}.", invoiceAmount);

        this.invoiceAmount = invoiceAmount;

        log.debug("Invoice amount set : {}.", this.invoiceAmount);

        return this;
    }

    public PaymentModel invoke() {

        calculationStep1();

        undoRedo();

        calculationStep2();

        undoRedo();

        calculationStep3();

        undoRedo();

        calculationStep4();

        undoRedo();

        calculationStep5();

        undoRedo();

        calculationStep6();

        undoRedo();

        calculationStep7();

        undoRedo();

        return paymentModel;
    }

    private void undoRedo() {
        log.debug("Saving state to memento object");
        paymentModelCareTaker.add(paymentModel.saveStateToMemento());
        calculationSteps++;
        if (undo) {
            paymentModel.getStateFromMemento(paymentModelCareTaker.get(--calculationSteps));
        } else if (redo) {
            paymentModel.getStateFromMemento(paymentModelCareTaker.get(calculationSteps++));
        }
    }

    private void calculationStep7() {
        toPrepay = ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

        paymentModel.setToPrepay(toPrepay);
    }

    private void calculationStep6() {
        prepaymentController.setExpenseAmount(total);
    }

    private void calculationStep5() {
        // These variables have not been computed but we do need to have them ready
        // as Zero values in the displayResults method

        paymentModel.setWithHoldingTax(ZERO);
    }

    private void calculationStep4() {
        log.debug("Calculating amount payable to vendor for : ", this.invoiceAmount);
        paymentModel.setToPayee(
                typicalPayment.calculatePayableToVendor(invoiceAmount));
    }

    private void calculationStep3() {
        log.debug("Calculating total expense for : ", this.invoiceAmount);
        total = typicalPayment.calculateTotalExpense(invoiceAmount);

        paymentModel.setTotal(total);
    }

    private void calculationStep2() {
        log.debug("Calculating withholding vat for : ", this.invoiceAmount);
        paymentModel.setWithHoldingVat(
                typicalPayment.calculateWithholdingVat(invoiceAmount));
    }

    private void calculationStep1() {
        log.debug("Calculating amount before vat for : ", this.invoiceAmount);
        paymentModel.setAmountB4Vat(
                typicalPayment.calculateAmountBeforeTax(invoiceAmount));
    }
}
