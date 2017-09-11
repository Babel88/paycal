package com.babel88.paycal.view;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.models.PaymentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelViewerVisitor implements Visitor {

    private final Logger log = LoggerFactory.getLogger(ModelViewerVisitor.class);
    private ModelViewerDelegate modelViwerDelegate = new ModelViewerDelegate(this);
    private PaymentModel paymentModel;

    public ModelViewerVisitor() {

        log.debug("Creating a paymentModelVisitor : {}",this);
    }

    @Override
    public void visit(PaymentModel paymentModel) {

        this.paymentModel = paymentModel;

        log.debug("Visiting the paymentModel : {}, and rendering with the modelViewerDelegate {}",
                paymentModel,modelViwerDelegate);

        String tableString = modelViwerDelegate.renderPaymentModel();

        log.debug("Printing rendered table...");

        System.out.println("\n\n"+tableString);
    }

    public DefaultPaymentModel getPaymentModel() {

        log.debug("Returning payment model : {} to the modelViewerDelegate",paymentModel);
        return paymentModel;
    }
}
