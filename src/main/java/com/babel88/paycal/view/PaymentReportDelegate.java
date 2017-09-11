package com.babel88.paycal.view;

import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This delegate is "newed" from the reportingVisitor which means it's lifecycle is
 * not managed by the spring container. This in turn means that the paymentAdvice
 * object used to print the report has to be newed here. I know i know the G.O.F. are
 * going to kill me, but hey when you find a way to run a delegate from the container
 * properly come and throw the first stone.
 *
 * Oh wait! The delegator which is the reportingVisitor happens to be managed by the
 * container. Awesome. We will configure it and inject it here from the reportingVisitor
 */
public class PaymentReportDelegate {

    private final Logger log = LoggerFactory.getLogger(PaymentReportDelegate.class);
    /* pointer to the delegator*/
    private ReportingVisitor delegator;

    private PaymentAdvice paymentAdvice;

    public PaymentReportDelegate(PaymentAdvice paymentAdvice,ReportingVisitor reportingVisitor) {

        log.debug("Creating a paymentReportDelegate : {}",this);

        this.delegator = reportingVisitor;
        this.paymentAdvice = paymentAdvice;
    }

    public void renderPaymentModelReport() {

        if(paymentAdvice != null) {
            log.debug("Rendering the payment model report");
        } else {

            log.warn("The payment advice object is null, newing it from the delegate");

            paymentAdvice = new PaymentAdvice();

            log.warn("A 'newed' up paymentAdice object has been created proceeding to set" +
                    "printing parameters and other paraphenalia");

        }

        paymentAdvice
                .setPrintAdvice(new FeedBackImpl().printReport())
                .forPayment(
                        delegator.getPaymentModel().getToPayee().toString(),
                        delegator.getPaymentModel().getWithholdingVat().toString(),
                        delegator.getPaymentModel().getWithholdingTax().toString()
                );
        System.out.println("A pdf report has been printed to the following path : "+
                paymentAdvice.getReportName());
    }
}
