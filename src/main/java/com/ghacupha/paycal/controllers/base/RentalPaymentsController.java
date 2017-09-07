package com.ghacupha.paycal.controllers.base;

import com.ghacupha.paycal.api.DefaultPaymentModel;
import com.ghacupha.paycal.api.controllers.DefaultControllers;
import com.ghacupha.paycal.api.logic.DefaultLogic;
import com.ghacupha.paycal.config.factory.LogicFactory;
import com.ghacupha.paycal.controllers.PaymentsControllerRunnerImpl;
import com.ghacupha.paycal.models.TTArguments;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for rental payments updates the payment model with assertions based on
 * the rentalPaymentLogic and sends the results to views
 * <p>
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentsController extends PaymentsControllerRunnerImpl implements DefaultControllers {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    private static DefaultControllers instance = new RentalPaymentsController();
    private final DefaultLogic rentalPaymentLogic;

    private RentalPaymentsController() {
        super();
        log.debug("Creating a rental payments controller using constructor in PaymentsControllerRunner");

        rentalPaymentLogic = LogicFactory.getInstance().getRentalPaymentLogic();
        log.debug("Fetching prepayment controller from controller factory");
    }

    @Contract(pure = true)
    public static DefaultControllers getInstance() {
        return instance;
    }

    @Override
    public void updateTotalExpense() {

        super.paymentModel.setTotalExpense(
                rentalPaymentLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

        super.paymentModel.setToPayee(
                rentalPaymentLogic.calculateToPayee(invoiceAmount)
        );

    }

    @Override
    public void updateWithholdingTax() {

        super.paymentModel.setWithholdingTax(
                rentalPaymentLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        super.paymentModel.setWithHoldingVat(
                rentalPaymentLogic.calculateWithholdingVat(invoiceAmount)
        );

    }

    @Override
    public DefaultPaymentModel getPaymentModel() {

        return super.paymentModel;
    }

    @Override
    public TTArguments getTtArguments() {
        return null;
    }
}
