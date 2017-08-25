package com.babel88.paycal.config.factory;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.view.DisplayImpl;
import com.babel88.paycal.view.ResultsOutput;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 25/08/2017.
 */
public class ModelViewFactory {
    private static final Logger log = LoggerFactory.getLogger(ModelViewFactory.class);

    public ModelViewFactory() {

        log.debug("Creating a model view factory");
    }

    private static ModelViewFactory instance = new ModelViewFactory();

    public static ModelViewFactory getInstance() {
        return instance;
    }

    public static ResultsViewer createResultsViewer(){

        return ResultsOutput.getInstance();
    }

    public static PaymentModelViewInterface createPaymentModelView(){

        return DisplayImpl.getInstance();
    }

    public static PaymentAdvice createPaymentAdvice(){

        return PaymentAdvice.getInstance();
    }
}
