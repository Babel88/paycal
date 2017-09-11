package com.babel88.paycal.view;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.models.PaymentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportingVisitor implements Visitor {

    private final Logger log = LoggerFactory.getLogger(ReportingVisitor.class);
    private PaymentReportDelegate paymentReportDelegate = new PaymentReportDelegate(this);
    private DefaultPaymentModel paymentModel;

    public ReportingVisitor() {

        log.debug("Creating a ReportingVisitor : {}",this);
    }

    @Override
    public void visit(PaymentModel visitable) {

        this.paymentModel = visitable;

        paymentReportDelegate.renderPaymentModelReport();
    }
}
