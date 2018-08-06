package com.babel88.paycal.view;

import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This delegate is "newed" from the reportingVisitor which means it's lifecycle is
 * not managed by the spring container. This in turn means that the paymentAdvice
 * object used to print the report has to be newed here. I know i know the G.O.F. are
 * going to kill me, but hey when you find a way to run a delegate from the container
 * properly come and throw the first stone.
 * <p>
 * Oh wait! The delegator which is the reportingVisitor happens to be managed by the
 * container. Awesome. We will configure it and inject it here from the reportingVisitor
 */
public class PaymentReportDelegate {

    private final Logger log = LoggerFactory.getLogger(PaymentReportDelegate.class);
    /* pointer to the delegator*/
    private ReportingVisitor delegator;

    // To inject this
    private PaymentAdvice paymentAdvice;
    private final FeedBack feedBack;

    private boolean printReport;

    public PaymentReportDelegate(ReportingVisitor reportingVisitor,final FeedBack feedBack) {
        this.feedBack = feedBack;

        log.debug("Creating a paymentReportDelegate : {}", this);

        this.delegator = reportingVisitor;
    }

    public void renderPaymentModelReport() {

        if (paymentAdvice != null) {
            log.debug("Rendering the payment model report using payment advice object" +
                    " : {} injected by the DI container", paymentAdvice);
        } else {
            log.warn("The payment advice object is null, newing it from the delegate");
            paymentAdvice = new PaymentAdvice();
            log.warn("A 'newed' up paymentAdice object {} has been created proceeding to set" +
                    "printing parameters and other paraphenalia", paymentAdvice);
        }

        printReport = feedBack.printReport();

        paymentAdvice
                .setPrintAdvice(printReport)
                .forPayment(
                        delegator.getPaymentModel().getToPayee().toString(),
                        delegator.getPaymentModel().getWithholdingVat().toString(),
                        delegator.getPaymentModel().getWithholdingTax().toString()
                );
        System.out.println("A pdf report has been printed to the following path : " +
                paymentAdvice.getReportName());
    }

    public ReportingVisitor getDelegator() {
        return delegator;
    }

    public PaymentReportDelegate setDelegator(ReportingVisitor delegator) {
        this.delegator = delegator;
        return this;
    }

    public PaymentAdvice getPaymentAdvice() {
        return paymentAdvice;
    }

    public PaymentReportDelegate setPaymentAdvice(PaymentAdvice paymentAdvice) {
        this.paymentAdvice = paymentAdvice;
        return this;
    }

    public boolean isPrintReport() {
        return printReport;
    }

    public PaymentReportDelegate setPrintReport(boolean printReport) {
        this.printReport = printReport;
        return this;
    }

    public FeedBack getFeedBack() {
        return feedBack;
    }

}