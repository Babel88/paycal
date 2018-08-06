package com.babel88.paycal.controllers.delegate;

import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.logic.PrepaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class PrepaymentsDelegate {

    private final Logger log = LoggerFactory.getLogger(PrepaymentsDelegate.class);

    /*This is the pointer to the controller model we are adjusting*/
    private PaymentsControllerRunner paymentsControllerRunner;

    public PrepaymentsDelegate(PaymentsControllerRunner paymentsControllerRunner) {

        log.debug("PrepaymentsDelegate {} has been  called with argument... : {}", this, paymentsControllerRunner);
        this.paymentsControllerRunner = paymentsControllerRunner;
    }

    @SuppressWarnings("all")
    public void updateToPrepay() {

        log.debug("updateToPrepay method has been called in the delegate");
        BigDecimal totalExpense = paymentsControllerRunner.getPaymentModel().getTotalExpense();

        log.debug("An amount of {} has been called from the prepayment model as expense and set as argument \n" +
                "in the setExpenseAmount() method, in the getPrepaymentController method.", totalExpense);

        paymentsControllerRunner.getPrepaymentController().setExpenseAmount(totalExpense);

        log.debug("We are now implementing the prepayment service using the prepayment controller");

        paymentsControllerRunner.getPaymentModel().setToPrepay(
                ((PrepaymentService) paymentsControllerRunner.getPrepaymentController()::getPrepayment).prepay(totalExpense)
        );

        log.debug("Running total expenses auto-adjustment algorithm");
        paymentsControllerRunner.getPaymentModel().setTotalExpense(
                paymentsControllerRunner
                        .getPaymentModel()
                        .getTotalExpense()
                        .subtract(
                                paymentsControllerRunner.getPaymentModel().getToPrepay()
                        )
        );
        log.debug("The auto-adjustment algorithm has been ran successfully");
    }

    @SuppressWarnings("unused")
    public PaymentsControllerRunner getPaymentsControllerRunner() {
        return paymentsControllerRunner;
    }

    @SuppressWarnings("unused")
    public PrepaymentsDelegate setPaymentsControllerRunner(PaymentsControllerRunner paymentsControllerRunner) {
        this.paymentsControllerRunner = paymentsControllerRunner;
        return this;
    }
}