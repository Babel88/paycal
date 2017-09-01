package com.babel88.paycal.config.factory;

import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.PaymentModelCareTaker;
import com.babel88.paycal.models.TTArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 25/08/2017.
 */
public class ModelFactory {
    private static final Logger log = LoggerFactory.getLogger(ModelFactory.class);
    private static ModelFactory instance = new ModelFactory();

    private ModelFactory() {

        log.debug("Creating a new Model Factory");
    }

    public static ModelFactory getInstance(){

        log.debug("Returning a singleton instance of the model factory");
        return instance;
    }

    public static PaymentModel createPaymentModel(){

        log.debug("Returning a singleton instance of the paymen model");

        return PaymentModel.getInstance();
    }

    public static PaymentModelCareTaker createPaymentModelCaretaker(){

        log.debug("Returning a singleton of the Payment model caretaker from the model \n" +
                "from the model factory");

        return PaymentModelCareTaker.getInstance();
    }

    public static TTArguments getTTArguments(){

        log.debug("Returning singleton instance of the TTArguments");

        return TTArguments.getInstance();
    }
}
