package com.babel88.paycal.controllers;

import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.view.ResultsOutput;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportsController implements ReportControllers {

    @Autowired
    private FeedBack feedBack;

    @Autowired
    private PaymentAdvice paymentAdvice;

    private Boolean printReport;

    public ReportsController() {
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
}
