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
    private PaymentReportDelegate paymentReportDelegate;
    private DefaultPaymentModel paymentModel;

    public ReportingVisitor() {

        log.debug("Creating a ReportingVisitor : {}",this);
    }

    @Override
    public void visit(PaymentModel visitable) {

        this.paymentModel = visitable;

        log.debug("Visit method of the reportingVisitor has been invoked...");

        paymentReportDelegate.renderPaymentModelReport();
    }

    public ReportingVisitor setPaymentAdvice(PaymentAdvice paymentAdvice) {
        this.paymentAdvice = paymentAdvice;
        return this;
    }

    public PaymentAdvice getPaymentAdvice() {
        return paymentAdvice;
    }

    public PaymentReportDelegate getPaymentReportDelegate() {
        return paymentReportDelegate;
    }

    public ReportingVisitor setPaymentReportDelegate(PaymentReportDelegate paymentReportDelegate) {
        this.paymentReportDelegate = paymentReportDelegate;
        return this;
    }

    public ReportingVisitor setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    public DefaultPaymentModel getPaymentModel() {
        return paymentModel;
    }
}
