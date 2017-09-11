package com.babel88.paycal.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentReportDelegate {

    private final Logger log = LoggerFactory.getLogger(PaymentReportDelegate.class);
    /* pointer to the delegator*/
    private ReportingVisitor delegator;

    public PaymentReportDelegate(ReportingVisitor reportingVisitor) {

        log.debug("Creating a paymentReportDelegate : {}",this);

        this.delegator = reportingVisitor;
    }

    public void renderPaymentModelReport() {

        //TODO render report from paymentModel in the delegator
    }
}
