package com.babel88.paycal.config.factory;

import com.babel88.paycal.controllers.support.PaymentModelTypicalControllerUpdate;
import com.babel88.paycal.controllers.support.undo.PaymentModelUndoHelper;
import com.babel88.paycal.models.PaymentModelCareTaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 8/23/17.
 */
public class UtilFactory {

    private static final Logger log = LoggerFactory.getLogger(UtilFactory.class);
    private static UtilFactory instance = new UtilFactory();

    public UtilFactory() {

        log.debug("Instantiating the utility factory");
    }

    public static UtilFactory getInstance() {

        log.debug("Returning utility factory singleton instance");
        return instance;
    }


    public PaymentModelTypicalControllerUpdate createPaymentModelTypicalControllerUpdate() {

        log.debug("Returning singleton instance of the PaymentModelUpdate helper from \n" +
                "utility factory");

        return PaymentModelTypicalControllerUpdate.getInstance();
    }

    public PaymentModelUndoHelper createPaymentModelUndoHelper() {

        log.debug("Returning singleton instance of the Payment model undo helper");

        return PaymentModelUndoHelper.getInstance();
    }

    public PaymentModelCareTaker createPaymentModelCareTaker(){

        log.debug("Returning singleton instance of the Payment model caretake");

        return PaymentModelCareTaker.getInstance();
    }
}
