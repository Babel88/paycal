package com.babel88.paycal.config.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 25/08/2017.
 */
@Deprecated
public class ModelViewFactory {
    private static final Logger log = LoggerFactory.getLogger(ModelViewFactory.class);
    private static ModelViewFactory instance = new ModelViewFactory();

    public ModelViewFactory() {

        log.debug("Creating a model view factory");
    }

    public static ModelViewFactory getInstance() {
        return instance;
    }

//    public static ResultsViewer createResultsViewer(){
//
//        return ResultsOutput.getInstance();
//    }
//
//    public static PaymentModelViewInterface createPaymentModelView(){
//
//        return DisplayImpl.getInstance();
//    }
//
//    public static PaymentAdvice createPaymentAdvice(){
//
//        return PaymentAdvice.getInstance();
//    }
}
