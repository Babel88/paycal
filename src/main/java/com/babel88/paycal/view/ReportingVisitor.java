package com.babel88.paycal.view;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportingVisitor implements Visitor {

    //private PaymentReportDelegate paymentReportDelegate = new PaymentReportDelegate(paymentAdvice,this);
    private final Logger log = LoggerFactory.getLogger(ReportingVisitor.class);
    private PaymentAdvice paymentAdvice;
    // = new PaymentReportDelegate(this);
    private final PaymentReportDelegate paymentReportDelegate;
    private DefaultPaymentModel paymentModel;

    public ReportingVisitor(FeedBack feedBack) {

        log.debug("Creating a ReportingVisitor : {}", this);

        paymentReportDelegate = new PaymentReportDelegate(this, feedBack);
    }

    @Override
    public void visit(PaymentModel visitable) {

        this.paymentModel = visitable;

        log.debug("Visit method of the reportingVisitor has been invoked...");

        paymentReportDelegate.renderPaymentModelReport();
    }

    public PaymentAdvice getPaymentAdvice() {
        return paymentAdvice;
    }

    public ReportingVisitor setPaymentAdvice(PaymentAdvice paymentAdvice) {
        this.paymentAdvice = paymentAdvice;
        return this;
    }

    public PaymentReportDelegate getPaymentReportDelegate() {
        return paymentReportDelegate;
    }

    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }

    public ReportingVisitor setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }
}
