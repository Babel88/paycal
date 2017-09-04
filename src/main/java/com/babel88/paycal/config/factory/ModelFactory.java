package com.babel88.paycal.config.factory;

import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.PaymentModelCareTaker;
import com.babel88.paycal.models.TTArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 25/08/2017.
 */
@Deprecated
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

//    public static PaymentModel getPaymentModel(){
//
//        log.debug("Returning a singleton instance of the {}",PaymentModel.getInstance());
//
//        return PaymentModel.getInstance();
//    }

//    public static PaymentModelCareTaker createPaymentModelCaretaker(){
//
//        log.debug("Returning a singleton instance of the {}",PaymentModelCareTaker.getInstance());
//
//        return PaymentModelCareTaker.getInstance();
//    }

//    public static TTArguments getTTArguments(){
//
//        log.debug("Returning a singleton instance of the {}",TTArguments.getInstance());
//
//        return TTArguments.getInstance();
//    }
}
