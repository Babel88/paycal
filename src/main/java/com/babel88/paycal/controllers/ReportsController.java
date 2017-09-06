package com.babel88.paycal.controllers;

import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ReportsController implements ReportControllers {

    private final Logger log= LoggerFactory.getLogger(ReportsController.class);

    private FeedBack feedBack;

    private PaymentAdvice paymentAdvice;

    private Boolean printReport;

    public ReportsController() {

        log.debug("Creating a reports controller object : {}",this);
    }

    // new feature, for printing reports
    @Override
    public ReportControllers printReport(){

        printReport = feedBack.printReport();

        return this;
    }


    @Override
    public ReportControllers forPayment(ResultsOutput resultsOutput) {

        paymentAdvice.setPrintAdvice(printReport).forPayment(resultsOutput);

        return this;
    }

    public ReportsController setFeedBack(FeedBack feedBack) {
        this.feedBack = feedBack;
        return this;
    }

    public ReportsController setPaymentAdvice(PaymentAdvice paymentAdvice) {
        this.paymentAdvice = paymentAdvice;
        return this;
    }

    public ReportsController setPrintReport(Boolean printReport) {
        this.printReport = printReport;
        return this;
    }
}
