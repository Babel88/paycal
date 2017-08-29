package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.factory.LogicFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentsController extends PaymentsControllerRunner implements DefaultControllers {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);
    private static DefaultControllers instance = new RentalPaymentsController();
    private final DefaultLogic rentalPaymentLogic;

    public RentalPaymentsController() {
        super();
        log.debug("Creating a rental payments controller using constructor in PaymentsControllerRunner");

        rentalPaymentLogic = LogicFactory.getInstance().createRentalPaymentLogic();
        log.debug("Fetching prepayment controller from controller factory");
    }

    public static DefaultControllers getInstance() {
        return instance;
    }

    @Override
    public void updateTotalExpense() {

        paymentModel.setTotalExpense(
                rentalPaymentLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

        paymentModel.setToPayee(
                rentalPaymentLogic.calculateToPayee(invoiceAmount)
        );

    }

    @Override
    public void updateWithholdingTax() {

        paymentModel.setToPayee(
                rentalPaymentLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        paymentModel.setToPayee(
                rentalPaymentLogic.calculateWithholdingVat(invoiceAmount)
        );

    }

    @Override
    public DefaultPaymentModel getPaymentModel() {

        return paymentModel;
    }
}
